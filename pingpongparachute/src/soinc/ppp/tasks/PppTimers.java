package soinc.ppp.tasks;

import java.util.Arrays;

import org.jutils.utils.Stopwatch;

import soinc.ppp.data.TrackData;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PppTimers
{
    /**  */
    private final Stopwatch [] timers;
    /**  */
    private final boolean [] used;

    /***************************************************************************
     * @param timerCount
     **************************************************************************/
    public PppTimers( int timerCount )
    {
        this.timers = new Stopwatch[timerCount];
        this.used = new boolean[timerCount];

        for( int i = 0; i < timers.length; i++ )
        {
            timers[i] = new Stopwatch();
            timers[i].stop();
            used[i] = false;
        }
    }

    /***************************************************************************
     * @param index
     **************************************************************************/
    public void clearTimer( int index )
    {
        used[index] = false;
        timers[index].stop();
    }

    /***************************************************************************
     * @param index
     * @return
     **************************************************************************/
    public boolean hasStopped( int index )
    {
        return used[index] && timers[index].isStopped();
    }

    /***************************************************************************
     * @param data
     **************************************************************************/
    public void setData( TrackData data )
    {
        long now = System.currentTimeMillis();
        for( int i = 0; i < timers.length; i++ )
        {
            if( used[i] )
            {
                data.timers[i] = timers[i].getElapsed( now );
            }
            else
            {
                data.timers[i] = -1L;
            }
        }

        data.officialTime = getOfficialDuration( now );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public boolean areComplete()
    {
        for( int i = 0; i < timers.length; i++ )
        {
            if( !timers[i].isStopped() )
            {
                return false;
            }
        }

        return true;
    }

    /***************************************************************************
     * @param index
     * @param started
     **************************************************************************/
    public void setTimerStarted( int index, boolean started )
    {
        if( started )
        {
            timers[index].start();
            used[index] = true;
        }
        else
        {
            timers[index].stop();
        }
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public boolean hasStarted()
    {
        for( int i = 0; i < timers.length; i++ )
        {
            if( used[i] )
            {
                return true;
            }
        }

        return false;
    }

    /***************************************************************************
     * @param index
     * @return
     **************************************************************************/
    public boolean hasStarted( int index )
    {
        return used[index];
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public long getOfficialDuration()
    {
        return getOfficialDuration( -1L );
    }

    /***************************************************************************
     * @param end
     * @return
     **************************************************************************/
    private long getOfficialDuration( long end )
    {
        long sum = 0L;
        int count = 0;
        long [] durations = new long[timers.length];

        for( int i = 0; i < timers.length; i++ )
        {
            if( used[i] )
            {
                if( end > -1L )
                {
                    durations[i] = timers[i].getElapsed( end );
                }
                else
                {
                    durations[i] = timers[i].getElapsed();
                }
                sum += durations[i];
                count++;
            }
        }

        switch( ( int )count )
        {
            case 0:
                return -1L;

            case 1:
                return sum;

            case 2:
                return sum / count;

            default:
                Arrays.sort( durations );
                return durations[durations.length - count + ( count ) / 2];
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void clear()
    {
        for( int i = 0; i < timers.length; i++ )
        {
            timers[i].stop();
            used[i] = false;
        }
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getSize()
    {
        return timers.length;
    }
}
