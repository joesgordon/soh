package soinc.rollercoaster.tasks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;

import org.jutils.io.LogUtils;

import soinc.rollercoaster.data.CompetitionState;
import soinc.rollercoaster.data.IRcCompetitionConfig;
import soinc.rollercoaster.data.RcCompetitionData;
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
        this.data = new RcCompetitionData();
        this.timer = new Timer( "RC Competition" );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void updateState()
    {
        LogUtils.printDebug( "updating" );
        // TODO Auto-generated method stub
    }

    /***************************************************************************
     * @param competitionView
     * @throws IOException
     **************************************************************************/
    public void connect( RcCompetitionView competitionView ) throws IOException
    {

        timer.scheduleAtFixedRate( new RunnableTask( () -> updateState() ), 100,
            100 );

        for( int i = 0; i < signals.getTimerCount(); i++ )
        {
            int index = i;
            signals.setTimerCallback( i,
                ( b ) -> signalTimerStart( index, b ) );
        }

        signalClearTeam();

        signals.connect( this, competitionView );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void disconnect()
    {
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
        return data.team != null;
    }

    public boolean areTimersComplete()
    {
        return timers.areComplete();
    }

    /**
     * @param message
     */
    private void showErrorMessage( String message )
    {
        // TODO show error
    }

    /***************************************************************************
     * @param team
     **************************************************************************/
    public void signalLoadTeam( RcTeam team )
    {
        String msg = stateMachine.signalTeamLoaded();
        if( msg != null )
        {
            data.team = team;
        }
        else
        {
            showErrorMessage( msg );
        }
    }

    /**
     * 
     */
    public void signalPeriodStart()
    {
        long startTime = System.currentTimeMillis();
        String msg = stateMachine.signalPeriodStarted();

        if( msg == null )
        {
            data.periodStart = startTime;
        }
        else
        {
            showErrorMessage( msg );
        }
    }

    /**
     * @param index
     * @param started
     */
    public void signalTimerStart( int index, boolean started )
    {
        String msg = stateMachine.signalTimersStarted();
        if( msg != null )
        {
            timers.setTimerStarted( index, started );
        }
        else
        {
            showErrorMessage( msg );
        }
    }

    /**
     * @param goodRun
     */
    public void signalRunFinished( boolean goodRun )
    {
        // TODO Auto-generated method stub
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void signalClearTeam()
    {
        String msg = stateMachine.signalClearTeam();
        if( msg == null )
        {
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
        private final long [] startTimes;
        private final long [] stopTimes;
        private final long [] durations;

        public RcTimers( int timerCount )
        {
            this.startTimes = new long[timerCount];
            this.stopTimes = new long[timerCount];
            this.durations = new long[timerCount];

            for( int i = 0; i < startTimes.length; i++ )
            {
                startTimes[i] = -1L;
                stopTimes[i] = -1L;
                durations[i] = 0L;
            }
        }

        public boolean areComplete()
        {
            // TODO Auto-generated method stub
            return false;
        }

        public void setTimerStarted( int index, boolean started )
        {
            startTimes[index] = started ? System.currentTimeMillis() : -1;
        }

        public boolean hasStarted()
        {
            for( int i = 0; i < startTimes.length; i++ )
            {
                if( startTimes[i] > -1 )
                {
                    return true;
                }
            }

            return false;
        }

        public long getOfficialDuration()
        {
            return getOfficialDuration( -1L );
        }

        private long getOfficialDuration( long end )
        {
            long sum = 0L;
            int count = 0;

            for( int i = 0; i < startTimes.length; i++ )
            {
                long start = startTimes[i];
                if( start > -1L )
                {
                    long stop = end < 0 ? stopTimes[i] : end;

                    durations[i] = stop - start;

                    count++;
                    sum += start;
                }
            }

            switch( ( int )count )
            {
                case 0:
                    return 0L;

                case 1:
                {
                    for( int i = 0; i < durations.length; i++ )
                    {
                        if( durations[i] > -1L )
                        {
                            return durations[i];
                        }
                    }
                }

                case 2:
                    return sum / count;

                default:
                    Arrays.sort( durations );
                    return durations[count + ( durations.length - count ) / 2];
            }
        }
    }
}
