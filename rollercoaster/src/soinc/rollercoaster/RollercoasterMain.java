package soinc.rollercoaster;

import java.io.File;

import org.jutils.io.IOUtils;
import org.jutils.io.LogUtils;
import org.jutils.io.options.IOptionsCreator;
import org.jutils.io.options.OptionsSerializer;
import org.jutils.ui.app.FrameRunner;

import soinc.rollercoaster.data.RollercoasterOptions;

/*******************************************************************************
 * 
 ******************************************************************************/
public class RollercoasterMain
{
    /**  */
    private static final File USERS_FILE = IOUtils.getUsersFile(
        ".ScienceOlympiad", "rollercoaster.xml" );
    /**  */
    public static final File RELAY_FILE = IOUtils.getInstallFile( "relay" );

    /**  */
    private static OptionsSerializer<RollercoasterOptions> userio;

    /***************************************************************************
     * @param args ignored
     **************************************************************************/
    public static void main( String [] args )
    {
        FrameRunner.invokeLater( new RollercoasterApp() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static OptionsSerializer<RollercoasterOptions> getOptions()
    {
        if( userio == null )
        {
            userio = OptionsSerializer.getOptions( new RudCreator(),
                USERS_FILE );
        }

        return userio;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class RudCreator
        implements IOptionsCreator<RollercoasterOptions>
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public RollercoasterOptions createDefaultOptions()
        {
            return new RollercoasterOptions();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public RollercoasterOptions initialize( RollercoasterOptions data )
        {
            return new RollercoasterOptions( data );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void warn( String message )
        {
            LogUtils.printWarning( message );
        }
    }
}
