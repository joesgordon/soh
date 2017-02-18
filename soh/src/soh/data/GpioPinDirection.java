package soh.data;

import org.jutils.INamedItem;

public enum GpioPinDirection implements INamedItem
{
    UNALLOCATED( "Unallocated" ),
    INPUT( "Input" ),
    OUTPUT( "Output" );

    public final String name;

    private GpioPinDirection( String name )
    {
        this.name = name;
    }

    @Override
    public String getName()
    {
        return name;
    }
}
