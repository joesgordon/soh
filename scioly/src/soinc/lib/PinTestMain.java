package soinc.lib;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JToolBar;

import org.jutils.IconConstants;
import org.jutils.OptionUtils;
import org.jutils.io.options.OptionsSerializer;
import org.jutils.ui.ABButton;
import org.jutils.ui.JGoodiesToolBar;
import org.jutils.ui.StandardFrameView;
import org.jutils.ui.app.FrameRunner;

import soinc.lib.data.PinTestSuite;
import soinc.lib.data.SciolyOptions;
import soinc.lib.gpio.SciolyGpio;
import soinc.lib.ui.PinTestSuiteView;

/*******************************************************************************
 *
 ******************************************************************************/
public class PinTestMain
{
    /***************************************************************************
     * @param args ignored
     **************************************************************************/
    public static void main( String [] args )
    {
        FrameRunner.invokeLater( () -> createFrame() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private static JFrame createFrame()
    {
        PinTestSuiteView view = new PinTestSuiteView();
        StandardFrameView frameView = new StandardFrameView();
        JFrame frame = frameView.getView();

        frameView.setToolbar( createToolbar() );
        frameView.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frameView.setTitle( "Pin Test" );
        frameView.setContent( view.getView() );
        frameView.setSize( 800, 800 );

        frame.addWindowListener( new WindowAdapter()
        {
            public void windowClosing( WindowEvent e )
            {
                view.unprovisionAll();
            }
        } );

        OptionsSerializer<SciolyOptions> options = UiUtils.getOptions();
        PinTestSuite suite = options.getOptions().testSuite;

        suite.initialize();

        view.setData( suite );

        return frame;
    }

    /**
     * @return
     */
    private static JToolBar createToolbar()
    {
        JToolBar toolbar = new JGoodiesToolBar();

        ABButton mockButton = new ABButton( "Mock I/O",
            IconConstants.getIcon( IconConstants.CHECK_16 ), () -> {
                SciolyGpio.FAUX_CONNECT = false;
                return true;
            }, "Mock I/O", IconConstants.getIcon( IconConstants.CLOSE_16 ),
            () -> {
                SciolyGpio.FAUX_CONNECT = true;
                return true;
            } );

        mockButton.setState( SciolyGpio.FAUX_CONNECT );

        ABButton connectButton = new ABButton( "Connect",
            SciolyIcons.getConnect16(), () -> connect(), "Disconnect",
            SciolyIcons.getDisconnect16(), () -> disconnect() );

        toolbar.add( mockButton.getView() );
        toolbar.addSeparator();
        toolbar.add( connectButton.getView() );

        return toolbar;
    }

    private static boolean connect()
    {
        try
        {
            SciolyGpio.startup();
            return true;
        }
        catch( IllegalStateException ex )
        {
            OptionUtils.showErrorMessage( null, "Setup Error",
                "Pi4j library was not found" );
        }
        return false;
    }

    private static boolean disconnect()
    {
        SciolyGpio.shutdown();

        return true;
    }
}
