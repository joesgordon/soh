package soinc.boomilever.tasks;

import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import org.jutils.SwingUtils;
import org.jutils.io.LogUtils;

import soinc.boomilever.data.TrackConfig;
import soinc.boomilever.ui.TrackView;
import soinc.lib.relay.IRelays;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TrackSignals
{
    /**  */
    private final IRelays relays;
    /**  */
    private final TrackConfig config;

    /**  */
    private TrackView view;

    /***************************************************************************
     * @param relays
     * @param config
     * @throws IOException
     **************************************************************************/
    public TrackSignals( IRelays relays, TrackConfig config )
    {
        this.relays = relays;
        this.config = config;
    }

    /***************************************************************************
     * @param competition
     * @param view
     * @throws IOException
     **************************************************************************/
    public void connect( Track competition, TrackView view ) throws IOException
    {
        this.view = view;

        relays.initialize();

        JComponent jview = view.getView();

        ActionListener callback;

        // ---------------------------------------------------------------------

        callback = ( e ) -> view.showPlayersChoices();
        SwingUtils.addKeyListener( jview, config.loadKey.keystroke, callback,
            "Load Team", true );

        callback = ( e ) -> competition.signalPeriodStartPause();
        SwingUtils.addKeyListener( jview, config.startPauseKey.keystroke,
            callback, "Period Start", true );

        callback = ( e ) -> competition.signalClearTeam();
        SwingUtils.addKeyListener( jview, config.clearKey.keystroke, callback,
            "Clear Team", true );

        if( config.enablePower )
        {
            relays.setRelay( config.powerRelay - 1, true );
        }
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

    /***************************************************************************
     * @param data
     **************************************************************************/
    public void updateUI( Track data )
    {
        SwingUtilities.invokeLater( () -> view.setData( data ) );
    }
}
