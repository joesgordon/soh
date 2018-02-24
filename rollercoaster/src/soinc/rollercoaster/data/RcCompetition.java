package soinc.rollercoaster.data;

import java.util.ArrayList;
import java.util.List;

import com.pi4j.io.gpio.GpioController;

import soinc.rollercoaster.RollercoasterMain;
import soinc.rollercoaster.relay.IRelay;
import soinc.rollercoaster.tasks.RcTimerPins;

/*******************************************************************************
 * 
 ******************************************************************************/
public class RcCompetition
{
    /**  */
    public final RollercoasterConfig config;

    /**  */
    private final GpioController gpio;
    /**  */
    private final RcTimerPins timerA;
    /**  */
    private final RcTimerPins timerS;
    /**  */
    private final RcTimerPins timerD;
    /**  */
    private final IRelay relay;

    /** Milliseconds */
    private long periodTime;
    /**  */
    private RcTeam team;

    /***************************************************************************
     * @param config
     * @param gpio
     **************************************************************************/
    public RcCompetition( RollercoasterConfig config, GpioController gpio )
    {
        this.config = config;
        this.gpio = gpio;
        this.timerA = new RcTimerPins();
        this.timerS = new RcTimerPins();
        this.timerD = new RcTimerPins();
        this.relay = RollercoasterMain.getRelay();
        this.periodTime = -1;
        this.team = null;

        reset();
    }

    public void connect()
    {
        timerA.provision( gpio, config.timerAOut, config.timerAIn, 'A' );
        timerS.provision( gpio, config.timerSOut, config.timerSIn, 'S' );
        timerD.provision( gpio, config.timerDOut, config.timerDIn, 'D' );
    }

    public void disconnect()
    {
        timerA.unprovisionAll( gpio );
        timerS.unprovisionAll( gpio );
        timerD.unprovisionAll( gpio );
    }

    public List<RcTeam> getAvailableTeams()
    {
        List<RcTeam> remaining = new ArrayList<>();

        for( RcTeam team : config.teams )
        {
            if( !team.loaded )
            {
                remaining.add( team );
            }
        }
        return remaining;
    }

    public boolean isRunning()
    {
        return team != null;
    }

    public void reset()
    {
        this.periodTime = -1;
        this.team = null;
    }
}
