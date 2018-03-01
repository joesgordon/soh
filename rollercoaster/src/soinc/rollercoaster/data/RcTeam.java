package soinc.rollercoaster.data;

/*******************************************************************************
 * 
 ******************************************************************************/
public class RcTeam
{
    public final String name;
    /** First successful time in tenths of seconds. */
    public int run1Time;
    /** Second successful time in tenths of seconds. */
    public int run2Time;
    /**  */
    public boolean loaded;

    /***************************************************************************
     * @param name
     **************************************************************************/
    public RcTeam( String name )
    {
        this.name = name;
        this.run1Time = -1;
        this.run2Time = -1;
        this.loaded = false;
    }

    public boolean isRunsComplete()
    {
        return ( run1Time > 0 && run2Time > 0 );
    }
}
