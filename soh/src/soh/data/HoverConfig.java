package soh.data;

import java.util.ArrayList;
import java.util.List;

/*******************************************************************************
 * 
 ******************************************************************************/
public class HoverConfig
{
    /**  */
    public final int periodTime;
    /**  */
    public final DivisionConfig divB;
    /**  */
    public final DivisionConfig divC;
    /**  */
    public final List<Team> teams;

    /***************************************************************************
     * 
     **************************************************************************/
    public HoverConfig()
    {
        this.periodTime = 8 * 60;
        this.divB = new DivisionConfig( Division.DIVISION_B );
        this.divC = new DivisionConfig( Division.DIVISION_C );

        this.teams = new ArrayList<>();
    }

    /***************************************************************************
     * @param d
     * @return
     **************************************************************************/
    public DivisionConfig getDivision( Division d )
    {
        switch( d )
        {
            case DIVISION_B:
                return divB;
            case DIVISION_C:
                return divC;
        }

        return null;
    }
}
