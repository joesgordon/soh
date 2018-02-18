package soinc.hovercraft;

import javax.swing.JFrame;

import org.jutils.ui.app.IFrameApp;

import soinc.hovercraft.ui.SohFrameView;

public class HovercraftApp implements IFrameApp
{
    private SohFrameView view;

    @Override
    public JFrame createFrame()
    {
        this.view = new SohFrameView();

        return view.getView();
    }

    @Override
    public void finalizeGui()
    {
    }
}
