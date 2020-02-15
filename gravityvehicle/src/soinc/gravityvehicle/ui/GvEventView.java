package soinc.gravityvehicle.ui;

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
import javax.swing.border.LineBorder;

import org.jutils.io.options.OptionsSerializer;
import org.jutils.ui.fields.DoubleFormField;
import org.jutils.ui.model.IDataView;

import soinc.gravityvehicle.GvIcons;
import soinc.gravityvehicle.GvMain;
import soinc.gravityvehicle.data.GvOptions;
import soinc.gravityvehicle.tasks.GvEvent;
import soinc.lib.UiUtils;

/*******************************************************************************
 * 
 ******************************************************************************/
public class GvEventView implements IDataView<GvEvent>
{
    /**  */
    private final JFrame frame;
    /**  */
    public final TrackView trackAView;

    /**  */
    private final JComponent content;

    /**  */
    private GvEvent event;

    /***************************************************************************
     * @param event
     * @param icons
     * @param size
     **************************************************************************/
    public GvEventView( GvEvent event, List<Image> icons, Dimension size )
    {
        this.frame = new JFrame( "Gravity Vehicle" );

        this.event = event;

        this.trackAView = new TrackView( event.trackA );

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
        Icon bannerIcon = GvIcons.getBannerImage();
        JLabel soLabel = new JLabel( bannerIcon );

        return soLabel;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public GvEvent getData()
    {
        return event;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( GvEvent data )
    {
        this.event = data;

        trackAView.setData( event.trackA );
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
        private final GvEventView view;

        /**
         * @param view
         */
        public EventFrameListener( GvEventView view )
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
                OptionsSerializer<GvOptions> userio = GvMain.getOptions();
                GvOptions options = new GvOptions( userio.getOptions() );
                options.config.set( view.event.trackA.config );
                userio.write( options );
                UiUtils.setFullScreen( false, view.frame );
                view.frame.setVisible( false );
            }
        }
    }
}
