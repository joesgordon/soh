package soinc.rollercoaster.ui;

import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

import org.jutils.IconConstants;
import org.jutils.ui.StandardFrameView;
import org.jutils.ui.event.ActionAdapter;
import org.jutils.ui.model.IView;

import soinc.lib.UiUtils;
import soinc.rollercoaster.RollercoasterIcons;

/*******************************************************************************
 *
 ******************************************************************************/
public class RollercoasterFrameView implements IView<JFrame>
{
    /**  */
    private final StandardFrameView view;

    /***************************************************************************
     * 
     **************************************************************************/
    public RollercoasterFrameView()
    {
        this.view = new StandardFrameView();

        createMenubar( view.getMenuBar(), view.getFileMenu() );

        view.getView().setIconImages(
            RollercoasterIcons.getRollercoasterIcons() );
        view.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        view.setSize( 500, 500 );
        view.setTitle( "Roller Coaster" );
    }

    /***************************************************************************
     * @param menubar
     * @param fileMenu
     **************************************************************************/
    private void createMenubar( JMenuBar menubar, JMenu fileMenu )
    {
        createFileMenu( fileMenu );
        menubar.add( createGoMenu() );
    }

    /***************************************************************************
     * @param fileMenu
     **************************************************************************/
    private void createFileMenu( JMenu fileMenu )
    {
        int row = 0;

        fileMenu.add( new JMenuItem( createTestSuiteAction() ), row++ );
        fileMenu.add( new JSeparator(), row++ );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JMenu createGoMenu()
    {
        JMenu menu = new JMenu( "Go" );
        Action action;
        JMenuItem item;

        menu.setMnemonic( 'G' );

        action = new ActionAdapter( ( e ) -> showCompetition( true ),
            "Start Competition", RollercoasterIcons.getRollercoaster16() );
        item = menu.add( action );

        item.setMnemonic( 'S' );

        return menu;
    }

    /***************************************************************************
     * @param show
     **************************************************************************/
    private void showCompetition( boolean show )
    {
        // fauxGpioMenuItem.setEnabled( false );
        //
        // HoverConfig config = saveUserConfig();
        //
        // if( show && competitionView == null )
        // {
        // try
        // {
        // HovercraftCompetition hc = HovercraftCompetition.connect(
        // config );
        //
        // this.competitionView = new CompetitionView( config, hc );
        //
        // this.competitionFrame = new JFrame( "Competition" );
        // competitionFrame.setContentPane( competitionView.createView() );
        // competitionFrame.setDefaultCloseOperation(
        // JDialog.DO_NOTHING_ON_CLOSE );
        // competitionFrame.addWindowListener(
        // new SohDialogListener( this ) );
        // competitionFrame.setUndecorated( true );
        // competitionFrame.setSize( getView().getWidth(),
        // getView().getHeight() );
        //
        // competitionFrame.validate();
        // competitionFrame.setVisible( true );
        //
        // UiUtils.addHotKey(
        // ( JComponent )competitionFrame.getContentPane(), "F8",
        // ( e ) -> showCompetition( this.competitionView == null ) );
        //
        // // setFullScreen( true );
        // }
        // catch( IllegalStateException ex )
        // {
        // SwingUtils.showErrorMessage( getView(), "Setup Error",
        // "Pi4j library was not found" );
        // return;
        // }
        // }
        // else if( !show && competitionView != null )
        // {
        // HovercraftCompetition.disconnect();
        //
        // competitionView = null;
        //
        // // setFullScreen( false );
        //
        // competitionFrame.dispose();
        // competitionFrame = null;
        // }
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Action createTestSuiteAction()
    {
        return new ActionAdapter(
            ( e ) -> UiUtils.showTestSuiteScreen( getView() ), "Test I/O",
            IconConstants.getIcon( IconConstants.CHECK_16 ) );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JFrame getView()
    {
        return view.getView();
    }
}
