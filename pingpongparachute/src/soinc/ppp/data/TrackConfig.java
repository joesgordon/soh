package soinc.ppp.data;

import soinc.lib.data.Pi3GpioPin;
import soinc.lib.data.Pi3InputPin;
import soinc.lib.data.Pi3OutputPin;
import soinc.lib.data.PinLevel;
import soinc.lib.data.PinResistance;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TrackConfig
{
    /**  */
    public final Pi3OutputPin timer1Out;
    /**  */
    public final Pi3InputPin timer1In;

    /**  */
    public final Pi3OutputPin timer2Out;
    /**  */
    public final Pi3InputPin timer2In;

    /***************************************************************************
     * 
     **************************************************************************/
    public TrackConfig()
    {
        this.timer1Out = new Pi3OutputPin( Pi3GpioPin.GPIO_02, PinLevel.LOW );
        this.timer1In = new Pi3InputPin( Pi3GpioPin.GPIO_03,
            PinResistance.PULL_UP );

        this.timer2Out = new Pi3OutputPin( Pi3GpioPin.GPIO_17, PinLevel.LOW );
        this.timer2In = new Pi3InputPin( Pi3GpioPin.GPIO_27,
            PinResistance.PULL_UP );
    }

    /***************************************************************************
     * @param track
     **************************************************************************/
    public void set( TrackConfig track )
    {
        timer1Out.set( track.timer1Out );
        timer1In.set( track.timer1In );

        timer2Out.set( track.timer2Out );
        timer2In.set( track.timer1In );
    }
}
