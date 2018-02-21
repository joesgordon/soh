package soinc.rollercoaster;

import javax.swing.JFrame;

import org.jutils.ui.app.IFrameApp;

import soinc.rollercoaster.ui.RollercoasterFrameView;

public class RollercoasterApp implements IFrameApp
{
    @Override
    public JFrame createFrame()
    {
        RollercoasterFrameView frameView = new RollercoasterFrameView();

        return frameView.getView();
    }

    @Override
    public void finalizeGui()
    {
    }
}
