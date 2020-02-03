package soinc.boomilever;

import java.io.File;

import org.jutils.io.IOUtils;
import org.jutils.io.LogUtils;
import org.jutils.io.options.IOptionsCreator;
import org.jutils.io.options.OptionsSerializer;
import org.jutils.ui.app.FrameRunner;

import soinc.boomilever.data.BlOptions;
import soinc.lib.gpio.SciolyGpio;
import soinc.lib.relay.IRelays;
import soinc.lib.relay.MockRelay;
import soinc.lib.relay.Relays;

/*******************************************************************************
 * 
 ******************************************************************************/
public class BlMain
{
    /**  */
    private static final File USERS_FILE = IOUtils.getUsersFile(
        ".ScienceOlympiad", "boomilever.xml" );

    /**  */
    private static OptionsSerializer<BlOptions> userio;

    /***************************************************************************
     * @param args ignored
     **************************************************************************/
    public static void main( String [] args )
    {
        FrameRunner.invokeLater( new BlApp() );
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
    public static OptionsSerializer<BlOptions> getOptions()
    {
        if( userio == null )
        {
            userio = OptionsSerializer.getOptions( BlOptions.class, USERS_FILE,
                new RudCreator() );
        }

        return userio;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class RudCreator implements IOptionsCreator<BlOptions>
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public BlOptions createDefaultOptions()
        {
            return new BlOptions();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public BlOptions initialize( BlOptions data )
        {
            return new BlOptions( data );
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
