package soinc.rollercoaster.data;

/*******************************************************************************
 * 
 ******************************************************************************/
public class RcOptions
{
    /**  */
    public final RcConfig config;
    /**  */
    public boolean useFauxGpio;

    /***************************************************************************
     * 
     **************************************************************************/
    public RcOptions()
    {
        this.config = new RcConfig();
        this.useFauxGpio = false;
    }

    /***************************************************************************
     * @param data
     **************************************************************************/
    public RcOptions( RcOptions data )
    {
        this.config = data.config == null ? new RcConfig()
            : new RcConfig( data.config );
        this.useFauxGpio = data.useFauxGpio;
    }
}
