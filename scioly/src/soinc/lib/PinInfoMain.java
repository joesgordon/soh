package soinc.lib;

import java.util.EnumSet;

import org.jutils.io.LogUtils;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.RaspiPin;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PinInfoMain
{
    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String [] args )
    {
        Pin [] pins = RaspiPin.allPins();

        for( Pin p : pins )
        {
            EnumSet<PinMode> modes = p.getSupportedPinModes();
            boolean canDigOutput = modes.contains( PinMode.DIGITAL_OUTPUT );
            boolean canDigIn = modes.contains( PinMode.DIGITAL_INPUT );
            LogUtils.printInfo( "Pin %s (%d) >%s< <%s>", p.getName(),
                p.getAddress(), canDigIn, canDigOutput );
        }
    }
}
