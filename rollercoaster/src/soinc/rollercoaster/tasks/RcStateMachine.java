package soinc.rollercoaster.tasks;

import soinc.rollercoaster.data.CompetitionState;

public class RcStateMachine
{
    /**  */
    private final RcTeamCompetition competition;

    /** The state of the competition. It should never be {@code null}. */
    private CompetitionState state;

    public RcStateMachine( RcTeamCompetition competition )
    {
        this.competition = competition;

        this.state = CompetitionState.NO_TEAM;
    }

    public CompetitionState getState()
    {
        return state;
    }

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

    public String signalPeriodStarted()
    {
        if( this.state == CompetitionState.LOADED )
        {
            this.state = CompetitionState.AWAITING;
            return null;
        }

        return "Unable to start period in state " + state.name;
    }

    public String signalTimersStarted()
    {
        if( this.state == CompetitionState.AWAITING )
        {
            this.state = CompetitionState.SCORE_TIME;
            return null;
        }
        else if( this.state == CompetitionState.SCORE_TIME ||
            this.state == CompetitionState.PENALTY_TIME ||
            this.state == CompetitionState.FAILED_TIME )
        {
            return null;
        }

        return "Unable to start a timer when in state " + this.state;
    }

    public String signalTargetTimeElapsed()
    {
        if( this.state == CompetitionState.SCORE_TIME )
        {
            this.state = CompetitionState.PENALTY_TIME;
            return null;
        }

        return "Unable to elapse target time when in state " + this.state;
    }

    public String signalMaxRunTimeElapsed()
    {
        if( this.state == CompetitionState.PENALTY_TIME )
        {
            this.state = CompetitionState.FAILED_TIME;
            return null;
        }

        return "Unable to elapse run time when in state " + this.state;
    }

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

    public String signalRunAccepted()
    {
        if( this.state == CompetitionState.SCORE_TIME ||
            this.state == CompetitionState.PENALTY_TIME ||
            this.state == CompetitionState.FAILED_TIME )
        {
            if( competition.areTimersComplete() )
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

    public String signalClearTeam()
    {
        if( this.state == CompetitionState.LOADED ||
            this.state == CompetitionState.COMPLETE )
        {
            this.state = CompetitionState.NO_TEAM;
        }

        return "Unable to clear track from state " + this.state;
    }
}
