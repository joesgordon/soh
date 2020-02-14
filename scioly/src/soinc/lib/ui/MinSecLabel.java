package soinc.lib.ui;

import java.awt.Color;

import javax.swing.JLabel;

import org.jutils.ui.model.IView;

import soinc.lib.UiUtils;
import soinc.lib.data.TimeDuration;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MinSecLabel implements IView<JLabel>
{
    /**  */
    public final JLabel view;
    /**  */
    private final String defaultText;

    /***************************************************************************
     * @param defaultText
     * @param fontSize
     **************************************************************************/
    public MinSecLabel( String defaultText, float fontSize )
    {
        this.defaultText = defaultText;
        this.view = UiUtils.createNumLabel( defaultText, fontSize );

        view.setOpaque( true );
        view.setBackground( Color.black );

        reset();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void reset()
    {
        view.setText( defaultText );
    }

    /***************************************************************************
     * @param milliseconds
     **************************************************************************/
    public void setTime( int milliseconds )
    {
        setTime( ( long )milliseconds );
    }

    /***************************************************************************
     * @param milliseconds
     **************************************************************************/
    public void setTime( long milliseconds )
    {
        TimeDuration d = new TimeDuration( milliseconds + 999 );
        String time = String.format( " %1d:%02d ", d.totalMinutes, d.seconds );
        view.setText( time );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JLabel getView()
    {
        return view;
    }
}
