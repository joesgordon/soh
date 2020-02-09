package soinc.ppp.data;

import java.awt.Color;

import javax.swing.Icon;

import org.jutils.INamedItem;

import soinc.ppp.PppIcons;

/*******************************************************************************
 * 
 ******************************************************************************/
public enum TrackState implements INamedItem
{
    /** No team has been loaded. */
    NO_TEAM( "New Run", Color.black, "NO_TEAM.png", false, 0x5 ),
    /** Team has been loaded, but the competition has not yet started. */
    LOADED( "Loaded", Color.black, "NO_TEAM.png", false, 0x7 ),
    /** Competitors are not running for record. */
    AWAITING( "Event", Color.black, "AWAITING.png", false, 0x3 ),
    /** Running before target time. */
    SCORE_TIME( "Score Time", new Color( 0x008000 ), "SCORE_TIME.png", true, 0x2 ),
    /** Running after target time before trial time. */
    PENALTY_TIME( "Penalty Time", new Color( 0xFFF200 ), Color.black, "PENALTY_TIME.png", true, 0x6 ),
    /** Running after trial time. */
    FAILED_TIME( "Failed", new Color( 0xFF0000 ), "FAILED_TIME.png", true, 0x4 ),
    /** 2 runs completed. */
    COMPLETE( "Complete", Color.black, new Color( 0x1F3FFF ), "COMPLETE.png", false, 0x1 );

    /**  */
    public final String name;
    /**  */
    public final Color background;
    /**  */
    public final Color foreground;
    /**  */
    public final Icon icon;
    /**  */
    public final boolean isRunning;
    /**  */
    public final int lights;

    /***************************************************************************
     * @param name
     * @param color
     * @param iconName
     **************************************************************************/
    private TrackState( String name, Color bg, String iconName,
        boolean isRunning, int lights )
    {
        this( name, bg, Color.white, iconName, isRunning, lights );
    }

    /***************************************************************************
     * @param name
     * @param color
     * @param iconName
     **************************************************************************/
    private TrackState( String name, Color bg, Color fg, String iconName,
        boolean isRunning, int lights )
    {
        this.name = name;
        this.background = bg;
        this.foreground = fg;
        this.icon = PppIcons.getResizedIcon( iconName, 0.65f );
        this.isRunning = isRunning;
        this.lights = ( lights ^ 0x4 );
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
