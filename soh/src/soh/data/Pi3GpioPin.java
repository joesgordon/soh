package soh.data;

import org.jutils.INamedItem;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

/*******************************************************************************
 * 
 ******************************************************************************/
public enum Pi3GpioPin implements INamedItem
{
    GPIO_02( 2, Pi3Pin.PIN_03, RaspiPin.GPIO_08 ),
    GPIO_03( 3, Pi3Pin.PIN_05, RaspiPin.GPIO_09 ),
    GPIO_04( 4, Pi3Pin.PIN_07, RaspiPin.GPIO_07 ),
    GPIO_05( 5, Pi3Pin.PIN_29, RaspiPin.GPIO_21 ),
    GPIO_06( 6, Pi3Pin.PIN_31, RaspiPin.GPIO_22 ),
    GPIO_07( 7, Pi3Pin.PIN_26, RaspiPin.GPIO_11 ),
    GPIO_08( 8, Pi3Pin.PIN_24, RaspiPin.GPIO_10 ),
    GPIO_09( 9, Pi3Pin.PIN_21, RaspiPin.GPIO_13 ),
    GPIO_10( 10, Pi3Pin.PIN_19, RaspiPin.GPIO_12 ),
    GPIO_11( 11, Pi3Pin.PIN_23, RaspiPin.GPIO_14 ),
    GPIO_12( 12, Pi3Pin.PIN_32, RaspiPin.GPIO_26 ),
    GPIO_13( 13, Pi3Pin.PIN_33, RaspiPin.GPIO_23 ),
    GPIO_14( 14, Pi3Pin.PIN_08, RaspiPin.GPIO_15 ),
    GPIO_15( 15, Pi3Pin.PIN_10, RaspiPin.GPIO_16 ),
    GPIO_16( 16, Pi3Pin.PIN_36, RaspiPin.GPIO_27 ),
    GPIO_17( 17, Pi3Pin.PIN_11, RaspiPin.GPIO_00 ),
    GPIO_18( 18, Pi3Pin.PIN_12, RaspiPin.GPIO_01 ),
    GPIO_19( 19, Pi3Pin.PIN_35, RaspiPin.GPIO_24 ),
    GPIO_20( 20, Pi3Pin.PIN_38, RaspiPin.GPIO_28 ),
    GPIO_21( 21, Pi3Pin.PIN_40, RaspiPin.GPIO_29 ),
    GPIO_22( 22, Pi3Pin.PIN_15, RaspiPin.GPIO_03 ),
    GPIO_23( 23, Pi3Pin.PIN_16, RaspiPin.GPIO_04 ),
    GPIO_24( 24, Pi3Pin.PIN_18, RaspiPin.GPIO_05 ),
    GPIO_25( 25, Pi3Pin.PIN_22, RaspiPin.GPIO_06 ),
    GPIO_26( 26, Pi3Pin.PIN_37, RaspiPin.GPIO_25 ),
    GPIO_27( 27, Pi3Pin.PIN_13, RaspiPin.GPIO_02 );

    /**  */
    public final int gpioNum;
    /**  */
    public final Pi3Pin pin;
    /**  */
    public final Pin hwPin;

    /***************************************************************************
     * @param gpioNum
     * @param pin
     * @param hwPin
     **************************************************************************/
    private Pi3GpioPin( int gpioNum, Pi3Pin pin, Pin hwPin )
    {
        this.gpioNum = gpioNum;
        this.pin = pin;
        this.hwPin = hwPin;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getName()
    {
        return String.format( "GPIO %02d", gpioNum );
    }

    /***************************************************************************
     * @param pin
     * @return
     **************************************************************************/
    public static Pi3GpioPin getPin( Pi3Pin pin )
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
