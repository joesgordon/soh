package soinc.gravityvehicle.data;

import org.jutils.INamedItem;

/*******************************************************************************
 * 
 ******************************************************************************/
public enum TrackState implements INamedItem
{
    UNINITIALIZED( "Uninitialized", false, false ),
    INITIALIZED( "Initialized", false, false ),
    PAUSE_A( "Pause A", false, false ),
    WAITING_A( "Waiting on Run A", false, false ),
    RUNNING_A( "Running A", false, false ),
    PAUSE_B( "Pause B", true, false ),
    WAITING_B( "Waiting on Run B", true, false ),
    RUNNING_B( "Runnning B", true, false ),
    FINISHED( "Finished", true, true );

    /**  */
    public final String name;
    /**  */
    public final boolean runaComplete;
    /**  */
    public final boolean runbComplete;

    /***************************************************************************
     * @param name
     * @param runaComplete
     * @param runbComplete
     **************************************************************************/
    private TrackState( String name, boolean runaComplete,
        boolean runbComplete )
    {
        this.name = name;
        this.runaComplete = runaComplete;
        this.runbComplete = runbComplete;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return name;
    }
}
