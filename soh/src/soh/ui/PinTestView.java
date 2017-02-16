package soh.ui;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.jutils.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PinTestView implements IView<JComponent>
{
    public PinTestView()
    {
        ;
    }

    @Override
    public JComponent getView()
    {
        return new JPanel();
    }
}
