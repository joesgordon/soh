package soinc.gravityvehicle.data;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Team
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
    public Team( String name )
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