package soinc.ppp.tasks;

import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JComponent;

import org.jutils.SwingUtils;
import org.jutils.io.LogUtils;

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
    private final TrackConfig config;

    /***************************************************************************
     * @param config
     * @param relays
     * @throws IOException
     **************************************************************************/
    public TrackSignals( TrackConfig config, IRelays relays )
    {
        this.config = config;
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
        SwingUtils.addKeyListener( jview, config.loadKey.keystroke, callback,
            "Load Team", true );

        // ---------------------------------------------------------------------

        callback = ( e ) -> track.signalPeriodStartPause();
        SwingUtils.addKeyListener( jview, config.startPauseKey.keystroke,
            callback, "Period Start", true );

        // ---------------------------------------------------------------------

        callback = ( e ) -> track.signalRunFinished( false );
        SwingUtils.addKeyListener( jview, config.badRunKey.keystroke, callback,
            "Fail Run", true );

        callback = ( e ) -> track.signalRunFinished( true );
        SwingUtils.addKeyListener( jview, config.goodRunKey.keystroke, callback,
            "Accept Run", true );

        // ---------------------------------------------------------------------

        callback = ( e ) -> track.signalClearTeam();
        SwingUtils.addKeyListener( jview, config.clearTeamKey.keystroke,
            callback, "Clear team", true );

        // ---------------------------------------------------------------------

        relays.setRelay( config.powerRelay - 1, true );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void disconnect()
    {
        relays.turnAllOff();
    }

    /***************************************************************************
     * @param red
     * @param green
     * @param blue
     **************************************************************************/
    public void setLights( boolean red, boolean green, boolean blue )
    {
        int redBit = config.redRelay - 1;
        int greenBit = config.greenRelay - 1;
        int blueBit = config.blueRelay - 1;

        int redMask = 1 << redBit;
        int greenMask = 1 << greenBit;
        int blueMask = 1 << blueBit;

        int mask = relays.getRelays();

        mask = red ? ( mask | redMask ) : ( mask & ~redMask );
        mask = green ? ( mask | greenMask ) : ( mask & ~greenMask );
        mask = blue ? ( mask | blueMask ) : ( mask & ~blueMask );

        LogUtils.printDebug(
            "\t\t\tTrackSignals::setLights(): %s, %s, %s : mask = %02X", red,
            green, blue, mask );

        relays.setRelays( mask );
    }
}
