package soh.gpio;

import com.pi4j.io.gpio.*;

import soh.data.TrackCfg;

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

    /**
     * @param gpio
     * @param config
     * @param start
     * @param stop
     */
    public TrackPins( GpioController gpio, TrackCfg config, Runnable start,
        Runnable stop )
    {
        this.config = config;

        this.startPin = SohGpio.provisionInputPin( gpio, config.startPin,
            config.startRes, "Track Start:" + config.startPin.pin.pinout,
            start );
        this.stopPin = SohGpio.provisionInputPin( gpio, config.stopPin,
            config.startRes, "Track Stop:" + config.stopPin.pin.pinout, stop );

        this.redPin = SohGpio.provisionOuputPin( gpio, config.redPin,
            config.redDefaultLevel, "Track Red:" + config.redPin.pin.pinout );

        this.greenPin = SohGpio.provisionOuputPin( gpio, config.greenPin,
            config.greenDefaultLevel,
            "Track Green:" + config.greenPin.pin.pinout );

        this.bluePin = SohGpio.provisionOuputPin( gpio, config.bluePin,
            config.blueDefaultLevel,
            "Track Blue:" + config.bluePin.pin.pinout );
    }

    /**
     * @param gpio
     */
    public void unprovisionAll( GpioController gpio )
    {
        gpio.unprovisionPin( startPin );
        gpio.unprovisionPin( stopPin );

        gpio.unprovisionPin( redPin );
        gpio.unprovisionPin( greenPin );
        gpio.unprovisionPin( bluePin );
    }
}
