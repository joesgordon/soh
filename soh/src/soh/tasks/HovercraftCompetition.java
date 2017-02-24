package soh.tasks;

import com.pi4j.io.gpio.GpioController;

public class HovercraftCompetition
{
    public final TrackCompetition track1;
    public final TrackCompetition track2;

    public HovercraftCompetition( TrackCompetition track1,
        TrackCompetition track2 )
    {
        this.track1 = track1;
        this.track2 = track2;
    }

    public void unprovisionAll( GpioController gpio )
    {
        track1.unprovisionAll( gpio );
        track2.unprovisionAll( gpio );
    }
}
