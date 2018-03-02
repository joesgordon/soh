package soinc.rollercoaster.data;

import java.awt.Color;

import javax.swing.Icon;

import org.jutils.INamedItem;

import soinc.rollercoaster.RcIcons;

/*******************************************************************************
 * 
 ******************************************************************************/
public enum CompetitionState implements INamedItem
{
    /** No team has been loaded. */
    NO_TEAM( "New Run", Color.black, "NO_TEAM.png", false, 0x0 ),
    /** Team has been loaded, but the competition has not yet started. */
    LOADED( "Loaded", Color.black, "NO_TEAM.png", false, 0x7 ),
    /** Competitors are not running for record. */
    AWAITING( "Event", Color.black, "AWAITING.png", false, 0x3 ),
    /** Running before target time. */
    SCORE_TIME( "Score Time", new Color( 0x22B14C ), "SCORE_TIME.png", true, 0x2 ),
    /** Running after target time before trial time. */
    PENALTY_TIME( "Penalty Time", new Color( 0xFFF200 ), "PENALTY_TIME.png", true, 0x6 ),
    /** Running after trial time. */
    FAILED_TIME( "Failed", new Color( 0xED1C24 ), "FAILED_TIME.png", true, 0x4 ),
    /** 2 runs completed. */
    COMPLETE( "Complete", new Color( 0x3F48CC ), "COMPLETE.png", false, 0x5 );

    /**  */
    public final String name;
    /**  */
    public final Color background;
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
    private CompetitionState( String name, Color color, String iconName,
        boolean isRunning, int lights )
    {
        this.name = name;
        this.background = color;
        this.icon = RcIcons.getIcon( iconName );
        this.isRunning = isRunning;
        this.lights = lights;
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
