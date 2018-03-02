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
    NO_TEAM( "New Run", null, "NO_TEAM.png", false ),
    /** Team has been loaded, but the competition has not yet started. */
    LOADED( "Loaded", null, "NO_TEAM.png", false ),
    /** Competitors are not running for record. */
    AWAITING( "Event", null, "AWAITING.png", false ),
    /** Running before target time. */
    SCORE_TIME( "Score Time", new Color( 0x22B14C ), "SCORE_TIME.png", true ),
    /** Running after target time before trial time. */
    PENALTY_TIME( "Penalty Time", new Color( 0xFFF200 ), "PENALTY_TIME.png", true ),
    /** Running after trial time. */
    FAILED_TIME( "Failed", new Color( 0xED1C24 ), "FAILED_TIME.png", true ),
    /** 2 runs completed. */
    COMPLETE( "Complete", new Color( 0x3F48CC ), "COMPLETE.png", false );

    /**  */
    public final String name;
    /**  */
    public final Color background;
    /**  */
    public final Icon icon;
    /**  */
    public final boolean isRunning;

    /***************************************************************************
     * @param name
     * @param color
     * @param iconName
     **************************************************************************/
    private CompetitionState( String name, Color color, String iconName,
        boolean isRunning )
    {
        this.name = name;
        this.background = color;
        this.icon = RcIcons.getIcon( iconName );
        this.isRunning = isRunning;
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
