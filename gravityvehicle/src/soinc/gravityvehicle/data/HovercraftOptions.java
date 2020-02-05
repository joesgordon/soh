package soinc.gravityvehicle.data;

import java.io.File;

/*******************************************************************************
 * 
 ******************************************************************************/
public class HovercraftOptions
{
    /**  */
    public boolean useFauxGpio;
    /**  */
    public HoverConfig config;
    /**  */
    public File lastConfigFile;

    /***************************************************************************
     * 
     **************************************************************************/
    public HovercraftOptions()
    {
        this.useFauxGpio = false;
        this.config = new HoverConfig();
        this.lastConfigFile = null;
    }

    /***************************************************************************
     * @param options
     **************************************************************************/
    public HovercraftOptions( HovercraftOptions options )
    {
        this.useFauxGpio = options.useFauxGpio;
        this.config = options.config != null ? new HoverConfig( options.config )
            : new HoverConfig();
        this.lastConfigFile = options.lastConfigFile;
    }
}
