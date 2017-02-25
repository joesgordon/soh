package soh.data;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TrackCfg
{
    /**  */
    public Pi3GpioPin startPin;
    /**  */
    public PinResistance startRes;
    /**  */
    public Pi3GpioPin stopPin;
    /**  */
    public PinResistance stopRes;

    /**  */
    public Pi3GpioPin redPin;
    /**  */
    public PinLevel redDefaultLevel;
    /**  */
    public Pi3GpioPin greenPin;
    /**  */
    public PinLevel greenDefaultLevel;
    /**  */
    public Pi3GpioPin bluePin;
    /**  */
    public PinLevel blueDefaultLevel;

    /***************************************************************************
     * 
     **************************************************************************/
    public TrackCfg()
    {
        this.startPin = Pi3GpioPin.GPIO_27;
        this.startRes = PinResistance.OFF;
        this.stopPin = Pi3GpioPin.GPIO_22;
        this.stopRes = PinResistance.OFF;

        this.redPin = Pi3GpioPin.GPIO_13;
        this.redDefaultLevel = PinLevel.HIGH;
        this.greenPin = Pi3GpioPin.GPIO_06;
        this.greenDefaultLevel = PinLevel.HIGH;
        this.bluePin = Pi3GpioPin.GPIO_05;
        this.blueDefaultLevel = PinLevel.HIGH;
    }

    /***************************************************************************
     * @param isTrack2
     **************************************************************************/
    public TrackCfg( boolean isTrack2 )
    {
        this();

        if( isTrack2 )
        {
            this.startPin = Pi3GpioPin.GPIO_23;
            this.stopPin = Pi3GpioPin.GPIO_24;

            this.redPin = Pi3GpioPin.GPIO_16;
            this.greenPin = Pi3GpioPin.GPIO_12;
            this.bluePin = Pi3GpioPin.GPIO_26;

        }
    }

    /***************************************************************************
     * @param track
     **************************************************************************/
    public TrackCfg( TrackCfg track )
    {
        this.startPin = track.startPin;
        this.startRes = track.startRes;
        this.stopPin = track.stopPin;
        this.stopRes = track.stopRes;

        this.redPin = track.redPin;
        this.redDefaultLevel = track.redDefaultLevel;
        this.bluePin = track.bluePin;
        this.blueDefaultLevel = track.blueDefaultLevel;
        this.greenPin = track.greenPin;
        this.greenDefaultLevel = track.greenDefaultLevel;
    }
}
