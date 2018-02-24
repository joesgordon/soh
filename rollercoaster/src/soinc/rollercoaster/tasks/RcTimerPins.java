package soinc.rollercoaster.tasks;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;

import soinc.lib.data.Pi3InputPin;
import soinc.lib.data.Pi3OutputPin;
import soinc.lib.gpio.SciolyGpio;

/*******************************************************************************
 * 
 ******************************************************************************/
public class RcTimerPins
{
    /**  */
    private GpioPinDigitalOutput outPin;
    /**  */
    private GpioPinDigitalInput inPin;

    public RcTimerPins()
    {
        this.outPin = null;
        this.inPin = null;
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

        this.outPin = SciolyGpio.provisionOuputPin( gpio, outputPin,
            timerStr + " Out" );
        this.inPin = SciolyGpio.provisionInputPin( gpio, inputPin,
            timerStr + " In", () -> togglePin() );
    }

    /***************************************************************************
     * @param gpio
     **************************************************************************/
    public void unprovisionAll( GpioController gpio )
    {
        gpio.unprovisionPin( outPin );
        gpio.unprovisionPin( inPin );
    }

    private void togglePin()
    {
        // TODO Auto-generated method stub
    }
}
