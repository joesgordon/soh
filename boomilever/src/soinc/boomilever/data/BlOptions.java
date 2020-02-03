package soinc.boomilever.data;

/*******************************************************************************
 * 
 ******************************************************************************/
public class BlOptions
{
    /**  */
    public final CompetitionConfig config;
    /**  */
    public boolean useFauxGpio;

    /***************************************************************************
     * 
     **************************************************************************/
    public BlOptions()
    {
        this.config = new CompetitionConfig();
        this.useFauxGpio = false;
    }

    /***************************************************************************
     * @param data
     **************************************************************************/
    public BlOptions( BlOptions data )
    {
        this.config = data.config == null ? new CompetitionConfig()
            : new CompetitionConfig( data.config );
        this.useFauxGpio = data.useFauxGpio;
    }
}
