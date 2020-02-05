package soinc.boomilever.tasks;

import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import org.jutils.SwingUtils;

import soinc.boomilever.data.CompetitionConfig;
import soinc.boomilever.data.CompetitionData;
import soinc.boomilever.ui.CompetitionView;
import soinc.lib.relay.IRelays;

/*******************************************************************************
 * 
 ******************************************************************************/
public class CompetitionSignals
{
    /**  */
    private final IRelays relays;
    /**  */
    private final CompetitionConfig config;

    /**  */
    private CompetitionView view;

    /***************************************************************************
     * @param relays
     * @param config
     * @throws IOException
     **************************************************************************/
    public CompetitionSignals( IRelays relays, CompetitionConfig config )
    {
        this.relays = relays;
        this.config = config;
    }

    /***************************************************************************
     * @param competition
     * @param view
     * @throws IOException
     **************************************************************************/
    public void connect( TeamCompetition competition, CompetitionView view )
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
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void disconnect()
    {
    }

    /***************************************************************************
     * @param red
     * @param green
     * @param blue
     **************************************************************************/
    public void setLights( boolean red, boolean green, boolean blue )
    {
        relays.setRelay( config.redRelay, red );
        relays.setRelay( config.greenRelay, green );
        relays.setRelay( config.blueRelay, blue );
    }

    /***************************************************************************
     * @param data
     **************************************************************************/
    public void updateUI( CompetitionData data )
    {
        SwingUtilities.invokeLater( () -> view.setData( data ) );
    }
}
