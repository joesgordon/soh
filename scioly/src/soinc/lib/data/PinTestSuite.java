package soinc.lib.data;

import java.util.ArrayList;
import java.util.List;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PinTestSuite
{
    /**  */
    public final List<PinData> pins;

    /***************************************************************************
     * 
     **************************************************************************/
    public PinTestSuite()
    {
        this.pins = new ArrayList<>();

        Pi3HeaderPin [] p3ps = Pi3HeaderPin.values();

        for( Pi3HeaderPin p3p : p3ps )
        {
            pins.add( new PinData( p3p ) );
        }
    }

    /***************************************************************************
     * @param testSuite
     **************************************************************************/
    public PinTestSuite( PinTestSuite suite )
    {
        this.pins = new ArrayList<>();

        if( suite.pins != null )
        {
            for( PinData pd : suite.pins )
            {
                pins.add( new PinData( pd ) );
            }
        }
    }

    public void initialize()
    {
        for( PinData pd : pins )
        {
            pd.provisioned = false;
        }
    }
}
