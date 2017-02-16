package soh;

import javax.swing.SwingUtilities;
import javax.swing.Timer;

import org.jutils.Stopwatch;
import org.jutils.io.LogUtils;

import soh.data.*;
import soh.ui.TrackView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TrackCompetition
{
    /**  */
    public final TrackView view;
    /**  */
    public final HoverConfig config;
    /**  */
    public final Team team;
    /**  */
    public final DivisionConfig division;
    /**  */
    private final javax.swing.Timer updateTimer;

    /**  */
    private final Stopwatch periodWatch;
    /**  */
    private final Stopwatch runWatch;

    /**  */
    private final Track track;

    /***************************************************************************
     * @param view
     * @param config
     * @param team
     **************************************************************************/
    public TrackCompetition( TrackView view, HoverConfig config, Team team )
    {
        this.view = view;
        this.config = config;
        this.team = team;
        this.division = config.getDivision( team.div );
        this.updateTimer = new Timer( 50, ( e ) -> edtUpdateTimes() );

        this.periodWatch = new Stopwatch();
        this.runWatch = new Stopwatch();

        this.track = new Track();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void initializeTrack()
    {
        LogUtils.printDebug( "Initializing track " + view.getTrackName() );

        track.reset();
        SwingUtilities.invokeLater(
            () -> view.setPeriodTime( config.periodTime ) );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void startPeriod()
    {
        LogUtils.printDebug(
            "Starting period for track " + view.getTrackName() );

        track.state = TrackState.WAITING_A;
        periodWatch.start();
        updateTimer.start();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void stopPeriod()
    {
        LogUtils.printDebug(
            "Stopping period for track " + view.getTrackName() );

        track.state = TrackState.FINISHED;
        periodWatch.stop();
        updateTimer.stop();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void startRun()
    {
        LogUtils.printDebug( "Starting run for track " + view.getTrackName() );

        runWatch.start();

        TrackState state = track.state;

        if( state == TrackState.WAITING_A )
        {
            track.state = TrackState.RUNNING_A;
        }
        else if( state == TrackState.WAITING_B )
        {
            track.state = TrackState.RUNNING_B;
        }
    }

    /***************************************************************************
     * Called when a
     **************************************************************************/
    public void stopRun()
    {
        LogUtils.printDebug( "Stopping run for track " + view.getTrackName() );

        runWatch.stop();

        TrackState state = track.state;

        if( state == TrackState.RUNNING_A )
        {
            track.runaTime = getRunTime();
            track.state = TrackState.WAITING_B;
        }
        else if( state == TrackState.RUNNING_B )
        {
            track.runbTime = getRunTime();
            stopPeriod();
            SwingUtilities.invokeLater( () -> edtUpdateTimes() );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void failRun()
    {
        LogUtils.printDebug( "Failing run for track " + view.getTrackName() );

        runWatch.stop();

        if( track.failCnt < 5 )
        {
            track.failCnt++;

            if( track.failCnt > 4 )
            {
                stopPeriod();
                SwingUtilities.invokeLater( () -> edtUpdateTimes() );
            }
            else
            {
                TrackState state = track.state;

                if( state == TrackState.RUNNING_A )
                {
                    track.state = TrackState.WAITING_A;
                }
                else if( state == TrackState.RUNNING_B )
                {
                    track.state = TrackState.WAITING_B;
                }
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void stop()
    {
        updateTimer.stop();
        track.reset();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public TrackState getState()
    {
        return track.state;
    }

    /***************************************************************************
     * Run on the EDT
     **************************************************************************/
    private void edtUpdateTimes()
    {
        int periodSecs = getPeriodTime();
        int runSecs = getRunTime();
        int runaSecs = track.runaTime;
        int runbSecs = track.runbTime;

        TrackState state = track.state;

        if( ( state == TrackState.RUNNING_A ||
            state == TrackState.RUNNING_B ) && runSecs > 0 &&
            runSecs > ( 3 * division.targetTime ) )
        {
            failRun();
        }

        state = track.state;

        if( periodSecs < 1 && state != TrackState.RUNNING_A &&
            state != TrackState.RUNNING_B )
        {
            stopPeriod();
        }

        state = track.state;

        if( state == TrackState.RUNNING_A )
        {
            runaSecs = runSecs;
        }
        else if( state == TrackState.RUNNING_B )
        {
            runbSecs = runSecs;
        }

        // LogUtils.printDebug( "Updating times for track " +
        // view.getTrackName() +
        // " @ " + runaSecs );

        if( state != TrackState.FINISHED )
        {
            view.setPeriodTime( periodSecs );
            view.setFinished( false );
        }
        else
        {
            view.setPeriodTime( periodSecs );
            view.setFinished( true );
        }

        view.setRunaTime( runaSecs, state.runaComplete );
        view.setRunbTime( runbSecs, state.runbComplete );
        view.setFailCount( track.failCnt );
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
}
