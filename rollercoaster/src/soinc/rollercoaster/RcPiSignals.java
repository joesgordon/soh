package soinc.rollercoaster;

import java.util.ArrayList;
import java.util.List;

import com.pi4j.io.gpio.GpioController;

import soinc.rollercoaster.data.RcConfig;
import soinc.rollercoaster.relay.IRelay;
import soinc.rollercoaster.tasks.RcTimerPins;

public class RcPiSignals implements IRcSignals
{
    /**  */
    private final GpioController gpio;
    /**  */
    private final RcTimerPins timerA;
    /**  */
    private final RcTimerPins timerS;
    /**  */
    private final RcTimerPins timerD;
    /**  */
    private final List<RcTimerPins> timerPins;
    /**  */
    private final IRelay relay;
    /**  */
    private final RcConfig config;

    public RcPiSignals( GpioController gpio, RcConfig config )
    {
        this.gpio = gpio;
        this.config = config;
        this.timerA = new RcTimerPins();
        this.timerS = new RcTimerPins();
        this.timerD = new RcTimerPins();
        this.timerPins = new ArrayList<>();
        this.relay = RcMain.getRelay();

        timerPins.add( timerA );
        timerPins.add( timerS );
        timerPins.add( timerD );
    }

    @Override
    public void connect()
    {
        timerA.provision( gpio, config.timerAOut, config.timerAIn, 'A' );
        timerS.provision( gpio, config.timerSOut, config.timerSIn, 'S' );
        timerD.provision( gpio, config.timerDOut, config.timerDIn, 'D' );
    }

    @Override
    public void disconnect()
    {
        timerA.unprovisionAll( gpio );
        timerS.unprovisionAll( gpio );
        timerD.unprovisionAll( gpio );
    }

    @Override
    public void setLights( boolean red, boolean green, boolean blue )
    {
        relay.setRelay( 0, red );
        relay.setRelay( 1, green );
        relay.setRelay( 2, blue );
    }

    @Override
    public int getTimerCount()
    {
        return timerPins.size();
    }

    @Override
    public void setTimerCallback( int index, Runnable callback )
    {
        timerPins.get( index ).setCallback( callback );
    }
}
