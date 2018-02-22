package soinc.rollercoaster.data;

/*******************************************************************************
 * 
 ******************************************************************************/
public class RollercoasterUserData
{
    /**  */
    public final RollercoasterConfig config;

    /***************************************************************************
     * 
     **************************************************************************/
    public RollercoasterUserData()
    {
        this.config = new RollercoasterConfig();
    }

    /***************************************************************************
     * @param data
     **************************************************************************/
    public RollercoasterUserData( RollercoasterUserData data )
    {
        this.config = data.config == null ? new RollercoasterConfig()
            : new RollercoasterConfig( data.config );
    }
}
