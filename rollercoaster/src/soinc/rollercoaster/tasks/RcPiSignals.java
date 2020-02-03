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
import soinc.lib.relay.IRelays;
import soinc.rollercoaster.RcMain;
import soinc.rollercoaster.data.RcCompetitionData;
import soinc.rollercoaster.data.RcConfig;
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
            config.timerAIn.resistance == PinResistance.PULL_UP );
        this.timerS = new RcTimerPins(
            config.timerSIn.resistance == PinResistance.PULL_UP );
        this.timerD = new RcTimerPins(
            config.timerDIn.resistance == PinResistance.PULL_UP );
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
        SwingUtils.addKeyListener( jview, "F1", callback, "Period Start",
            true );

        // ---------------------------------------------------------------------

        callback = ( e ) -> competition.signalRunFinished( false );
        SwingUtils.addKeyListener( jview, "F", callback, "Fail Run", true );

        callback = ( e ) -> competition.signalRunFinished( true );
        SwingUtils.addKeyListener( jview, "G", callback, "Accept Run", true );

        // ---------------------------------------------------------------------

        callback = ( e ) -> timerA.togglePin();
        SwingUtils.addKeyListener( jview, "J", callback, "TimerA toggle",
            true );

        callback = ( e ) -> clearTimer( competition, timerA, 0 );
        SwingUtils.addKeyListener( jview, "A", callback, "TimerA clear", true );

        callback = ( e ) -> timerS.togglePin();
        SwingUtils.addKeyListener( jview, "K", callback, "TimerS toggle",
            true );

        callback = ( e ) -> clearTimer( competition, timerS, 1 );
        SwingUtils.addKeyListener( jview, "S", callback, "TimerS clear", true );

        callback = ( e ) -> timerD.togglePin();
        SwingUtils.addKeyListener( jview, "L", callback, "TimerD toggle",
            true );

        callback = ( e ) -> clearTimer( competition, timerD, 2 );
        SwingUtils.addKeyListener( jview, "D", callback, "TimerD clear", true );

        // ---------------------------------------------------------------------

        callback = ( e ) -> competition.signalClearTeam();
        SwingUtils.addKeyListener( jview, "F4", callback, "Clear team", true );

        // ---------------------------------------------------------------------

        timerA.provision( gpio, config.timerAOut, config.timerAIn, 'A' );
        timerS.provision( gpio, config.timerSOut, config.timerSIn, 'S' );
        timerD.provision( gpio, config.timerDOut, config.timerDIn, 'D' );
    }

    /**
     * @param competition
     * @param timer
     * @param index
     */
    private void clearTimer( RcTeamCompetition competition, RcTimerPins timer,
        int index )
    {
        timer.clear();
        competition.signalTimerClear( index );
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
