package soinc.rollercoaster.tasks;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import org.jutils.SwingUtils;

import com.pi4j.io.gpio.GpioController;

import soinc.lib.data.PinResistance;
import soinc.rollercoaster.RcMain;
import soinc.rollercoaster.data.RcConfig;
import soinc.rollercoaster.relay.IRelays;

/*******************************************************************************
 * 
 ******************************************************************************/
public class RcPiSignals implements IRcSignals
{
    /**  */
    private final JComponent view;
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
    public RcPiSignals( JComponent view, GpioController gpio, RcConfig config )
    {
        this.view = view;
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
     * @param view
     **************************************************************************/
    @Override
    public void connect( RcTeamCompetition competition ) throws IOException
    {
        relay.initialize();

        ActionListener callback;

        // ---------------------------------------------------------------------

        callback = ( e ) -> competition.signalPeriodStart();
        SwingUtils.addKeyListener( view, "F1", true, callback, "Period Start" );

        // ---------------------------------------------------------------------

        callback = ( e ) -> competition.signalRunFinished( false );
        SwingUtils.addKeyListener( view, "f", true, callback, "Fail Run" );

        callback = ( e ) -> competition.signalRunFinished( true );
        SwingUtils.addKeyListener( view, "g", true, callback, "Accept Run" );

        // ---------------------------------------------------------------------

        callback = ( e ) -> timerA.togglePin();
        SwingUtils.addKeyListener( view, "a", true, callback, "TimerA toggle" );

        callback = ( e ) -> timerA.clear();
        SwingUtils.addKeyListener( view, "j", true, callback, "TimerA clear" );

        callback = ( e ) -> timerS.togglePin();
        SwingUtils.addKeyListener( view, "s", true, callback, "TimerS toggle" );

        callback = ( e ) -> timerS.clear();
        SwingUtils.addKeyListener( view, "k", true, callback, "TimerS clear" );

        callback = ( e ) -> timerD.togglePin();
        SwingUtils.addKeyListener( view, "d", true, callback, "TimerD toggle" );

        callback = ( e ) -> timerD.clear();
        SwingUtils.addKeyListener( view, "l", true, callback, "TimerD clear" );

        // ---------------------------------------------------------------------

        callback = ( e ) -> competition.signalClearTeam();
        SwingUtils.addKeyListener( view, "F4", true, callback, "Clear team" );

        // ---------------------------------------------------------------------

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
