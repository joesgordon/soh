package soinc.hovercraft.data;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TrackData
{
    /**  */
    public TrackState state;
    /**  */
    public HcTeam team;
    /**  */
    public int targetTime;
    /**  */
    public int periodTime;
    /**  */
    public int run1Time;
    /**  */
    public int run2Time;
    /**  */
    public int failedCount;
    /**  */
    public String errorMsg;

    /***************************************************************************
     * 
     **************************************************************************/
    public TrackData()
    {
        clearTrack();
    }

    /***************************************************************************
     * @param td
     **************************************************************************/
    public TrackData( TrackData td )
    {
        this.state = td.state;
        this.team = td.team;
        this.targetTime = td.targetTime;
        this.periodTime = td.periodTime;
        this.run1Time = td.run1Time;
        this.run2Time = td.run2Time;
        this.failedCount = td.failedCount;
        this.errorMsg = td.errorMsg;
    }

    /***************************************************************************
     * @param team
     * @param div
     **************************************************************************/
    public void loadTrack( HcTeam team, DivisionConfig div )
    {
        this.state = TrackState.INITIALIZED;
        this.team = team;
        this.targetTime = div.targetTime;
        this.failedCount = team.failedCount;
        this.run1Time = team.run1Time;
        this.run2Time = team.run2Time;
        this.errorMsg = "";
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void clearTrack()
    {
        this.state = TrackState.UNINITIALIZED;
        this.team = null;
        this.targetTime = -1;
        this.failedCount = 0;
        this.run1Time = -1;
        this.run2Time = -1;
        this.errorMsg = "";
    }

    /***************************************************************************
     * Returns the school code of the currently loaded team or "N/A".
     **************************************************************************/
    public String getTeamCode()
    {
        return team == null ? "N/A" : team.schoolCode;
    }

    /***************************************************************************
     * @param runTime
     **************************************************************************/
    public void setRunTime( int runTime )
    {
        if( state == TrackState.RUNNING_A )
        {
            run1Time = runTime;
        }
        else if( state == TrackState.RUNNING_B )
        {
            run2Time = runTime;
        }
        else
        {
            if( !state.runaComplete )
            {
                run1Time = -1;
            }

            if( !state.runbComplete )
            {
                run2Time = -1;
            }
        }
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public boolean checkTimeFail()
    {
        int runTime = -1;

        if( state == TrackState.RUNNING_A )
        {
            runTime = run1Time;
        }
        else if( state == TrackState.RUNNING_B )
        {
            runTime = run2Time;
        }

        return runTime >= ( 3 * targetTime );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void completeTrack()
    {
        state = TrackState.FINISHED;

        team.run1Time = run1Time;
        team.run2Time = run2Time;
        team.failedCount = failedCount;
    }
}
