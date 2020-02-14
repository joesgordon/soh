package soinc.gravityvehicle.data;

/*******************************************************************************
 * 
 ******************************************************************************/
public class GvOptions
{
    /**  */
    public final GvEventConfig config;
    /**  */
    public boolean useFauxGpio;

    /***************************************************************************
     * 
     **************************************************************************/
    public GvOptions()
    {
        this.config = new GvEventConfig();
        this.useFauxGpio = false;
    }

    /***************************************************************************
     * @param data
     **************************************************************************/
    public GvOptions( GvOptions data )
    {
        this.config = data.config == null ? new GvEventConfig()
            : new GvEventConfig( data.config );
        this.useFauxGpio = data.useFauxGpio;
    }
}
