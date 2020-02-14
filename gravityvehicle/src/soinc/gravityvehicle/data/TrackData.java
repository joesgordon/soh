package soinc.gravityvehicle.data;

/***************************************************************************
 * 
 **************************************************************************/
public class TrackData
{
    /**  */
    public Team team;
    /**  */
    public TrackState state;
    /** Milliseconds */
    public long periodTime;

    /***************************************************************************
     * @param timerCount
     **************************************************************************/
    public TrackData()
    {
        reset();
    }

    /***************************************************************************
     * @param data
     **************************************************************************/
    public TrackData( TrackData data )
    {
        this.team = data.team;
        this.state = data.state;
        this.periodTime = data.periodTime;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void reset()
    {
        this.team = null;
        this.state = TrackState.NO_TEAM;
        this.periodTime = -1L;
    }
}
