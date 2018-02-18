package soinc.hovercraft.data;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;

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

        this.startPin = SciolyGpio.provisionInputPin( gpio, config.startPin,
            config.startRes, "Track Start:" + config.startPin.pin.pinout,
            start );
        this.stopPin = SciolyGpio.provisionInputPin( gpio, config.stopPin,
            config.startRes, "Track Stop:" + config.stopPin.pin.pinout, stop );

        this.redPin = SciolyGpio.provisionOuputPin( gpio, config.redPin,
            config.redDefaultLevel, "Track Red:" + config.redPin.pin.pinout );

        this.greenPin = SciolyGpio.provisionOuputPin( gpio, config.greenPin,
            config.greenDefaultLevel,
            "Track Green:" + config.greenPin.pin.pinout );

        this.bluePin = SciolyGpio.provisionOuputPin( gpio, config.bluePin,
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
