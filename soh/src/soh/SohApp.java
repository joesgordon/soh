package soh;

import javax.swing.JFrame;

import org.jutils.ui.app.IFrameApp;

import soh.ui.SohFrameView;

public class SohApp implements IFrameApp
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
