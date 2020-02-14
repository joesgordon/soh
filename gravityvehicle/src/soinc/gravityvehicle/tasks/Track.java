package soinc.gravityvehicle.tasks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import org.jutils.io.LogUtils;
import org.jutils.utils.Stopwatch;

import soinc.gravityvehicle.data.GvEventConfig;
import soinc.gravityvehicle.data.Team;
import soinc.gravityvehicle.data.TrackConfig;
import soinc.gravityvehicle.data.TrackData;
import soinc.gravityvehicle.data.TrackState;
import soinc.gravityvehicle.ui.TrackView;
import soinc.lib.RunnableTask;
import soinc.lib.relay.IRelays;
import soinc.lib.relay.Relays;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Track
{
    /**  */
    public final GvEventConfig config;
    /**  */
    private final TrackSignals signals;

    /** The state of the competition. It should never be {@code null}. */
    private final StateMachine stateMachine;
    /**  */
    public final TrackData data;
    /**  */
    private final Timer timer;

    /**  */
    private final Stopwatch periodTimer;

    /***************************************************************************
     * @param config
     * @param trackConfig
     * @param relays
     **************************************************************************/
    public Track( GvEventConfig config, TrackConfig trackConfig,
        IRelays relays )
    {
        this.config = config;
        this.signals = new TrackSignals( relays, trackConfig );
        this.stateMachine = new StateMachine();
        this.data = new TrackData();
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

        boolean red = ( state.lights & Relays.RED_MASK ) == Relays.RED_MASK;
        boolean green = ( state.lights &
            Relays.GREEN_MASK ) == Relays.GREEN_MASK;
        boolean blue = ( state.lights & Relays.BLUE_MASK ) == Relays.BLUE_MASK;

        // LogUtils.printDebug(
        // "\t\t\tTrack::handleStateUpdated(): %s -> 0x%X, %s", state.name,
        // state.lights, state.background.toString() );

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
                data.state.isRunning && data.state != TrackState.WARNING )
            {
                signalPeriodWarning();
            }
        }

        signals.updateUI( this );
    }

    /***************************************************************************
     * @param competitionView
     * @throws IOException
     **************************************************************************/
    public void connect( TrackView competitionView ) throws IOException
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
    public boolean isRunning()
    {
        return !periodTimer.isStopped();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void setPeriodComplete()
    {
        // LogUtils.printDebug( "Setting Period Complete" );
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
    public void signalLoadTeam( Team team )
    {
        String msg = stateMachine.signalTeamLoaded();

        if( msg == null )
        {
            data.reset();
            data.team = team;
            data.state = TrackState.LOADED;
            data.team.loaded = true;
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
        // LogUtils.printDebug( "Signalling period start/pause" );

        if( periodTimer.isStopped() )
        {
            long now = System.currentTimeMillis();
            String msg = stateMachine.signalPeriodStarted();

            if( msg == null )
            {
                periodTimer.start( now );
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
        TrackState state = this.data.state;
        String msg = stateMachine.signalClearTeam();

        if( msg == null )
        {
            if( state == TrackState.LOADED )
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
    public TrackData getData()
    {
        return new TrackData( data );
    }
}
