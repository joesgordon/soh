package soh.data;

import org.jutils.INamedItem;

/***************************************************************************
 * 
 **************************************************************************/
public enum Division implements INamedItem
{
    DIVISION_B( "Division B", 130 ),
    DIVISION_C( "Division C", 200 );

    /** The name of the division */
    public final String name;
    /** The default target time for the division in tenths of a second. */
    public final int defaultTargetTime;

    /***************************************************************************
     * @param name
     * @param defaultTargetTime
     **************************************************************************/
    private Division( String name, int defaultTargetTime )
    {
        this.name = name;
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
