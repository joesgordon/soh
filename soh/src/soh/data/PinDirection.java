package soh.data;

import org.jutils.INamedItem;

public enum PinDirection implements INamedItem
{
    UNALLOCATED( "Unallocated" ),
    INPUT( "Input" ),
    OUTPUT( "Output" );

    public final String name;

    private PinDirection( String name )
    {
        this.name = name;
    }

    @Override
    public String getName()
    {
        return name;
    }
}
