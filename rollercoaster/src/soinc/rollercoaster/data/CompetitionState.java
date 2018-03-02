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
    NO_TEAM( "New Run", null, "NO_TEAM.png" ),
    /** Team has been loaded, but the competition has not yet started. */
    LOADED( "Loaded", null, "NO_TEAM.png" ),
    /** Competitors are not running for record. */
    AWAITING( "Event", null, "AWAITING.png" ),
    /** Running before target time. */
    SCORE_TIME( "Score Time", new Color( 0x22B14C ), "SCORE_TIME.png" ),
    /** Running after target time before trial time. */
    PENALTY_TIME( "Penalty Time", new Color( 0xFFF200 ), "PENALTY_TIME.png" ),
    /** Running after trial time. */
    FAILED_TIME( "Failed", new Color( 0xED1C24 ), "FAILED_TIME.png" ),
    /** 2 runs completed. */
    COMPLETE( "Complete", new Color( 0x3F48CC ), "COMPLETE.png" );

    /**  */
    public final String name;
    /**  */
    public final Color background;
    /**  */
    public final Icon icon;

    /***************************************************************************
     * @param name
     * @param color
     * @param iconName
     **************************************************************************/
    private CompetitionState( String name, Color color, String iconName )
    {
        this.name = name;
        this.background = color;
        this.icon = RcIcons.getIcon( iconName );
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
