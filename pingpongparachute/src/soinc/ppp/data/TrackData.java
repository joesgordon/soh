package soinc.ppp.data;

import java.util.Arrays;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TrackData
{
    /**  */
    public Team team;
    /**  */
    public TrackState state;

    /** Milliseconds */
    public long periodTime;

    /**  */
    public long [] timers;

    /**  */
    public long officialTime;

    /**  */
    public long run1Time;
    /**  */
    public RunState run1State;
    /**  */
    public long run2Time;
    /**  */
    public RunState run2State;

    /***************************************************************************
     * @param timerCount
     **************************************************************************/
    public TrackData( int timerCount )
    {
        this.timers = new long[timerCount];
        reset();
    }

    /***************************************************************************
     * @param data
     **************************************************************************/
    public TrackData( TrackData data )
    {
        this.team = data.team;
        this.state = data.state;
        this.periodTime = data.periodTime;
        this.timers = Arrays.copyOf( data.timers, data.timers.length );
        this.officialTime = data.officialTime;
        this.run1Time = data.run1Time;
        this.run1State = data.run1State;
        this.run2Time = data.run2Time;
        this.run2State = data.run2State;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public boolean isRunning()
    {
        return run1State == RunState.RUNNING || run2State == RunState.RUNNING;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void reset()
    {
        this.team = null;
        this.state = TrackState.NO_TEAM;
        this.periodTime = -1L;
        for( int i = 0; i < timers.length; i++ )
        {
            timers[i] = -1L;
        }
        this.officialTime = -1L;
        this.run1Time = -1L;
        this.run1State = RunState.NOT_RUN;
        this.run2Time = -1L;
        this.run2State = RunState.NOT_RUN;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static enum RunState
    {
        /**  */
        NOT_RUN( false ),
        /**  */
        RUNNING( false ),
        /**  */
        FAILED( true ),
        /**  */
        SUCCESS( true );

        public final boolean isComplete;

        private RunState( boolean isComplete )
        {
            this.isComplete = isComplete;
        }
    }
}
