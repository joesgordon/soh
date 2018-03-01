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
    NO_TEAM( "New Run", Color.blue, "NO_TEAM.png" ),
    /** Team has been loaded, but the competition has not yet started. */
    LOADED( "Loaded", Color.blue, "NO_TEAM.png" ),
    /** Competitors are not running for record. */
    AWAITING( "New Run", Color.blue, "AWAITING.png" ),
    /** Running before target time. */
    SCORE_TIME( "New Run", Color.blue, "SCORE_TIME.png" ),
    /** Running after target time before trial time. */
    PENALTY_TIME( "New Run", Color.blue, "PENALTY_TIME.png" ),
    /** Running after trial time. */
    FAILED_TIME( "New Run", Color.blue, "FAILED_TIME.png" ),
    /** 2 runs completed. */
    COMPLETE( "New Run", Color.blue, "COMPLETE.png" );

    /**  */
    public final String name;
    /**  */
    public final Color color;
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
        this.color = color;
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
