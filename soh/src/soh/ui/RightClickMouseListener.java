package soh.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

import org.jutils.ui.event.ItemActionEvent;
import org.jutils.ui.event.ItemActionListener;

public class RightClickMouseListener extends MouseAdapter
{
    private final ItemActionListener<MouseEvent> listener;

    public RightClickMouseListener( ItemActionListener<MouseEvent> listener )
    {
        this.listener = listener;
    }

    @Override
    public void mouseReleased( MouseEvent e )
    {
        if( SwingUtilities.isRightMouseButton( e ) )
        {
            listener.actionPerformed( new ItemActionEvent<>( this, e ) );
        }
    }
}
