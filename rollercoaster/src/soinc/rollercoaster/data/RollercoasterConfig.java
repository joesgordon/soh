package soinc.rollercoaster.data;

import java.util.ArrayList;
import java.util.List;

/*******************************************************************************
 * 
 ******************************************************************************/
public class RollercoasterConfig
{
    /**  */
    public int periodTime;
    /**
     * Time for the roller coaster to complete its track. Must be between 20 and
     * 45 seconds (in 5s intervals for regional, 2s intervals for state, and 1s
     * intervals for national tournaments).
     */
    public int targetTime;
    /**  */
    public int trialTimeout;
    /**  */
    public final List<String> teams;

    /***************************************************************************
     * 
     **************************************************************************/
    public RollercoasterConfig()
    {
        this.periodTime = 8 * 60;
        this.trialTimeout = 60;
        this.targetTime = 20;
        this.teams = new ArrayList<>();
    }

    /***************************************************************************
     * @param cfg
     **************************************************************************/
    public RollercoasterConfig( RollercoasterConfig cfg )
    {
        this();

        this.periodTime = cfg.periodTime;
        this.trialTimeout = cfg.trialTimeout;
        this.targetTime = cfg.targetTime;

        if( cfg.teams != null )
        {
            this.teams.addAll( cfg.teams );
        }
    }
}
