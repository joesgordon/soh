package soh.data;

public class Track
{
    public TrackState state;
    public int runaTime;
    public int runbTime;
    public int failCnt;

    /***************************************************************************
     * 
     **************************************************************************/
    public Track()
    {
        reset();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void reset()
    {
        this.state = TrackState.INITIALIZED;
        this.runaTime = -1;
        this.runbTime = -1;
        this.failCnt = 0;
    }
}
