package soinc.ppp.data;

import soinc.lib.ui.PhysicalKey;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TrackConfig
{
    /**  */
    public int redRelay;
    /**  */
    public int greenRelay;
    /**  */
    public int blueRelay;
    /**  */
    public int powerRelay;

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
        this.redRelay = 2;
        this.greenRelay = 3;
        this.blueRelay = 4;
        this.powerRelay = 1;

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
        this.redRelay = cfg.redRelay;
        this.greenRelay = cfg.greenRelay;
        this.blueRelay = cfg.blueRelay;
        this.powerRelay = cfg.powerRelay;

        this.loadKey = cfg.loadKey;
        this.startPauseKey = cfg.startPauseKey;
        this.goodRunKey = cfg.goodRunKey;
        this.badRunKey = cfg.badRunKey;
        this.clearTeamKey = cfg.clearTeamKey;
    }
}
