package soinc.rollercoaster.data;

import java.util.List;

/*******************************************************************************
 * 
 ******************************************************************************/
public interface IRcCompetitionConfig
{
    /***************************************************************************
     * @return
     **************************************************************************/
    public int getPeriodTime();

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getTargetTime();

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getRunTimeout();

    /***************************************************************************
     * @return
     **************************************************************************/
    public List<RcTeam> getTeams();
}
