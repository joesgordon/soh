package soh.ui;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.*;

public class TextZoomer
{
    private final JComponent parent;
    private int mag;

    private TextZoomer( JComponent parent )
    {
        this.parent = parent;
        this.mag = 0;

        KeyStroke ks;
        ActionListener listener;

        ks = KeyStroke.getKeyStroke( KeyEvent.VK_ADD, KeyEvent.CTRL_DOWN_MASK );
        listener = ( e ) -> zoomText( true );
        UiUtils.addHotKey( parent, ks, listener );

        ks = KeyStroke.getKeyStroke( KeyEvent.VK_EQUALS,
            KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK );
        listener = ( e ) -> zoomText( true );
        UiUtils.addHotKey( parent, ks, listener );

        ks = KeyStroke.getKeyStroke( KeyEvent.VK_MINUS,
            KeyEvent.CTRL_DOWN_MASK );
        listener = ( e ) -> zoomText( false );
        UiUtils.addHotKey( parent, ks, listener );

        ks = KeyStroke.getKeyStroke( KeyEvent.VK_NUMPAD0,
            KeyEvent.CTRL_DOWN_MASK );
        listener = ( e ) -> normalize();
        UiUtils.addHotKey( parent, ks, listener );
    }

    public void install( JComponent parent )
    {
        new TextZoomer( parent );
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
