package soinc.gravityvehicle.tasks;

import java.io.IOException;

import soinc.gravityvehicle.data.GvEventConfig;
import soinc.gravityvehicle.ui.GvEventView;
import soinc.lib.relay.IRelays;

/*******************************************************************************
 * 
 ******************************************************************************/
public class GvEvent
{
    /**  */
    public final Track trackA;

    /***************************************************************************
     * @param config
     * @param relays
     **************************************************************************/
    public GvEvent( GvEventConfig config, IRelays relays )
    {
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
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void disconnect()
    {
        trackA.disconnect();
    }
}
