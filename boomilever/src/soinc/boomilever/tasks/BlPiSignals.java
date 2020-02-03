package soinc.boomilever.tasks;

import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import org.jutils.SwingUtils;

import soinc.boomilever.data.BlCompetitionData;
import soinc.boomilever.data.CompetitionConfig;
import soinc.boomilever.ui.BlCompetitionView;
import soinc.lib.relay.IRelays;

/*******************************************************************************
 * 
 ******************************************************************************/
public class BlPiSignals
{
    /**  */
    private final IRelays relays;
    /**  */
    private final CompetitionConfig config;

    /**  */
    private BlCompetitionView view;

    /***************************************************************************
     * @param gpio
     * @param config
     * @throws IOException
     **************************************************************************/
    public BlPiSignals( IRelays relays, CompetitionConfig config )
    {
        this.relays = relays;
        this.config = config;
    }

    /***************************************************************************
     * @param competition
     * @param view
     * @throws IOException
     **************************************************************************/
    public void connect( BlTeamCompetition competition, BlCompetitionView view )
        throws IOException
    {
        this.view = view;

        relays.initialize();

        JComponent jview = view.getContent();

        ActionListener callback;

        // ---------------------------------------------------------------------

        callback = ( e ) -> competition.signalPeriodStartPause();
        SwingUtils.addKeyListener( jview, config.startPauseKey.keystroke,
            callback, "Period Start", true );

        callback = ( e ) -> competition.signalClearTeam();
        SwingUtils.addKeyListener( jview, config.clearKey.keystroke, callback,
            "Clear Team", true );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    public void disconnect()
    {
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    public void setLights( boolean red, boolean green, boolean blue )
    {
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    public void updateUI( BlCompetitionData data )
    {
        SwingUtilities.invokeLater( () -> view.setData( data ) );
    }
}
