package soinc.rollercoaster.data;

/*******************************************************************************
 * 
 ******************************************************************************/
public class RcTeam
{
    public String name;
    /** First successful time in tenths of seconds. */
    public int run1Time;
    /** Second successful time in tenths of seconds. */
    public int run2Time;
    /**  */
    public boolean loaded;
    /**  */
    public boolean complete;

    /***************************************************************************
     * @param name
     **************************************************************************/
    public RcTeam( String name )
    {
        this.name = name;

        reset();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void reset()
    {
        this.run1Time = -1;
        this.run2Time = -1;
        this.loaded = false;
        this.complete = false;
    }
}
