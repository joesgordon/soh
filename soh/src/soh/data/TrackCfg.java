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

    /***************************************************************************
     * 
     **************************************************************************/
    public TrackCfg()
    {
        this.startPin = Pi3GpioPin.GPIO_27;
        this.startRes = PinResistance.OFF;
        this.stopPin = Pi3GpioPin.GPIO_22;
        this.stopRes = PinResistance.OFF;
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
    }
}
