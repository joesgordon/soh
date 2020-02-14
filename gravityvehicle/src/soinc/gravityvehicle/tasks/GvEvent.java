package soinc.gravityvehicle.tasks;

import java.io.IOException;

import org.jutils.io.LogUtils;

import soinc.gravityvehicle.data.GvEventConfig;
import soinc.gravityvehicle.ui.GvEventView;
import soinc.lib.relay.IRelays;

/*******************************************************************************
 * 
 ******************************************************************************/
public class GvEvent
{
    /**  */
    private GvEventConfig config;
    /**  */
    private IRelays relays;

    /**  */
    public final Track trackA;

    /***************************************************************************
     * @param config
     * @param relays
     **************************************************************************/
    public GvEvent( GvEventConfig config, IRelays relays )
    {
        this.config = config;
        this.relays = relays;

        this.trackA = new Track( config, config.trackA, relays );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public boolean isRunning()
    {
        return trackA.getState().isRunning;
    }

    /***************************************************************************
     * @param view
     * @throws IOException
     **************************************************************************/
    public void connect( GvEventView view ) throws IOException
    {
        trackA.connect( view.trackAView );

        LogUtils.printDebug( "\t\t\tTrack::connect()" );
        setStripsEnabled( true );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void disconnect()
    {
        setStripsEnabled( false );
    }

    /***************************************************************************
     * @param enabled
     **************************************************************************/
    public void setStripsEnabled( boolean enabled )
    {
        relays.setRelays( enabled ? ( 1 << config.trackA.powerRelay - 1 ) : 0 );
    }
}
