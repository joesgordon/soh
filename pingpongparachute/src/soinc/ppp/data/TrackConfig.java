package soinc.ppp.data;

import soinc.lib.ui.PhysicalKey;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TrackConfig
{
    /**  */
    public PhysicalKey loadKey;
    /**  */
    public PhysicalKey startPauseKey;
    /**  */
    public PhysicalKey goodRunKey;
    /**  */
    public PhysicalKey badRunKey;
    /**  */
    public PhysicalKey clearTeamKey;

    /***************************************************************************
     * 
     **************************************************************************/
    public TrackConfig()
    {
        this.loadKey = PhysicalKey.Q;
        this.startPauseKey = PhysicalKey.W;
        this.goodRunKey = PhysicalKey.E;
        this.badRunKey = PhysicalKey.R;
        this.clearTeamKey = PhysicalKey.F;
    }

    /***************************************************************************
     * @param cfg
     **************************************************************************/
    public void set( TrackConfig cfg )
    {
        this.loadKey = cfg.loadKey;
        this.startPauseKey = cfg.startPauseKey;
        this.goodRunKey = cfg.goodRunKey;
        this.badRunKey = cfg.badRunKey;
        this.clearTeamKey = cfg.clearTeamKey;
    }
}
