package soinc.hovercraft.data;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import soinc.lib.data.Pi3Pin;

public class Pi3PinTests
{
    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void ensureNoDuplicatePins()
    {
        Pi3Pin [] pins = Pi3Pin.values();
        ArrayList<Pi3Pin> pinList = new ArrayList<>( pins.length );

        for( Pi3Pin pin : pins )
        {
            Pi3Pin prevPin = containsPinout( pinList, pin.pinout );

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
    private static Pi3Pin containsPinout( ArrayList<Pi3Pin> pins, int pinout )
    {
        for( Pi3Pin pin : pins )
        {
            if( pin.pinout == pinout )
            {
                return pin;
            }
        }

        return null;
    }
}
