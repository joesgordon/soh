package soinc.gravityvehicle;

import javax.swing.JFrame;

import org.jutils.ui.app.IFrameApp;

import soinc.gravityvehicle.ui.GvFrameView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class GvApp implements IFrameApp
{
    private GvFrameView frameView = null;

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JFrame createFrame()
    {
        frameView = new GvFrameView();

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
