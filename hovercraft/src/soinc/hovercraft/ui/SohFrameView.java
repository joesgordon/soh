package soinc.hovercraft.ui;

import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

import org.jutils.IconConstants;
import org.jutils.SwingUtils;
import org.jutils.io.XStreamUtils;
import org.jutils.io.options.OptionsSerializer;
import org.jutils.task.TaskView;
import org.jutils.ui.OkDialogView;
import org.jutils.ui.OkDialogView.OkDialogButtons;
import org.jutils.ui.StandardFrameView;
import org.jutils.ui.event.ActionAdapter;
import org.jutils.ui.event.FileChooserListener;
import org.jutils.ui.event.FileChooserListener.IFileSelected;
import org.jutils.ui.event.FileChooserListener.ILastFile;
import org.jutils.ui.model.IView;

import com.thoughtworks.xstream.XStreamException;

import soinc.hovercraft.GpioInputExample;
import soinc.hovercraft.GpioOutputExample;
import soinc.hovercraft.HovercraftIcons;
import soinc.hovercraft.HovercraftMain;
import soinc.hovercraft.data.HoverConfig;
import soinc.hovercraft.data.SohOptions;
import soinc.hovercraft.tasks.HovercraftCompetition;
import soinc.hovercraft.ui.cfg.HoverConfigView;
import soinc.hovercraft.ui.live.CompetitionView;
import soinc.lib.data.PinTestSuite;
import soinc.lib.gpio.SciolyGpio;
import soinc.lib.ui.PinTestSuiteView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SohFrameView implements IView<JFrame>
{
    /**  */
    private final StandardFrameView frameView;
    /**  */
    private final JCheckBoxMenuItem fauxGpioMenuItem;
    /**  */
    private final HoverConfigView configView;

    /**  */
    private final Action newAction;
    /**  */
    private final Action openAction;
    /**  */
    private final Action saveAction;

    /**  */
    private CompetitionView competitionView;
    /**  */
    private JFrame competitionFrame;

    /***************************************************************************
     * 
     **************************************************************************/
    public SohFrameView()
    {
        this.frameView = new StandardFrameView();
        this.fauxGpioMenuItem = new JCheckBoxMenuItem();
        this.configView = new HoverConfigView();

        this.newAction = createNewAction();
        this.openAction = createOpenAction();
        this.saveAction = createSaveAction();

        this.competitionView = null;

        createMenubar( frameView.getMenuBar(), frameView.getFileMenu() );

        frameView.getView().addWindowListener( new SohWindowListener() );
        frameView.setContent( configView.getView() );
        frameView.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frameView.setSize( 1280, 800 );
        frameView.getView().setIconImages(
            HovercraftIcons.getHovercraftIcons() );

        OptionsSerializer<SohOptions> options = HovercraftMain.getOptions();

        configView.setData( options.getOptions().config );

        UiUtils.addHotKey( ( JComponent )frameView.getView().getContentPane(),
            "F8", ( e ) -> showCompetition( competitionView == null ) );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Action createNewAction()
    {
        Icon icon = IconConstants.getIcon( IconConstants.NEW_FILE_16 );
        ActionListener listener = ( e ) -> configView.setData(
            new HoverConfig() );

        return new ActionAdapter( listener, "New", icon );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Action createOpenAction()
    {
        Icon icon = IconConstants.getIcon( IconConstants.OPEN_FILE_16 );
        IFileSelected ifs = ( f ) -> openFile( f );
        ILastFile ifl = () -> HovercraftMain.getOptions().getOptions().lastConfigFile;
        FileChooserListener listener = new FileChooserListener( getView(),
            "Open Configuration", false, ifs, ifl );

        listener.addExtension( "Hovercraft Config File", "hcfg" );

        return new ActionAdapter( listener, "Open", icon );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Action createSaveAction()
    {
        Icon icon = IconConstants.getIcon( IconConstants.SAVE_16 );
        IFileSelected ifs = ( f ) -> saveFile( f );
        ILastFile ifl = () -> HovercraftMain.getOptions().getOptions().lastConfigFile;
        FileChooserListener listener = new FileChooserListener( getView(),
            "Save Configuration", true, ifs, ifl );

        listener.addExtension( "Hovercraft Config File", "hcfg" );

        return new ActionAdapter( listener, "Save", icon );
    }

    /***************************************************************************
     * @param file
     **************************************************************************/
    private void openFile( File file )
    {
        OptionsSerializer<SohOptions> options = HovercraftMain.getOptions();
        options.getOptions().lastConfigFile = file;
        options.write();

        try
        {
            HoverConfig config = XStreamUtils.readObjectXStream( file );

            configView.setData( config );
        }
        catch( XStreamException ex )
        {
            SwingUtils.showErrorMessage( getView(),
                "Configuration file is not formatted correctly: " +
                    file.getAbsolutePath(),
                "File Format Error" );
        }
        catch( FileNotFoundException ex )
        {
            SwingUtils.showErrorMessage( getView(),
                "Configuration file not found: " + file.getAbsolutePath(),
                "File Format Error" );
        }
        catch( IOException ex )
        {
            SwingUtils.showErrorMessage( getView(),
                "Configuration file is cannot be read: " +
                    file.getAbsolutePath(),
                "File Format Error" );
        }

        saveUserConfig();
    }

    /***************************************************************************
     * @param file
     **************************************************************************/
    private void saveFile( File file )
    {
        OptionsSerializer<SohOptions> options = HovercraftMain.getOptions();
        options.getOptions().lastConfigFile = file;
        options.write();

        HoverConfig config = configView.getData();

        try
        {
            XStreamUtils.writeObjectXStream( config, file );
        }
        catch( XStreamException ex )
        {
            SwingUtils.showErrorMessage( getView(),
                "Configuration file cannot formatted correctly: " +
                    ex.getMessage(),
                "File Format Error" );
        }
        catch( IOException ex )
        {
            SwingUtils.showErrorMessage( getView(),
                "Configuration file is cannot be written: " +
                    file.getAbsolutePath(),
                "File Format Error" );
        }

        saveUserConfig();
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
        JMenuItem exitItem = fileMenu.getItem( 0 );
        int row = 0;

        fileMenu.add( new JMenuItem( newAction ), row++ );
        fileMenu.add( new JMenuItem( openAction ), row++ );
        fileMenu.add( new JMenuItem( saveAction ), row++ );
        fileMenu.add( new JSeparator(), row++ );
        fileMenu.add( new JMenuItem( createTestSuiteAction() ), row++ );
        fileMenu.add( new JMenuItem( createTestInputAction() ), row++ );
        fileMenu.add( new JMenuItem( createTestOutputAction() ), row++ );
        fileMenu.add( new JSeparator(), row++ );
        fileMenu.add( createMockGpioMenuItem(), row++ );
        fileMenu.add( new JSeparator(), row++ );

        exitItem.addActionListener( new FrameExitListener( this ) );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Action createTestSuiteAction()
    {
        return new ActionAdapter( ( e ) -> showTestSuiteScreen(), "Test I/O",
            IconConstants.getIcon( IconConstants.CHECK_16 ) );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Action createTestInputAction()
    {
        ActionListener listener = ( e ) -> {
            fauxGpioMenuItem.setEnabled( false );
            TaskView.startAndShow( getView(), new GpioInputExample(),
                "Testing Input" );
        };
        return new ActionAdapter( listener, "Test Input",
            IconConstants.getIcon( IconConstants.IMPORT_16 ) );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Action createTestOutputAction()
    {
        ActionListener listener = ( e ) -> {
            fauxGpioMenuItem.setEnabled( false );
            TaskView.startAndShow( getView(), new GpioOutputExample(),
                "Testing Output" );
        };

        return new ActionAdapter( listener, "Test Output",
            IconConstants.getIcon( IconConstants.EXPORT_16 ) );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JCheckBoxMenuItem createMockGpioMenuItem()
    {
        ActionListener listener = ( e ) -> {
            SciolyGpio.FAUX_CONNECT = !SciolyGpio.FAUX_CONNECT;
            fauxGpioMenuItem.setSelected( SciolyGpio.FAUX_CONNECT );
            OptionsSerializer<SohOptions> options = HovercraftMain.getOptions();

            options.getOptions().useFauxGpio = SciolyGpio.FAUX_CONNECT;
            options.write();
            // LogUtils.printDebug( "Faux connect: %s", SohGpio.FAUX_CONNECT );
        };
        // Icon icon = IconConstants.getIcon( IconConstants.EXPORT_16 );
        Icon icon = null;
        Action action = new ActionAdapter( listener, "Mock GPIO", icon );

        fauxGpioMenuItem.setAction( action );
        fauxGpioMenuItem.setSelected( SciolyGpio.FAUX_CONNECT );

        return fauxGpioMenuItem;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JMenu createGoMenu()
    {
        JMenu menu = new JMenu( "Go" );
        Action action;

        action = new ActionAdapter( ( e ) -> showCompetition( true ),
            "Start Competition", HovercraftIcons.getHovercraft16() );
        menu.add( action );

        return menu;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void showTestSuiteScreen()
    {
        PinTestSuiteView view = new PinTestSuiteView();
        OkDialogView okView = new OkDialogView( getView(), view.getView(),
            ModalityType.DOCUMENT_MODAL, OkDialogButtons.OK_ONLY );

        try
        {
            SciolyGpio.startup();
        }
        catch( IllegalStateException ex )
        {
            SwingUtils.showErrorMessage( getView(), "Setup Error",
                "Pi4j library was not found" );
            return;
        }

        OptionsSerializer<SohOptions> options = HovercraftMain.getOptions();
        PinTestSuite suite = options.getOptions().testSuite;

        suite.initialize();

        view.setData( suite );

        okView.show( "I/O Test", new Dimension( 850, 700 ) );

        options.getOptions().testSuite = view.getData();
        options.write();

        view.unprovisionAll();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private HoverConfig saveUserConfig()
    {
        HoverConfig config = configView.getData();
        OptionsSerializer<SohOptions> options = HovercraftMain.getOptions();

        options.getOptions().config = config;
        options.write();

        return config;
    }

    /***************************************************************************
     * @param show
     **************************************************************************/
    private void showCompetition( boolean show )
    {
        fauxGpioMenuItem.setEnabled( false );

        HoverConfig config = saveUserConfig();

        if( show && competitionView == null )
        {
            try
            {
                HovercraftCompetition hc = HovercraftCompetition.connect(
                    config );

                this.competitionView = new CompetitionView( config, hc );

                this.competitionFrame = new JFrame( "Competition" );
                competitionFrame.setContentPane( competitionView.createView() );
                competitionFrame.setDefaultCloseOperation(
                    JDialog.DO_NOTHING_ON_CLOSE );
                competitionFrame.addWindowListener(
                    new SohDialogListener( this ) );
                competitionFrame.setUndecorated( true );
                competitionFrame.setSize( getView().getWidth(),
                    getView().getHeight() );

                competitionFrame.validate();
                competitionFrame.setVisible( true );

                UiUtils.addHotKey(
                    ( JComponent )competitionFrame.getContentPane(), "F8",
                    ( e ) -> showCompetition( this.competitionView == null ) );

                // setFullScreen( true );
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
            HovercraftCompetition.disconnect();

            competitionView = null;

            // setFullScreen( false );

            competitionFrame.dispose();
            competitionFrame = null;
        }
    }

    /***************************************************************************
     * @param fullscreen
     **************************************************************************/
    private void setFullScreen( boolean fullscreen )
    {
        // dialog.setAlwaysOnTop( fullscreen );

        if( fullscreen )
        {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice device = ge.getDefaultScreenDevice();

            if( device.isFullScreenSupported() && !"".isEmpty() )
            {
                device.setFullScreenWindow( this.competitionFrame );
            }
            else
            {
                competitionFrame.setBounds( ge.getMaximumWindowBounds() );
            }
        }
        else
        {
            competitionFrame.setSize( 500, 500 );
            competitionFrame.setLocationRelativeTo( getView() );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JFrame getView()
    {
        return frameView.getView();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class FrameExitListener implements ActionListener
    {
        private final SohFrameView view;

        public FrameExitListener( SohFrameView view )
        {
            this.view = view;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            if( view.competitionView == null ||
                !view.competitionView.isRunning() )
            {
                view.saveUserConfig();
                SciolyGpio.shutdown();
                System.exit( 0 );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class SohDialogListener extends WindowAdapter
    {
        private final SohFrameView view;

        public SohDialogListener( SohFrameView view )
        {
            this.view = view;
        }

        @Override
        public void windowClosing( WindowEvent e )
        {
            if( view.competitionView == null ||
                !view.competitionView.isRunning() )
            {
                view.saveUserConfig();
                view.setFullScreen( false );
                view.competitionFrame.setVisible( false );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class SohWindowListener extends WindowAdapter
    {
        @Override
        public void windowClosing( WindowEvent e )
        {
            SciolyGpio.shutdown();
        }
    }
}
