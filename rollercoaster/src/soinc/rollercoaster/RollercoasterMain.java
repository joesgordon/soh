package soinc.rollercoaster;

import java.io.File;

import org.jutils.io.IOUtils;
import org.jutils.io.LogUtils;
import org.jutils.io.options.IOptionsCreator;
import org.jutils.io.options.OptionsSerializer;
import org.jutils.ui.app.FrameRunner;

import soinc.rollercoaster.data.RollercoasterUserData;

/*******************************************************************************
 * 
 ******************************************************************************/
public class RollercoasterMain
{
    /**  */
    private static final File USERS_FILE = IOUtils.getUsersFile(
        ".ScienceOlympiad", "rollercoaster.xml" );

    /**  */
    private static OptionsSerializer<RollercoasterUserData> userio;

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
    public static OptionsSerializer<RollercoasterUserData> getUserOptions()
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
        implements IOptionsCreator<RollercoasterUserData>
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public RollercoasterUserData createDefaultOptions()
        {
            return new RollercoasterUserData();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public RollercoasterUserData initialize( RollercoasterUserData data )
        {
            return new RollercoasterUserData( data );
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
