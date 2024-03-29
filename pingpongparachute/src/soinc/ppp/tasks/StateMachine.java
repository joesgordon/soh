package soinc.ppp.tasks;

import org.jutils.ui.event.updater.IUpdater;

import soinc.ppp.data.TrackData;
import soinc.ppp.data.TrackState;

/*******************************************************************************
 * 
 ******************************************************************************/
public class StateMachine
{
    /**  */
    private final Track competition;

    /** The state of the competition. It should never be {@code null}. */
    private TrackState state;

    private IUpdater<TrackState> updater;

    /***************************************************************************
     * @param competition
     **************************************************************************/
    public StateMachine( Track competition )
    {
        this.competition = competition;

        setState( TrackState.NO_TEAM );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public TrackState getState()
    {
        return state;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public String signalTeamLoaded()
    {
        if( this.state == TrackState.NO_TEAM ||
            this.state == TrackState.LOADED ||
            this.state == TrackState.COMPLETE )
        {
            setState( TrackState.LOADED );
            return null;
        }

        return "Unable to load team in state " + state.name;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public String signalPeriodStartPause()
    {
        if( this.state == TrackState.LOADED ||
            this.state == TrackState.RUNNING )
        {
            setState( TrackState.RUNNING );
            return null;
        }

        return "Unable to start period in state " + state.name;
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
                setState( TrackState.PAUSED );
                break;

            case PAUSED:
                if( isWarning )
                {
                    setState( TrackState.WARNING );
                }
                else
                {
                    setState( TrackState.RUNNING );
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
                setState( TrackState.WARNING );
                break;

            default:
                return "Unable to Warn in state " + state.name;
        }

        return null;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public String signalTimersStarted()
    {
        if( this.state == TrackState.RUNNING ||
            this.state == TrackState.LAUNCHED )
        {
            setState( TrackState.LAUNCHED );
            return null;
        }

        return "Unable to start a timer when in state " + this.state;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public String signalPeriodTimeElapsed()
    {
        if( this.state.isRunning )
        {
            setState( TrackState.COMPLETE );

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
            TrackData data = competition.data;
            if( data.run1State.isComplete )
            {
                setState( TrackState.COMPLETE );
            }
            else
            {
                setState( TrackState.RUNNING );
            }
            return null;
        }

        return "Unable to accept time when in state " + this.state;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public String signalRunClear()
    {
        if( this.state.isRunning )
        {
            setState( TrackState.RUNNING );

            return null;
        }

        return "Unable to clear run when in state " + this.state;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public String signalResetRun()
    {
        if( this.state == TrackState.RUNNING ||
            this.state == TrackState.COMPLETE )
        {
            setState( TrackState.RUNNING );
            return null;
        }

        return "Unable to accept time when in state " + this.state;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public String signalClearTeam()
    {
        if( this.state == TrackState.LOADED ||
            this.state == TrackState.COMPLETE ||
            this.state == TrackState.NO_TEAM )
        {
            setState( TrackState.NO_TEAM );
            return null;
        }

        return "Unable to clear team from state " + this.state;
    }

    /***************************************************************************
     * @param state
     **************************************************************************/
    private void setState( TrackState state )
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
    public void setUpdater( IUpdater<TrackState> updater )
    {
        this.updater = updater;
    }
}
