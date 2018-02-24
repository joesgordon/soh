package soinc.lib.data;

import org.jutils.INamedItem;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

/*******************************************************************************
 * Defines the set of PI GPIO pins and couples them with the pi4j enumerations
 * (which were named according to a previous PI).
 * @see <a href="http://pi4j.com/pins/model-3b-rev1.html">Pin Numbering</a>.
 ******************************************************************************/
public enum Pi3GpioPin implements INamedItem
{
    GPIO_02( 2, Pi3HeaderPin.PIN_03, RaspiPin.GPIO_08 ),
    GPIO_03( 3, Pi3HeaderPin.PIN_05, RaspiPin.GPIO_09 ),
    GPIO_04( 4, Pi3HeaderPin.PIN_07, RaspiPin.GPIO_07 ),
    GPIO_05( 5, Pi3HeaderPin.PIN_29, RaspiPin.GPIO_21 ),
    GPIO_06( 6, Pi3HeaderPin.PIN_31, RaspiPin.GPIO_22 ),
    GPIO_07( 7, Pi3HeaderPin.PIN_26, RaspiPin.GPIO_11 ),
    GPIO_08( 8, Pi3HeaderPin.PIN_24, RaspiPin.GPIO_10 ),
    GPIO_09( 9, Pi3HeaderPin.PIN_21, RaspiPin.GPIO_13 ),
    GPIO_10( 10, Pi3HeaderPin.PIN_19, RaspiPin.GPIO_12 ),
    GPIO_11( 11, Pi3HeaderPin.PIN_23, RaspiPin.GPIO_14 ),
    GPIO_12( 12, Pi3HeaderPin.PIN_32, RaspiPin.GPIO_26 ),
    GPIO_13( 13, Pi3HeaderPin.PIN_33, RaspiPin.GPIO_23 ),
    GPIO_14( 14, Pi3HeaderPin.PIN_08, RaspiPin.GPIO_15 ),
    GPIO_15( 15, Pi3HeaderPin.PIN_10, RaspiPin.GPIO_16 ),
    GPIO_16( 16, Pi3HeaderPin.PIN_36, RaspiPin.GPIO_27 ),
    GPIO_17( 17, Pi3HeaderPin.PIN_11, RaspiPin.GPIO_00 ),
    GPIO_18( 18, Pi3HeaderPin.PIN_12, RaspiPin.GPIO_01 ),
    GPIO_19( 19, Pi3HeaderPin.PIN_35, RaspiPin.GPIO_24 ),
    GPIO_20( 20, Pi3HeaderPin.PIN_38, RaspiPin.GPIO_28 ),
    GPIO_21( 21, Pi3HeaderPin.PIN_40, RaspiPin.GPIO_29 ),
    GPIO_22( 22, Pi3HeaderPin.PIN_15, RaspiPin.GPIO_03 ),
    GPIO_23( 23, Pi3HeaderPin.PIN_16, RaspiPin.GPIO_04 ),
    GPIO_24( 24, Pi3HeaderPin.PIN_18, RaspiPin.GPIO_05 ),
    GPIO_25( 25, Pi3HeaderPin.PIN_22, RaspiPin.GPIO_06 ),
    GPIO_26( 26, Pi3HeaderPin.PIN_37, RaspiPin.GPIO_25 ),
    GPIO_27( 27, Pi3HeaderPin.PIN_13, RaspiPin.GPIO_02 );

    /** The GPIO number for this pin. */
    public final int gpioNum;
    /** The header pin for this GPIO pin. */
    public final Pi3HeaderPin pin;
    /** The pi4j pin for this GPIO pin. */
    public final Pin hwPin;

    /***************************************************************************
     * Creates a new GPIO pin with the provided parameters.
     * @param gpioNum the GPIO number for this pin.
     * @param pin the header pin for this GPIO pin.
     * @param hwPin the pi4j pin for this GPIO pin.
     **************************************************************************/
    private Pi3GpioPin( int gpioNum, Pi3HeaderPin pin, Pin hwPin )
    {
        this.gpioNum = gpioNum;
        this.pin = pin;
        this.hwPin = hwPin;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return String.format( "GPIO %02d", gpioNum );
    }

    /***************************************************************************
     * Finds the GPIO pin that matches the provided header pin.
     * @param pin the header pin to be matched.
     * @return the GPIO pin found or {@code null} if non found (when specifying
     * a header pin that is not allocated as GPIO).
     **************************************************************************/
    public static Pi3GpioPin getPin( Pi3HeaderPin pin )
    {
        for( Pi3GpioPin p : values() )
        {
            if( p.pin == pin )
            {
                return p;
            }
        }

        return null;
    }
}
