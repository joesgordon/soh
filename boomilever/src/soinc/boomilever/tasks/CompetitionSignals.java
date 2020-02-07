package soinc.boomilever.tasks;

import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import org.jutils.SwingUtils;

import soinc.boomilever.data.TrackConfig;
import soinc.boomilever.ui.TrackView;
import soinc.lib.relay.IRelays;

/*******************************************************************************
 * 
 ******************************************************************************/
public class CompetitionSignals
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
    public CompetitionSignals( IRelays relays, TrackConfig config )
    {
        this.relays = relays;
        this.config = config;
    }

    /***************************************************************************
     * @param competition
     * @param view
     * @throws IOException
     **************************************************************************/
    public void connect( Track competition, TrackView view )
        throws IOException
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
        relays.setRelay( config.redRelay - 1, red );
        relays.setRelay( config.greenRelay - 1, green );
        relays.setRelay( config.blueRelay - 1, blue );
    }

    /***************************************************************************
     * @param data
     **************************************************************************/
    public void updateUI( Track data )
    {
        SwingUtilities.invokeLater( () -> view.setData( data ) );
    }
}
