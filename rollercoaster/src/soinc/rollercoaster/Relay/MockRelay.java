package soinc.rollercoaster.relay;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MockRelay implements IRelay
{
    private final boolean [] relays;

    public MockRelay()
    {
        this.relays = new boolean[Relay.RELAY_COUNT];
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public int getRelayCount()
    {
        return relays.length;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public boolean isRelayOn( int index )
    {
        return relays[index];
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void turnRelayOn( int index )
    {
        setRelay( index, true );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void turnRelayOff( int index )
    {
        setRelay( index, false );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setRelay( int index, boolean isOn )
    {
        relays[index] = isOn;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void turnAllOn()
    {
        for( int i = 0; i < relays.length; i++ )
        {
            relays[i] = true;
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void turnAllOff()
    {
        for( int i = 0; i < relays.length; i++ )
        {
            relays[i] = true;
        }
    }
}
