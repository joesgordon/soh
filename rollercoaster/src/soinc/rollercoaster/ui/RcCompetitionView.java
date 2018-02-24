package soinc.rollercoaster.ui;

import java.awt.Container;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jutils.ui.model.IView;

import soinc.lib.UiUtils;
import soinc.rollercoaster.RollercoasterMain;
import soinc.rollercoaster.data.RcCompetition;

/*******************************************************************************
 * 
 ******************************************************************************/
public class RcCompetitionView implements IView<JFrame>
{
    /**  */
    private final JFrame frame;
    /**  */
    private final RcCompetition competition;

    /***************************************************************************
     * @param competition
     * @param icons
     **************************************************************************/
    public RcCompetitionView( RcCompetition competition, List<Image> icons )
    {
        this.competition = competition;
        this.frame = new JFrame( "Roller Coaster Competition" );

        frame.setIconImages( icons );
        frame.setContentPane( createContent() );
        frame.setDefaultCloseOperation( JDialog.DO_NOTHING_ON_CLOSE );
        frame.addWindowListener( new CompetitionFrameListener( this ) );
        frame.setUndecorated( true );
        frame.setSize( 500, 500 );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Container createContent()
    {
        JPanel panel = new JPanel();

        // TODO Auto-generated method stub

        return panel;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JFrame getView()
    {
        return frame;
    }

    /***************************************************************************
     * @param visible
     **************************************************************************/
    public void setVisible( boolean visible )
    {
        if( visible )
        {
            frame.validate();
            frame.setVisible( true );
        }
        else
        {
            competition.disconnect();

            // setFullScreen( false );

            frame.setVisible( false );
            frame.dispose();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class CompetitionFrameListener extends WindowAdapter
    {
        private final RcCompetitionView view;

        public CompetitionFrameListener( RcCompetitionView view )
        {
            this.view = view;
        }

        @Override
        public void windowClosing( WindowEvent e )
        {
            if( !view.competition.isRunning() )
            {
                RollercoasterMain.getOptions().write();
                UiUtils.setFullScreen( false, view.frame );
                view.frame.setVisible( false );
            }
        }
    }
}
