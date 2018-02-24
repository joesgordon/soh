package soinc.lib.data;

/*******************************************************************************
 * Defines an output GPIO pin with a starting/current output level.
 ******************************************************************************/
public class Pi3OutputPin
{
    /** The GPIO pin to be set as an output. */
    public Pi3GpioPin gpioPin;
    /** The default level of the output pin. */
    public PinLevel level;

    /***************************************************************************
     * Creates a new output pin with {@link Pi3GpioPin#GPIO_02} assigned with
     * output level {@link PinLevel#LOW}.
     **************************************************************************/
    public Pi3OutputPin()
    {
        this.gpioPin = Pi3GpioPin.GPIO_02;
        this.level = PinLevel.LOW;
    }

    /***************************************************************************
     * Creates a new output pin with the provided GPIO pin and level.
     * @param gpioPin the GPIO pin assigned for input.
     * @param level the default level of the output pin.
     **************************************************************************/
    public Pi3OutputPin( Pi3GpioPin gpioPin, PinLevel level )
    {
        this.gpioPin = gpioPin;
        this.level = level;
    }

    /***************************************************************************
     * Sets this pin according to the provided pin (makes a copy in effect).
     * @param pin the pin to copy; does nothing if {@code null}.
     **************************************************************************/
    public void set( Pi3OutputPin pin )
    {
        if( pin != null )
        {
            this.gpioPin = pin.gpioPin;
            this.level = pin.level;
        }
    }
}
