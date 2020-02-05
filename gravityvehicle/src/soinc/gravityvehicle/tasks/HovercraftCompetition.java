package soinc.gravityvehicle.tasks;

import com.pi4j.io.gpio.GpioController;

import soinc.gravityvehicle.data.HoverConfig;
import soinc.lib.gpio.SciolyGpio;

/*******************************************************************************
 * 
 ******************************************************************************/
public class HovercraftCompetition
{
    /**  */
    public static HovercraftCompetition competition;

    /**  */
    public final HcTeamCompetition track1;
    /**  */
    public final HcTeamCompetition track2;

    /***************************************************************************
     * @param track1
     * @param track2
     **************************************************************************/
    public HovercraftCompetition( HcTeamCompetition track1,
        HcTeamCompetition track2 )
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

        HcTeamCompetition tc1 = new HcTeamCompetition( config, config.track1,
            controller );
        HcTeamCompetition tc2 = new HcTeamCompetition( config, config.track2,
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
