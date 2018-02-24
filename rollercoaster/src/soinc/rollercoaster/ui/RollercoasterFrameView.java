package soinc.rollercoaster.ui;

import java.awt.event.ActionListener;

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
import org.jutils.SwingUtils;
import org.jutils.io.LogUtils;
import org.jutils.io.options.OptionsSerializer;
import org.jutils.ui.StandardFrameView;
import org.jutils.ui.event.ActionAdapter;
import org.jutils.ui.model.IView;

import com.pi4j.io.gpio.GpioController;

import soinc.lib.UiUtils;
import soinc.lib.gpio.SciolyGpio;
import soinc.rollercoaster.RollercoasterIcons;
import soinc.rollercoaster.RollercoasterMain;
import soinc.rollercoaster.data.RcCompetition;
import soinc.rollercoaster.data.RollercoasterOptions;

/*******************************************************************************
 *
 ******************************************************************************/
public class RollercoasterFrameView implements IView<JFrame>
{
    /**  */
    private final StandardFrameView view;
    /**  */
    private final RollercoasterConfigView configView;

    /**  */
    private final JCheckBoxMenuItem mockIoMenuItem;

    /**  */
    private RcCompetitionView competitionView;

    /***************************************************************************
     * 
     **************************************************************************/
    public RollercoasterFrameView()
    {
        this.view = new StandardFrameView();
        this.configView = new RollercoasterConfigView();
        this.mockIoMenuItem = createMockGpioMenuItem();
        this.competitionView = null;

        createMenubar( view.getMenuBar(), view.getFileMenu() );

        view.setContent( configView.getView() );
        view.getView().setIconImages(
            RollercoasterIcons.getRollercoasterIcons() );
        view.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        view.setSize( 1280, 800 );
        view.setTitle( "Roller Coaster" );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JCheckBoxMenuItem createMockGpioMenuItem()
    {
        boolean mockIo = RollercoasterMain.getOptions().getOptions().useFauxGpio;
        SciolyGpio.FAUX_CONNECT = mockIo;
        JCheckBoxMenuItem item = new JCheckBoxMenuItem( "Mock I/O" );
        item.setSelected( SciolyGpio.FAUX_CONNECT );
        ActionListener listener = ( e ) -> {
            SciolyGpio.FAUX_CONNECT = !SciolyGpio.FAUX_CONNECT;
            mockIoMenuItem.setSelected( SciolyGpio.FAUX_CONNECT );
            OptionsSerializer<RollercoasterOptions> options = RollercoasterMain.getOptions();

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

        fileMenu.add( new JMenuItem( createTestSuiteAction() ), row++ );
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

        action = new ActionAdapter( ( e ) -> showCompetition( true ),
            "Start Competition", RollercoasterIcons.getRollercoaster16() );
        KeyStroke key = KeyStroke.getKeyStroke( "F8" );
        action.putValue( Action.ACCELERATOR_KEY, key );
        item = menu.add( action );

        item.setMnemonic( 'S' );

        UiUtils.addHotKey( ( JComponent )getView().getContentPane(), "F8",
            ( e ) -> showCompetition( competitionView == null ) );

        return menu;
    }

    /***************************************************************************
     * @param show
     **************************************************************************/
    private void showCompetition( boolean show )
    {
        mockIoMenuItem.setEnabled( false );

        LogUtils.printDebug( "showCompetition(%s)", show );

        OptionsSerializer<RollercoasterOptions> userio = RollercoasterMain.getOptions();
        RollercoasterOptions options = userio.getOptions();

        if( show && competitionView == null )
        {
            try
            {
                GpioController gpio = SciolyGpio.startup();
                RcCompetition competition = new RcCompetition( options.config,
                    gpio );

                competition.connect();

                this.competitionView = new RcCompetitionView( competition,
                    getView().getIconImages(), getView().getSize() );

                JFrame frame = competitionView.getView();

                UiUtils.addHotKey( ( JComponent )frame.getContentPane(), "F8",
                    ( e ) -> showCompetition( this.competitionView == null ) );

                // setFullScreen( true );

                competitionView.setVisible( true );
            }
            catch( IllegalStateException ex )
            {
                SwingUtils.showErrorMessage( getView(), "Setup Error",
                    "Pi4j library was not found" );
                return;
            }
        }
        else if( !show && competitionView != null )
        {
            competitionView.setVisible( false );
            competitionView = null;
        }
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Action createTestSuiteAction()
    {
        return new ActionAdapter(
            ( e ) -> UiUtils.showTestSuiteScreen( getView() ), "Test I/O",
            IconConstants.getIcon( IconConstants.CHECK_16 ) );
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
