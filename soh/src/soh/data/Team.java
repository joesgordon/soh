package soh.data;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Team
{
    // Inputs

    /**  */
    public String schoolCode;
    /**  */
    public Division div;

    // Outputs

    /**  */
    public int failedCount;
    /** First successful time in tenths of seconds. */
    public int time1;
    /** Second successful time in tenths of seconds. */
    public int time2;
    /**  */
    public boolean finished;

    /***************************************************************************
     * 
     **************************************************************************/
    public Team()
    {
        this.schoolCode = "";
        this.div = Division.DIVISION_B;
        this.failedCount = 0;
        this.time1 = -1;
        this.time2 = -1;
        this.finished = false;
    }

    /***************************************************************************
     * @param t
     **************************************************************************/
    public Team( Team t )
    {
        this.schoolCode = t.schoolCode;
        this.div = t.div;
        this.failedCount = t.failedCount;
        this.time1 = t.time1;
        this.time2 = t.time2;
        this.finished = t.finished;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void initTrials()
    {
        this.failedCount = 0;
        this.time1 = -1;
        this.time2 = -1;
        this.finished = false;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public boolean isFinished()
    {
        return finished;
    }
}
