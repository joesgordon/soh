package soinc.gravityvehicle.data;

import org.jutils.INamedItem;

/***************************************************************************
 * 
 **************************************************************************/
public enum Division implements INamedItem
{
    DIVISION_B( "Division B", 'B', 130 ),
    DIVISION_C( "Division C", 'C', 200 );

    /** The name of the division */
    public final String name;
    /**  */
    public final char designation;
    /** The default target time for the division in tenths of a second. */
    public final int defaultTargetTime;

    /***************************************************************************
     * @param name
     * @param designation
     * @param defaultTargetTime
     **************************************************************************/
    private Division( String name, char designation, int defaultTargetTime )
    {
        this.name = name;
        this.designation = designation;
        this.defaultTargetTime = defaultTargetTime;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getName()
    {
        return name;
    }
}
