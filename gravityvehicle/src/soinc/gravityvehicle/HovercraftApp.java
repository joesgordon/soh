package soinc.gravityvehicle;

import javax.swing.JFrame;

import org.jutils.ui.app.IFrameApp;

import soinc.gravityvehicle.ui.HovercraftFrameView;

public class HovercraftApp implements IFrameApp
{
    private HovercraftFrameView view;

    @Override
    public JFrame createFrame()
    {
        this.view = new HovercraftFrameView();

        return view.getView();
    }

    @Override
    public void finalizeGui()
    {
    }
}
