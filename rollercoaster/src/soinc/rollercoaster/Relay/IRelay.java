package soinc.rollercoaster.relay;

/**
 * 
 */
public interface IRelay
{
    /**
     * @return
     */
    public int getRelayCount();

    /**
     * @param index
     */
    public void turnRelayOn( int index );

    /**
     * @param index
     */
    public void turnRelayOff( int index );

    /**
     * @param index
     * @param isOn
     */
    public void setRelay( int index, boolean isOn );

    /**
     * 
     */
    public void turnAllOn();

    /**
     * 
     */
    public void turnAllOff();
}
