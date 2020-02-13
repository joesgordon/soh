package soinc.ppp.tasks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import org.jutils.io.LogUtils;
import org.jutils.utils.Stopwatch;

import soinc.lib.RunnableTask;
import soinc.ppp.data.EventConfig;
import soinc.ppp.data.Team;
import soinc.ppp.data.TrackData;
import soinc.ppp.data.TrackData.RunState;
import soinc.ppp.data.TrackState;
import soinc.ppp.ui.TrackView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Track
{
    /**  */
    public final EventConfig config;
    /**  */
    private final TrackSignals signals;

    /** The state of the competition. It should never be {@code null}. */
    private final StateMachine stateMachine;
    /**  */
    private final TrackData data;
    /**  */
    private final Timer timer;

    /**  */
    private final Stopwatch periodTimer;

    /***************************************************************************
     * @param config
     * @param signals
     * @param timerCount
     **************************************************************************/
    public Track( EventConfig config, TrackSignals signals, int timerCount )
    {
        this.config = config;
        this.signals = signals;
        this.stateMachine = new StateMachine( this );
        this.data = new TrackData( timerCount );
        this.timer = new Timer( "RC Competition" );
        this.periodTimer = new Stopwatch();

        this.periodTimer.stop();

        signalClearTeam();

        stateMachine.setUpdater( ( d ) -> handleStateUpdated( d ) );
    }

    /***************************************************************************
     * @param state
     **************************************************************************/
    private void handleStateUpdated( TrackState state )
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

        signals.updateUI( this );
    }

    /***************************************************************************
     * @param trackView
     * @throws IOException
     **************************************************************************/
    public void connect( TrackView trackView ) throws IOException
    {
        signals.connect( this, trackView );

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
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public List<Team> getAvailableTeams()
    {
        List<Team> remaining = new ArrayList<>();

        for( Team team : config.teams )
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
    public Team getTeam()
    {
        return data.team;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public TrackState getState()
    {
        return stateMachine.getState();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public TrackData getData()
    {
        return new TrackData( data );
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
        periodTimer.stop();

        data.team.run1Time = ( int )( data.run1Time / 100 );
        data.team.run2Time = ( int )( data.run2Time / 100 );

        if( data.run1State == RunState.NOT_RUN )
        {
            data.run1State = RunState.FAILED;
        }
        if( data.run2State == RunState.NOT_RUN )
        {
            data.run2State = RunState.FAILED;
        }

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
    public void signalLoadTeam( Team team )
    {
        String msg = stateMachine.signalTeamLoaded();

        if( msg == null )
        {
            data.reset();
            data.team = team;
            data.state = TrackState.LOADED;
        }
        else
        {
            showErrorMessage( msg );
        }
    }

    /***************************************************************************
     * @param index s
     **************************************************************************/
    public void signalTimerClear( int index )
    {
        String msg = stateMachine.signalRunClear();

        if( msg == null )
        {
            if( data.run1State == RunState.RUNNING )
            {
                data.run1State = RunState.NOT_RUN;
            }
            else if( data.run2State == RunState.RUNNING )
            {
                data.run2State = RunState.NOT_RUN;
            }
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
                if( periodTimer.isStopped() )
                {
                    periodTimer.start( now );
                }
                else
                {
                    periodTimer.pauseResume( now );
                }
                data.team.loaded = true;
            }
            else
            {
                showErrorMessage( msg );
            }
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
     * @param index
     * @param start
     **************************************************************************/
    public boolean signalTimerStart( int index, boolean start )
    {
        String msg = stateMachine.signalTimersStarted();

        if( msg == null )
        {
            if( data.run1State.isComplete )
            {
                data.run2State = RunState.RUNNING;
            }
            else
            {
                data.run1State = RunState.RUNNING;
            }
        }
        else
        {
            showErrorMessage( msg );

            return false;
        }

        return true;
    }

    /***************************************************************************
     * @param goodRun
     **************************************************************************/
    public void signalRunFinished( boolean goodRun )
    {
        String msg = stateMachine.signalRunFinished();

        if( msg == null )
        {
            RunState rs = goodRun ? RunState.SUCCESS : RunState.FAILED;

            if( data.run1State == RunState.RUNNING )
            {
                data.run1State = rs;
            }
            else if( data.run2State == RunState.RUNNING )
            {
                data.run2State = rs;
            }

            if( data.state == TrackState.COMPLETE )
            {
                setPeriodComplete();
            }
        }
        else
        {
            showErrorMessage( msg );
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
        String msg = stateMachine.signalPeriodTimeElapsed();

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
        String msg = stateMachine.signalClearTeam();

        if( msg == null )
        {
            periodTimer.stop();
            data.reset();
        }
        else
        {
            showErrorMessage( msg );
        }
    }
}
