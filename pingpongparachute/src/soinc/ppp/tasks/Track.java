package soinc.ppp.tasks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import org.jutils.io.LogUtils;
import org.jutils.ui.event.updater.IUpdater;
import org.jutils.utils.Stopwatch;

import soinc.lib.relay.Relays;
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
    /**  */
    private final IUpdater<Track> runCompleteCallback;

    /** The state of the competition. It should never be {@code null}. */
    private final StateMachine stateMachine;
    /**  */
    public final TrackData data;

    /**  */
    private final Stopwatch periodTimer;
    /**  */
    private TrackView view;
    /**  */
    public boolean isSelected;

    /***************************************************************************
     * @param config
     * @param signals
     * @param timerCount
     * @param runCompleteCallback
     **************************************************************************/
    public Track( EventConfig config, TrackSignals signals, int timerCount,
        IUpdater<Track> runCompleteCallback )
    {
        this.config = config;
        this.signals = signals;
        this.runCompleteCallback = runCompleteCallback;
        this.stateMachine = new StateMachine( this );
        this.data = new TrackData( timerCount );
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
    public void updateState()
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

        updateUI();
    }

    /***************************************************************************
     * @param trackView
     * @throws IOException
     **************************************************************************/
    public void connect( TrackView view ) throws IOException
    {
        this.view = view;

        signals.connect( this, view );

        signalClearTeam();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void disconnect()
    {
        // timer.cancel();
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
            data.team.loaded = true;
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
            String msg = stateMachine.signalPeriodStartPause();

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

            runCompleteCallback.update( this );

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
     * @param data
     **************************************************************************/
    public void updateUI()
    {
        SwingUtilities.invokeLater( () -> view.setData( this ) );
    }
}
