package soh.data;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import com.pi4j.io.gpio.Pin;

import soinc.lib.data.Pi3GpioPin;
import soinc.lib.data.Pi3Pin;

public class Pi3GpioPinTests
{
    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void ensureNoDuplicatePinDatas()
    {
        Pi3GpioPin [] pins = Pi3GpioPin.values();
        ArrayList<Pi3GpioPin> pinNumList = new ArrayList<>( pins.length );
        ArrayList<Pi3GpioPin> pinPinList = new ArrayList<>( pins.length );
        ArrayList<Pi3GpioPin> pinRaspiList = new ArrayList<>( pins.length );

        for( Pi3GpioPin pin : pins )
        {
            Pi3GpioPin prevPin;

            prevPin = containsPinout( pinNumList, pin.gpioNum );
            if( prevPin != null )
            {
                String msg = String.format(
                    "Pins %s and %s contain the same GPIO # %d", prevPin, pin,
                    pin.gpioNum );
                Assert.fail( msg );
            }
            pinNumList.add( pin );

            prevPin = containsPin( pinPinList, pin.pin );
            if( prevPin != null )
            {
                String msg = String.format(
                    "Pins %s and %s contain the same physical pin %d", prevPin,
                    pin, pin.pin.pinout );
                Assert.fail( msg );
            }
            pinPinList.add( pin );

            prevPin = containsRaspi( pinRaspiList, pin.hwPin );
            if( prevPin != null )
            {
                String msg = String.format(
                    "Pins %s and %s contain the same pin %s", prevPin, pin,
                    pin.pin );
                Assert.fail( msg );
            }
            pinRaspiList.add( pin );
        }
    }

    /***************************************************************************
     * @param pins
     * @param gpioNum
     * @return
     **************************************************************************/
    private static Pi3GpioPin containsPinout( ArrayList<Pi3GpioPin> pins,
        int gpioNum )
    {
        for( Pi3GpioPin pin : pins )
        {
            if( pin.gpioNum == gpioNum )
            {
                return pin;
            }
        }

        return null;
    }

    /***************************************************************************
     * @param pins
     * @param pin
     * @return
     **************************************************************************/
    private static Pi3GpioPin containsPin( ArrayList<Pi3GpioPin> pins,
        Pi3Pin pin )
    {
        for( Pi3GpioPin p : pins )
        {
            if( p.pin == pin )
            {
                return p;
            }
        }

        return null;
    }

    /***************************************************************************
     * @param pins
     * @param hwPin
     * @return
     **************************************************************************/
    private static Pi3GpioPin containsRaspi( ArrayList<Pi3GpioPin> pins,
        Pin hwPin )
    {
        for( Pi3GpioPin pin : pins )
        {
            if( pin.hwPin == hwPin )
            {
                return pin;
            }
        }

        return null;
    }
}
