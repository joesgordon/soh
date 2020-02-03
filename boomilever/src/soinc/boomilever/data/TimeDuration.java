package soinc.boomilever.data;

public class TimeDuration
{
    public final long totalMillis;
    public long millis;

    public long totalSeconds;
    public long seconds;

    public long totalMinutes;

    public TimeDuration( long milliseconds )
    {
        this.totalMillis = milliseconds;
        this.millis = milliseconds % 1000;
        this.totalSeconds = milliseconds / 1000;
        this.seconds = totalSeconds % 60;
        this.totalMinutes = totalSeconds / 60;
    }
}
