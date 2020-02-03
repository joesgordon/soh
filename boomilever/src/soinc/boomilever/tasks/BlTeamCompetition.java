package soinc.boomilever.tasks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import org.jutils.io.LogUtils;
import org.jutils.utils.Stopwatch;

import soinc.boomilever.data.BlCompetitionData;
import soinc.boomilever.data.BlTeam;
import soinc.boomilever.data.CompetitionConfig;
import soinc.boomilever.data.CompetitionState;
import soinc.boomilever.ui.BlCompetitionView;
import soinc.lib.RunnableTask;

/*******************************************************************************
 * 
 ******************************************************************************/
public class BlTeamCompetition
{
    /**  */
    public final CompetitionConfig config;
    /**  */
    private final BlPiSignals signals;

    /** The state of the competition. It should never be {@code null}. */
    private final BlStateMachine stateMachine;
    /**  */
    private final BlCompetitionData data;
    /**  */
    private final Timer timer;

    /**  */
    private final Stopwatch periodTimer;

    /***************************************************************************
     * @param config
     * @param signals
     **************************************************************************/
    public BlTeamCompetition( CompetitionConfig config, BlPiSignals signals )
    {
        this.config = config;
        this.signals = signals;
        this.stateMachine = new BlStateMachine();
        this.data = new BlCompetitionData();
        this.timer = new Timer( "RC Competition" );
        this.periodTimer = new Stopwatch();

        this.periodTimer.stop();

        signalClearTeam();

        stateMachine.setUpdater( ( d ) -> handleStateUpdated( d ) );
    }

    /***************************************************************************
     * @param state
     **************************************************************************/
    private void handleStateUpdated( CompetitionState state )
    {
        this.data.state = state;

        boolean red = ( state.lights & 0x4 ) == 0x4;
        boolean green = ( state.lights & 0x2 ) == 0x2;
        boolean blue = ( state.lights & 0x1 ) == 0x1;

        signals.setLights( red, green, blue );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void updateState()
    {
        // LogUtils.printDebug( "updating" );

        if( data.state.isRunning )
        {
            this.data.periodTime = periodTimer.getElapsed();

            if( data.periodTime > config.periodTime * 1000 &&
                data.state.isRunning )
            {
                signalPeriodElapsed();
            }
            else if( data.periodTime > config.periodWarning * 1000 &&
                data.state.isRunning )
            {
                signalPeriodWarning();
            }
        }

        signals.updateUI( new BlCompetitionData( data ) );
    }

    /***************************************************************************
     * @param competitionView
     * @throws IOException
     **************************************************************************/
    public void connect( BlCompetitionView competitionView ) throws IOException
    {
        signals.connect( this, competitionView );

        signalClearTeam();

        timer.scheduleAtFixedRate( new RunnableTask( () -> updateState() ), 100,
            100 );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void disconnect()
    {
        timer.cancel();
        signals.disconnect();

    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public List<BlTeam> getAvailableTeams()
    {
        List<BlTeam> remaining = new ArrayList<>();

        for( BlTeam team : config.teams )
        {
            if( !team.loaded )
            {
                remaining.add( team );
            }
        }
        return remaining;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public BlTeam getTeam()
    {
        return data.team;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public CompetitionState getState()
    {
        return stateMachine.getState();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public boolean isRunning()
    {
        return !periodTimer.isStopped();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void setPeriodComplete()
    {
        LogUtils.printDebug( "Setting Period Complete" );
        periodTimer.stop();

        data.team.complete = true;
    }

    /***************************************************************************
     * @param message
     **************************************************************************/
    private void showErrorMessage( String message )
    {
        LogUtils.printError( message );
        // TODO show error
    }

    /***************************************************************************
     * @param team
     **************************************************************************/
    public void signalLoadTeam( BlTeam team )
    {
        String msg = stateMachine.signalTeamLoaded();

        if( msg == null )
        {
            data.reset();
            data.team = team;
            data.state = CompetitionState.LOADED;
        }
        else
        {
            showErrorMessage( msg );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void signalPeriodStartPause()
    {
        LogUtils.printDebug( "Signalling period start/pause" );

        if( periodTimer.isStopped() )
        {
            long now = System.currentTimeMillis();
            String msg = stateMachine.signalPeriodStarted();

            if( msg == null )
            {
                periodTimer.start( now );
            }
            else
            {
                showErrorMessage( msg );
            }
            data.team.loaded = true;
        }
        else
        {
            boolean isWarn = periodTimer.getElapsed() > config.periodWarning *
                1000;
            stateMachine.signalPeriodPauseResume( isWarn );
            periodTimer.pauseResume();
            return;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void signalPeriodWarning()
    {
        String msg = stateMachine.signalPeriodWarning();

        if( msg == null )
        {
            ;
        }
        else
        {
            showErrorMessage( msg );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void signalPeriodElapsed()
    {
        String msg = stateMachine.signalPeriodFinished();

        if( msg == null )
        {
            setPeriodComplete();
        }
        else
        {
            showErrorMessage( msg );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void signalClearTeam()
    {
        CompetitionState state = this.data.state;
        String msg = stateMachine.signalClearTeam();

        if( msg == null )
        {
            if( state == CompetitionState.LOADED )
            {
                data.team.loaded = false;
            }
            periodTimer.stop();
            data.reset();
        }
        else
        {
            showErrorMessage( msg );
        }
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public BlCompetitionData getData()
    {
        return new BlCompetitionData( data );
    }
}
