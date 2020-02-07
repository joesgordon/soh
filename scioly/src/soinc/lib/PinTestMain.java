package soinc.lib;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import org.jutils.OptionUtils;
import org.jutils.io.options.OptionsSerializer;
import org.jutils.ui.StandardFrameView;
import org.jutils.ui.app.FrameRunner;

import soinc.lib.data.PinTestSuite;
import soinc.lib.data.SciolyOptions;
import soinc.lib.gpio.SciolyGpio;
import soinc.lib.ui.PinTestSuiteView;

/***************************************************************************
 *
 **************************************************************************/
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

        try
        {
            SciolyGpio.startup();
        }
        catch( IllegalStateException ex )
        {
            OptionUtils.showErrorMessage( frame, "Setup Error",
                "Pi4j library was not found" );
            return frame;
        }

        OptionsSerializer<SciolyOptions> options = UiUtils.getOptions();
        PinTestSuite suite = options.getOptions().testSuite;

        suite.initialize();

        view.setData( suite );

        return frame;
    }
}
