package soinc.boomilever.data;

import java.util.ArrayList;
import java.util.List;

import soinc.lib.relay.Relays;
import soinc.lib.ui.PhysicalKey;

/*******************************************************************************
 * 
 ******************************************************************************/
public class CompetitionConfig
{
    /** The number of seconds in the trial period. */
    public int periodTime;
    /**
     * The number of seconds before warning the competitors that time is running
     * out. Must be less than period time.
     */
    public int periodWarning;

    /**  */
    public int redRelay;
    /**  */
    public int greenRelay;
    /**  */
    public int blueRelay;

    /**  */
    public PhysicalKey startPauseKey;
    /**  */
    public PhysicalKey clearKey;

    /**  */
    public final List<BlTeam> teams;

    /***************************************************************************
     * 
     **************************************************************************/
    public CompetitionConfig()
    {
        this.periodTime = 6 * 60;
        this.periodWarning = 5 * 60;

        this.redRelay = Relays.RED_MASK;
        this.greenRelay = Relays.GREEN_MASK;
        this.blueRelay = Relays.BLUE_MASK;

        this.startPauseKey = PhysicalKey.SPACE;
        this.clearKey = PhysicalKey.ENTER;

        this.teams = new ArrayList<>();
    }

    /***************************************************************************
     * @param cfg
     **************************************************************************/
    public CompetitionConfig( CompetitionConfig cfg )
    {
        this();

        this.periodTime = cfg.periodTime;
        this.periodWarning = cfg.periodWarning;

        this.redRelay = cfg.redRelay;
        this.greenRelay = cfg.greenRelay;
        this.blueRelay = cfg.blueRelay;

        this.startPauseKey = cfg.startPauseKey;
        this.clearKey = cfg.clearKey;

        if( cfg.teams != null )
        {
            this.teams.addAll( cfg.teams );
        }
    }
}
