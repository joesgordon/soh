package soinc.gravityvehicle.tasks;

import org.jutils.io.LogUtils;
import org.jutils.ui.event.ItemActionList;
import org.jutils.ui.event.ItemActionListener;
import org.jutils.utils.Stopwatch;

import com.pi4j.io.gpio.GpioController;

import soinc.gravityvehicle.data.DivisionConfig;
import soinc.gravityvehicle.data.HcTeam;
import soinc.gravityvehicle.data.HoverConfig;
import soinc.gravityvehicle.data.TrackCfg;
import soinc.gravityvehicle.data.TrackData;
import soinc.gravityvehicle.data.TrackPins;
import soinc.gravityvehicle.data.TrackState;

/*******************************************************************************
 * 
 ******************************************************************************/
public class HcTeamCompetition
{
    /**  */
    private final HoverConfig config;
    /**  */
    public final TrackCfg track;
    /**  */
    private final TrackPins pins;

    /**  */
    private final TrackData data;
    /**  */
    private final Stopwatch periodWatch;
    /**  */
    private final Stopwatch runWatch;

    /**  */
    private final ItemActionList<TrackData> dataListeners;

    /***************************************************************************
     * @param config
     * @param track
     * @param gpio
     **************************************************************************/
    public HcTeamCompetition( HoverConfig config, TrackCfg track,
        GpioController gpio )
    {
        Runnable start = () -> signalStartRun();
        Runnable stop = () -> signalCompleteRun();

        this.config = config;
        this.track = track;
        this.pins = new TrackPins( gpio, track, start, stop );

        this.periodWatch = new Stopwatch();
        this.runWatch = new Stopwatch();

        this.data = new TrackData();

        this.dataListeners = new ItemActionList<>();

        setOutputUninitialized();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void signalLoadTrack( HcTeam team )
    {
        DivisionConfig div = config.getDivision( team.div );

        data.loadTrack( team, div );

        // LogUtils.printDebug( "Initializing track " + view.getTrackName() );

        setOutputInitPaused();

        dataListeners.fireListeners( this, data );

        printInfo( "signal track load" );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void signalUnloadTrack()
    {
        data.team.loaded = false;
        data.clearTrack();
        dataListeners.fireListeners( this, data );
        setOutputUninitialized();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void signalStartPauseTrack()
    {
        // LogUtils.printDebug(
        // "Starting period for track " + view.getTrackName() );

        if( data.state == TrackState.INITIALIZED )
        {
            periodWatch.start();
            data.state = TrackState.WAITING_A;
            setOutputWaiting();

            dataListeners.fireListeners( this, data );
        }
        else
        {
            TrackState newState = null;

            switch( data.state )
            {
                case WAITING_A:
                    newState = TrackState.PAUSE_A;
                    break;
                case WAITING_B:
                    newState = TrackState.PAUSE_B;
                    break;
                case PAUSE_A:
                    newState = TrackState.WAITING_A;
                    break;
                case PAUSE_B:
                    newState = TrackState.WAITING_B;
                    break;
                default:
                    newState = null;
                    break;
            }

            if( newState != null )
            {
                data.state = newState;
                periodWatch.pauseResume();

                if( newState == TrackState.PAUSE_A ||
                    newState == TrackState.PAUSE_B )
                {
                    setOutputInitPaused();
                }
                else
                {
                    setOutputWaiting();
                }

                dataListeners.fireListeners( this, data );
            }
        }

        printInfo( "signal start/pause" );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void signalStartRun()
    {
        // LogUtils.printDebug(
        // "Starting run for track. state: " + data.state.name );

        TrackState localState = data.state;

        // Don't display a warning message if the sensor was tripped in a bad
        // state.
        if( checkStates( localState, TrackState.WAITING_A,
            TrackState.WAITING_B ) )
        {
            runWatch.start();

            if( localState == TrackState.WAITING_A )
            {
                data.state = TrackState.RUNNING_A;
                setOutputRunning();
            }
            else if( localState == TrackState.WAITING_B )
            {
                data.state = TrackState.RUNNING_B;
                setOutputRunning();
            }

            dataListeners.fireListeners( this, data );
        }

        printInfo( "signal start run" );
    }

    /***************************************************************************
     * Called when a
     **************************************************************************/
    public void signalCompleteRun()
    {
        // LogUtils.printDebug( "Stopping run for track " );

        TrackState localState = data.state;

        if( checkStates( localState, TrackState.RUNNING_A,
            TrackState.RUNNING_B ) )
        {
            runWatch.stop();

            if( localState == TrackState.RUNNING_A )
            {
                data.team.run1Time = getRunTime();
                data.state = TrackState.WAITING_B;
                setOutputWaiting();
            }
            else if( localState == TrackState.RUNNING_B )
            {
                data.team.run2Time = getRunTime();
                stopPeriod();
            }

            data.team.failedCount = data.failedCount;

            dataListeners.fireListeners( this, data );
        }

        printInfo( "signal complete run" );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void signalFailRun()
    {
        // LogUtils.printDebug( "Failing run for track " + view.getTrackName()
        // );

        if( checkState( "fail a run", TrackState.WAITING_A,
            TrackState.RUNNING_A, TrackState.WAITING_B, TrackState.RUNNING_B ) )
        {
            runWatch.stop();

            if( data.failedCount < 5 )
            {
                data.failedCount++;

                if( data.failedCount > 4 )
                {
                    stopPeriod();
                }
                else
                {
                    TrackState localState = data.state;

                    if( localState == TrackState.RUNNING_A )
                    {
                        data.state = TrackState.WAITING_A;
                        setOutputWaiting();
                    }
                    else if( localState == TrackState.RUNNING_B )
                    {
                        data.state = TrackState.WAITING_B;
                        setOutputWaiting();
                    }
                }
            }

            dataListeners.fireListeners( this, data );

            printInfo( "signal fail run" );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void signalResetRun()
    {
        if( checkState( "reset a run", TrackState.RUNNING_A,
            TrackState.RUNNING_B ) )
        {
            runWatch.stop();

            TrackState localState = data.state;

            if( localState == TrackState.RUNNING_A )
            {
                data.state = TrackState.WAITING_A;
                setOutputWaiting();
            }
            else if( localState == TrackState.RUNNING_B )
            {
                data.state = TrackState.WAITING_B;
                setOutputWaiting();
            }

            dataListeners.fireListeners( this, data );
        }

        printInfo( "signal reset run" );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void signalClearTrack()
    {
        // LogUtils.printDebug( "clearing track" );

        if( data.state != TrackState.FINISHED )
        {
            data.errorMsg = "Wait for " + data.getTeamCode() +
                " to finish before clearing track data";
            return;
        }

        this.data.clearTrack();

        setOutputUninitialized();

        dataListeners.fireListeners( this, data );

        printInfo( "signal clear track" );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void signalResetFailure()
    {
        if( data.state == TrackState.FINISHED && data.run2Time > -1 )
        {
            return;
        }
        else if( data.state == TrackState.FINISHED )
        {
            if( data.run1Time < 0 )
            {
                data.state = TrackState.PAUSE_A;
            }
            else
            {
                data.state = TrackState.PAUSE_B;
            }
        }

        data.failedCount--;

        dataListeners.fireListeners( this, data );

        printInfo( "signal reset failure" );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void signalResetSuccess()
    {
        if( data.state == TrackState.FINISHED && data.failedCount == 5 )
        {
            return;
        }
        else if( data.state == TrackState.FINISHED )
        {
            if( data.run2Time < 0 )
            {
                data.run1Time = -1;
                data.team.run1Time = -1;
                data.state = TrackState.PAUSE_A;
            }
            else
            {
                data.run2Time = -1;
                data.team.run2Time = -1;
                data.state = TrackState.PAUSE_B;
            }
        }
        else
        {
            if( data.run2Time < 0 )
            {
                data.run1Time = -1;
                data.team.run1Time = -1;
                data.state = TrackState.WAITING_A;
            }
            else
            {
                data.run2Time = -1;
                data.team.run2Time = -1;
                data.state = TrackState.WAITING_B;
            }
        }

        dataListeners.fireListeners( this, data );

        printInfo( "signal reset success" );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public TrackData updateData()
    {
        data.periodTime = getPeriodTime();

        if( data.state == TrackState.UNINITIALIZED ||
            data.state == TrackState.INITIALIZED )
        {
            data.periodTime = -1;
        }
        else if( data.periodTime < 1 && data.state != TrackState.RUNNING_A &&
            data.state != TrackState.RUNNING_B &&
            data.state != TrackState.FINISHED )
        {
            stopPeriod();
            data.periodTime = getPeriodTime();
        }

        data.setRunTime( getRunTime() );

        return data;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public boolean isRunning()
    {
        return data.state != TrackState.UNINITIALIZED &&
            data.state != TrackState.FINISHED;
    }

    /***************************************************************************
     * @param gpio
     **************************************************************************/
    public void unprovisionAll( GpioController gpio )
    {
        pins.unprovisionAll( gpio );
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addDataListener( ItemActionListener<TrackData> l )
    {
        dataListeners.addListener( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void stopPeriod()
    {
        // LogUtils.printDebug(
        // "Stopping period for track " + view.getTrackName() );

        if( data.state != TrackState.PAUSE_A &&
            data.state != TrackState.PAUSE_B )
        {
            periodWatch.pauseResume();
        }

        data.completeTrack();

        setOutputFinished();

        dataListeners.fireListeners( this, data );
    }

    /***************************************************************************
     * @param message
     **************************************************************************/
    private void printInfo( String message )
    {
        String teamStr = data.team != null ? data.team.schoolCode : "N/A";

        LogUtils.printInfo( "Team [%s] : %s in %s @ %.1f", teamStr, message,
            data.state.getName(), periodWatch.getElapsed() / 10.0 );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private int getPeriodTime()
    {
        int time = Math.round( periodWatch.getElapsed() / 1000.0f );

        time = config.periodTime - time;
        time = Math.max( time, 0 );

        return time;
    }

    /***************************************************************************
     * Returns the run time in tenths of a second.
     **************************************************************************/
    private int getRunTime()
    {
        return Math.round( runWatch.getElapsed() / 100.0f );
    }

    /***************************************************************************
     * @param state
     * @param track
     * @return
     **************************************************************************/
    private boolean checkState( String action, TrackState... states )
    {
        TrackState curState = data.state;

        if( curState == TrackState.UNINITIALIZED )
        {
            data.errorMsg = "No Team Chosen";
            return false;
        }
        else if( states != null && action != null )
        {
            if( !checkStates( curState, states ) )
            {
                data.errorMsg = "Cannot " + action + " in state " +
                    data.state.name;
                return false;
            }
        }

        data.errorMsg = "";
        return true;
    }

    /***************************************************************************
     * Returns {@code true} if the current state is one of the provided states;
     * {@code false} otherwise.
     * @param states the states to be checked.
     **************************************************************************/
    private static boolean checkStates( TrackState curState,
        TrackState... states )
    {
        for( TrackState state : states )
        {
            if( curState == state )
            {
                return true;
            }
        }

        return false;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void setOutputUninitialized()
    {
        pins.redPin.setState( track.redPin.level.inverse().state );
        pins.greenPin.setState( track.greenPin.level.state );
        pins.bluePin.setState( track.bluePin.level.inverse().state );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void setOutputInitPaused()
    {
        pins.redPin.setState( track.redPin.level.inverse().state );
        pins.greenPin.setState( track.greenPin.level.state );
        pins.bluePin.setState( track.bluePin.level.state );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void setOutputWaiting()
    {
        pins.redPin.setState( track.redPin.level.inverse().state );
        pins.greenPin.setState( track.greenPin.level.inverse().state );
        pins.bluePin.setState( track.bluePin.level.inverse().state );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void setOutputRunning()
    {
        pins.redPin.setState( track.redPin.level.state );
        pins.greenPin.setState( track.greenPin.level.inverse().state );
        pins.bluePin.setState( track.bluePin.level.state );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void setOutputFinished()
    {
        pins.redPin.setState( track.redPin.level.state );
        pins.greenPin.setState( track.greenPin.level.state );
        pins.bluePin.setState( track.bluePin.level.inverse().state );
    }
}
