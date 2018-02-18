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
import soinc.lib.data.PinLevel;
import soinc.lib.data.PinResistance;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SciolyGpio
{
    /**  */
    public static boolean FAUX_CONNECT = true;

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
     * @param name
     * @param callback
     * @return
     **************************************************************************/
    public static GpioPinDigitalInput provisionInputPin( GpioController gpio,
        Pi3GpioPin pin, PinResistance res, String name, Runnable callback )
    {
        GpioPinDigitalInput inputPin = gpio.provisionDigitalInputPin( pin.hwPin,
            name, res.res );

        inputPin.setShutdownOptions( true, PinState.LOW,
            PinPullResistance.OFF );
        inputPin.addListener( new PhotogatePinListener( callback ) );

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
     * 
     **************************************************************************/
    private static final class PhotogatePinListener
        implements GpioPinListenerDigital
    {
        private final Runnable r;

        public PhotogatePinListener( Runnable r )
        {
            this.r = r;
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

            if( edge == PinEdge.RISING )
            {
                r.run();
            }
        }
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static GpioController getController()
    {
        return gpio;
    }
}
