package soinc.boomilever.data;

/*******************************************************************************
 * 
 ******************************************************************************/
public class BlOptions
{
    /**  */
    public final BlEventConfig config;
    /**  */
    public boolean useFauxGpio;

    /***************************************************************************
     * 
     **************************************************************************/
    public BlOptions()
    {
        this.config = new BlEventConfig();
        this.useFauxGpio = false;
    }

    /***************************************************************************
     * @param data
     **************************************************************************/
    public BlOptions( BlOptions data )
    {
        this.config = data.config == null ? new BlEventConfig()
            : new BlEventConfig( data.config );
        this.useFauxGpio = data.useFauxGpio;
    }
}
