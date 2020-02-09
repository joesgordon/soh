package soinc.ppp.tasks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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

    /**  */
    private final RcTimers timers;
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
     * @param gpio
     **************************************************************************/
    public Track( EventConfig config, TrackSignals signals )
    {
        this.config = config;
        this.signals = signals;
        this.timers = new RcTimers( signals.getTimerCount() );
        this.stateMachine = new StateMachine( this );
        this.data = new TrackData( signals.getTimerCount() );
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

        if( !periodTimer.isStopped() )
        {
            this.data.periodTime = periodTimer.getElapsed();
            timers.setData( data );

            if( data.periodTime > config.periodTime * 1000 &&
                !data.state.isRunning )
            {
                signalPeriodElapsed();
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

        for( int i = 0; i < signals.getTimerCount(); i++ )
        {
            int index = i;
            signals.setTimerCallback( i,
                ( b ) -> signalTimerStart( index, b ) );
        }

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
     * @return
     **************************************************************************/
    public boolean areTimersComplete()
    {
        return timers.areComplete();
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
        timers.clear();
        timers.setData( data );

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
     * 
     **************************************************************************/
    public void signalPeriodStart()
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

    /***************************************************************************
     * @param index
     * @param start
     **************************************************************************/
    public boolean signalTimerStart( int index, boolean start )
    {
        boolean started = timers.hasStarted( index );

        if( start && !timers.hasStarted() )
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
                timers.setTimerStarted( index, start );
                started = true;
            }
            else
            {
                showErrorMessage( msg );
            }
        }
        else
        {
            boolean timerHasStopped = timers.hasStopped( index );

            boolean timerHasStarted = timers.hasStarted( index );

            if( start && !timerHasStarted )
            {
                timers.setTimerStarted( index, start );
                started = true;
            }
            else if( !start && timerHasStarted && !timerHasStopped )
            {
                timers.setTimerStarted( index, start );
                started = false;
            }
            else
            {
                String action = start ? "start" : "stop";
                String state = timerHasStarted ? "has started"
                    : "has not started";
                String msg = "Unable to " + action + " when " + index + " " +
                    state;

                showErrorMessage( msg );
                started = false;
            }
        }

        return started;
    }

    /***************************************************************************
     * @param index s
     **************************************************************************/
    public void signalTimerClear( int index )
    {
        timers.clearTimer( index );

        if( !timers.hasStarted() )
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
    }

    /***************************************************************************
     * @param goodRun
     **************************************************************************/
    public void signalRunFinished( boolean goodRun )
    {
        if( !timers.areComplete() )
        {
            showErrorMessage(
                "Unable to finish run when timers are not compelete." );
            return;
        }

        String msg = stateMachine.signalRunFinished();

        if( msg == null )
        {
            RunState rs = goodRun ? RunState.SUCCESS : RunState.FAILED;

            if( data.run1State == RunState.RUNNING )
            {
                data.run1State = rs;
                data.run1Time = timers.getOfficialDuration();
            }
            else if( data.run2State == RunState.RUNNING )
            {
                data.run2State = rs;
                data.run2Time = timers.getOfficialDuration();
            }

            timers.clear();

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
            timers.clear();
            data.reset();
        }
        else
        {
            showErrorMessage( msg );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class RcTimers
    {
        /**  */
        private final Stopwatch [] timers;
        /**  */
        private final boolean [] used;

        /**
         * @param timerCount
         */
        public RcTimers( int timerCount )
        {
            this.timers = new Stopwatch[timerCount];
            this.used = new boolean[timerCount];

            for( int i = 0; i < timers.length; i++ )
            {
                timers[i] = new Stopwatch();
                timers[i].stop();
                used[i] = false;
            }
        }

        public void clearTimer( int index )
        {
            used[index] = false;
            timers[index].stop();
        }

        public boolean hasStopped( int index )
        {
            return used[index] && timers[index].isStopped();
        }

        public void setData( TrackData data )
        {
            long now = System.currentTimeMillis();
            for( int i = 0; i < timers.length; i++ )
            {
                if( used[i] )
                {
                    data.timers[i] = timers[i].getElapsed( now );
                }
                else
                {
                    data.timers[i] = -1L;
                }
            }

            data.officialTime = getOfficialDuration( now );
        }

        /**
         * @return
         */
        public boolean areComplete()
        {
            for( int i = 0; i < timers.length; i++ )
            {
                if( !timers[i].isStopped() )
                {
                    return false;
                }
            }

            return true;
        }

        /**
         * @param index
         * @param started
         */
        public void setTimerStarted( int index, boolean started )
        {
            if( started )
            {
                timers[index].start();
                used[index] = true;
            }
            else
            {
                timers[index].stop();
            }
        }

        /**
         * @return
         */
        public boolean hasStarted()
        {
            for( int i = 0; i < timers.length; i++ )
            {
                if( used[i] )
                {
                    return true;
                }
            }

            return false;
        }

        public boolean hasStarted( int index )
        {
            return used[index];
        }

        /**
         * @return
         */
        public long getOfficialDuration()
        {
            return getOfficialDuration( -1L );
        }

        /**
         * @param end
         * @return
         */
        private long getOfficialDuration( long end )
        {
            long sum = 0L;
            int count = 0;
            long [] durations = new long[timers.length];

            for( int i = 0; i < timers.length; i++ )
            {
                if( used[i] )
                {
                    if( end > -1L )
                    {
                        durations[i] = timers[i].getElapsed( end );
                    }
                    else
                    {
                        durations[i] = timers[i].getElapsed();
                    }
                    sum += durations[i];
                    count++;
                }
            }

            switch( ( int )count )
            {
                case 0:
                    return -1L;

                case 1:
                    return sum;

                case 2:
                    return sum / count;

                default:
                    Arrays.sort( durations );
                    return durations[durations.length - count + ( count ) / 2];
            }
        }

        public void clear()
        {
            for( int i = 0; i < timers.length; i++ )
            {
                timers[i].stop();
                used[i] = false;
            }
        }
    }

    public TrackData getData()
    {
        return new TrackData( data );
    }
}
