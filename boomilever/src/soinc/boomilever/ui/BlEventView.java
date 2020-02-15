package soinc.boomilever.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
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
import javax.swing.border.LineBorder;

import org.jutils.io.options.OptionsSerializer;
import org.jutils.ui.fields.DoubleFormField;
import org.jutils.ui.model.IDataView;

import soinc.boomilever.BlIcons;
import soinc.boomilever.BlMain;
import soinc.boomilever.data.BlOptions;
import soinc.boomilever.tasks.BlEvent;
import soinc.lib.UiUtils;

/*******************************************************************************
 * 
 ******************************************************************************/
public class BlEventView implements IDataView<BlEvent>
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
    private BlEvent event;

    /***************************************************************************
     * @param event
     * @param icons
     * @param size
     **************************************************************************/
    public BlEventView( BlEvent event, List<Image> icons, Dimension size )
    {
        this.frame = new JFrame( "Boomilever" );

        this.event = event;

        this.trackAView = new TrackView( event.trackA );
        this.trackBView = new TrackView( event.trackB );

        this.content = createCompetitionPanel();

        frame.setIconImages( icons );
        frame.setContentPane( content );
        frame.setDefaultCloseOperation( JDialog.DISPOSE_ON_CLOSE );
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
        panel.add( createConversionPanel(), BorderLayout.SOUTH );

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

    private JComponent createConversionPanel()
    {
        DoubleFormField kgField = new DoubleFormField( "Kilograms" );
        DoubleFormField lbField = new DoubleFormField( "Pounds" );
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        JComponent lbComp = lbField.getTextField();

        lbField.getValidationField().setValidBackground( Color.black );
        lbField.setUpdater( ( d ) -> kgField.setValue( d * 2.2 ) );
        lbField.setValue( 10.0 );
        lbComp.setBorder( new LineBorder( Color.white, 2 ) );
        lbComp.setForeground( Color.white );
        lbComp.setFont(
            lbComp.getFont().deriveFont( 14.0f ).deriveFont( Font.BOLD ) );

        JComponent kgComp = kgField.getTextField();

        kgField.setEditable( false );
        kgComp.setBorder( new LineBorder( Color.white, 2 ) );
        kgComp.setFont(
            lbComp.getFont().deriveFont( 14.0f ).deriveFont( Font.BOLD ) );
        kgField.setValue( 2.2 * lbField.getValue() );

        panel.setOpaque( false );

        JLabel lbLabel = new JLabel( "Pounds :" );
        lbLabel.setFont(
            lbComp.getFont().deriveFont( 14.0f ).deriveFont( Font.BOLD ) );
        lbLabel.setForeground( Color.white );

        JLabel kgLabel = new JLabel( "= Kilograms :" );
        kgLabel.setFont(
            lbComp.getFont().deriveFont( 14.0f ).deriveFont( Font.BOLD ) );
        kgLabel.setForeground( Color.white );

        int x = 0;

        constraints = new GridBagConstraints( x++, 0, 1, 1, 0, 0,
            GridBagConstraints.EAST, GridBagConstraints.NONE,
            new Insets( 8, 8, 8, 8 ), 0, 0 );
        panel.add( lbLabel, constraints );

        constraints = new GridBagConstraints( x++, 0, 1, 1, 0, 0,
            GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets( 8, 8, 8, 8 ), 0, 0 );
        panel.add( lbComp, constraints );

        constraints = new GridBagConstraints( x++, 0, 1, 1, 0, 0,
            GridBagConstraints.EAST, GridBagConstraints.NONE,
            new Insets( 8, 8, 8, 8 ), 0, 0 );
        panel.add( kgLabel, constraints );

        constraints = new GridBagConstraints( x++, 0, 1, 1, 0, 0,
            GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets( 8, 8, 8, 0 ), 0, 0 );
        panel.add( kgComp, constraints );

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
    public BlEvent getData()
    {
        return event;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( BlEvent data )
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
        private final BlEventView view;

        /**
         * @param view
         */
        public EventFrameListener( BlEventView view )
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
                OptionsSerializer<BlOptions> userio = BlMain.getOptions();
                BlOptions options = new BlOptions( userio.getOptions() );
                options.config.set( view.event.trackA.config );
                userio.write( options );
                UiUtils.setFullScreen( false, view.frame );
                view.frame.setVisible( false );
            }
        }
    }
}
