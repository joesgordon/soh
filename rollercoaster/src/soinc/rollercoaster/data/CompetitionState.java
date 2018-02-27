package soinc.rollercoaster.data;

import javax.swing.Icon;

import org.jutils.INamedItem;

import com.sun.prism.paint.Color;

import soinc.rollercoaster.RollercoasterIcons;

/*******************************************************************************
 * 
 ******************************************************************************/
public enum CompetitionState implements INamedItem
{
    /** No team has been loaded. */
    NO_TEAM( "New Run", Color.BLUE, "NO_TEAM.png" ),
    /** Competitors are not running for record. */
    AWAITING( "New Run", Color.BLUE, "AWAITING.png" ),
    /** Running before target time. */
    SCORE_TIME( "New Run", Color.BLUE, "SCORE_TIME.png" ),
    /** Running after target time before trial time. */
    PENALTY_TIME( "New Run", Color.BLUE, "PENALTY_TIME.png" ),
    /** Running after trial time. */
    FAILED_TIME( "New Run", Color.BLUE, "FAILED_TIME.png" ),
    /** 2 runs completed. */
    COMPLETE( "New Run", Color.BLUE, "COMPLETE.png" );

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
        this.icon = RollercoasterIcons.getIcon( iconName );
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
