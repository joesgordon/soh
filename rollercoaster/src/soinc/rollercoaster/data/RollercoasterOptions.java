package soinc.rollercoaster.data;

/*******************************************************************************
 * 
 ******************************************************************************/
public class RollercoasterOptions
{
    /**  */
    public final RollercoasterConfig config;
    /**  */
    public boolean useFauxGpio;

    /***************************************************************************
     * 
     **************************************************************************/
    public RollercoasterOptions()
    {
        this.config = new RollercoasterConfig();
        this.useFauxGpio = false;
    }

    /***************************************************************************
     * @param data
     **************************************************************************/
    public RollercoasterOptions( RollercoasterOptions data )
    {
        this.config = data.config == null ? new RollercoasterConfig()
            : new RollercoasterConfig( data.config );
        this.useFauxGpio = data.useFauxGpio;
    }
}
