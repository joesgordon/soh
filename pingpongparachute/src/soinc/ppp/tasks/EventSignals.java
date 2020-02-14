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
    private final TimerPins timer1;
    /**  */
    private final TimerPins timer2;
    /**  */
    private final List<TimerPins> timerPins;

    /**  */
    private ITimerCallback timer3Callback;

    private boolean timer3Started;

    /***************************************************************************
     * @param eventCfg
     * @param relays
     **************************************************************************/
    public EventSignals( EventConfig eventCfg, IRelays relays )
    {
        this.eventCfg = eventCfg;

        this.trackA = new TrackSignals( eventCfg.trackA, relays );
        this.trackB = new TrackSignals( eventCfg.trackB, relays );

        this.timer1 = new TimerPins(
            eventCfg.timer1In.resistance == PinResistance.PULL_UP );
        this.timer2 = new TimerPins(
            eventCfg.timer2In.resistance == PinResistance.PULL_UP );
        this.timerPins = new ArrayList<>();

        this.timer3Callback = null;
        this.timer3Started = false;

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
        SwingUtils.addKeyListener( jview, eventCfg.timer1StartStopKey.keystroke,
            callback, "Timer 1 Toggle", true );

        callback = ( e ) -> clearTimer( event, timer1, 0 );
        SwingUtils.addKeyListener( jview, eventCfg.timer1ClearKey.keystroke,
            callback, "Timer 1 Clear", true );

        // ---------------------------------------------------------------------

        callback = ( e ) -> timer2.togglePin();
        SwingUtils.addKeyListener( jview, eventCfg.timer2StartStopKey.keystroke,
            callback, "Timer 2 Toggle", true );

        callback = ( e ) -> clearTimer( event, timer2, 1 );
        SwingUtils.addKeyListener( jview, eventCfg.timer2ClearKey.keystroke,
            callback, "Timer 2 Clear", true );

        // ---------------------------------------------------------------------

        callback = ( e ) -> {
            timer3Started = !timer3Started;
            if( timer3Callback != null )
            {
                timer3Callback.setTimerStarted( timer3Started );
            }
        };
        SwingUtils.addKeyListener( jview, eventCfg.timer3StartStopKey.keystroke,
            callback, "Timer 3 Toggle", true );

        callback = ( e ) -> timer3Started = false;
        SwingUtils.addKeyListener( jview, eventCfg.timer3ClearKey.keystroke,
            callback, "Timer 3 Clear", true );

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
        if( index < 2 )
        {
            timerPins.get( index ).setCallback( callback );
        }
        else
        {
            this.timer3Callback = callback;
        }
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
     * @param event
     * @param timer
     * @param index
     **************************************************************************/
    private void clearTimer( PppEvent event, TimerPins timer, int index )
    {
        timer.clear();
        event.signalTimerClear( index );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getTimerCount()
    {
        return 3;
    }
}
