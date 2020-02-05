package soinc.ppp.data;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PppOptions
{
    /**  */
    public final CompetitionConfig config;
    /**  */
    public boolean useFauxGpio;

    /***************************************************************************
     * 
     **************************************************************************/
    public PppOptions()
    {
        this.config = new CompetitionConfig();
        this.useFauxGpio = false;
    }

    /***************************************************************************
     * @param data
     **************************************************************************/
    public PppOptions( PppOptions data )
    {
        this.config = data.config == null ? new CompetitionConfig()
            : new CompetitionConfig( data.config );
        this.useFauxGpio = data.useFauxGpio;
    }
}
