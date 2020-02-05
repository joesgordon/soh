package soinc.ppp;

import javax.swing.JFrame;

import org.jutils.ui.app.IFrameApp;

import soinc.ppp.ui.PppFrameView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PppApp implements IFrameApp
{
    private PppFrameView frameView = null;

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JFrame createFrame()
    {
        frameView = new PppFrameView();

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
