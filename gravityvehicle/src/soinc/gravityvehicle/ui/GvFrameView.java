package soinc.gravityvehicle.ui;

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

import soinc.gravityvehicle.GvIcons;
import soinc.gravityvehicle.GvMain;
import soinc.gravityvehicle.data.GvEventConfig;
import soinc.gravityvehicle.data.GvOptions;
import soinc.gravityvehicle.tasks.GvEvent;
import soinc.lib.UiUtils;
import soinc.lib.gpio.SciolyGpio;
import soinc.lib.relay.IRelays;
import soinc.lib.ui.RelayTestView;

/*******************************************************************************
 *
 ******************************************************************************/
public class GvFrameView implements IView<JFrame>
{
    /**  */
    private final StandardFrameView view;
    /**  */
    private final GvEventConfigView configView;

    /**  */
    private final JCheckBoxMenuItem mockIoMenuItem;

    /**  */
    private GvEventView eventView;

    /***************************************************************************
     * 
     **************************************************************************/
    public GvFrameView()
    {
        this.view = new StandardFrameView();
        this.configView = new GvEventConfigView();
        this.mockIoMenuItem = createMockGpioMenuItem();
        this.eventView = null;

        createMenubar( view.getMenuBar(), view.getFileMenu() );

        view.setContent( configView.getView() );
        view.getView().setIconImages( GvIcons.getRollercoasterIcons() );
        view.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        view.setSize( 1280, 800 );
        view.setTitle( "Gravity Vehicle" );
        view.getView().setLocation( 0, 0 );

        OptionsSerializer<GvOptions> options = GvMain.getOptions();

        configView.setData( options.getOptions().config );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JCheckBoxMenuItem createMockGpioMenuItem()
    {
        boolean mockIo = GvMain.getOptions().getOptions().useFauxGpio;
        SciolyGpio.FAUX_CONNECT = mockIo;
        JCheckBoxMenuItem item = new JCheckBoxMenuItem( "Mock I/O" );
        item.setSelected( SciolyGpio.FAUX_CONNECT );
        ActionListener listener = ( e ) -> {
            SciolyGpio.FAUX_CONNECT = !SciolyGpio.FAUX_CONNECT;
            mockIoMenuItem.setSelected( SciolyGpio.FAUX_CONNECT );
            OptionsSerializer<GvOptions> options = GvMain.getOptions();

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
            "Start Competition", GvIcons.getRollercoaster16() );
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

        GvEventConfig eventCfg = configView.getData();

        // LogUtils.printDebug( "showCompetition(%s)", show );

        OptionsSerializer<GvOptions> userio = GvMain.getOptions();
        GvOptions options = new GvOptions( userio.getOptions() );
        options.config.set( eventCfg );
        userio.write( options );

        if( show && eventView == null )
        {
            try
            {
                IRelays relays = GvMain.getRelays();
                GvEvent event = new GvEvent( eventCfg, relays );

                this.eventView = new GvEventView( event,
                    getView().getIconImages(), getView().getSize() );

                event.connect( eventView );

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
            catch( IOException ex )
            {
                OptionUtils.showErrorMessage( getView(),
                    "Unable to connect to Pi " + ex.getMessage(), "I/O Error" );
                return;
            }
        }
        else if( !show && eventView != null )
        {
            eventView.setVisible( false );
            eventView = null;
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
        RelayTestView view = new RelayTestView( GvMain.getRelays() );
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
