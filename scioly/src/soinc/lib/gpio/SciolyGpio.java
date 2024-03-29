package soinc.lib.gpio;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinEdge;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

import soinc.lib.data.Pi3GpioPin;
import soinc.lib.data.Pi3InputPin;
import soinc.lib.data.Pi3OutputPin;
import soinc.lib.data.PinLevel;
import soinc.lib.data.PinResistance;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SciolyGpio
{
    /**  */
    public static boolean FAUX_CONNECT = false;

    /**  */
    private static GpioController gpio;

    /***************************************************************************
     * 
     **************************************************************************/
    private static void setProvider()
    {
        if( FAUX_CONNECT )
        {
            FauxGpioProvider gp = new FauxGpioProvider();
            GpioFactory.setDefaultProvider( gp );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static GpioController startup() throws IllegalStateException
    {
        if( gpio == null )
        {
            try
            {
                setProvider();
                gpio = GpioFactory.getInstance();
            }
            catch( UnsatisfiedLinkError ex )
            {
                throw new IllegalStateException(
                    "Pi4j library was not found: " + ex.getMessage(), ex );
            }
        }

        return gpio;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static void shutdown()
    {
        if( gpio != null )
        {
            gpio.shutdown();
        }

        gpio = null;
    }

    /***************************************************************************
     * @param gpio
     * @param pin
     * @param shortName
     * @param callback
     * @return
     **************************************************************************/
    public static GpioPinDigitalInput provisionInputPin( GpioController gpio,
        Pi3InputPin pin, String shortName, Runnable callback, PinEdge onEdge )
    {
        String name = shortName + ":" + pin.gpioPin.pin.pinout;
        return provisionInputPin( gpio, pin.gpioPin, pin.resistance, name,
            callback, onEdge );
    }

    /***************************************************************************
     * @param gpio
     * @param pin
     * @param name
     * @param callback
     * @return
     **************************************************************************/
    public static GpioPinDigitalInput provisionInputPin( GpioController gpio,
        Pi3GpioPin pin, PinResistance res, String name, Runnable callback,
        PinEdge onEdge )
    {
        GpioPinDigitalInput inputPin = gpio.provisionDigitalInputPin( pin.hwPin,
            name, res.res );

        inputPin.setShutdownOptions( true, PinState.LOW,
            PinPullResistance.OFF );
        inputPin.addListener( new Pi3PinListener( callback, onEdge ) );

        return inputPin;
    }

    /***************************************************************************
     * @param gpio
     * @param pin
     * @param defaultLevel
     * @param name
     * @return
     **************************************************************************/
    public static GpioPinDigitalOutput provisionOuputPin( GpioController gpio,
        Pi3GpioPin pin, PinLevel defaultLevel, String name )
    {
        GpioPinDigitalOutput outputPin = gpio.provisionDigitalOutputPin(
            pin.hwPin, name, defaultLevel.state );

        outputPin.setShutdownOptions( true, defaultLevel.state,
            PinPullResistance.OFF );

        return outputPin;
    }

    /***************************************************************************
     * @param gpio
     * @param pin
     * @param shortName
     * @return
     **************************************************************************/
    public static GpioPinDigitalOutput provisionOuputPin( GpioController gpio,
        Pi3OutputPin pin, String shortName )
    {
        return provisionOuputPin( gpio, pin.gpioPin, pin.level,
            shortName + ":" + pin.gpioPin.pin.pinout );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static GpioController getController()
    {
        return gpio;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class Pi3PinListener implements GpioPinListenerDigital
    {
        private final Runnable callback;
        private final PinEdge onEdge;

        // public Pi3PinListener( Runnable callback )
        // {
        // this( callback, PinEdge.RISING );
        // }

        public Pi3PinListener( Runnable callback, PinEdge onEdge )
        {
            this.callback = callback;
            this.onEdge = onEdge;
        }

        @Override
        public void handleGpioPinDigitalStateChangeEvent(
            GpioPinDigitalStateChangeEvent e )
        {
            // GpioPin p = e.getPin();
            PinEdge edge = e.getEdge();

            // LogUtils.printDebug( "GPIO pin %s (%d) is %s",
            // p.getPin().getName(),
            // p.getPin().getAddress(), edge.getName() );

            if( onEdge == null || edge == onEdge )
            {
                callback.run();
            }
        }
    }
}
