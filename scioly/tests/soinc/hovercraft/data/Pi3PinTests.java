package soinc.hovercraft.data;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import soinc.lib.data.Pi3HeaderPin;

public class Pi3PinTests
{
    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void ensureNoDuplicatePins()
    {
        Pi3HeaderPin [] pins = Pi3HeaderPin.values();
        ArrayList<Pi3HeaderPin> pinList = new ArrayList<>( pins.length );

        for( Pi3HeaderPin pin : pins )
        {
            Pi3HeaderPin prevPin = containsPinout( pinList, pin.pinout );

            if( prevPin != null )
            {
                String msg = String.format(
                    "Pins %s and %s contain the same pinout # %d", prevPin, pin,
                    pin.pinout );
                Assert.fail( msg );
            }

            pinList.add( pin );
        }
    }

    /***************************************************************************
     * @param pins
     * @param pinout
     * @return
     **************************************************************************/
    private static Pi3HeaderPin containsPinout( ArrayList<Pi3HeaderPin> pins, int pinout )
    {
        for( Pi3HeaderPin pin : pins )
        {
            if( pin.pinout == pinout )
            {
                return pin;
            }
        }

        return null;
    }
}
