package soh.gpio;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

import soh.data.*;
import soh.tasks.HovercraftCompetition;
import soh.tasks.TrackCompetition;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SohGpio
{
    /**  */
    public static boolean FAUX_CONNECT = true;

    /**  */
    private static GpioController gpio;

    /**  */
    public static HovercraftCompetition competition;

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

    /**
     * @return *************************************************************************
     **************************************************************************/
    public static HovercraftCompetition connect( HoverConfig config )
        throws IllegalStateException
    {
        if( competition != null )
        {
            return null;
        }

        startup();

        TrackCompetition tc1 = new TrackCompetition( config, config.track1,
            gpio );
        TrackCompetition tc2 = new TrackCompetition( config, config.track2,
            gpio );

        competition = new HovercraftCompetition( tc1, tc2 );

        return competition;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static void disconnect()
    {
        if( competition != null )
        {
            competition.unprovisionAll( gpio );
        }

        competition = null;
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
    static GpioPinDigitalInput provisionInputPin( GpioController gpio,
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
    static GpioPinDigitalOutput provisionOuputPin( GpioController gpio,
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
}
