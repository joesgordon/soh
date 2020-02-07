package soinc.boomilever.data;

import java.util.ArrayList;
import java.util.List;

/*******************************************************************************
 * 
 ******************************************************************************/
public class BlEventConfig
{
    /** The number of seconds in the trial period. */
    public int periodTime;
    /**
     * The number of seconds before warning the competitors that time is running
     * out. Must be less than period time.
     */
    public int periodWarning;
    /**  */
    public final List<Team> teams;
    /**  */
    public final TrackConfig trackA;
    /**  */
    public final TrackConfig trackB;

    /***************************************************************************
     * 
     **************************************************************************/
    public BlEventConfig()
    {
        this.periodTime = 6 * 60;
        this.periodWarning = 5 * 60;
        this.teams = new ArrayList<>();

        this.trackA = new TrackConfig();
        this.trackB = new TrackConfig();

        trackB.powerRelay = 5;
        trackB.redRelay = 6;
        trackB.greenRelay = 7;
        trackB.blueRelay = 8;
    }

    /***************************************************************************
     * @param cfg
     **************************************************************************/
    public BlEventConfig( BlEventConfig cfg )
    {
        this();

        set( cfg );
    }

    /***************************************************************************
     * @param cfg
     **************************************************************************/
    public void set( BlEventConfig cfg )
    {
        if( this == cfg )
        {
            return;
        }

        this.periodTime = cfg.periodTime;
        this.periodWarning = cfg.periodWarning;

        this.teams.clear();
        if( cfg.teams != null )
        {
            this.teams.addAll( cfg.teams );
        }

        this.trackA.set( cfg.trackA );
        this.trackB.set( cfg.trackB );
    }
}
