package soh.ui;

import java.awt.*;
import java.awt.Dialog.ModalityType;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;

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
import org.jutils.ui.model.IView;

import com.thoughtworks.xstream.XStreamException;

import soh.SohIcons;
import soh.SohMain;
import soh.data.*;
import soh.gpio.*;

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
    private final CompetitionView competitionView;
    /**  */
    private final TextZoomer zoomer;

    /**  */
    private final Action newAction;
    /**  */
    private final Action openAction;
    /**  */
    private final Action saveAction;

    /***************************************************************************
     * 
     **************************************************************************/
    public SohFrameView()
    {
        this.frameView = new StandardFrameView();
        this.fauxGpioMenuItem = new JCheckBoxMenuItem();
        this.configView = new HoverConfigView();
        this.competitionView = new CompetitionView();
        this.zoomer = new TextZoomer( competitionView.getView() );

        this.newAction = createNewAction();
        this.openAction = createOpenAction();
        this.saveAction = createSaveAction();

        createMenubar( frameView.getMenuBar(), frameView.getFileMenu() );

        frameView.getView().addWindowListener( new SohWindowListener( this ) );
        frameView.setContent( configView.getView() );
        frameView.setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE );
        // frameView.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frameView.setSize( 1280, 800 );
        // frameView.getView().setExtendedState( JFrame.MAXIMIZED_BOTH );
        frameView.getView().setIconImages( SohIcons.getSohIcons() );

        // setFullScreen( frameView.getView() );

        setupHotkeys();

        OptionsSerializer<SohOptions> options = SohMain.getOptions();

        configView.setData( options.getOptions().config );
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
        FileChooserListener listener = new FileChooserListener( getView(),
            "Open Configuration", false, ifs );

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
        FileChooserListener listener = new FileChooserListener( getView(),
            "Save Configuration", true, ifs );

        listener.addExtension( "Hovercraft Config File", "hcfg" );

        // TODO add last file

        return new ActionAdapter( listener, "Save", icon );
    }

    /***************************************************************************
     * @param file
     **************************************************************************/
    private void openFile( File file )
    {
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
    }

    /***************************************************************************
     * @param file
     **************************************************************************/
    private void saveFile( File file )
    {
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
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void setupHotkeys()
    {
        JComponent contentPane = ( JComponent )frameView.getView().getContentPane();

        // addHotKeys( contentPane, TrackType.TRACK_1, "F1", "F2", "F3",
        // "control 1", "control 2" );
        addHotKeys( competitionView.getView(), TrackType.TRACK_1, "F1", "F2",
            "F3", "control 1", "control 2" );

        // addHotKeys( contentPane, TrackType.TRACK_2, "F10", "F11", "F13",
        // "control 9", "control 0" );
        addHotKeys( competitionView.getView(), TrackType.TRACK_2, "F10", "F11",
            "F12", "control 9", "control 0" );

        UiUtils.addHotKey( contentPane, "F8", ( e ) -> showCompetition(
            !competitionView.getView().isShowing() ) );

        KeyStroke ks;
        ActionListener listener;

        ks = KeyStroke.getKeyStroke( KeyEvent.VK_ADD, KeyEvent.CTRL_DOWN_MASK );
        listener = ( e ) -> zoomer.zoomText( true );
        UiUtils.addHotKey( competitionView.getView(), ks, listener );

        ks = KeyStroke.getKeyStroke( KeyEvent.VK_EQUALS,
            KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK );
        listener = ( e ) -> zoomer.zoomText( true );
        UiUtils.addHotKey( competitionView.getView(), ks, listener );

        ks = KeyStroke.getKeyStroke( KeyEvent.VK_MINUS,
            KeyEvent.CTRL_DOWN_MASK );
        listener = ( e ) -> zoomer.zoomText( false );
        UiUtils.addHotKey( competitionView.getView(), ks, listener );

        ks = KeyStroke.getKeyStroke( KeyEvent.VK_NUMPAD0,
            KeyEvent.CTRL_DOWN_MASK );
        listener = ( e ) -> zoomer.normalize();
        UiUtils.addHotKey( competitionView.getView(), ks, listener );

        // ks = KeyStroke.getKeyStroke( KeyEvent.VK_0, KeyEvent.CTRL_DOWN_MASK
        // );
        // listener = ( e ) -> zoomer.normalize();
        // UiUtils.addHotKey( competitionView.getView(), ks, listener );
    }

    /***************************************************************************
     * @param comp
     * @param t
     * @param startPeriodKey
     * @param failKey
     * @param clearKey
     * @param startRunKey
     * @param stopRunKey
     **************************************************************************/
    private void addHotKeys( JComponent comp, TrackType t,
        String startPeriodKey, String failKey, String clearKey,
        String startRunKey, String stopRunKey )
    {
        UiUtils.addHotKey( comp, startPeriodKey,
            ( e ) -> competitionView.startPeriod( t ) );

        UiUtils.addHotKey( comp, failKey,
            ( e ) -> competitionView.failRun( t ) );

        UiUtils.addHotKey( comp, clearKey,
            ( e ) -> competitionView.clearTrack( t ) );

        UiUtils.addHotKey( comp, startRunKey,
            ( e ) -> competitionView.startRun( t ) );

        UiUtils.addHotKey( comp, stopRunKey,
            ( e ) -> competitionView.stopRun( t ) );
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
        fileMenu.add( new JMenuItem( createTestIoAction() ), row++ );
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
    private Action createTestIoAction()
    {
        return new ActionAdapter( ( e ) -> showInputScreen(), "Test I/O",
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
            SohGpio.FAUX_CONNECT = !SohGpio.FAUX_CONNECT;
            fauxGpioMenuItem.setSelected( SohGpio.FAUX_CONNECT );
            // LogUtils.printDebug( "Faux connect: %s", SohGpio.FAUX_CONNECT );
        };
        // Icon icon = IconConstants.getIcon( IconConstants.EXPORT_16 );
        Icon icon = null;
        Action action = new ActionAdapter( listener, "Mock GPIO", icon );

        fauxGpioMenuItem.setAction( action );
        fauxGpioMenuItem.setSelected( SohGpio.FAUX_CONNECT );

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
            "Start Competition", SohIcons.getSoh16() );
        menu.add( action );

        return menu;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void showInputScreen()
    {
        PinsTestView view = new PinsTestView();
        OkDialogView okView = new OkDialogView( getView(), view.getView(),
            ModalityType.DOCUMENT_MODAL, OkDialogButtons.OK_ONLY );

        try
        {
            SohGpio.startup();
        }
        catch( IllegalStateException ex )
        {
            SwingUtils.showErrorMessage( getView(), "Setup Error",
                "Pi4j library was not found" );
            return;
        }

        okView.show( "I/O Test", new Dimension( 700, 800 ) );
    }

    /***************************************************************************
     * @param show
     **************************************************************************/
    private void showCompetition( boolean show )
    {
        fauxGpioMenuItem.setEnabled( false );

        if( show )
        {
            OptionsSerializer<SohOptions> options = SohMain.getOptions();

            options.getOptions().config = configView.getData();

            options.write();

            Runnable startT1 = () -> competitionView.startRun(
                TrackType.TRACK_1 );
            Runnable stopT1 = () -> competitionView.stopRun(
                TrackType.TRACK_1 );
            Runnable startT2 = () -> competitionView.startRun(
                TrackType.TRACK_2 );
            Runnable stopT2 = () -> competitionView.stopRun(
                TrackType.TRACK_2 );

            try
            {
                SohGpio.startup();
                SohGpio.connect( startT1, stopT1, startT2, stopT2 );
            }
            catch( IllegalStateException ex )
            {
                SwingUtils.showErrorMessage( getView(), "Setup Error",
                    "Pi4j library was not found" );
                return;
            }

            competitionView.setConfig( configView.getData() );

            frameView.getStatusBar().getView().setVisible( false );
            frameView.getMenuBar().setVisible( false );
            frameView.setContent( competitionView.getView() );

            // setFullScreen( true );
        }
        else
        {
            if( competitionView.getView().isShowing() &&
                !competitionView.isRunning() )
            {
                SohGpio.disconnect();

                frameView.getStatusBar().getView().setVisible( true );
                frameView.getMenuBar().setVisible( true );
                frameView.setContent( configView.getView() );

                // setFullScreen( false );
            }
        }
    }

    /***************************************************************************
     * @param fullscreen
     **************************************************************************/
    private void setFullScreen( boolean fullscreen )
    {
        JFrame frame = frameView.getView();

        // frame.setUndecorated( true );
        frame.setAlwaysOnTop( fullscreen );

        if( fullscreen )
        {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice device = ge.getDefaultScreenDevice();

            if( device.isFullScreenSupported() && !"".isEmpty() )
            {
                device.setFullScreenWindow( frame );
            }
            else
            {
                frame.setExtendedState( JFrame.MAXIMIZED_BOTH );
            }
        }
        else
        {
            frame.setExtendedState( frame.getExtendedState() );
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
    private static final class TextZoomer
    {
        private final Container parent;
        private int mag;

        public TextZoomer( Container parent )
        {
            this.parent = parent;
            this.mag = 0;
        }

        public void normalize()
        {
            boolean bigger = mag < 0;
            int cnt = Math.abs( mag / 2 );

            // LogUtils.printDebug( "normalizing text %s for %d",
            // bigger ? "larger" : "smaller", cnt );
            for( int i = 0; i < cnt; i++ )
            {
                zoomText( bigger );
            }

            mag = 0;
        }

        public void zoomText( boolean bigger )
        {
            // LogUtils.printDebug( "zooming text %s",
            // bigger ? "larger" : "smaller" );
            zoomText( parent, bigger );

            mag += bigger ? 2 : -2;
        }

        private void zoomText( Container parent, boolean bigger )
        {
            int cnt = parent.getComponentCount();

            for( int i = 0; i < cnt; i++ )
            {
                Component c = parent.getComponent( i );

                if( c instanceof JLabel )
                {
                    Font f = c.getFont();
                    int size = f.getSize();

                    size += bigger ? 2 : -2;

                    f = f.deriveFont( ( float )size );

                    c.setFont( f );
                }
                else if( c instanceof Container )
                {
                    zoomText( ( Container )c, bigger );
                }
            }
        }
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
            if( !view.competitionView.getView().isShowing() )
            {
                SohGpio.shutdown();
                System.exit( 0 );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class SohWindowListener extends WindowAdapter
    {
        private final SohFrameView view;

        public SohWindowListener( SohFrameView view )
        {
            this.view = view;
        }

        public void windowClosing( WindowEvent e )
        {
            if( !view.competitionView.getView().isShowing() )
            {
                System.exit( 0 );
            }
        }
    }
}
