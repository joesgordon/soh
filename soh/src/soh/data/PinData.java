package soh.data;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PinData
{
    /**  */
    public final Pi3Pin pinout;
    /**  */
    public final Pi3GpioPin gpio;
    /**  */
    public PinDirection direction;
    /**  */
    public PinResistance pullRes;
    /**  */
    public PinLevel defaultLevel;
    /**  */
    public boolean provisioned;

    /***************************************************************************
     * @param pinout
     **************************************************************************/
    public PinData( Pi3Pin pinout )
    {
        this.pinout = pinout;
        this.gpio = Pi3GpioPin.getPin( pinout );
        this.direction = PinDirection.INPUT;
        this.pullRes = PinResistance.PULL_UP;
        this.defaultLevel = PinLevel.LOW;
        this.provisioned = false;
    }
}
