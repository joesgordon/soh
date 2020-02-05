package soinc.boomilever.tasks;

import org.jutils.ui.event.updater.IUpdater;

import soinc.boomilever.data.CompetitionState;

/*******************************************************************************
 * 
 ******************************************************************************/
public class StateMachine
{
    /** The state of the competition. It should never be {@code null}. */
    private CompetitionState state;

    /**  */
    private IUpdater<CompetitionState> updater;

    /***************************************************************************
     * @param competition
     **************************************************************************/
    public StateMachine()
    {
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
        switch( this.state )
        {
            case NO_TEAM:
            case LOADED:
            case COMPLETE:
                setState( CompetitionState.LOADED );
                break;

            default:
                return "Unable to load team in state " + state.name;
        }

        return null;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public String signalPeriodStarted()
    {
        switch( this.state )
        {
            case LOADED:
                setState( CompetitionState.RUNNING );

            case RUNNING:
                break;

            case WARNING:
                break;

            default:
                return "Unable to start period in state " + state.name;
        }

        return null;
    }

    /***************************************************************************
     * @param isWarning
     * @return
     **************************************************************************/
    public String signalPeriodPauseResume( boolean isWarning )
    {
        switch( this.state )
        {
            case RUNNING:
            case WARNING:
                setState( CompetitionState.PAUSED );
                break;

            case PAUSED:
                if( isWarning )
                {
                    setState( CompetitionState.WARNING );
                }
                else
                {
                    setState( CompetitionState.RUNNING );
                }
                break;

            default:
                return "Unable to Pause/Resume in state " + state.name;
        }

        return null;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public String signalPeriodWarning()
    {
        switch( this.state )
        {
            case RUNNING:
            case WARNING:
                setState( CompetitionState.WARNING );
                break;

            default:
                return "Unable to Warn in state " + state.name;
        }

        return null;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public String signalPeriodFinished()
    {
        if( this.state.isRunning )
        {
            setState( CompetitionState.COMPLETE );

            return null;
        }

        return "Unable to accept time when in state " + this.state;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public String signalClearTeam()
    {
        switch( this.state )
        {
            case NO_TEAM:
            case LOADED:
            case RUNNING:
            case PAUSED:
            case WARNING:
            case COMPLETE:
                setState( CompetitionState.NO_TEAM );
                break;

            default:
                return "Unable to clear team from state " + this.state;
        }

        return null;
    }

    /***************************************************************************
     * @param state
     **************************************************************************/
    private void setState( CompetitionState state )
    {
        this.state = state;

        // LogUtils.printDebug( "Setting state to %s", state.name );
        //
        // if( state == CompetitionState.COMPLETE )
        // {
        // Utils.printStackTrace();
        // }

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
