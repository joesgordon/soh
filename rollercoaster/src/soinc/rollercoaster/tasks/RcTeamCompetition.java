package soinc.rollercoaster.tasks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;

import org.jutils.io.LogUtils;
import org.jutils.utils.Stopwatch;

import soinc.rollercoaster.data.CompetitionState;
import soinc.rollercoaster.data.IRcCompetitionConfig;
import soinc.rollercoaster.data.RcCompetitionData;
import soinc.rollercoaster.data.RcCompetitionData.RunState;
import soinc.rollercoaster.data.RcTeam;
import soinc.rollercoaster.ui.RcCompetitionView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class RcTeamCompetition
{
    /**  */
    public final IRcCompetitionConfig config;
    /**  */
    private final IRcSignals signals;

    /**  */
    private final RcTimers timers;
    /** The state of the competition. It should never be {@code null}. */
    private final RcStateMachine stateMachine;
    /**  */
    private final RcCompetitionData data;
    /**  */
    private final Timer timer;

    /**  */
    private final Stopwatch periodTimer;

    /***************************************************************************
     * @param config
     * @param gpio
     **************************************************************************/
    public RcTeamCompetition( IRcCompetitionConfig config, IRcSignals signals )
    {
        this.config = config;
        this.signals = signals;
        this.timers = new RcTimers( signals.getTimerCount() );
        this.stateMachine = new RcStateMachine( this );
        this.data = new RcCompetitionData( signals.getTimerCount() );
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

        if( !periodTimer.isStopped() )
        {
            this.data.periodTime = periodTimer.getElapsed();
            timers.setData( data );

            if( data.officialTime > config.getTargetTime() * 1000 &&
                data.state == CompetitionState.SCORE_TIME )
            {
                signalTargetTimeElapsed();
            }
            else if( data.officialTime > config.getRunTimeout() * 1000 &&
                data.state == CompetitionState.PENALTY_TIME )
            {
                signalMaxRunTimeElapsed();
            }

            if( data.periodTime > config.getPeriodTime() * 1000 &&
                !data.state.isRunning )
            {
                signalPeriodElapsed();
            }
        }

        signals.updateUI( new RcCompetitionData( data ) );
    }

    /***************************************************************************
     * @param competitionView
     * @throws IOException
     **************************************************************************/
    public void connect( RcCompetitionView competitionView ) throws IOException
    {
        signals.connect( this, competitionView );

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
    public List<RcTeam> getAvailableTeams()
    {
        List<RcTeam> remaining = new ArrayList<>();

        for( RcTeam team : config.getTeams() )
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
    public RcTeam getTeam()
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
        return data.state.isRunning;
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
    public void signalLoadTeam( RcTeam team )
    {
        String msg = stateMachine.signalTeamLoaded();

        if( msg == null )
        {
            data.team = team;
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
            goodRun = goodRun &&
                data.officialTime <= config.getRunTimeout() * 1000;
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

            if( data.state == CompetitionState.COMPLETE )
            {
                setPeriodComplete();
            }
        }
        else
        {
            showErrorMessage( msg );
        }
    }

    private void signalTargetTimeElapsed()
    {
        String msg = stateMachine.signalTargetTimeElapsed();
        if( msg == null )
        {
        }
        else
        {
            showErrorMessage( msg );
        }
    }

    private void signalMaxRunTimeElapsed()
    {
        String msg = stateMachine.signalMaxRunTimeElapsed();
        if( msg == null )
        {
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

        public void setData( RcCompetitionData data )
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

    public RcCompetitionData getData()
    {
        return new RcCompetitionData( data );
    }
}
