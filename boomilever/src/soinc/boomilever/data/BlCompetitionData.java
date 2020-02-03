package soinc.boomilever.data;

public class BlCompetitionData
{
    /**  */
    public BlTeam team;
    /**  */
    public CompetitionState state;
    /** Milliseconds */
    public long periodTime;

    /**
     * @param timerCount
     */
    public BlCompetitionData()
    {
        reset();
    }

    public BlCompetitionData( BlCompetitionData data )
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
