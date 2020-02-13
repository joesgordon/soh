package soinc.ppp.tasks;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import org.jutils.SwingUtils;

import com.pi4j.io.gpio.GpioController;

import soinc.lib.data.PinResistance;
import soinc.lib.gpio.ITimerCallback;
import soinc.lib.gpio.TimerPins;
import soinc.lib.relay.IRelays;
import soinc.ppp.data.EventConfig;
import soinc.ppp.ui.EventView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class EventSignals
{
    /**  */
    public final EventConfig eventCfg;

    /**  */
    public final TrackSignals trackA;
    /**  */
    public final TrackSignals trackB;

    /**  */
    public final TimerPins timer1;
    /**  */
    public final TimerPins timer2;
    /**  */
    public final List<TimerPins> timerPins;

    /***************************************************************************
     * @param eventCfg
     * @param gpio
     * @param relays
     **************************************************************************/
    public EventSignals( EventConfig eventCfg, IRelays relays )
    {
        this.eventCfg = eventCfg;

        this.trackA = new TrackSignals( eventCfg.trackA, relays );
        this.trackB = new TrackSignals( eventCfg.trackA, relays );

        this.timer1 = new TimerPins(
            eventCfg.timer1In.resistance == PinResistance.PULL_UP );
        this.timer2 = new TimerPins(
            eventCfg.timer2In.resistance == PinResistance.PULL_UP );
        this.timerPins = new ArrayList<>();

        timerPins.add( timer1 );
        timerPins.add( timer2 );
    }

    /***************************************************************************
     * @param event
     * @param eventView
     * @param gpio
     * @throws IOException
     **************************************************************************/
    public void connect( PppEvent event, EventView eventView,
        GpioController gpio ) throws IOException
    {
        ActionListener callback;

        JComponent jview = eventView.getContent();

        // ---------------------------------------------------------------------

        callback = ( e ) -> timer1.togglePin();
        SwingUtils.addKeyListener( jview, "J", callback, "TimerA toggle",
            true );

        callback = ( e ) -> clearTimer( event, timer1, 0 );
        SwingUtils.addKeyListener( jview, eventCfg.timer1ClearKey.keystroke,
            callback, "Timer 1 clear", true );

        callback = ( e ) -> timer2.togglePin();
        SwingUtils.addKeyListener( jview, "K", callback, "TimerS toggle",
            true );

        callback = ( e ) -> clearTimer( event, timer2, 1 );
        SwingUtils.addKeyListener( jview, eventCfg.timer2ClearKey.keystroke,
            callback, "Timer 2 clear", true );

        // ---------------------------------------------------------------------

        timer1.provision( gpio, eventCfg.timer1Out, eventCfg.timer1In, '1' );
        timer2.provision( gpio, eventCfg.timer2Out, eventCfg.timer2In, '2' );

        trackA.connect( event.trackA, eventView.trackAView );
        trackB.connect( event.trackB, eventView.trackBView );
    }

    /***************************************************************************
     * @param gpio
     **************************************************************************/
    public void disconnect( GpioController gpio )
    {
        timer1.unprovisionAll( gpio );
        timer2.unprovisionAll( gpio );
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
     * @param competition
     * @param timer
     * @param index
     **************************************************************************/
    private void clearTimer( PppEvent event, TimerPins timer, int index )
    {
        timer.clear();
        event.signalTimerClear( index );
    }
}
