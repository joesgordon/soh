package soinc.hovercraft.data;

import java.io.File;

import soinc.lib.data.PinTestSuite;

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
    public PinTestSuite testSuite;
    /**  */
    public File lastConfigFile;

    /***************************************************************************
     * 
     **************************************************************************/
    public SohOptions()
    {
        this.useFauxGpio = false;
        this.config = new HoverConfig();
        this.testSuite = new PinTestSuite();
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
        this.testSuite = options.testSuite != null
            ? new PinTestSuite( options.testSuite ) : new PinTestSuite();
        this.lastConfigFile = options.lastConfigFile;
    }
}
