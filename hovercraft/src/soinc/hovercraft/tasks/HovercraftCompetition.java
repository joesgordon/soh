package soinc.hovercraft.tasks;

import com.pi4j.io.gpio.GpioController;

import soinc.hovercraft.data.HoverConfig;
import soinc.lib.gpio.SciolyGpio;

public class HovercraftCompetition
{
    /**  */
    public static HovercraftCompetition competition;

    /**  */
    public final TrackCompetition track1;
    /**  */
    public final TrackCompetition track2;

    public HovercraftCompetition( TrackCompetition track1,
        TrackCompetition track2 )
    {
        this.track1 = track1;
        this.track2 = track2;
    }

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

        TrackCompetition tc1 = new TrackCompetition( config, config.track1,
            controller );
        TrackCompetition tc2 = new TrackCompetition( config, config.track2,
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
