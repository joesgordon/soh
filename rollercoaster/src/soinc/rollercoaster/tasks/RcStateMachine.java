package soinc.rollercoaster.tasks;

import soinc.rollercoaster.data.CompetitionState;
import soinc.rollercoaster.data.RcCompetitionData;

/*******************************************************************************
 * 
 ******************************************************************************/
public class RcStateMachine
{
    /**  */
    private final RcTeamCompetition competition;

    /** The state of the competition. It should never be {@code null}. */
    private CompetitionState state;

    /***************************************************************************
     * @param competition
     **************************************************************************/
    public RcStateMachine( RcTeamCompetition competition )
    {
        this.competition = competition;

        this.state = CompetitionState.NO_TEAM;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public CompetitionState getState()
    {
        return state;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public String signalTeamLoaded()
    {
        if( this.state == CompetitionState.NO_TEAM ||
            this.state == CompetitionState.LOADED ||
            this.state == CompetitionState.COMPLETE )
        {
            this.state = CompetitionState.LOADED;
            return null;
        }

        return "Unable to load team in state " + state.name;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public String signalPeriodStarted()
    {
        if( this.state == CompetitionState.LOADED ||
            this.state == CompetitionState.AWAITING )
        {
            this.state = CompetitionState.AWAITING;
            return null;
        }

        return "Unable to start period in state " + state.name;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public String signalTimersStarted()
    {
        if( this.state == CompetitionState.AWAITING )
        {
            this.state = CompetitionState.SCORE_TIME;
            return null;
        }

        return "Unable to start a timer when in state " + this.state;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public String signalTargetTimeElapsed()
    {
        if( this.state == CompetitionState.SCORE_TIME )
        {
            this.state = CompetitionState.PENALTY_TIME;
            return null;
        }

        return "Unable to elapse target time when in state " + this.state;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public String signalMaxRunTimeElapsed()
    {
        if( this.state == CompetitionState.PENALTY_TIME )
        {
            this.state = CompetitionState.FAILED_TIME;
            return null;
        }

        return "Unable to elapse run time when in state " + this.state;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public String signalPeriodTimeElapsed()
    {
        if( this.state == CompetitionState.AWAITING )
        {
            this.state = CompetitionState.COMPLETE;
            return null;
        }
        else if( this.state == CompetitionState.SCORE_TIME ||
            this.state == CompetitionState.PENALTY_TIME ||
            this.state == CompetitionState.FAILED_TIME )
        {
            return null;
        }

        return "Unable to elapse run time when in state " + this.state;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public String signalRunFinished()
    {
        if( this.state == CompetitionState.SCORE_TIME ||
            this.state == CompetitionState.PENALTY_TIME ||
            this.state == CompetitionState.FAILED_TIME )
        {
            RcCompetitionData data = competition.getData();
            if( data.run1State.isComplete )
            {
                this.state = CompetitionState.COMPLETE;
            }
            else
            {
                this.state = CompetitionState.AWAITING;
            }
            return null;
        }

        return "Unable to accept time when in state " + this.state;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public String signalResetRun()
    {
        if( this.state == CompetitionState.AWAITING ||
            this.state == CompetitionState.COMPLETE )
        {
            this.state = CompetitionState.AWAITING;
            return null;
        }

        return "Unable to accept time when in state " + this.state;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public String signalClearTeam()
    {
        if( this.state == CompetitionState.LOADED ||
            this.state == CompetitionState.COMPLETE ||
            this.state == CompetitionState.NO_TEAM )
        {
            this.state = CompetitionState.NO_TEAM;
        }

        return "Unable to clear track from state " + this.state;
    }
}
