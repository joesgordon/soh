package soinc.boomilever.data;

/***************************************************************************
 * 
 **************************************************************************/
public class Event
{
    /**  */
    public final CompetitionData trackA;
    /**  */
    public final CompetitionData trackB;

    /***************************************************************************
     * 
     **************************************************************************/
    public Event()
    {
        this.trackA = new CompetitionData();
        this.trackB = new CompetitionData();
    }
}
