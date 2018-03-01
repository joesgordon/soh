package soinc.rollercoaster.relay;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jutils.io.IOUtils;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Relays implements IRelays
{
    /**  */
    public static final int BOARD_COUNT = 2;
    /**  */
    private static final File RELAY_EXE = IOUtils.getInstallFile( "set_relay" );
    /**  */
    public static final int RELAY_COUNT = BOARD_COUNT * 2;
    /**  */
    private static final String PREFIX = "usb-Devantec_Ltd._USB-RLY02._000";

    /**  */
    private final List<RelayBoard> boards;

    /***************************************************************************
     * 
     **************************************************************************/
    public Relays()
    {
        this.boards = new ArrayList<>();
    }

    /***************************************************************************
     * @param board
     **************************************************************************/
    private void runProcess( RelayBoard board )
    {
        ProcessBuilder pb = new ProcessBuilder( RELAY_EXE.getAbsolutePath(),
            board.getArg() );

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
    public void initialize() throws IOException
    {
        File serialsDir = new File( "/dev/serial/by-id" );

        if( !RELAY_EXE.isFile() )
        {
            throw new IOException( "Relay executable does not exist: " +
                RELAY_EXE.getAbsolutePath() );
        }

        if( !serialsDir.isDirectory() )
        {
            throw new IOException(
                "Directory does not exist: " + serialsDir.getAbsolutePath() );
        }

        File [] files = serialsDir.listFiles();

        if( files == null )
        {
            throw new IOException(
                "Unable to list files in " + serialsDir.getAbsolutePath() );
        }

        Arrays.sort( files );

        for( File file : files )
        {
            if( file.getName().startsWith( PREFIX ) )
            {
                String relaySerial = file.getName().substring( PREFIX.length(),
                    PREFIX.length() + 5 );
                Path link = file.toPath();
                try
                {
                    Path target = Files.readSymbolicLink( link );
                    File targetFile = target.toFile();
                    System.out.format( "Board %s links to %s", relaySerial,
                        target.getFileName() );
                    boards.add( new RelayBoard( targetFile.getName() ) );
                }
                catch( IOException x )
                {
                    System.err.println( x );
                }
            }
        }

        for( int b = 0; b < BOARD_COUNT; b++ )
        {
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public int getRelayCount()
    {
        return boards.size();
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
            return file + " " + ( relay1 ? "1" : "0" ) + ( relay2 ? "1" : "0" );
        }
    }
}
