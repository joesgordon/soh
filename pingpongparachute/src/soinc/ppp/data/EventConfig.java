package soinc.ppp.data;

import java.util.ArrayList;
import java.util.List;

import soinc.lib.data.Pi3GpioPin;

/*******************************************************************************
 * 
 ******************************************************************************/
public class EventConfig
{
    /** The number of seconds each team has to execute 2 launches. */
    public int periodTime;

    /**  */
    public final List<Team> teams;

    /**  */
    public final TrackConfig trackA;
    /**  */
    public final TrackConfig trackB;

    /***************************************************************************
     * 
     **************************************************************************/
    public EventConfig()
    {
        this.periodTime = 8 * 60;

        this.trackA = new TrackConfig();
        this.trackB = new TrackConfig();

        this.teams = new ArrayList<>();

        trackB.timer1Out.gpioPin = Pi3GpioPin.GPIO_14;
        trackB.timer1In.gpioPin = Pi3GpioPin.GPIO_15;

        trackB.timer2Out.gpioPin = Pi3GpioPin.GPIO_23;
        trackB.timer2Out.gpioPin = Pi3GpioPin.GPIO_24;
    }

    /***************************************************************************
     * @param cfg
     **************************************************************************/
    public EventConfig( EventConfig cfg )
    {
        this();

        set( cfg );
    }

    /***************************************************************************
     * @param cfg
     **************************************************************************/
    public void set( EventConfig cfg )
    {
        this.periodTime = cfg.periodTime;

        this.trackA.set( cfg.trackA );
        this.trackB.set( cfg.trackB );

        this.teams.clear();
        if( cfg.teams != null )
        {
            this.teams.addAll( cfg.teams );
        }
    }
}
