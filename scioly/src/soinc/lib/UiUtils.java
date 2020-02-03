package soinc.lib;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.KeyStroke;

import org.jutils.OptionUtils;
import org.jutils.io.IOUtils;
import org.jutils.io.options.IOptionsCreator;
import org.jutils.io.options.OptionsSerializer;
import org.jutils.ui.OkDialogView;
import org.jutils.ui.OkDialogView.OkDialogButtons;
import org.jutils.ui.event.ActionAdapter;

import soinc.lib.data.PinTestSuite;
import soinc.lib.data.SciolyOptions;
import soinc.lib.gpio.SciolyGpio;
import soinc.lib.ui.PinTestSuiteView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class UiUtils
{
    /**  */
    private static final File userFile = IOUtils.getUsersFile(
        ".ScienceOlympiad", "soh.xml" );
    /**  */
    public static final float DEFAULT_FONT_SIZE = 24.0f;
    /**  */
    public static final float SMALL_FONT_SIZE = 20.0f;
    /**  */
    public static final String TEXT_FONT = "Courier New";
    /**  */
    public static final String NUMBER_FONT = "Consolas";
    /** Softer, gentler, weaker, lamer blue */
    // public static final Color UNI_MAIN_COLOR = new Color( 135, 209, 255 );
    /** Super awesome blue. */
    public static final Color UNI_MAIN_COLOR = new Color( 31, 63, 255 );

    /**  */
    private static OptionsSerializer<SciolyOptions> options;

    /***************************************************************************
     * Private constructor to prevent instantiation.
     **************************************************************************/
    private UiUtils()
    {
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static void showTestGpioScreen( Component parent )
    {
        PinTestSuiteView view = new PinTestSuiteView();
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

        OptionsSerializer<SciolyOptions> options = getOptions();
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
    private static OptionsSerializer<SciolyOptions> getOptions()
    {
        if( options == null )
        {
            IOptionsCreator<SciolyOptions> ioc = new SciolyOptionsCreator();
            options = OptionsSerializer.getOptions( SciolyOptions.class,
                userFile, ioc );
        }

        return options;
    }

    /***************************************************************************
     * @param text
     * @return
     **************************************************************************/
    public static JLabel createTextLabel( String text )
    {
        return createTextLabel( text, DEFAULT_FONT_SIZE );
    }

    /***************************************************************************
     * @param text
     * @param size
     * @return
     **************************************************************************/
    public static JLabel createTextLabel( String text, float size )
    {
        return createLabel( text, size, TEXT_FONT );
    }

    /***************************************************************************
     * @param text
     * @return
     **************************************************************************/
    public static JLabel createNumLabel( String text )
    {
        return createNumLabel( text, DEFAULT_FONT_SIZE );
    }

    /***************************************************************************
     * @param text
     * @param size
     * @return
     **************************************************************************/
    public static JLabel createNumLabel( String text, float size )
    {
        return createLabel( text, size, NUMBER_FONT );
    }

    /***************************************************************************
     * @param text
     * @param size
     * @param font
     * @return
     **************************************************************************/
    public static JLabel createLabel( String text, float size, String font )
    {
        JLabel label = new JLabel( text );
        Font f = Font.decode( font ).deriveFont( size ).deriveFont( Font.BOLD );

        label.setFont( f );
        label.setForeground( Color.white );

        return label;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static Font getTextFont()
    {
        return getTextFont( DEFAULT_FONT_SIZE );
    }

    /***************************************************************************
     * @param size
     * @return
     **************************************************************************/
    public static Font getTextFont( float size )
    {
        return Font.decode( TEXT_FONT ).deriveFont( size ).deriveFont(
            Font.BOLD );
    }

    /**
     * @param comp
     * @param keyName
     * @param listener
     */
    public static void addHotKey( JComponent comp, String keyName,
        ActionListener listener )
    {
        String actionName = keyName + "_Listener";
        KeyStroke key;
        Action action;
        InputMap inMap = comp.getInputMap( JComponent.WHEN_IN_FOCUSED_WINDOW );
        ActionMap acMap = comp.getActionMap();

        action = new ActionAdapter( listener, actionName, null );
        key = KeyStroke.getKeyStroke( keyName );
        action.putValue( Action.ACCELERATOR_KEY, key );
        inMap.put( key, actionName );
        acMap.put( actionName, action );
    }

    /***************************************************************************
     * @param comp
     * @param key
     * @param listener
     **************************************************************************/
    public static void addHotKey( JComponent comp, KeyStroke key,
        ActionListener listener )
    {
        String actionName = key.getKeyCode() + "_Listener";
        Action action;
        InputMap inMap = comp.getInputMap( JComponent.WHEN_IN_FOCUSED_WINDOW );
        ActionMap acMap = comp.getActionMap();

        action = new ActionAdapter( listener, actionName, null );
        action.putValue( Action.ACCELERATOR_KEY, key );
        inMap.put( key, actionName );
        acMap.put( actionName, action );
    }

    /***************************************************************************
     * @param fullscreen
     **************************************************************************/
    public static void setFullScreen( boolean fullscreen, JFrame frame )
    {
        // dialog.setAlwaysOnTop( fullscreen );

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
                frame.setBounds( ge.getMaximumWindowBounds() );
            }
        }
        else
        {
            frame.setSize( 500, 500 );
            frame.setLocation( 0, 0 );
        }
    }

    /***************************************************************************
     *
     **************************************************************************/
    private static final class SciolyOptionsCreator
        implements IOptionsCreator<SciolyOptions>
    {
        @Override
        public SciolyOptions createDefaultOptions()
        {
            return new SciolyOptions();
        }

        @Override
        public SciolyOptions initialize( SciolyOptions options )
        {
            return new SciolyOptions( options );
        }

        @Override
        public void warn( String message )
        {
            // TODO Auto-generated method stub

        }
    }
}
