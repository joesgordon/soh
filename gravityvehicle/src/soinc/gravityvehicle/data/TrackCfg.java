package soinc.gravityvehicle.data;

import soinc.lib.data.Pi3GpioPin;
import soinc.lib.data.Pi3InputPin;
import soinc.lib.data.Pi3OutputPin;
import soinc.lib.data.PinLevel;
import soinc.lib.data.PinResistance;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TrackCfg
{
    /**  */
    public Division division;

    /**  */
    public final Pi3InputPin startPin;
    /**  */
    public final Pi3InputPin stopPin;

    /**  */
    public Pi3OutputPin redPin;
    /**  */
    public Pi3OutputPin greenPin;
    /**  */
    public Pi3OutputPin bluePin;

    /***************************************************************************
     * 
     **************************************************************************/
    public TrackCfg()
    {
        this.division = Division.DIVISION_C;

        this.startPin = new Pi3InputPin( Pi3GpioPin.GPIO_27,
            PinResistance.OFF );
        this.stopPin = new Pi3InputPin( Pi3GpioPin.GPIO_22, PinResistance.OFF );

        this.redPin = new Pi3OutputPin( Pi3GpioPin.GPIO_13, PinLevel.HIGH );
        this.greenPin = new Pi3OutputPin( Pi3GpioPin.GPIO_05, PinLevel.HIGH );
        this.bluePin = new Pi3OutputPin( Pi3GpioPin.GPIO_06, PinLevel.HIGH );
    }

    /***************************************************************************
     * @param isTrack2
     **************************************************************************/
    public TrackCfg( boolean isTrack2 )
    {
        this();

        if( isTrack2 )
        {
            this.startPin.gpioPin = Pi3GpioPin.GPIO_23;
            this.stopPin.gpioPin = Pi3GpioPin.GPIO_24;

            this.redPin.gpioPin = Pi3GpioPin.GPIO_16;
            this.greenPin.gpioPin = Pi3GpioPin.GPIO_26;
            this.bluePin.gpioPin = Pi3GpioPin.GPIO_12;
        }
    }

    /***************************************************************************
     * @param track
     **************************************************************************/
    public TrackCfg( TrackCfg track )
    {
        this();

        this.division = track.division;

        this.startPin.set( track.startPin );
        this.stopPin.set( track.stopPin );

        this.redPin.set( track.redPin );
        this.bluePin.set( track.bluePin );
        this.greenPin.set( track.greenPin );
    }
}
