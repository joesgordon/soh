package soinc.rollercoaster.data;

public class RcCompetitionData
{
    /**  */
    public RcTeam team;
    /** Milliseconds */
    public long periodStart;
    /**  */
    public long run1Time;
    /**  */
    public boolean run1Complete;
    /**  */
    public long run2Time;
    /**  */
    public boolean run2Complete;

    public RcCompetitionData()
    {
        reset();
    }

    public void reset()
    {
        this.team = null;
        this.periodStart = -1L;
        this.run1Time = -1L;
        this.run1Complete = false;
        this.run2Time = -1L;
        this.run2Complete = false;
    }
}
