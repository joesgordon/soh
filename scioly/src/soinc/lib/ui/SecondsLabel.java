package soinc.lib.ui;

import java.awt.Color;

import javax.swing.JLabel;

import org.jutils.ui.model.IView;

import soinc.lib.UiUtils;
import soinc.lib.data.TimeDuration;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SecondsLabel implements IView<JLabel>
{
    /**  */
    public final JLabel view;
    /**  */
    private final String defaultText;

    /***************************************************************************
     * @param defaultText
     * @param fontSize
     **************************************************************************/
    public SecondsLabel( String defaultText, float fontSize )
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
        TimeDuration d = new TimeDuration( milliseconds );
        String time = String.format( " %02d.%02d ", d.totalSeconds,
            d.millis / 10 );
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
