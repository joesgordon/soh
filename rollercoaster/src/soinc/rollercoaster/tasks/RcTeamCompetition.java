package soinc.rollercoaster.tasks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import soinc.rollercoaster.data.CompetitionState;
import soinc.rollercoaster.data.IRcCompetitionConfig;
import soinc.rollercoaster.data.RcTeam;

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

    /** Milliseconds */
    private long periodStart;
    /**  */
    private RcTeam team;

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

        for( int i = 0; i < signals.getTimerCount(); i++ )
        {
            int index = i;
            signals.setTimerCallback( i,
                ( b ) -> signalTimerStart( index, b ) );
        }

        signalClearTeam();
    }

    /***************************************************************************
     * @throws IOException
     **************************************************************************/
    public void connect() throws IOException
    {
        signals.connect( this );
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
        return team;
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
        return team != null;
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
            this.team = team;
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
            this.periodStart = startTime;
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
            this.periodStart = -1L;
            this.team = null;
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
