package soinc.rollercoaster.relay;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jutils.io.IOUtils;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Relay implements IRelay
{
    /**  */
    public static final int BOARD_COUNT = 2;
    /**  */
    public static final int RELAY_COUNT = BOARD_COUNT * 2;

    /**  */
    private final List<RelayBoard> boards;

    /***************************************************************************
     * 
     **************************************************************************/
    public Relay()
    {
        this.boards = new ArrayList<>();

        for( int b = 0; b < BOARD_COUNT; b++ )
        {
            File file = IOUtils.getInstallFile( "relay" + ( b + 1 ) );
            boards.add( new RelayBoard( file.getAbsolutePath() ) );
        }
    }

    /***************************************************************************
     * @param board
     **************************************************************************/
    private void runProcess( RelayBoard board )
    {
        ProcessBuilder pb = new ProcessBuilder( board.file, board.getArg() );

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
     * @param index
     * @return
     **************************************************************************/
    private RelayBoard getRelayBoard( int index )
    {
        return boards.get( index / 2 );
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
        RelayBoard board = getRelayBoard( index );
        if( board != null )
        {
            return board.getRelay( index % 2 );
        }
        return false;
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
        RelayBoard board = getRelayBoard( index );
        if( board != null )
        {
            board.setRelay( index % 2, isOn );
            runProcess( board );
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void turnAllOn()
    {
        for( RelayBoard b : boards )
        {
            b.relay1 = true;
            b.relay2 = true;
            runProcess( b );
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void turnAllOff()
    {
        for( RelayBoard b : boards )
        {
            b.relay1 = false;
            b.relay2 = false;
            runProcess( b );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class RelayBoard
    {
        public final String file;
        public boolean relay1;
        public boolean relay2;

        public RelayBoard( String file )
        {
            this.file = file;
        }

        public boolean getRelay( int idx )
        {
            switch( idx )
            {
                case 0:
                    return relay1;

                case 1:
                    return relay2;

                default:
                    return false;
            }
        }

        public void setRelay( int idx, boolean isOn )
        {
            switch( idx )
            {
                case 0:
                    relay1 = isOn;
                    break;

                case 1:
                    relay2 = isOn;
                    break;

                default:
                    break;
            }
        }

        public String getArg()
        {
            return ( relay1 ? "1" : "0" ) + ( relay2 ? "1" : "0" );
        }
    }
}
