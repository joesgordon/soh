package soh.gpio;

import org.jutils.io.LogUtils;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

import soh.data.Pi3GpioPin;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SohGpio
{
    /**  */
    public static boolean FAUX_CONNECT = true;
    /**  */
    private static final PinPullResistance PHOTOGATE_PULL_RES = PinPullResistance.OFF;

    /**  */
    private static GpioController gpio;

    /**  */
    private static GpioPinDigitalInput t1StartPin;
    /**  */
    private static GpioPinDigitalInput t1StopPin;
    /**  */
    private static GpioPinDigitalInput t2StartPin;
    /**  */
    private static GpioPinDigitalInput t2StopPin;

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
    public static void connect( Runnable startT1, Runnable stopT1,
        Runnable startT2, Runnable stopT2 ) throws IllegalStateException
    {
        if( t1StartPin != null )
        {
            return;
        }

        gpio = GpioFactory.getInstance();

        t1StartPin = provisionPin( gpio, Pi3GpioPin.GPIO_03.hwPin,
            "Track 1 Start Pin", startT1 );

        t1StopPin = provisionPin( gpio, Pi3GpioPin.GPIO_04.hwPin,
            "Track 1 Stop Pin", stopT1 );

        t2StartPin = provisionPin( gpio, Pi3GpioPin.GPIO_14.hwPin,
            "Track 2 Start Pin", startT2 );

        t2StopPin = provisionPin( gpio, Pi3GpioPin.GPIO_15.hwPin,
            "Track 2 Stop Pin", stopT2 );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static void disconnect()
    {
        if( t1StartPin != null )
        {
            gpio.unprovisionPin( t1StartPin );
            gpio.unprovisionPin( t1StopPin );
            gpio.unprovisionPin( t2StartPin );
            gpio.unprovisionPin( t2StopPin );
        }

        t1StartPin = null;
        t1StopPin = null;
        t2StartPin = null;
        t2StopPin = null;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static void startup() throws IllegalStateException
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
    private static GpioPinDigitalInput provisionPin( GpioController gpio,
        Pin pin, String name, Runnable callback )
    {
        GpioPinDigitalInput inputPin = gpio.provisionDigitalInputPin( pin, name,
            PHOTOGATE_PULL_RES );

        inputPin.setShutdownOptions( true );
        inputPin.addListener( new PhotogatePinListener( callback ) );

        return inputPin;
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
            GpioPin p = e.getPin();
            PinEdge edge = e.getEdge();

            LogUtils.printDebug( "GPIO pin %s (%d) is %s", p.getPin().getName(),
                p.getPin().getAddress(), edge.getName() );

            if( e.getEdge() == PinEdge.RISING )
            {
                r.run();
            }
        }
    }
}
