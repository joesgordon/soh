package soh;

import java.io.File;

import org.jutils.io.IOUtils;
import org.jutils.io.options.OptionsSerializer;
import org.jutils.ui.app.FrameRunner;

import soh.data.HoverConfig;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SohMain
{
    /**  */
    private static final File userFile = IOUtils.getUsersFile(
        "ScienceOlympiad", "soh.xml" );

    /**  */
    private static OptionsSerializer<HoverConfig> options;

    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String [] args )
    {
        FrameRunner.invokeLater( new SohApp() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static OptionsSerializer<HoverConfig> getOptions()
    {
        if( options == null )
        {
            options = OptionsSerializer.getOptions( HoverConfig.class,
                userFile );
        }

        return options;
    }
}
