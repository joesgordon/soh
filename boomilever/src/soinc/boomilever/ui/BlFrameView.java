package soinc.boomilever.ui;

import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;

import org.jutils.IconConstants;
import org.jutils.OptionUtils;
import org.jutils.io.options.OptionsSerializer;
import org.jutils.ui.OkDialogView;
import org.jutils.ui.OkDialogView.OkDialogButtons;
import org.jutils.ui.StandardFrameView;
import org.jutils.ui.event.ActionAdapter;
import org.jutils.ui.model.IView;

import soinc.boomilever.BlIcons;
import soinc.boomilever.BlMain;
import soinc.boomilever.data.BlEventConfig;
import soinc.boomilever.data.BlOptions;
import soinc.boomilever.tasks.BlEvent;
import soinc.boomilever.tasks.CompetitionSignals;
import soinc.boomilever.tasks.Track;
import soinc.lib.UiUtils;
import soinc.lib.gpio.SciolyGpio;
import soinc.lib.ui.RelayTestView;

/*******************************************************************************
 *
 ******************************************************************************/
public class BlFrameView implements IView<JFrame>
{
    /**  */
    private final StandardFrameView view;
    /**  */
    private final BlEventConfigView configView;

    /**  */
    private final JCheckBoxMenuItem mockIoMenuItem;

    /**  */
    private BlEventView eventView;

    /***************************************************************************
     * 
     **************************************************************************/
    public BlFrameView()
    {
        this.view = new StandardFrameView();
        this.configView = new BlEventConfigView();
        this.mockIoMenuItem = createMockGpioMenuItem();
        this.eventView = null;

        createMenubar( view.getMenuBar(), view.getFileMenu() );

        view.setContent( configView.getView() );
        view.getView().setIconImages( BlIcons.getRollercoasterIcons() );
        view.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        view.setSize( 1280, 800 );
        view.setTitle( "Boomilever" );
        view.getView().setLocation( 0, 0 );

        OptionsSerializer<BlOptions> options = BlMain.getOptions();

        configView.setData( options.getOptions().config );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JCheckBoxMenuItem createMockGpioMenuItem()
    {
        boolean mockIo = BlMain.getOptions().getOptions().useFauxGpio;
        SciolyGpio.FAUX_CONNECT = mockIo;
        JCheckBoxMenuItem item = new JCheckBoxMenuItem( "Mock I/O" );
        item.setSelected( SciolyGpio.FAUX_CONNECT );
        ActionListener listener = ( e ) -> {
            SciolyGpio.FAUX_CONNECT = !SciolyGpio.FAUX_CONNECT;
            mockIoMenuItem.setSelected( SciolyGpio.FAUX_CONNECT );
            OptionsSerializer<BlOptions> options = BlMain.getOptions();

            options.getOptions().useFauxGpio = SciolyGpio.FAUX_CONNECT;
            options.write();
            // LogUtils.printDebug( "Faux connect: %s", SohGpio.FAUX_CONNECT );
        };
        // Icon icon = IconConstants.getIcon( IconConstants.EXPORT_16 );
        Icon icon = null;
        Action action = new ActionAdapter( listener, "Mock GPIO", icon );

        item.setAction( action );
        item.setSelected( SciolyGpio.FAUX_CONNECT );

        return item;
    }

    /***************************************************************************
     * @param menubar
     * @param fileMenu
     **************************************************************************/
    private void createMenubar( JMenuBar menubar, JMenu fileMenu )
    {
        createFileMenu( fileMenu );
        menubar.add( createGoMenu() );
    }

    /***************************************************************************
     * @param fileMenu
     **************************************************************************/
    private void createFileMenu( JMenu fileMenu )
    {
        int row = 0;

        fileMenu.add( new JMenuItem( createTestGpioAction() ), row++ );
        fileMenu.add( new JMenuItem( createTestRelayAction() ), row++ );
        fileMenu.add( new JSeparator(), row++ );
        fileMenu.add( mockIoMenuItem, row++ );
        fileMenu.add( new JSeparator(), row++ );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JMenu createGoMenu()
    {
        JMenu menu = new JMenu( "Go" );
        Action action;
        JMenuItem item;

        menu.setMnemonic( 'G' );

        action = new ActionAdapter( ( e ) -> showEvent( true ),
            "Start Competition", BlIcons.getRollercoaster16() );
        KeyStroke key = KeyStroke.getKeyStroke( "F8" );
        action.putValue( Action.ACCELERATOR_KEY, key );
        item = menu.add( action );

        item.setMnemonic( 'S' );

        UiUtils.addHotKey( ( JComponent )getView().getContentPane(), "F8",
            ( e ) -> showEvent( eventView == null ) );

        return menu;
    }

    /***************************************************************************
     * @param show
     **************************************************************************/
    private void showEvent( boolean show )
    {
        mockIoMenuItem.setEnabled( false );

        // LogUtils.printDebug( "showCompetition(%s)", show );

        OptionsSerializer<BlOptions> userio = BlMain.getOptions();
        BlOptions options = new BlOptions( userio.getOptions() );
        options.config.set( configView.getData() );
        userio.write( options );

        if( show && eventView == null )
        {
            BlEventConfig cfg = configView.getData();
            CompetitionSignals signalsA = new CompetitionSignals(
                BlMain.getRelays(), cfg.trackA );
            CompetitionSignals signalsB = new CompetitionSignals(
                BlMain.getRelays(), cfg.trackB );
            BlEvent evt = new BlEvent( cfg, signalsA, signalsB );

            this.eventView = new BlEventView( evt, getView().getIconImages(),
                getView().getSize() );

            showCompetition( evt.trackA, eventView.trackAView );
            showCompetition( evt.trackB, eventView.trackBView );
        }
        else if( !show && eventView != null )
        {
            eventView.setVisible( false );
            eventView = null;
        }
    }

    /**
     * @param competition
     * @param trackAView
     */
    private void showCompetition( Track competition, TrackView trackView )
    {
        try
        {
            try
            {
                competition.connect( trackView );
            }
            catch( IOException ex )
            {
                OptionUtils.showErrorMessage( getView(),
                    "Unable to connect to Pi " + ex.getMessage(), "I/O Error" );
                return;
            }

            JFrame frame = eventView.getView();

            JComponent comp = ( JComponent )frame.getContentPane();
            ActionListener hideListener = ( e ) -> showEvent(
                this.eventView == null );

            UiUtils.addHotKey( comp, "F8", hideListener );
            UiUtils.addHotKey( comp, "ESCAPE", hideListener );

            // setFullScreen( true );
            configView.deselectTeams();

            eventView.setVisible( true );
        }
        catch( IllegalStateException ex )
        {
            OptionUtils.showErrorMessage( getView(), "Setup Error",
                "Pi4j library was not found" );
            return;
        }
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Action createTestGpioAction()
    {
        return new ActionAdapter(
            ( e ) -> UiUtils.showTestGpioScreen( getView() ), "Test GPIO",
            IconConstants.getIcon( IconConstants.CHECK_16 ) );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Action createTestRelayAction()
    {
        return new ActionAdapter( ( e ) -> showTestRelayScreen( getView() ),
            "Test Relay", IconConstants.getIcon( IconConstants.CHECK_16 ) );
    }

    /***************************************************************************
     * @param parent
     **************************************************************************/
    private static void showTestRelayScreen( JFrame parent )
    {
        RelayTestView view = new RelayTestView( BlMain.getRelays() );
        OkDialogView okView = new OkDialogView( parent, view.getView(),
            ModalityType.DOCUMENT_MODAL, OkDialogButtons.OK_ONLY );

        try
        {
            SciolyGpio.startup();
        }
        catch( IllegalStateException ex )
        {
            OptionUtils.showErrorMessage( parent, "Setup Error",
                "Pi4j library was not found" );
            return;
        }

        okView.show( "Relay Test", new Dimension( 400, 400 ) );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JFrame getView()
    {
        return view.getView();
    }
}
