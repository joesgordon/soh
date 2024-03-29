package soinc.rollercoaster.tasks;

import java.io.IOException;

import soinc.lib.gpio.ITimerCallback;
import soinc.rollercoaster.data.RcCompetitionData;
import soinc.rollercoaster.ui.RcCompetitionView;

/*******************************************************************************
 * 
 ******************************************************************************/
public interface IRcSignals
{
    /***************************************************************************
     * @throws IOException
     **************************************************************************/
    public void connect( RcTeamCompetition competition, RcCompetitionView view )
        throws IOException;

    /***************************************************************************
     * 
     **************************************************************************/
    public void disconnect();

    /***************************************************************************
     * @param red
     * @param green
     * @param blue
     **************************************************************************/
    public void setLights( boolean red, boolean green, boolean blue );

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getTimerCount();

    /***************************************************************************
     * @param index
     * @param callback
     **************************************************************************/
    public void setTimerCallback( int index, ITimerCallback callback );

    /***************************************************************************
     * 
     **************************************************************************/
    public void clearTimers();

    /***************************************************************************
     * @param data
     **************************************************************************/
    public void updateUI( RcCompetitionData data );
}
