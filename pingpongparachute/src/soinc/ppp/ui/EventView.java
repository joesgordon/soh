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
import soinc.lib.ui.SecondsLabel;
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
    private final JComponent content;

    /**  */
    public final TrackView trackAView;
    /**  */
    public final TrackView trackBView;

    /**  */
    private final SecondsLabel timer1Field;
    /**  */
    private final SecondsLabel timer2Field;
    /**  */
    private final SecondsLabel timer3Field;

    /**  */
    private PppEvent event;

    /***************************************************************************
     * @param event
     * @param icons
     * @param size
     **************************************************************************/
    public EventView( PppEvent event, List<Image> icons, Dimension size )
    {
        this.event = event;
        this.frame = new JFrame( "Ping Pong Parachute Event" );

        this.trackAView = new TrackView( event.config, event.trackA );
        this.trackBView = new TrackView( event.config, event.trackB );

        this.timer1Field = new SecondsLabel( " --.-- s ", TrackView.REG_FONT );
        this.timer2Field = new SecondsLabel( " --.-- s ", TrackView.REG_FONT );
        this.timer3Field = new SecondsLabel( " --.-- s ", TrackView.REG_FONT );

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
        panel.add( createTimersPanel(), BorderLayout.SOUTH );

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

        constraints = new GridBagConstraints( 0, 1, 3, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 10, 10, 10 ), 0, 0 );
        panel.add( new JSeparator( JSeparator.HORIZONTAL ), constraints );

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
     * @return
     **************************************************************************/
    private Component createTimersPanel()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        panel.setOpaque( false );

        // ---------------------------------------------------------------------

        JLabel timer1Label = UiUtils.createTextLabel( "Timer 1:",
            TrackView.REG_FONT );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE,
            new Insets( 0, 0, 20, 0 ), 0, 0 );
        panel.add( timer1Label, constraints );

        constraints = new GridBagConstraints( 1, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE,
            new Insets( 0, 20, 20, 0 ), 0, 0 );
        panel.add( timer1Field.view, constraints );

        // ---------------------------------------------------------------------

        JLabel timer2Label = UiUtils.createTextLabel( "Timer 2:",
            TrackView.REG_FONT );

        constraints = new GridBagConstraints( 2, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE,
            new Insets( 10, 40, 20, 0 ), 0, 0 );
        panel.add( timer2Label, constraints );

        constraints = new GridBagConstraints( 3, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE,
            new Insets( 10, 20, 20, 0 ), 0, 0 );
        panel.add( timer2Field.view, constraints );

        // ---------------------------------------------------------------------

        JLabel timer3Label = UiUtils.createTextLabel( "Timer 3:",
            TrackView.REG_FONT );

        constraints = new GridBagConstraints( 4, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE,
            new Insets( 10, 40, 20, 0 ), 0, 0 );
        panel.add( timer3Label, constraints );

        constraints = new GridBagConstraints( 5, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE,
            new Insets( 10, 20, 20, 0 ), 0, 0 );
        panel.add( timer3Field.view, constraints );

        return panel;
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

        if( event.timers.hasStarted( 0 ) )
        {
            timer1Field.setTime( event.timers.getTimeElapsed( 0 ) );
        }
        else
        {
            timer1Field.reset();
        }

        if( event.timers.hasStarted( 1 ) )
        {
            timer2Field.setTime( event.timers.getTimeElapsed( 1 ) );
        }
        else
        {
            timer2Field.reset();
        }

        if( event.timers.hasStarted( 2 ) )
        {
            timer3Field.setTime( event.timers.getTimeElapsed( 2 ) );
        }
        else
        {
            timer3Field.reset();
        }
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
            event.disconnect();

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
