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

    /***************************************************************************
     * 
     **************************************************************************/
    public Team()
    {
        this.schoolCode = "";
        this.div = Division.DIVISION_B;
        this.failedCount = 0;
        this.time1 = 0;
        this.time2 = 0;
    }
}
