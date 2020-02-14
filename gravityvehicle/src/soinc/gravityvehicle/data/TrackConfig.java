package soinc.gravityvehicle.data;

import soinc.lib.relay.Relays;
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
    public boolean enablePower;

    /**  */
    public PhysicalKey loadKey;
    /**  */
    public PhysicalKey startPauseKey;
    /**  */
    public PhysicalKey clearKey;

    /***************************************************************************
     * 
     **************************************************************************/
    public TrackConfig()
    {
        this.redRelay = Relays.RED_MASK;
        this.greenRelay = Relays.GREEN_MASK;
        this.blueRelay = Relays.BLUE_MASK;
        this.powerRelay = 1;

        this.loadKey = PhysicalKey.F1;
        this.startPauseKey = PhysicalKey.SPACE;
        this.clearKey = PhysicalKey.ENTER;
    }

    /***************************************************************************
     * @param cfg
     **************************************************************************/
    public TrackConfig( TrackConfig cfg )
    {
        this();

        set( cfg );
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
        this.clearKey = cfg.clearKey;
    }
}
