package soinc.boomilever.data;

import java.awt.Color;

import org.jutils.INamedItem;

import soinc.lib.relay.Relays;

/*******************************************************************************
 * 
 ******************************************************************************/
public enum CompetitionState implements INamedItem
{
    /** No team has been loaded. */
    NO_TEAM( "No Team", Color.black, false, Relays.BLUE_LIGHT ),
    /** Team has been loaded, but the competition has not yet started. */
    LOADED( "Loaded", new Color( 0x3A6EA7 ), false, Relays.WHITE_LIGHT ),
    /** The competition has been started.. */
    RUNNING( "Running", new Color( 0x008000 ), true, Relays.GREEN_LIGHT ),
    /** The timer has reached the warning limit. */
    WARNING( "Warning", new Color( 0x008000 ), true, Relays.YELLOW_LIGHT ),
    /** 2 runs completed. */
    COMPLETE( "Complete", Color.red, Color.black, false, Relays.RED_LIGHT ),
    /** If for any reason the event supervisor pauses the timer. */
    PAUSED( "Paused", Color.yellow, Color.black, false, Relays.PURPLE_LIGHT );

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

    /***************************************************************************
     * @param name
     * @param color
     * @param iconName
     **************************************************************************/
    private CompetitionState( String name, Color bg, boolean isRunning,
        int lights )
    {
        this( name, bg, Color.white, isRunning, lights );
    }

    /***************************************************************************
     * @param name
     * @param bg
     * @param fg
     * @param isRunning
     * @param lights
     **************************************************************************/
    private CompetitionState( String name, Color bg, Color fg,
        boolean isRunning, int lights )
    {
        this.name = name;
        this.background = bg;
        this.foreground = fg;
        this.isRunning = isRunning;
        this.lights = ( lights ^ 0x4 ) & 0x7;
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
