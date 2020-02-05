package soinc.gravityvehicle.data;

/*******************************************************************************
 * 
 ******************************************************************************/
public class HcTeam
{
    // -------------------------------------------------------------------------
    // Configuration
    // -------------------------------------------------------------------------

    /**  */
    public String schoolCode;
    /**  */
    public Division div;

    // -------------------------------------------------------------------------
    // Outputs
    // -------------------------------------------------------------------------

    /**  */
    public int failedCount;
    /** First successful time in tenths of seconds. */
    public int run1Time;
    /** Second successful time in tenths of seconds. */
    public int run2Time;
    /**  */
    public boolean loaded;

    /***************************************************************************
     * 
     **************************************************************************/
    public HcTeam()
    {
        this.schoolCode = "";
        this.div = Division.DIVISION_B;
        this.failedCount = 0;
        this.run1Time = -1;
        this.run2Time = -1;
        this.loaded = false;
    }

    /***************************************************************************
     * @param t
     **************************************************************************/
    public HcTeam( HcTeam t )
    {
        this.schoolCode = t.schoolCode;
        this.div = t.div;
        this.failedCount = t.failedCount;
        this.run1Time = t.run1Time;
        this.run2Time = t.run2Time;
        this.loaded = t.loaded;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void initTrials()
    {
        this.failedCount = 0;
        this.run1Time = -1;
        this.run2Time = -1;
        this.loaded = false;
    }

    public boolean isFinished()
    {
        return run2Time > 0 || failedCount >= 5;
    }
}
