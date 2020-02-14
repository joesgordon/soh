package soinc.ppp.tasks;

import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JComponent;

import org.jutils.SwingUtils;

import soinc.lib.relay.IRelays;
import soinc.ppp.data.TrackConfig;
import soinc.ppp.ui.TrackView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TrackSignals
{
    /**  */
    private final IRelays relays;
    /**  */
    private final TrackConfig trackCfg;

    /***************************************************************************
     * @param trackCfg
     * @param gpio
     * @param relays
     * @throws IOException
     **************************************************************************/
    public TrackSignals( TrackConfig trackCfg, IRelays relays )
    {
        this.trackCfg = trackCfg;
        this.relays = relays;
    }

    /***************************************************************************
     * @param track
     * @param view
     * @throws IOException
     **************************************************************************/
    public void connect( Track track, TrackView view ) throws IOException
    {
        relays.initialize();

        JComponent jview = view.getView();

        ActionListener callback;

        // ---------------------------------------------------------------------

        callback = ( e ) -> view.showTeamChooser();
        SwingUtils.addKeyListener( jview, trackCfg.loadKey.keystroke, callback,
            "Load Team", true );

        // ---------------------------------------------------------------------

        callback = ( e ) -> track.signalPeriodStartPause();
        SwingUtils.addKeyListener( jview, trackCfg.startPauseKey.keystroke,
            callback, "Period Start", true );

        // ---------------------------------------------------------------------

        callback = ( e ) -> track.signalRunFinished( false );
        SwingUtils.addKeyListener( jview, trackCfg.badRunKey.keystroke,
            callback, "Fail Run", true );

        callback = ( e ) -> track.signalRunFinished( true );
        SwingUtils.addKeyListener( jview, trackCfg.goodRunKey.keystroke,
            callback, "Accept Run", true );

        // ---------------------------------------------------------------------

        callback = ( e ) -> track.signalClearTeam();
        SwingUtils.addKeyListener( jview, trackCfg.clearTeamKey.keystroke,
            callback, "Clear team", true );
    }

    /***************************************************************************
     * @param red
     * @param green
     * @param blue
     **************************************************************************/
    public void setLights( boolean red, boolean green, boolean blue )
    {
        relays.setRelay( 0, red );
        relays.setRelay( 1, green );
        relays.setRelay( 2, blue );
    }
}
