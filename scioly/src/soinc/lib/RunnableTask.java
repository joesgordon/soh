package soinc.lib;

import java.util.TimerTask;

/*******************************************************************************
 * 
 ******************************************************************************/
public class RunnableTask extends TimerTask
{
    /** */
    private final Runnable callback;

    /***************************************************************************
     * @param callback
     **************************************************************************/
    public RunnableTask( Runnable callback )
    {
        this.callback = callback;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void run()
    {
        callback.run();
    }
}
