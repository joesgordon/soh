package soinc.hovercraft.tasks;

import com.pi4j.io.gpio.GpioController;

import soinc.hovercraft.data.HoverConfig;
import soinc.lib.gpio.SciolyGpio;

/*******************************************************************************
 * 
 ******************************************************************************/
public class HovercraftCompetition
{
    /**  */
    public static HovercraftCompetition competition;

    /**  */
    public final TeamCompetition track1;
    /**  */
    public final TeamCompetition track2;

    /***************************************************************************
     * @param track1
     * @param track2
     **************************************************************************/
    public HovercraftCompetition( TeamCompetition track1,
        TeamCompetition track2 )
    {
        this.track1 = track1;
        this.track2 = track2;
    }

    /***************************************************************************
     * @param gpio
     **************************************************************************/
    public void unprovisionAll( GpioController gpio )
    {
        track1.unprovisionAll( gpio );
        track2.unprovisionAll( gpio );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static HovercraftCompetition connect( HoverConfig config )
        throws IllegalStateException
    {
        if( competition != null )
        {
            return null;
        }

        GpioController controller = SciolyGpio.startup();

        TeamCompetition tc1 = new TeamCompetition( config, config.track1,
            controller );
        TeamCompetition tc2 = new TeamCompetition( config, config.track2,
            controller );

        competition = new HovercraftCompetition( tc1, tc2 );

        return competition;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static void disconnect()
    {
        if( competition != null )
        {
            competition.unprovisionAll( SciolyGpio.getController() );
        }

        competition = null;
    }
}
