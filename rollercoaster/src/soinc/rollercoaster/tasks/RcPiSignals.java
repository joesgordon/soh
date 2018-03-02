package soinc.rollercoaster.tasks;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import org.jutils.SwingUtils;

import com.pi4j.io.gpio.GpioController;

import soinc.lib.data.PinResistance;
import soinc.rollercoaster.RcMain;
import soinc.rollercoaster.data.RcCompetitionData;
import soinc.rollercoaster.data.RcConfig;
import soinc.rollercoaster.relay.IRelays;
import soinc.rollercoaster.ui.RcCompetitionView;

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

    /**  */
    private RcCompetitionView view;

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
     * @param view
     **************************************************************************/
    @Override
    public void connect( RcTeamCompetition competition, RcCompetitionView view )
        throws IOException
    {
        this.view = view;

        relay.initialize();

        JComponent jview = view.getContent();

        ActionListener callback;

        // ---------------------------------------------------------------------

        callback = ( e ) -> competition.signalPeriodStart();
        SwingUtils.addKeyListener( jview, "F1", true, callback,
            "Period Start" );

        // ---------------------------------------------------------------------

        callback = ( e ) -> competition.signalRunFinished( false );
        SwingUtils.addKeyListener( jview, "F", true, callback, "Fail Run" );

        callback = ( e ) -> competition.signalRunFinished( true );
        SwingUtils.addKeyListener( jview, "G", true, callback, "Accept Run" );

        // ---------------------------------------------------------------------

        callback = ( e ) -> timerA.togglePin();
        SwingUtils.addKeyListener( jview, "A", true, callback,
            "TimerA toggle" );

        callback = ( e ) -> timerA.clear();
        SwingUtils.addKeyListener( jview, "J", true, callback, "TimerA clear" );

        callback = ( e ) -> timerS.togglePin();
        SwingUtils.addKeyListener( jview, "S", true, callback,
            "TimerS toggle" );

        callback = ( e ) -> timerS.clear();
        SwingUtils.addKeyListener( jview, "K", true, callback, "TimerS clear" );

        callback = ( e ) -> timerD.togglePin();
        SwingUtils.addKeyListener( jview, "D", true, callback,
            "TimerD toggle" );

        callback = ( e ) -> timerD.clear();
        SwingUtils.addKeyListener( jview, "L", true, callback, "TimerD clear" );

        // ---------------------------------------------------------------------

        callback = ( e ) -> competition.signalClearTeam();
        SwingUtils.addKeyListener( jview, "F4", true, callback, "Clear team" );

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

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void clearTimers()
    {
        for( RcTimerPins pin : timerPins )
        {
            pin.clear();
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void updateUI( RcCompetitionData data )
    {
        SwingUtilities.invokeLater( () -> view.setData( data ) );
    }
}
