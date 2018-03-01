package soinc.rollercoaster;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.pi4j.io.gpio.GpioController;

import soinc.lib.data.PinResistance;
import soinc.rollercoaster.data.RcConfig;
import soinc.rollercoaster.relay.IRelays;
import soinc.rollercoaster.tasks.RcTimerPins;

/*******************************************************************************
 * 
 ******************************************************************************/
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
    private final IRelays relay;
    /**  */
    private final RcConfig config;

    /***************************************************************************
     * @param gpio
     * @param config
     * @throws IOException
     **************************************************************************/
    public RcPiSignals( GpioController gpio, RcConfig config )
    {
        this.gpio = gpio;
        this.config = config;
        this.timerA = new RcTimerPins(
            config.timerAIn.resistance == PinResistance.PULL_DOWN );
        this.timerS = new RcTimerPins(
            config.timerSIn.resistance == PinResistance.PULL_DOWN );
        this.timerD = new RcTimerPins(
            config.timerDIn.resistance == PinResistance.PULL_DOWN );
        this.timerPins = new ArrayList<>();
        this.relay = RcMain.getRelay();

        timerPins.add( timerA );
        timerPins.add( timerS );
        timerPins.add( timerD );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void connect() throws IOException
    {
        relay.initialize();

        timerA.provision( gpio, config.timerAOut, config.timerAIn, 'A' );
        timerS.provision( gpio, config.timerSOut, config.timerSIn, 'S' );
        timerD.provision( gpio, config.timerDOut, config.timerDIn, 'D' );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void disconnect()
    {
        timerA.unprovisionAll( gpio );
        timerS.unprovisionAll( gpio );
        timerD.unprovisionAll( gpio );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setLights( boolean red, boolean green, boolean blue )
    {
        relay.setRelay( 0, red );
        relay.setRelay( 1, green );
        relay.setRelay( 2, blue );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public int getTimerCount()
    {
        return timerPins.size();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setTimerCallback( int index, ITimerCallback callback )
    {
        timerPins.get( index ).setCallback( callback );
    }
}
