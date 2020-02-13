package soinc.ppp.tasks;

import java.io.IOException;

import org.jutils.io.LogUtils;

import com.pi4j.io.gpio.GpioController;

import soinc.lib.relay.IRelays;
import soinc.ppp.data.EventConfig;
import soinc.ppp.ui.EventView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PppEvent
{
    /**  */
    public final EventConfig config;
    /**  */
    public final GpioController gpio;
    /**  */
    public final IRelays relays;

    /**  */
    public final EventSignals signals;

    /**  */
    public final Track trackA;
    /**  */
    public final Track trackB;

    /**  */
    public final PppTimers timers;

    /**  */
    public Track selectedTrack;

    /***************************************************************************
     * @param config
     * @param gpio
     * @param relays
     **************************************************************************/
    public PppEvent( EventConfig config, GpioController gpio, IRelays relays )
    {
        this.config = config;
        this.gpio = gpio;
        this.relays = relays;

        this.signals = new EventSignals( config, relays );

        int timerCount = signals.timerPins.size();

        this.trackA = new Track( config, signals.trackA, timerCount );
        this.trackB = new Track( config, signals.trackB, timerCount );

        this.timers = new PppTimers( timerCount );

        this.selectedTrack = trackA;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public boolean isRunning()
    {
        return trackA.getState().isRunning || trackB.getState().isRunning;
    }

    /***************************************************************************
     * @param eventView
     * @param gpio
     * @throws IOException
     **************************************************************************/
    public void connect( EventView eventView ) throws IOException
    {
        signals.connect( this, eventView, gpio );

        for( int i = 0; i < timers.getSize(); i++ )
        {
            int index = i;
            signals.setTimerCallback( i,
                ( b ) -> signalTimerStart( index, b ) );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void disconnect()
    {
        trackA.disconnect();
        trackB.disconnect();
        signals.disconnect( gpio );
    }

    /***************************************************************************
     * @param index
     * @param start
     * @return
     **************************************************************************/
    public boolean signalTimerStart( int index, boolean start )
    {
        boolean started = timers.hasStarted( index );

        if( start && !timers.hasStarted() )
        {
            if( selectedTrack.signalTimerStart( index, start ) )
            {
                timers.setTimerStarted( index, start );
                started = true;
            }
        }
        else
        {
            boolean timerHasStopped = timers.hasStopped( index );

            boolean timerHasStarted = timers.hasStarted( index );

            if( start && !timerHasStarted )
            {
                timers.setTimerStarted( index, start );
                started = true;
            }
            else if( !start && timerHasStarted && !timerHasStopped )
            {
                timers.setTimerStarted( index, start );
                started = false;
            }
            else
            {
                String action = start ? "start" : "stop";
                String state = timerHasStarted ? "has started"
                    : "has not started";
                String msg = "Unable to " + action + " when " + index + " " +
                    state;

                showErrorMessage( msg );
                started = false;
            }
        }

        return started;
    }

    /***************************************************************************
     * @param message
     **************************************************************************/
    private void showErrorMessage( String message )
    {
        LogUtils.printError( message );
        // TODO show error
    }

    /***************************************************************************
     * @param index s
     **************************************************************************/
    public void signalTimerClear( int index )
    {
        timers.clearTimer( index );

        if( !timers.hasStarted() )
        {
            selectedTrack.signalTimerClear( index );
        }
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public boolean areTimersComplete()
    {
        return timers.areComplete();
    }
}
