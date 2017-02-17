package soh.ui;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.jutils.ui.StandardFormView;
import org.jutils.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PinTestView implements IView<JComponent>
{
    private final JPanel view;

    public PinTestView()
    {
        view = createView();
    }

    private JPanel createView()
    {
        StandardFormView form = new StandardFormView();
        // TODO Auto-generated method stub
        return form.getView();
    }

    @Override
    public JComponent getView()
    {
        return new JPanel();
    }
}
