package soinc.gravityvehicle.data;

import java.util.ArrayList;
import java.util.List;

/*******************************************************************************
 * 
 ******************************************************************************/
public class GvEventConfig
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

    /***************************************************************************
     * 
     **************************************************************************/
    public GvEventConfig()
    {
        this.periodTime = 6 * 60;
        this.periodWarning = 5 * 60;
        this.teams = new ArrayList<>();

        this.trackA = new TrackConfig();
    }

    /***************************************************************************
     * @param cfg
     **************************************************************************/
    public GvEventConfig( GvEventConfig cfg )
    {
        this();

        set( cfg );
    }

    /***************************************************************************
     * @param cfg
     **************************************************************************/
    public void set( GvEventConfig cfg )
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
    }
}
