package soh.data;

import java.io.File;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SohOptions
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
    public SohOptions()
    {
        this.useFauxGpio = false;
        this.config = new HoverConfig();
        this.lastConfigFile = null;
    }

    /***************************************************************************
     * @param options
     **************************************************************************/
    public SohOptions( SohOptions options )
    {
        this.useFauxGpio = options.useFauxGpio;
        this.config = options.config != null ? new HoverConfig( options.config )
            : new HoverConfig();
        this.lastConfigFile = options.lastConfigFile;
    }
}
