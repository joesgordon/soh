package soinc.ppp;

import java.io.File;

import org.jutils.io.IOUtils;
import org.jutils.io.LogUtils;
import org.jutils.io.options.IOptionsCreator;
import org.jutils.io.options.OptionsSerializer;
import org.jutils.ui.app.FrameRunner;

import soinc.lib.gpio.SciolyGpio;
import soinc.lib.relay.IRelays;
import soinc.lib.relay.MockRelay;
import soinc.lib.relay.Relays;
import soinc.ppp.data.PppOptions;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PppMain
{
    /**  */
    private static final File USERS_FILE = IOUtils.getUsersFile(
        ".ScienceOlympiad", "rollercoaster.xml" );

    /**  */
    private static OptionsSerializer<PppOptions> userio;

    /***************************************************************************
     * @param args ignored
     **************************************************************************/
    public static void main( String [] args )
    {
        FrameRunner.invokeLater( new PppApp() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static IRelays getRelays()
    {
        return getRelays( SciolyGpio.FAUX_CONNECT );
    }

    /***************************************************************************
     * @param mock
     * @return
     **************************************************************************/
    public static IRelays getRelays( boolean mock )
    {
        if( mock )
        {
            LogUtils.printDebug( "Getting mocked relays" );
            return new MockRelay();
        }

        LogUtils.printDebug( "Getting real relays" );
        return new Relays();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static OptionsSerializer<PppOptions> getOptions()
    {
        if( userio == null )
        {
            userio = OptionsSerializer.getOptions( PppOptions.class, USERS_FILE,
                new RudCreator() );
        }

        return userio;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class RudCreator implements IOptionsCreator<PppOptions>
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public PppOptions createDefaultOptions()
        {
            return new PppOptions();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public PppOptions initialize( PppOptions data )
        {
            return new PppOptions( data );
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
