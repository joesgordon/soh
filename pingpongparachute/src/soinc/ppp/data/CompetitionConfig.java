package soinc.ppp.data;

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
public class CompetitionConfig implements ICompetitionConfig
{
    /** The number of seconds each team has to execute 2 launches. */
    public int periodTime;

    /**  */
    public final List<Team> teams;

    /**  */
    public final Pi3OutputPin timerAOut;
    /**  */
    public final Pi3InputPin timerAIn;

    /**  */
    public final Pi3OutputPin timerSOut;
    /**  */
    public final Pi3InputPin timerSIn;

    /**  */
    public final Pi3OutputPin timerDOut;
    /**  */
    public final Pi3InputPin timerDIn;

    /***************************************************************************
     * 
     **************************************************************************/
    public CompetitionConfig()
    {
        this.periodTime = 8 * 60;

        this.timerAOut = new Pi3OutputPin( Pi3GpioPin.GPIO_02, PinLevel.LOW );
        this.timerAIn = new Pi3InputPin( Pi3GpioPin.GPIO_03,
            PinResistance.PULL_UP );

        this.timerSOut = new Pi3OutputPin( Pi3GpioPin.GPIO_17, PinLevel.LOW );
        this.timerSIn = new Pi3InputPin( Pi3GpioPin.GPIO_27,
            PinResistance.PULL_UP );

        this.timerDOut = new Pi3OutputPin( Pi3GpioPin.GPIO_10, PinLevel.LOW );
        this.timerDIn = new Pi3InputPin( Pi3GpioPin.GPIO_09,
            PinResistance.PULL_UP );

        this.teams = new ArrayList<>();
    }

    /***************************************************************************
     * @param cfg
     **************************************************************************/
    public CompetitionConfig( CompetitionConfig cfg )
    {
        this();

        this.periodTime = cfg.periodTime;

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

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public int getPeriodTime()
    {
        return periodTime;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public List<Team> getTeams()
    {
        return teams;
    }
}
