package soinc.rollercoaster;

import javax.swing.JFrame;

import org.jutils.ui.app.IFrameApp;

import soinc.rollercoaster.ui.RcFrameView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class RcApp implements IFrameApp
{
    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JFrame createFrame()
    {
        RcFrameView frameView = new RcFrameView();

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
