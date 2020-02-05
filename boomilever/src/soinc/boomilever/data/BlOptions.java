package soinc.boomilever.data;

/*******************************************************************************
 * 
 ******************************************************************************/
public class BlOptions
{
    /**  */
    public final EventConfig config;
    /**  */
    public boolean useFauxGpio;

    /***************************************************************************
     * 
     **************************************************************************/
    public BlOptions()
    {
        this.config = new EventConfig();
        this.useFauxGpio = false;
    }

    /***************************************************************************
     * @param data
     **************************************************************************/
    public BlOptions( BlOptions data )
    {
        this.config = data.config == null ? new EventConfig()
            : new EventConfig( data.config );
        this.useFauxGpio = data.useFauxGpio;
    }
}
