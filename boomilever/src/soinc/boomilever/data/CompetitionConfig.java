package soinc.boomilever.data;

import soinc.lib.relay.Relays;
import soinc.lib.ui.PhysicalKey;

/*******************************************************************************
 *
 ******************************************************************************/
public class CompetitionConfig
{
    /**  */
    public int redRelay;
    /**  */
    public int greenRelay;
    /**  */
    public int blueRelay;

    /**  */
    public PhysicalKey loadKey;
    /**  */
    public PhysicalKey startPauseKey;
    /**  */
    public PhysicalKey clearKey;

    /***************************************************************************
     * 
     **************************************************************************/
    public CompetitionConfig()
    {
        this.redRelay = Relays.RED_MASK;
        this.greenRelay = Relays.GREEN_MASK;
        this.blueRelay = Relays.BLUE_MASK;

        this.loadKey = PhysicalKey.F1;
        this.startPauseKey = PhysicalKey.SPACE;
        this.clearKey = PhysicalKey.ENTER;
    }

    /***************************************************************************
     * @param cfg
     **************************************************************************/
    public CompetitionConfig( CompetitionConfig cfg )
    {
        this.redRelay = cfg.redRelay;
        this.greenRelay = cfg.greenRelay;
        this.blueRelay = cfg.blueRelay;

        this.loadKey = cfg.loadKey;
        this.startPauseKey = cfg.startPauseKey;
        this.clearKey = cfg.clearKey;
    }

    /***************************************************************************
     * @param comp
     **************************************************************************/
    public void set( CompetitionConfig comp )
    {
        this.redRelay = comp.redRelay;
        this.greenRelay = comp.greenRelay;
        this.blueRelay = comp.blueRelay;

        this.loadKey = comp.loadKey;
        this.startPauseKey = comp.startPauseKey;
        this.clearKey = comp.clearKey;
    }
}
