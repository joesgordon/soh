package soinc.boomilever.data;

/*******************************************************************************
 * 
 ******************************************************************************/
public class BlTeam
{
    /**  */
    public String name;
    /**  */
    public boolean loaded;
    /**  */
    public boolean complete;

    /***************************************************************************
     * @param name
     **************************************************************************/
    public BlTeam( String name )
    {
        this.name = name;

        reset();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void reset()
    {
        this.loaded = false;
        this.complete = false;
    }
}
