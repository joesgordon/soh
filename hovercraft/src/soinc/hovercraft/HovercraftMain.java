package soinc.hovercraft;

import java.io.File;

import org.jutils.io.IOUtils;
import org.jutils.io.LogUtils;
import org.jutils.io.options.IOptionsCreator;
import org.jutils.io.options.OptionsSerializer;
import org.jutils.ui.app.FrameRunner;

import soinc.hovercraft.data.SohOptions;
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
    private static OptionsSerializer<SohOptions> options;

    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String [] args )
    {
        OptionsSerializer<SohOptions> options = HovercraftMain.getOptions();

        SciolyGpio.FAUX_CONNECT = options.getOptions().useFauxGpio;

        FrameRunner.invokeLater( new HovercraftApp() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static OptionsSerializer<SohOptions> getOptions()
    {
        if( options == null )
        {
            IOptionsCreator<SohOptions> ioc = new OptionsCreator();
            options = OptionsSerializer.getOptions( ioc, userFile );
        }

        return options;
    }

    private static final class OptionsCreator
        implements IOptionsCreator<SohOptions>
    {

        @Override
        public SohOptions createDefaultOptions()
        {
            return new SohOptions();
        }

        @Override
        public SohOptions initialize( SohOptions data )
        {
            return new SohOptions( data );
        }

        @Override
        public void warn( String msg )
        {
            LogUtils.printWarning( msg );
        }
    }
}
