package soinc.gravityvehicle;

import java.io.File;

import org.jutils.io.IOUtils;
import org.jutils.io.LogUtils;
import org.jutils.io.options.IOptionsCreator;
import org.jutils.io.options.OptionsSerializer;
import org.jutils.ui.app.FrameRunner;

import soinc.gravityvehicle.data.GvOptions;
import soinc.lib.gpio.SciolyGpio;
import soinc.lib.relay.IRelays;
import soinc.lib.relay.MockRelay;
import soinc.lib.relay.Relays;

/*******************************************************************************
 * 
 ******************************************************************************/
public class GvMain
{
    /**  */
    private static final File USERS_FILE = IOUtils.getUsersFile(
        ".ScienceOlympiad", "boomilever.xml" );

    /**  */
    private static OptionsSerializer<GvOptions> userio;

    /***************************************************************************
     * @param args ignored
     **************************************************************************/
    public static void main( String [] args )
    {
        FrameRunner.invokeLater( new GvApp() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static IRelays getRelays()
    {
        return getRelay( SciolyGpio.FAUX_CONNECT );
    }

    /***************************************************************************
     * @param mock
     * @return
     **************************************************************************/
    public static IRelays getRelay( boolean mock )
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
    public static OptionsSerializer<GvOptions> getOptions()
    {
        if( userio == null )
        {
            userio = OptionsSerializer.getOptions( GvOptions.class, USERS_FILE,
                new RudCreator() );
        }

        return userio;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class RudCreator implements IOptionsCreator<GvOptions>
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public GvOptions createDefaultOptions()
        {
            return new GvOptions();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public GvOptions initialize( GvOptions data )
        {
            return new GvOptions( data );
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
