package soinc.boomilever.tasks;

import soinc.boomilever.data.BlEventConfig;

/*******************************************************************************
 * 
 ******************************************************************************/
public class BlEvent
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
    public BlEvent( BlEventConfig config, CompetitionSignals signalsA,
        CompetitionSignals signalsB )
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
