package soinc.lib.data;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import org.jutils.INamedItem;

import com.pi4j.io.gpio.PinMode;

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

    public static List<GpioPinDirection> getValues( EnumSet<PinMode> modes )
    {
        List<GpioPinDirection> gpds = new ArrayList<>( values().length );

        gpds.add( UNALLOCATED );

        for( PinMode mode : modes )
        {
            if( mode == PinMode.DIGITAL_INPUT )
            {
                gpds.add( INPUT );
            }
            else if( mode == PinMode.DIGITAL_OUTPUT )
            {
                gpds.add( OUTPUT );
            }
        }

        return gpds;
    }
}
