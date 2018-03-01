package soinc.rollercoaster.data;

import java.util.ArrayList;
import java.util.List;

import soinc.rollercoaster.IRcSignals;

/*******************************************************************************
 * 
 ******************************************************************************/
public class RcCompetition
{
    /**  */
    public final IRcCompetitionConfig config;
    /**  */
    private final IRcSignals signals;

    /** Milliseconds */
    private long periodStart;
    /** Milliseconds */
    private long runStart;
    /**  */
    private RcTeam team;
    /** The state of the competition. It should never be {@code null}. */
    private CompetitionState state;

    /***************************************************************************
     * @param config
     * @param gpio
     **************************************************************************/
    public RcCompetition( IRcCompetitionConfig config, IRcSignals signals )
    {
        this.config = config;
        this.signals = signals;

        reset();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void connect()
    {
        signals.connect();
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
    public boolean isRunning()
    {
        return team != null;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void reset()
    {
        this.periodStart = -1L;
        this.runStart = -1L;
        this.team = null;
        this.state = CompetitionState.NO_TEAM;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public RcTeam getTeam()
    {
        return team;
    }

    /***************************************************************************
     * @param team
     **************************************************************************/
    public void loadTeam( RcTeam team )
    {
        this.team = team;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public CompetitionState getState()
    {
        return state;
    }

    private static final class RcStateMachine
    {
        /**  */
        private final RcCompetition competition;

        /** The state of the competition. It should never be {@code null}. */
        private CompetitionState state;

        public RcStateMachine( RcCompetition competition )
        {
            this.competition = competition;

            this.state = CompetitionState.NO_TEAM;
        }

        public void signalTeamLoaded()
        {
            // TODO set state
        }

        public void signalTimersStarted()
        {
            // TODO set state
        }
    }
}
