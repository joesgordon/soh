package soinc.boomilever.data;

public class CompetitionData
{
    /**  */
    public Team team;
    /**  */
    public CompetitionState state;
    /** Milliseconds */
    public long periodTime;

    /**
     * @param timerCount
     */
    public CompetitionData()
    {
        reset();
    }

    public CompetitionData( CompetitionData data )
    {
        this.team = data.team;
        this.state = data.state;
        this.periodTime = data.periodTime;
    }

    public void reset()
    {
        this.team = null;
        this.state = CompetitionState.NO_TEAM;
        this.periodTime = -1L;
    }
}
