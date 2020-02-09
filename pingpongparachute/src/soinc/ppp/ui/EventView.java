package soinc.ppp.ui;

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
import javax.swing.JSeparator;

import org.jutils.io.options.OptionsSerializer;
import org.jutils.ui.model.IDataView;

import soinc.lib.UiUtils;
import soinc.ppp.PppIcons;
import soinc.ppp.PppMain;
import soinc.ppp.data.PppOptions;
import soinc.ppp.tasks.PppEvent;

/*******************************************************************************
 * 
 ******************************************************************************/
public class EventView implements IDataView<PppEvent>
{
    /**  */
    private final JFrame frame;
    /**  */
    public final TrackView trackAView;
    /**  */
    public final TrackView trackBView;

    /**  */
    private final JComponent content;

    /**  */
    private PppEvent event;

    /***************************************************************************
     * @param competition
     * @param icons
     * @param dimension
     **************************************************************************/
    public EventView( PppEvent event, List<Image> icons, Dimension size )
    {
        this.frame = new JFrame( "Ping Pong Parachute Event" );

        this.event = event;

        this.trackAView = new TrackView( event.trackA );
        this.trackBView = new TrackView( event.trackB );

        this.content = createCompetitionPanel();

        frame.setIconImages( icons );
        frame.setContentPane( content );
        frame.setDefaultCloseOperation( JDialog.DO_NOTHING_ON_CLOSE );
        frame.addWindowListener( new EventFrameListener( this ) );
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

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createTracksPanel()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        panel.setOpaque( false );

        // ---------------------------------------------------------------------

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 10, 0, 0, 0 ), 0, 0 );
        panel.add( trackAView.getView(), constraints );

        // ---------------------------------------------------------------------

        constraints = new GridBagConstraints( 1, 0, 1, 1, 0.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 10, 10, 10, 10 ), 0, 0 );
        panel.add( new JSeparator( JSeparator.VERTICAL ), constraints );

        // ---------------------------------------------------------------------

        constraints = new GridBagConstraints( 2, 0, 1, 1, 1.0, 1.0,
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
        Icon bannerIcon = PppIcons.getBannerImage();
        JLabel soLabel = new JLabel( bannerIcon );

        return soLabel;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public PppEvent getData()
    {
        return event;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( PppEvent data )
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
            trackAView.disconnect();
            trackBView.disconnect();

            // setFullScreen( false );

            frame.setVisible( false );
            frame.dispose();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class EventFrameListener extends WindowAdapter
    {
        /**  */
        private final EventView view;

        /**
         * @param view
         */
        public EventFrameListener( EventView view )
        {
            this.view = view;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void windowClosing( WindowEvent e )
        {
            if( !view.event.isRunning() )
            {
                OptionsSerializer<PppOptions> userio = PppMain.getOptions();
                PppOptions options = new PppOptions( userio.getOptions() );
                options.config.set( view.event.trackA.config );
                userio.write( options );
                UiUtils.setFullScreen( false, view.frame );
                view.frame.setVisible( false );
            }
        }
    }
}
