package soinc.lib.data;

public class SciolyOptions
{
    /**  */
    public PinTestSuite testSuite;

    /**
     * 
     */
    public SciolyOptions()
    {
        this.testSuite = new PinTestSuite();
    }

    public SciolyOptions( SciolyOptions options )
    {
        this.testSuite = options.testSuite != null
            ? new PinTestSuite( options.testSuite ) : new PinTestSuite();
    }
}
