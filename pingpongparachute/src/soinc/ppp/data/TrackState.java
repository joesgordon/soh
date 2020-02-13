package soinc.ppp.data;

import java.awt.Color;

import org.jutils.INamedItem;

import soinc.lib.relay.Relays;

/*******************************************************************************
 * 
 ******************************************************************************/
public enum TrackState implements INamedItem
{
    /** No team has been loaded. */
    NO_TEAM( "No Team", new Color( 0x3A6EA7 ), false, Relays.BLUE_LIGHT ),
    /** Team has been loaded, but the competition has not yet started. */
    LOADED( "Loaded", Color.white, Color.black, false, Relays.WHITE_LIGHT ),
    /** The competition has been started.. */
    RUNNING( "Running", new Color( 0x008000 ), true, Relays.GREEN_LIGHT ),
    /** The rocket has been launched */
    LAUNCHED( "Running", new Color( 0x008000 ), Color.white, true,
        Relays.GREEN_LIGHT, true ),
    /** The timer has reached the warning limit. */
    WARNING( "Warning", Color.yellow, Color.black, true, Relays.YELLOW_LIGHT ),
    /** 2 runs completed. */
    COMPLETE( "Complete", Color.red, Color.black, false, Relays.RED_LIGHT ),
    /** If for any reason the event supervisor pauses the timer. */
    PAUSED( "Paused", new Color( 0xCC00CC ), false, Relays.PURPLE_LIGHT );

    /**  */
    public final String name;
    /**  */
    public final Color background;
    /**  */
    public final Color foreground;
    /**  */
    public final boolean isRunning;
    /**  */
    public final int lights;
    /**  */
    public final boolean isFlashing;

    /***************************************************************************
     * @param name
     * @param bg
     * @param isRunning
     * @param lights
     **************************************************************************/
    private TrackState( String name, Color bg, boolean isRunning, int lights )
    {
        this( name, bg, Color.white, isRunning, lights );
    }

    /***************************************************************************
     * @param name
     * @param bg
     * @param fg
     * @param isRunning
     * @param lights
     * @param color
     **************************************************************************/
    private TrackState( String name, Color bg, Color fg, boolean isRunning,
        int lights )
    {
        this( name, bg, fg, isRunning, lights, false );
    }

    /***************************************************************************
     * @param name
     * @param bg
     * @param fg
     * @param isRunning
     * @param lights
     * @param isFlashing
     **************************************************************************/
    private TrackState( String name, Color bg, Color fg, boolean isRunning,
        int lights, boolean isFlashing )
    {
        this.name = name;
        this.background = bg;
        this.foreground = fg;
        this.isRunning = isRunning;
        this.lights = lights;
        this.isFlashing = isFlashing;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return name;
    }
}
