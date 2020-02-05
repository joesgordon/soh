package soinc.gravityvehicle.data;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinEdge;

import soinc.lib.gpio.SciolyGpio;

public class TrackPins
{
    /**  */
    public final TrackCfg config;

    /**  */
    public final GpioPinDigitalInput startPin;
    /**  */
    public final GpioPinDigitalInput stopPin;

    /**  */
    public final GpioPinDigitalOutput redPin;
    /**  */
    public final GpioPinDigitalOutput greenPin;
    /**  */
    public final GpioPinDigitalOutput bluePin;

    /***************************************************************************
     * @param gpio
     * @param config
     * @param start
     * @param stop
     **************************************************************************/
    public TrackPins( GpioController gpio, TrackCfg config, Runnable start,
        Runnable stop )
    {
        this.config = config;

        this.startPin = SciolyGpio.provisionInputPin( gpio, config.startPin,
            "Track Start", start, PinEdge.RISING );
        this.stopPin = SciolyGpio.provisionInputPin( gpio, config.stopPin,
            "Track Stop", stop, PinEdge.RISING );

        this.redPin = SciolyGpio.provisionOuputPin( gpio, config.redPin,
            "Track Red" );
        this.greenPin = SciolyGpio.provisionOuputPin( gpio, config.greenPin,
            "Track Green" );
        this.bluePin = SciolyGpio.provisionOuputPin( gpio, config.bluePin,
            "Track Blue" );
    }

    /***************************************************************************
     * @param gpio
     **************************************************************************/
    public void unprovisionAll( GpioController gpio )
    {
        gpio.unprovisionPin( startPin );
        gpio.unprovisionPin( stopPin );

        gpio.unprovisionPin( redPin );
        gpio.unprovisionPin( greenPin );
        gpio.unprovisionPin( bluePin );
    }
}
