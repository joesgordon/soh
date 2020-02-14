package soinc.ppp.data;

import java.util.ArrayList;
import java.util.List;

import soinc.lib.data.Pi3GpioPin;
import soinc.lib.data.Pi3InputPin;
import soinc.lib.data.Pi3OutputPin;
import soinc.lib.data.PinLevel;
import soinc.lib.data.PinResistance;
import soinc.lib.ui.PhysicalKey;

/*******************************************************************************
 * 
 ******************************************************************************/
public class EventConfig
{
    /** The number of seconds each team has to execute 2 launches. */
    public int periodTime;
    /**  */
    public int periodWarning;

    /**  */
    public PhysicalKey fireToggleKey;

    /**  */
    public final Pi3OutputPin timer1Out;
    /**  */
    public final Pi3InputPin timer1In;
    /**  */
    public PhysicalKey timer1StartStopKey;
    /**  */
    public PhysicalKey timer1ClearKey;

    /**  */
    public final Pi3OutputPin timer2Out;
    /**  */
    public final Pi3InputPin timer2In;
    /**  */
    public PhysicalKey timer2StartStopKey;
    /**  */
    public PhysicalKey timer2ClearKey;

    /**  */
    public PhysicalKey timer3StartStopKey;
    /**  */
    public PhysicalKey timer3ClearKey;

    /**  */
    public final TrackConfig trackA;
    /**  */
    public final TrackConfig trackB;

    /**  */
    public final List<Team> teams;

    /***************************************************************************
     * 
     **************************************************************************/
    public EventConfig()
    {
        this.periodTime = 5 * 60;
        this.periodWarning = 4 * 60;

        this.fireToggleKey = PhysicalKey.SPACE;

        this.timer1Out = new Pi3OutputPin( Pi3GpioPin.GPIO_02, PinLevel.LOW );
        this.timer1In = new Pi3InputPin( Pi3GpioPin.GPIO_03,
            PinResistance.PULL_UP );
        this.timer1StartStopKey = PhysicalKey.Z;
        this.timer1ClearKey = PhysicalKey.A;

        this.timer2Out = new Pi3OutputPin( Pi3GpioPin.GPIO_17, PinLevel.LOW );
        this.timer2In = new Pi3InputPin( Pi3GpioPin.GPIO_27,
            PinResistance.PULL_UP );
        this.timer2StartStopKey = PhysicalKey.X;
        this.timer2ClearKey = PhysicalKey.S;

        this.timer3StartStopKey = PhysicalKey.C;
        this.timer3ClearKey = PhysicalKey.D;

        this.trackA = new TrackConfig();
        this.trackB = new TrackConfig();

        this.teams = new ArrayList<>();

        trackB.loadKey = PhysicalKey.U;
        trackB.startPauseKey = PhysicalKey.I;
        trackB.goodRunKey = PhysicalKey.O;
        trackB.badRunKey = PhysicalKey.P;
        trackB.clearTeamKey = PhysicalKey.SEMICOLON;
    }

    /***************************************************************************
     * @param cfg
     **************************************************************************/
    public EventConfig( EventConfig cfg )
    {
        this();

        set( cfg );
    }

    /***************************************************************************
     * @param cfg
     **************************************************************************/
    public void set( EventConfig cfg )
    {
        this.periodTime = cfg.periodTime;
        this.periodWarning = cfg.periodWarning;

        this.fireToggleKey = cfg.fireToggleKey;

        this.timer1Out.set( cfg.timer1Out );
        this.timer1In.set( cfg.timer1In );
        this.timer1StartStopKey = cfg.timer1StartStopKey;
        this.timer1ClearKey = cfg.timer1ClearKey;

        this.timer2Out.set( cfg.timer2Out );
        this.timer2In.set( cfg.timer2In );
        this.timer2StartStopKey = cfg.timer2StartStopKey;
        this.timer2ClearKey = cfg.timer2ClearKey;

        this.timer3StartStopKey = cfg.timer3StartStopKey;
        this.timer3ClearKey = cfg.timer3ClearKey;

        this.trackA.set( cfg.trackA );
        this.trackB.set( cfg.trackB );

        this.teams.clear();
        if( cfg.teams != null )
        {
            this.teams.addAll( cfg.teams );
        }
    }
}
