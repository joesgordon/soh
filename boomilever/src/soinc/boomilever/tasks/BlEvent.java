package soinc.boomilever.tasks;

import java.io.IOException;

import org.jutils.io.LogUtils;

import soinc.boomilever.data.BlEventConfig;
import soinc.boomilever.ui.BlEventView;
import soinc.lib.relay.IRelays;

/*******************************************************************************
 * 
 ******************************************************************************/
public class BlEvent
{
    /**  */
    private BlEventConfig config;
    /**  */
    private IRelays relays;

    /**  */
    public final Track trackA;
    /**  */
    public final Track trackB;

    /***************************************************************************
     * @param config
     * @param relays
     **************************************************************************/
    public BlEvent( BlEventConfig config, IRelays relays )
    {
        this.config = config;
        this.relays = relays;

        this.trackA = new Track( config, config.trackA, relays );
        this.trackB = new Track( config, config.trackB, relays );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public boolean isRunning()
    {
        return trackA.getState().isRunning || trackB.getState().isRunning;
    }

    /***************************************************************************
     * @param view
     * @throws IOException
     **************************************************************************/
    public void connect( BlEventView view ) throws IOException
    {
        trackA.connect( view.trackAView );
        trackB.connect( view.trackBView );

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
        relays.setRelays(
            enabled
                ? ( 1 << config.trackA.powerRelay - 1 ) |
                    ( 1 << config.trackB.powerRelay - 1 )
                : 0 );
    }
}
