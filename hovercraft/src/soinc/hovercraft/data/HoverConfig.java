package soinc.hovercraft.data;

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
    public final TrackCfg track1;
    /**  */
    public final TrackCfg track2;
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

        this.track1 = new TrackCfg();
        this.track2 = new TrackCfg( true );

        this.teams = new ArrayList<>();

        divB.targetLength = 165;
        divB.targetTime = 150;

        divC.targetLength = 175;
        divC.targetTime = 150;

        track1.division = Division.DIVISION_B;
        track2.division = Division.DIVISION_C;
    }

    /***************************************************************************
     * @param config
     **************************************************************************/
    public HoverConfig( HoverConfig config )
    {
        this.periodTime = config.periodTime;

        this.divB = new DivisionConfig( config.divB );
        this.divC = new DivisionConfig( config.divC );

        this.track1 = config.track1 == null ? new TrackCfg()
            : new TrackCfg( config.track1 );
        this.track2 = config.track2 == null ? new TrackCfg( true )
            : new TrackCfg( config.track2 );

        this.teams = new ArrayList<>();

        if( config.teams != null )
        {
            for( Team t : config.teams )
            {
                teams.add( new Team( t ) );
            }
        }
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

    /***************************************************************************
     * @param division
     * @return
     **************************************************************************/
    public List<Team> getAvailableTeams( Division division )
    {
        List<Team> teams = new ArrayList<>();

        for( Team t : this.teams )
        {
            if( !t.loaded && !t.isFinished() && t.div == division )
            {
                teams.add( t );
            }
        }

        return teams;
    }
}
