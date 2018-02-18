package soinc.hovercraft.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.KeyStroke;

import org.jutils.ui.event.ActionAdapter;

/*******************************************************************************
 * 
 ******************************************************************************/
public final class UiUtils
{
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

    /***************************************************************************
     * 
     **************************************************************************/
    private UiUtils()
    {
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
}
