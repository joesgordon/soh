package soinc.rollercoaster;

import javax.swing.JFrame;

import org.jutils.ui.app.IFrameApp;

import soinc.rollercoaster.ui.RcFrameView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class RcApp implements IFrameApp
{
    private RcFrameView frameView = null;

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JFrame createFrame()
    {
        frameView = new RcFrameView();

        return frameView.getView();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void finalizeGui()
    {
        frameView.getView().setLocation( 0, 0 );
    }
}
