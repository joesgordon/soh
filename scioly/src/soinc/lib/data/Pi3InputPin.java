package soinc.lib.data;

/*******************************************************************************
 * Defines an input GPIO pin.
 ******************************************************************************/
public class Pi3InputPin
{
    /** The GPIO pin to be set as an input. */
    public Pi3GpioPin gpioPin;
    /** The resistance of the GPIO pin. */
    public PinResistance resistance;

    /***************************************************************************
     * Creates a new input pin with {@link Pi3GpioPin#GPIO_02} assigned with
     * resistance {@link PinResistance#OFF}.
     **************************************************************************/
    public Pi3InputPin()
    {
        this( Pi3GpioPin.GPIO_02, PinResistance.OFF );
    }

    /***************************************************************************
     * Creates a new input pin with the provided GPIO pin and resistance.
     * @param gpioPin the GPIO pin assigned for input.
     * @param resistance the resistance of the GPIO pin.
     **************************************************************************/
    public Pi3InputPin( Pi3GpioPin gpioPin, PinResistance resistance )
    {
        this.gpioPin = gpioPin;
        this.resistance = resistance;
    }

    /***************************************************************************
     * Sets this pin according to the provided pin (makes a copy in effect).
     * @param pin the pin to copy; does nothing if {@code null}.
     **************************************************************************/
    public void set( Pi3InputPin pin )
    {
        if( pin != null )
        {
            this.gpioPin = pin.gpioPin;
            this.resistance = pin.resistance;
        }
    }
}
