package soinc.rollercoaster;

import java.io.File;

import org.jutils.io.IOUtils;
import org.jutils.io.LogUtils;
import org.jutils.io.options.IOptionsCreator;
import org.jutils.io.options.OptionsSerializer;
import org.jutils.ui.app.FrameRunner;

import soinc.lib.gpio.SciolyGpio;
import soinc.rollercoaster.data.RcOptions;
import soinc.rollercoaster.relay.IRelay;
import soinc.rollercoaster.relay.MockRelay;
import soinc.rollercoaster.relay.Relay;

/*******************************************************************************
 * 
 ******************************************************************************/
public class RcMain
{
    /**  */
    private static final File USERS_FILE = IOUtils.getUsersFile(
        ".ScienceOlympiad", "rollercoaster.xml" );

    /**  */
    private static OptionsSerializer<RcOptions> userio;

    /***************************************************************************
     * @param args ignored
     **************************************************************************/
    public static void main( String [] args )
    {
        FrameRunner.invokeLater( new RcApp() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static IRelay getRelay()
    {
        return getRelay( SciolyGpio.FAUX_CONNECT );
    }

    /***************************************************************************
     * @param mock
     * @return
     **************************************************************************/
    public static IRelay getRelay( boolean mock )
    {
        if( mock )
        {
            return new MockRelay();
        }

        return new Relay();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static OptionsSerializer<RcOptions> getOptions()
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
        implements IOptionsCreator<RcOptions>
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public RcOptions createDefaultOptions()
        {
            return new RcOptions();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public RcOptions initialize( RcOptions data )
        {
            return new RcOptions( data );
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
