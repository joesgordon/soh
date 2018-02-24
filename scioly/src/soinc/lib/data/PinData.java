package soinc.lib.data;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PinData
{
    /**  */
    public final Pi3HeaderPin pinout;
    /**  */
    public final Pi3GpioPin gpio;
    /**  */
    public GpioPinDirection direction;
    /**  */
    public PinResistance pullRes;
    /**  */
    public PinLevel defaultLevel;
    /**  */
    public boolean provisioned;

    /***************************************************************************
     * @param pinout
     **************************************************************************/
    public PinData( Pi3HeaderPin pinout )
    {
        this.pinout = pinout;
        this.gpio = Pi3GpioPin.getPin( pinout );
        this.direction = GpioPinDirection.INPUT;
        this.pullRes = PinResistance.PULL_UP;
        this.defaultLevel = PinLevel.LOW;
        this.provisioned = false;
    }

    /***************************************************************************
     * @param pd
     **************************************************************************/
    public PinData( PinData pd )
    {
        this.pinout = pd.pinout;
        this.gpio = pd.gpio;
        this.direction = pd.direction;
        this.pullRes = pd.pullRes;
        this.defaultLevel = pd.defaultLevel;
        this.provisioned = pd.provisioned;
    }
}
