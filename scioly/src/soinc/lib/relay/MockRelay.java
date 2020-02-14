package soinc.lib.relay;

import java.io.IOException;

import org.jutils.io.LogUtils;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MockRelay implements IRelays
{
    /**  */
    private final boolean [] relays;

    /***************************************************************************
     * 
     **************************************************************************/
    public MockRelay()
    {
        this.relays = new boolean[Relays.RELAY_COUNT];
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void initialize() throws IOException
    {
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
        int mask = getRelays();
        int m = 1 << index;

        if( isOn )
        {
            mask |= m;
        }
        else
        {
            mask &= ~m;
        }

        setRelays( mask );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void turnAllOn()
    {
        setRelays( 0xFF );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void turnAllOff()
    {
        setRelays( 0 );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setRelays( int mask )
    {
        for( int i = 0; i < relays.length; i++ )
        {
            int m = 1 << i;

            relays[i] = ( mask & m ) == m;
        }

        String relayStr = "";
        for( int i = relays.length - 1; i > -1; i-- )
        {
            if( relayStr.length() > 0 )
            {
                relayStr += ", ";
            }
            relayStr += relays[i];
        }

        LogUtils.printDebug(
            "MockRelay::setRelays(int): Set relays to %02X : %s", mask,
            relayStr );
        // Utils.printStackTrace();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public int getRelays()
    {
        int mask = 0;

        for( int i = 0; i < relays.length; i++ )
        {
            if( relays[i] )
            {
                int m = 1 << i;

                mask |= m;
            }
        }

        return mask;
    }
}
