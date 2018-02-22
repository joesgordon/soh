package soinc.rollercoaster;

import javax.swing.JFrame;

import org.jutils.ui.app.IFrameApp;

import soinc.rollercoaster.ui.RollercoasterFrameView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class RollercoasterApp implements IFrameApp
{
    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JFrame createFrame()
    {
        RollercoasterFrameView frameView = new RollercoasterFrameView();

        return frameView.getView();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void finalizeGui()
    {
    }
}
