package soinc.ppp.data;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PppOptions
{
    /**  */
    public final EventConfig config;
    /**  */
    public boolean useFauxGpio;

    /***************************************************************************
     * 
     **************************************************************************/
    public PppOptions()
    {
        this.config = new EventConfig();
        this.useFauxGpio = false;
    }

    /***************************************************************************
     * @param data
     **************************************************************************/
    public PppOptions( PppOptions data )
    {
        this.config = data.config == null ? new EventConfig()
            : new EventConfig( data.config );
        this.useFauxGpio = data.useFauxGpio;
    }
}
