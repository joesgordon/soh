package soh;

import javax.swing.SwingUtilities;
import javax.swing.Timer;

import org.jutils.Stopwatch;

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
    private final javax.swing.Timer updateTimer;

    /**  */
    private final Stopwatch periodWatch;
    /**  */
    private final Stopwatch runWatch;

    /**  */
    private TrackState state;

    /**  */
    public Team team;
    /**  */
    public int targetTime;

    /***************************************************************************
     * @param view
     * @param config
     * @param team
     **************************************************************************/
    public TrackCompetition( TrackView view, HoverConfig config )
    {
        this.view = view;
        this.config = config;
        this.updateTimer = new Timer( 50, ( e ) -> edtUpdateTimes() );

        this.periodWatch = new Stopwatch();
        this.runWatch = new Stopwatch();

        this.state = TrackState.UNINTIALIZED;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void initializeTrack( Team team )
    {
        DivisionConfig div = config.getDivision( team.div );
        this.team = team;
        this.targetTime = div.targetTime;
        this.state = TrackState.INITIALIZED;

        // LogUtils.printDebug( "Initializing track " + view.getTrackName() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void startPeriod()
    {
        // LogUtils.printDebug(
        // "Starting period for track " + view.getTrackName() );

        SwingUtilities.invokeLater(
            () -> view.setPeriodTime( config.periodTime ) );

        state = TrackState.WAITING_A;
        periodWatch.start();
        updateTimer.start();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void stopPeriod()
    {
        // LogUtils.printDebug(
        // "Stopping period for track " + view.getTrackName() );

        periodWatch.stop();
        updateTimer.stop();

        state = TrackState.FINISHED;
        team.finished = true;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void pauseResume()
    {
        periodWatch.pauseResume();

        TrackState localState = state;

        if( localState.runaComplete )
        {
            state = TrackState.WAITING_B;
        }
        else
        {
            state = TrackState.WAITING_A;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void startRun()
    {
        // LogUtils.printDebug( "Starting run for track " + view.getTrackName()
        // );

        runWatch.start();

        TrackState localState = state;

        if( localState == TrackState.WAITING_A )
        {
            state = TrackState.RUNNING_A;
        }
        else if( localState == TrackState.WAITING_B )
        {
            state = TrackState.RUNNING_B;
        }
    }

    /***************************************************************************
     * Called when a
     **************************************************************************/
    public void stopRun()
    {
        // LogUtils.printDebug( "Stopping run for track " + view.getTrackName()
        // );

        runWatch.stop();

        TrackState localState = state;

        if( localState == TrackState.RUNNING_A )
        {
            team.time1 = getRunTime();
            state = TrackState.WAITING_B;
        }
        else if( localState == TrackState.RUNNING_B )
        {
            team.time2 = getRunTime();
            stopPeriod();
            SwingUtilities.invokeLater( () -> edtUpdateTimes() );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void failRun()
    {
        // LogUtils.printDebug( "Failing run for track " + view.getTrackName()
        // );

        runWatch.stop();

        if( team.failedCount < 5 )
        {
            team.failedCount++;

            if( team.failedCount > 4 )
            {
                stopPeriod();
                SwingUtilities.invokeLater( () -> edtUpdateTimes() );
            }
            else
            {
                TrackState localState = state;

                if( localState == TrackState.RUNNING_A )
                {
                    state = TrackState.WAITING_A;
                }
                else if( localState == TrackState.RUNNING_B )
                {
                    state = TrackState.WAITING_B;
                }
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void resetTrial()
    {
        runWatch.stop();

        TrackState localState = state;

        if( localState == TrackState.RUNNING_A )
        {
            team.time1 = -1;
            state = TrackState.WAITING_A;
        }
        else if( localState == TrackState.RUNNING_B )
        {
            team.time2 = -1;
            state = TrackState.WAITING_B;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void clearTrack()
    {
        updateTimer.stop();
        this.state = TrackState.UNINTIALIZED;
        this.team = null;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public TrackState getState()
    {
        return state;
    }

    /***************************************************************************
     * Run on the EDT
     **************************************************************************/
    private void edtUpdateTimes()
    {
        int periodSecs = getPeriodTime();
        int runSecs = getRunTime();
        int runaSecs = team.time1;
        int runbSecs = team.time2;

        TrackState localState = state;

        if( ( localState == TrackState.RUNNING_A ||
            localState == TrackState.RUNNING_B ) && runSecs > 0 &&
            runSecs > ( 3 * targetTime ) )
        {
            failRun();
        }

        localState = state;

        if( periodSecs < 1 && localState != TrackState.RUNNING_A &&
            localState != TrackState.RUNNING_B )
        {
            stopPeriod();
        }

        localState = state;

        if( localState == TrackState.RUNNING_A )
        {
            runaSecs = runSecs;
        }
        else if( localState == TrackState.RUNNING_B )
        {
            runbSecs = runSecs;
        }

        // LogUtils.printDebug( "Updating times for track " +
        // view.getTrackName() +
        // " @ " + runaSecs );

        if( localState != TrackState.FINISHED )
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
        view.setFailCount( team.failedCount );
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
