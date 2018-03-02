package soinc.rollercoaster.tasks;

import org.jutils.ui.event.updater.IUpdater;

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

    private IUpdater<CompetitionState> updater;

    /***************************************************************************
     * @param competition
     **************************************************************************/
    public RcStateMachine( RcTeamCompetition competition )
    {
        this.competition = competition;

        setState( CompetitionState.NO_TEAM );
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
            setState( CompetitionState.LOADED );
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
            setState( CompetitionState.AWAITING );
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
            setState( CompetitionState.SCORE_TIME );
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
            setState( CompetitionState.PENALTY_TIME );
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
            setState( CompetitionState.FAILED_TIME );
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
            setState( CompetitionState.COMPLETE );
            return null;
        }
        else if( this.state.isRunning )
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
        if( this.state.isRunning )
        {
            RcCompetitionData data = competition.getData();
            if( data.run1State.isComplete )
            {
                setState( CompetitionState.COMPLETE );
            }
            else
            {
                setState( CompetitionState.AWAITING );
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
            setState( CompetitionState.AWAITING );
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
            setState( CompetitionState.NO_TEAM );
            return null;
        }

        return "Unable to clear team from state " + this.state;
    }

    /***************************************************************************
     * @param state
     **************************************************************************/
    private void setState( CompetitionState state )
    {
        this.state = state;

        if( updater != null )
        {
            updater.update( state );
        }
    }

    /***************************************************************************
     * @param updater
     **************************************************************************/
    public void setUpdater( IUpdater<CompetitionState> updater )
    {
        this.updater = updater;
    }
}
