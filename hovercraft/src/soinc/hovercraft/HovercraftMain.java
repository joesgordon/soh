package soinc.hovercraft;

import java.io.File;

import org.jutils.io.IOUtils;
import org.jutils.io.LogUtils;
import org.jutils.io.options.IOptionsCreator;
import org.jutils.io.options.OptionsSerializer;
import org.jutils.ui.app.FrameRunner;

import soinc.hovercraft.data.HovercraftOptions;
import soinc.lib.gpio.SciolyGpio;

/*******************************************************************************
 * 
 ******************************************************************************/
public class HovercraftMain
{
    /**  */
    private static final File userFile = IOUtils.getUsersFile(
        ".ScienceOlympiad", "soh.xml" );

    /**  */
    private static OptionsSerializer<HovercraftOptions> options;

    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String [] args )
    {
        OptionsSerializer<HovercraftOptions> options = HovercraftMain.getOptions();

        SciolyGpio.FAUX_CONNECT = options.getOptions().useFauxGpio;

        FrameRunner.invokeLater( new HovercraftApp() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static OptionsSerializer<HovercraftOptions> getOptions()
    {
        if( options == null )
        {
            IOptionsCreator<HovercraftOptions> ioc = new OptionsCreator();
            options = OptionsSerializer.getOptions( ioc, userFile );
        }

        return options;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class OptionsCreator
        implements IOptionsCreator<HovercraftOptions>
    {

        @Override
        public HovercraftOptions createDefaultOptions()
        {
            return new HovercraftOptions();
        }

        @Override
        public HovercraftOptions initialize( HovercraftOptions data )
        {
            return new HovercraftOptions( data );
        }

        @Override
        public void warn( String msg )
        {
            LogUtils.printWarning( msg );
        }
    }
}
