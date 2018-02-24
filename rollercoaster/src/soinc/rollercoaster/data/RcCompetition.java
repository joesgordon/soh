package soinc.rollercoaster.data;

import java.util.ArrayList;
import java.util.List;

/*******************************************************************************
 * 
 ******************************************************************************/
public class RcCompetition
{
    /**  */
    public final RollercoasterConfig config;
    /**  */
    public final List<RcTeam> teams;

    /***************************************************************************
     * @param config
     **************************************************************************/
    public RcCompetition( RollercoasterConfig config )
    {
        this.config = config;
        this.teams = new ArrayList<>();
    }

    public void connect()
    {
        // TODO Auto-generated method stub
    }

    public boolean isRunning()
    {
        // TODO Auto-generated method stub
        return false;
    }

    public void disconnect()
    {
        // TODO Auto-generated method stub

    }
}
