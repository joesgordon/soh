package soinc.rollercoaster.data;

import java.util.ArrayList;
import java.util.List;

import soinc.lib.data.Pi3GpioPin;
import soinc.lib.data.Pi3InputPin;
import soinc.lib.data.Pi3OutputPin;
import soinc.lib.data.PinLevel;
import soinc.lib.data.PinResistance;

/*******************************************************************************
 * 
 ******************************************************************************/
public class RollercoasterConfig
{
    /**  */
    public int periodTime;
    /**
     * Time for the roller coaster to complete its track. Must be between 20 and
     * 45 seconds (in 5s intervals for regional, 2s intervals for state, and 1s
     * intervals for national tournaments).
     */
    public int targetTime;
    /**  */
    public int trialTimeout;
    /**  */
    public final List<RcTeam> teams;

    /**  */
    public final Pi3InputPin timerAIn;
    /**  */
    public final Pi3OutputPin timerAOut;
    /**  */
    public final Pi3InputPin timerSIn;
    /**  */
    public final Pi3OutputPin timerSOut;
    /**  */
    public final Pi3InputPin timerDIn;
    /**  */
    public final Pi3OutputPin timerDOut;

    /***************************************************************************
     * 
     **************************************************************************/
    public RollercoasterConfig()
    {
        this.periodTime = 8 * 60;
        this.trialTimeout = 60;
        this.targetTime = 20;

        this.timerAIn = new Pi3InputPin( Pi3GpioPin.GPIO_03,
            PinResistance.PULL_DOWN );
        this.timerAOut = new Pi3OutputPin( Pi3GpioPin.GPIO_02, PinLevel.HIGH );

        this.timerSIn = new Pi3InputPin( Pi3GpioPin.GPIO_27,
            PinResistance.PULL_DOWN );
        this.timerSOut = new Pi3OutputPin( Pi3GpioPin.GPIO_17, PinLevel.HIGH );

        this.timerDIn = new Pi3InputPin( Pi3GpioPin.GPIO_21,
            PinResistance.PULL_DOWN );
        this.timerDOut = new Pi3OutputPin( Pi3GpioPin.GPIO_10, PinLevel.HIGH );

        this.teams = new ArrayList<>();
    }

    /***************************************************************************
     * @param cfg
     **************************************************************************/
    public RollercoasterConfig( RollercoasterConfig cfg )
    {
        this();

        this.periodTime = cfg.periodTime;
        this.trialTimeout = cfg.trialTimeout;
        this.targetTime = cfg.targetTime;

        this.timerAIn.set( cfg.timerAIn );
        this.timerAOut.set( cfg.timerAOut );

        this.timerSIn.set( cfg.timerSIn );
        this.timerSOut.set( cfg.timerSOut );

        this.timerDIn.set( cfg.timerDIn );
        this.timerDOut.set( cfg.timerDOut );

        if( cfg.teams != null )
        {
            this.teams.addAll( cfg.teams );
        }
    }
}
