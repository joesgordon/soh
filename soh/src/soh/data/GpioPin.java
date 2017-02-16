package soh.data;

import org.jutils.INamedItem;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

/*******************************************************************************
 * 
 ******************************************************************************/
public enum GpioPin implements INamedItem
{
    GPIO_02( 2, Pinout.PIN_03, RaspiPin.GPIO_08 ),
    GPIO_03( 3, Pinout.PIN_05, RaspiPin.GPIO_09 ),
    GPIO_04( 4, Pinout.PIN_07, RaspiPin.GPIO_07 ),
    GPIO_05( 5, Pinout.PIN_29, RaspiPin.GPIO_21 ),
    GPIO_06( 6, Pinout.PIN_31, RaspiPin.GPIO_22 ),
    GPIO_07( 7, Pinout.PIN_26, RaspiPin.GPIO_11 ),
    GPIO_08( 8, Pinout.PIN_24, RaspiPin.GPIO_10 ),
    GPIO_09( 9, Pinout.PIN_21, RaspiPin.GPIO_13 ),
    GPIO_10( 10, Pinout.PIN_19, RaspiPin.GPIO_12 ),
    GPIO_11( 11, Pinout.PIN_23, RaspiPin.GPIO_14 ),
    GPIO_12( 12, Pinout.PIN_32, RaspiPin.GPIO_26 ),
    GPIO_13( 13, Pinout.PIN_33, RaspiPin.GPIO_23 ),
    GPIO_14( 14, Pinout.PIN_08, RaspiPin.GPIO_15 ),
    GPIO_15( 15, Pinout.PIN_10, RaspiPin.GPIO_16 ),
    GPIO_16( 16, Pinout.PIN_36, RaspiPin.GPIO_27 ),
    GPIO_17( 17, Pinout.PIN_11, RaspiPin.GPIO_00 ),
    GPIO_18( 18, Pinout.PIN_12, RaspiPin.GPIO_01 ),
    GPIO_19( 19, Pinout.PIN_35, RaspiPin.GPIO_24 ),
    GPIO_20( 20, Pinout.PIN_38, RaspiPin.GPIO_28 ),
    GPIO_21( 21, Pinout.PIN_40, RaspiPin.GPIO_29 ),
    GPIO_22( 22, Pinout.PIN_15, RaspiPin.GPIO_03 ),
    GPIO_23( 23, Pinout.PIN_16, RaspiPin.GPIO_04 ),
    GPIO_24( 24, Pinout.PIN_18, RaspiPin.GPIO_05 ),
    GPIO_25( 25, Pinout.PIN_22, RaspiPin.GPIO_06 ),
    GPIO_26( 26, Pinout.PIN_37, RaspiPin.GPIO_25 ),
    GPIO_27( 27, Pinout.PIN_13, RaspiPin.GPIO_02 );

    /**  */
    public final int gpioNum;
    /**  */
    public final Pinout pin;
    /**  */
    public final Pin hwPin;

    /***************************************************************************
     * @param gpioNum
     * @param pin
     * @param hwPin
     **************************************************************************/
    private GpioPin( int gpioNum, Pinout pin, Pin hwPin )
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
}
