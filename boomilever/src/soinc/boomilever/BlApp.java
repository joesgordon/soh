package soinc.boomilever;

import javax.swing.JFrame;

import org.jutils.ui.app.IFrameApp;

import soinc.boomilever.ui.BlFrameView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class BlApp implements IFrameApp
{
    private BlFrameView frameView = null;

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JFrame createFrame()
    {
        frameView = new BlFrameView();

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
