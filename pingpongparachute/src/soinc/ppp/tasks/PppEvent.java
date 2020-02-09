package soinc.ppp.tasks;

import soinc.ppp.data.EventConfig;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PppEvent
{
    /**  */
    public final Track trackA;
    /**  */
    public final Track trackB;

    /***************************************************************************
     * @param config
     * @param signalsA
     * @param signalsB
     **************************************************************************/
    public PppEvent( EventConfig config, TrackSignals signalsA,
        TrackSignals signalsB )
    {
        this.trackA = new Track( config, signalsA );
        this.trackB = new Track( config, signalsB );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public boolean isRunning()
    {
        return trackA.getState().isRunning || trackB.getState().isRunning;
    }
}
