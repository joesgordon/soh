package soinc.ppp.tasks;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import org.jutils.SwingUtils;

import com.pi4j.io.gpio.GpioController;

import soinc.lib.data.PinResistance;
import soinc.lib.gpio.ITimerCallback;
import soinc.lib.gpio.TimerPins;
import soinc.lib.relay.IRelays;
import soinc.ppp.PppMain;
import soinc.ppp.data.TrackConfig;
import soinc.ppp.ui.TrackView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TrackSignals
{
    /**  */
    private final GpioController gpio;
    /**  */
    private final TimerPins timer1;
    /**  */
    private final TimerPins timer2;
    /**  */
    private final List<TimerPins> timerPins;
    /**  */
    private final IRelays relays;
    /**  */
    private final TrackConfig trackCfg;

    /**  */
    private TrackView view;

    /***************************************************************************
     * @param trackCfg
     * @param gpio
     * @param relays
     * @throws IOException
     **************************************************************************/
    public TrackSignals( TrackConfig trackCfg, GpioController gpio,
        IRelays relays )
    {
        this.trackCfg = trackCfg;
        this.gpio = gpio;

        this.timer1 = new TimerPins(
            trackCfg.timer1In.resistance == PinResistance.PULL_UP );
        this.timer2 = new TimerPins(
            trackCfg.timer2In.resistance == PinResistance.PULL_UP );
        this.timerPins = new ArrayList<>();
        this.relays = PppMain.getRelays();

        timerPins.add( timer1 );
        timerPins.add( timer2 );
    }

    /***************************************************************************
     * @param competition
     * @param view
     * @throws IOException
     **************************************************************************/
    public void connect( Track competition, TrackView view ) throws IOException
    {
        this.view = view;

        relays.initialize();

        JComponent jview = view.getView();

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

        callback = ( e ) -> timer1.togglePin();
        SwingUtils.addKeyListener( jview, "J", callback, "TimerA toggle",
            true );

        callback = ( e ) -> clearTimer( competition, timer1, 0 );
        SwingUtils.addKeyListener( jview, "A", callback, "TimerA clear", true );

        callback = ( e ) -> timer2.togglePin();
        SwingUtils.addKeyListener( jview, "K", callback, "TimerS toggle",
            true );

        callback = ( e ) -> clearTimer( competition, timer2, 1 );
        SwingUtils.addKeyListener( jview, "S", callback, "TimerS clear", true );

        // ---------------------------------------------------------------------

        callback = ( e ) -> competition.signalClearTeam();
        SwingUtils.addKeyListener( jview, "F4", callback, "Clear team", true );

        // ---------------------------------------------------------------------

        timer1.provision( gpio, trackCfg.timer1Out, trackCfg.timer1In, 'A' );
        timer2.provision( gpio, trackCfg.timer2Out, trackCfg.timer2In, 'S' );
    }

    /***************************************************************************
     * @param competition
     * @param timer
     * @param index
     **************************************************************************/
    private void clearTimer( Track competition, TimerPins timer, int index )
    {
        timer.clear();
        competition.signalTimerClear( index );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void disconnect()
    {
        timer1.unprovisionAll( gpio );
        timer2.unprovisionAll( gpio );
    }

    /***************************************************************************
     * @param red
     * @param green
     * @param blue
     **************************************************************************/
    public void setLights( boolean red, boolean green, boolean blue )
    {
        relays.setRelay( 0, red );
        relays.setRelay( 1, green );
        relays.setRelay( 2, blue );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getTimerCount()
    {
        return timerPins.size();
    }

    /***************************************************************************
     * @param index
     * @param callback
     **************************************************************************/
    public void setTimerCallback( int index, ITimerCallback callback )
    {
        timerPins.get( index ).setCallback( callback );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void clearTimers()
    {
        for( TimerPins pin : timerPins )
        {
            pin.clear();
        }
    }

    /***************************************************************************
     * @param data
     **************************************************************************/
    public void updateUI( Track data )
    {
        SwingUtilities.invokeLater( () -> view.setData( data ) );
    }
}
