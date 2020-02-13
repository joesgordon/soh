package soinc.lib.relay;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.jutils.Utils;
import org.jutils.io.IOUtils;
import org.jutils.io.LogUtils;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Relays implements IRelays
{
    /**  */
    public static final int RED_MASK = 0x1;
    /**  */
    public static final int GREEN_MASK = 0x2;
    /**  */
    public static final int BLUE_MASK = 0x4;

    /**  */
    public static final int RED_LIGHT = RED_MASK;
    /**  */
    public static final int GREEN_LIGHT = GREEN_MASK;
    /**  */
    public static final int BLUE_LIGHT = BLUE_MASK;
    /**  */
    public static final int WHITE_LIGHT = RED_MASK & GREEN_MASK & BLUE_MASK;
    /**  */
    public static final int PURPLE_LIGHT = RED_MASK & BLUE_MASK;
    /**  */
    public static final int YELLOW_LIGHT = RED_MASK & GREEN_MASK;
    /** Looks washed out. */
    public static final int CYAN_LIGHT = BLUE_MASK & GREEN_MASK;
    /** Not really black. Just off. */
    public static final int BLACK_LIGHT = 0;

    /**  */
    private static final File RELAY_EXE = IOUtils.getInstallFile(
        "krelay.exe" );
    /**  */
    public static final int RELAY_COUNT = 8;

    /**  */
    private int relaysMask;

    /***************************************************************************
     * 
     **************************************************************************/
    public Relays()
    {
        this.relaysMask = 0;
    }

    /***************************************************************************
     * @param mask
     **************************************************************************/
    private void runProcess( int mask )
    {
        ProcessBuilder pb = new ProcessBuilder( RELAY_EXE.getAbsolutePath(),
            "" + mask );

        String cmd = Utils.collectionToString( new ArrayList<>( pb.command() ),
            " " );
        LogUtils.printDebug( "Running \"%s\"", cmd );

        pb.inheritIO();

        try
        {
            Process p = pb.start();
            p.waitFor();
        }
        catch( IOException ex )
        {
            ex.printStackTrace();
        }
        catch( InterruptedException ex )
        {
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void initialize() throws IOException
    {
        LogUtils.printDebug( "Initializing Relays" );

        if( !RELAY_EXE.isFile() )
        {
            throw new IOException( "Relay executable does not exist: " +
                RELAY_EXE.getAbsolutePath() );
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public int getRelayCount()
    {
        return RELAY_COUNT;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public boolean isRelayOn( int index )
    {
        int mask = 1 << index;

        return ( relaysMask & mask ) == mask;
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
        int mask = 1 << index;

        if( isOn )
        {
            relaysMask |= mask;
        }
        else
        {
            mask &= ~mask;
        }

        runProcess( relaysMask );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void turnAllOn()
    {
        relaysMask = 0xFF;
        runProcess( relaysMask );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void turnAllOff()
    {
        relaysMask = 0;
        runProcess( relaysMask );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setRelays( int mask )
    {
        relaysMask = mask;
        runProcess( relaysMask );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public int getRelays()
    {
        return relaysMask;
    }
}
