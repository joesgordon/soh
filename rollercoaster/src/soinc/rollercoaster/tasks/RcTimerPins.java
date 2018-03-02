package soinc.rollercoaster.tasks;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinEdge;

import soinc.lib.data.Pi3InputPin;
import soinc.lib.data.Pi3OutputPin;
import soinc.lib.gpio.SciolyGpio;
import soinc.rollercoaster.tasks.IRcSignals.ITimerCallback;

/*******************************************************************************
 * 
 ******************************************************************************/
public class RcTimerPins
{
    /**  */
    private final boolean isInverted;

    /**  */
    private GpioPinDigitalOutput outPin;
    /**  */
    private GpioPinDigitalInput inPin;
    /**  */
    private ITimerCallback callback;

    /**  */
    private boolean started;

    /***************************************************************************
     * @param isInverted
     **************************************************************************/
    public RcTimerPins( boolean isInverted )
    {
        this.isInverted = isInverted;
        this.outPin = null;
        this.inPin = null;
        this.callback = null;
        this.started = false;
    }

    /***************************************************************************
     * @param gpio
     * @param inputPin
     * @param outputPin
     * @param config
     * @param toggleA
     * @param toggleS
     * @param toggleD
     **************************************************************************/
    public void provision( GpioController gpio, Pi3OutputPin outputPin,
        Pi3InputPin inputPin, char timer )
    {
        String timerStr = "Timer " + timer;
        PinEdge edge = isInverted ? PinEdge.RISING : PinEdge.FALLING;

        this.outPin = SciolyGpio.provisionOuputPin( gpio, outputPin,
            timerStr + " Out" );
        this.inPin = SciolyGpio.provisionInputPin( gpio, inputPin,
            timerStr + " In", () -> togglePin(), edge );
    }

    /***************************************************************************
     * @param gpio
     **************************************************************************/
    public void unprovisionAll( GpioController gpio )
    {
        gpio.unprovisionPin( outPin );
        gpio.unprovisionPin( inPin );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void togglePin()
    {
        this.started = !started;
        if( callback != null )
        {
            callback.setTimerStarted( started );
        }
    }

    /***************************************************************************
     * @param callback
     **************************************************************************/
    public void setCallback( ITimerCallback callback )
    {
        this.callback = callback;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void clear()
    {
        if( started )
        {
            togglePin();
        }
    }
}
