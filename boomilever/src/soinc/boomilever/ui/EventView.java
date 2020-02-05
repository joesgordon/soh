package soinc.boomilever.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jutils.ui.model.IDataView;

import soinc.boomilever.BlIcons;
import soinc.boomilever.BlMain;
import soinc.boomilever.data.Event;
import soinc.boomilever.tasks.TeamCompetition;
import soinc.lib.UiUtils;

/*******************************************************************************
 * 
 ******************************************************************************/
public class EventView implements IDataView<Event>
{
    /**  */
    private final TeamCompetition competition;

    /**  */
    private final JFrame frame;
    /**  */
    public final CompetitionView trackAView;
    /**  */
    public final CompetitionView trackBView;

    /**  */
    private final JComponent content;

    private Event event;

    /***************************************************************************
     * @param competition
     * @param icons
     * @param size
     **************************************************************************/
    public EventView( TeamCompetition competition, List<Image> icons,
        Dimension size )
    {
        this.competition = competition;

        this.frame = new JFrame( "Roller Coaster Competition" );

        this.trackAView = new CompetitionView();
        this.trackBView = new CompetitionView();

        this.content = createCompetitionPanel();

        frame.setIconImages( icons );
        frame.setContentPane( content );
        frame.setDefaultCloseOperation( JDialog.DO_NOTHING_ON_CLOSE );
        frame.addWindowListener( new CompetitionFrameListener( this ) );
        frame.setUndecorated( true );
        frame.setSize( size );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createCompetitionPanel()
    {
        JPanel panel = new JPanel( new BorderLayout() );

        panel.setBackground( Color.black );
        // panel.setOpaque( false );

        panel.add( createBannerPanel(), BorderLayout.NORTH );
        panel.add( createTracksPanel(), BorderLayout.CENTER );

        // ---------------------------------------------------------------------

        return panel;
    }

    private Component createTracksPanel()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        // ---------------------------------------------------------------------

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 10, 0, 0, 0 ), 0, 0 );
        panel.add( trackAView.getView(), constraints );

        // ---------------------------------------------------------------------

        constraints = new GridBagConstraints( 1, 0, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 10, 0, 0, 0 ), 0, 0 );
        panel.add( trackBView.getView(), constraints );

        // ---------------------------------------------------------------------

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private static Component createBannerPanel()
    {
        Icon bannerIcon = BlIcons.getBannerImage();
        JLabel soLabel = new JLabel( bannerIcon );

        return soLabel;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Event getData()
    {
        return event;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( Event data )
    {
        this.event = data;

        trackAView.setData( event.trackA );
        trackBView.setData( event.trackB );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JFrame getView()
    {
        return frame;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public JComponent getContent()
    {
        return content;
    }

    /***************************************************************************
     * @param visible
     **************************************************************************/
    public void setVisible( boolean visible )
    {
        if( visible )
        {
            trackAView.reset();
            trackBView.reset();

            frame.validate();
            frame.setVisible( true );
        }
        else
        {
            competition.disconnect();

            // setFullScreen( false );

            frame.setVisible( false );
            frame.dispose();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class CompetitionFrameListener extends WindowAdapter
    {
        /**  */
        private final EventView view;

        /**
         * @param view
         */
        public CompetitionFrameListener( EventView view )
        {
            this.view = view;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void windowClosing( WindowEvent e )
        {
            if( !view.competition.isRunning() )
            {
                BlMain.getOptions().write();
                UiUtils.setFullScreen( false, view.frame );
                view.frame.setVisible( false );
            }
        }
    }
}
