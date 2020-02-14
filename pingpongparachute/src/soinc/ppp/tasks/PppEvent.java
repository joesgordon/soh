package soinc.ppp.tasks;

import java.io.IOException;
import java.util.Timer;

import javax.swing.SwingUtilities;

import org.jutils.io.LogUtils;

import com.pi4j.io.gpio.GpioController;

import soinc.lib.RunnableTask;
import soinc.lib.relay.IRelays;
import soinc.ppp.data.EventConfig;
import soinc.ppp.data.TrackData;
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
    private final Timer timer;

    /**  */
    public Track selectedTrack;
    /**  */
    private EventView view;

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

        int timerCount = signals.getTimerCount();

        this.timers = new PppTimers( timerCount );

        this.trackA = new Track( config, signals.trackA, timerCount,
            ( t ) -> handleRunComplete( t ) );
        this.trackB = new Track( config, signals.trackB, timerCount,
            ( t ) -> handleRunComplete( t ) );

        this.timer = new Timer( "PPP Event" );

        this.selectedTrack = null;

        setSelected( trackA );
    }

    /***************************************************************************
     * @param track
     **************************************************************************/
    private void setSelected( Track track )
    {
        this.selectedTrack = track;

        trackA.isSelected = trackA == track;
        trackB.isSelected = trackB == track;
    }

    /***************************************************************************
     * @param track
     **************************************************************************/
    private void handleRunComplete( Track track )
    {
        TrackData data = track.data;

        timers.setData( data );
        timers.clear();

        LogUtils.printDebug( "Official time is %d: Runs complete %s, %s",
            data.officialTime, data.run1State.isComplete,
            data.run2State.isComplete );

        if( data.run2State.isComplete )
        {
            data.run2Time = ( int )data.officialTime;
        }
        else if( data.run1State.isComplete )
        {
            data.run1Time = ( int )data.officialTime;
        }
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public boolean isRunning()
    {
        return trackA.getState().isRunning || trackB.getState().isRunning;
    }

    /***************************************************************************
     * @param view
     * @param gpio
     * @throws IOException
     **************************************************************************/
    public void connect( EventView view ) throws IOException
    {
        this.view = view;

        signals.connect( this, view, gpio );

        for( int i = 0; i < timers.getSize(); i++ )
        {
            int index = i;
            signals.setTimerCallback( i,
                ( b ) -> signalTimerStart( index, b ) );
        }

        trackA.connect( view.trackAView );
        trackB.connect( view.trackBView );

        timer.scheduleAtFixedRate( new RunnableTask( () -> updateState() ), 100,
            100 );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void updateState()
    {
        if( trackA.data.isRunning() )
        {
            timers.setData( trackA.data );
        }
        else if( trackB.data.isRunning() )
        {
            timers.setData( trackB.data );
        }

        updateUI();

        trackA.updateState();
        trackB.updateState();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void disconnect()
    {
        timer.cancel();
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

        LogUtils.printDebug( "PppEvent::signalTimerStart( %d, %s )", index,
            start );

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

            LogUtils.printDebug(
                "PppEvent::signalTimerStart( %d, %s ): stopped: %s, started: %s",
                index, start, timerHasStopped, timerHasStarted );

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

    /***************************************************************************
     * 
     **************************************************************************/
    public void updateUI()
    {
        SwingUtilities.invokeLater( () -> view.setData( this ) );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void toggleFire()
    {
        if( !selectedTrack.data.isRunning() )
        {
            setSelected( selectedTrack == trackA ? trackB : trackA );
        }
    }
}
