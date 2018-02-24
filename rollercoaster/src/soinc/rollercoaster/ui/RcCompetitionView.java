package soinc.rollercoaster.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.Action;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import org.jutils.SwingUtils;
import org.jutils.ui.OkDialogView;
import org.jutils.ui.OkDialogView.OkDialogButtons;
import org.jutils.ui.event.ActionAdapter;
import org.jutils.ui.event.RightClickListener;
import org.jutils.ui.fields.ItemsListField;
import org.jutils.ui.model.IView;
import org.jutils.ui.model.LabelListCellRenderer.IListCellLabelDecorator;

import soinc.lib.UiUtils;
import soinc.rollercoaster.RollercoasterIcons;
import soinc.rollercoaster.RollercoasterMain;
import soinc.rollercoaster.data.RcCompetition;
import soinc.rollercoaster.data.RcTeam;

/*******************************************************************************
 * 
 ******************************************************************************/
public class RcCompetitionView implements IView<JFrame>
{
    /**  */
    private final RcCompetition competition;

    /**  */
    private final JFrame frame;
    /**  */
    private final JButton teamButton;
    /**  */
    private final JLabel periodField;
    /**  */
    private final JLabel timer1Field;
    /**  */
    private final JLabel timer2Field;
    /**  */
    private final JLabel timer3Field;
    /**  */
    private final JLabel averageField;
    /**  */
    private final JLabel run1Field;
    /**  */
    private final JLabel run2Field;

    /***************************************************************************
     * @param competition
     * @param icons
     * @param dimension
     **************************************************************************/
    public RcCompetitionView( RcCompetition competition, List<Image> icons,
        Dimension size )
    {
        this.competition = competition;
        this.frame = new JFrame( "Roller Coaster Competition" );

        this.teamButton = new JButton( "Select Team" );
        this.periodField = UiUtils.createNumLabel( "-:--", 64 );
        this.timer1Field = UiUtils.createNumLabel( "--.- s" );
        this.timer2Field = UiUtils.createNumLabel( "--.- s" );
        this.timer3Field = UiUtils.createNumLabel( "--.- s" );
        this.averageField = UiUtils.createNumLabel( "--.- s" );
        this.run1Field = UiUtils.createNumLabel( "--.- s" );
        this.run2Field = UiUtils.createNumLabel( "--.- s" );

        frame.setIconImages( icons );
        frame.setContentPane( createContent() );
        frame.setDefaultCloseOperation( JDialog.DO_NOTHING_ON_CLOSE );
        frame.addWindowListener( new CompetitionFrameListener( this ) );
        frame.setUndecorated( true );
        frame.setSize( size );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Container createContent()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        panel.setBackground( Color.black );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( createHeaderPanel(), constraints );

        constraints = new GridBagConstraints( 0, 1, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( createCompetitionPanel(), constraints );

        constraints = new GridBagConstraints( 0, 2, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( Box.createHorizontalStrut( 0 ), constraints );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private static Component createHeaderPanel()
    {
        Icon bannerIcon = RollercoasterIcons.getBannerImage();
        JLabel soLabel = new JLabel( bannerIcon );

        return soLabel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createCompetitionPanel()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        panel.setOpaque( false );

        constraints = new GridBagConstraints( 0, 0, 2, 1, 2.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 30, 0, 0, 0 ), 0, 0 );
        panel.add( createPeriodPanel(), constraints );

        constraints = new GridBagConstraints( 0, 1, 1, 1, 1.0, 1.0,
            GridBagConstraints.NORTHEAST, GridBagConstraints.NONE,
            new Insets( 0, 0, 0, 40 ), 0, 0 );
        panel.add( createTimersPanel(), constraints );

        constraints = new GridBagConstraints( 1, 1, 1, 1, 1.0, 1.0,
            GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
            new Insets( 0, 40, 0, 0 ), 0, 0 );
        panel.add( createRunsPanel(), constraints );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createPeriodPanel()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        panel.setOpaque( false );

        teamButton.setFont( UiUtils.getTextFont() );
        // teamButton.setBorder( new LineBorder( Color.lightGray, 2 ) );
        teamButton.setOpaque( false );
        teamButton.setForeground( Color.white );
        teamButton.addActionListener( ( e ) -> showTeamChooser() );
        teamButton.addMouseListener(
            new RightClickListener( ( e ) -> showTeamPopup( e ) ) );
        // teamButton.setBorderPainted( false );

        constraints = new GridBagConstraints( 0, 0, 2, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 0, 0, 20, 0 ), 0, 0 );
        panel.add( teamButton, constraints );

        JLabel periodLabel = UiUtils.createTextLabel( "Testing Period:" );

        constraints = new GridBagConstraints( 0, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.EAST, GridBagConstraints.NONE,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( periodLabel, constraints );

        constraints = new GridBagConstraints( 1, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.EAST, GridBagConstraints.NONE,
            new Insets( 0, 20, 0, 0 ), 0, 0 );
        panel.add( periodField, constraints );

        return panel;
    }

    private void showTeamPopup( MouseEvent e )
    {
        if( competition.isRunning() )
        {
            JPopupMenu menu = new JPopupMenu();
            ActionListener listener = ( evt ) -> unloadTeam();
            Action a = new ActionAdapter( listener, "Unload Team", null );
            menu.add( a );
            menu.show( e.getComponent(), e.getX(), e.getY() );
        }
        else
        {
            JPopupMenu menu = new JPopupMenu();
            List<RcTeam> teams = competition.getAvailableTeams();

            for( RcTeam t : teams )
            {
                ActionListener listener = ( evt ) -> setTeamData( t );
                Action a = new ActionAdapter( listener, t.name, null );
                menu.add( a );
            }

            menu.show( e.getComponent(), e.getX(), e.getY() );
        }
    }

    private void showTeamChooser()
    {
        if( competition.isRunning() )
        {
            SwingUtils.showErrorMessage(
                getView(), "Cannot change teams until " +
                    competition.getTeam().name + " has finished",
                "Input Error" );
            return;
        }

        List<RcTeam> teams = competition.getAvailableTeams();
        ItemsListField<RcTeam> teamsField = new ItemsListField<>( "Teams",
            teams, ( t ) -> t.name );
        teamsField.setDecorator( new TeamsDecorator() );

        OkDialogView okDialog = new OkDialogView( teamButton,
            teamsField.getView(), ModalityType.DOCUMENT_MODAL,
            OkDialogButtons.OK_CANCEL );

        okDialog.addOkListener( ( e ) -> {
            if( e.getItem() )
            {
                RcTeam t = teamsField.getValue();

                if( t != null )
                {
                    setTeamData( t );
                }
            }
        } );

        JDialog dialog = okDialog.getView();

        dialog.setTitle( "Choose Next Team" );
        dialog.setSize( 300, 400 );
        dialog.validate();
        dialog.setLocationRelativeTo( teamButton );
        dialog.setVisible( true );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createTimersPanel()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        panel.setOpaque( false );

        // ---------------------------------------------------------------------

        JLabel timer1Label = UiUtils.createTextLabel( "Timer 1:" );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.EAST, GridBagConstraints.NONE,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( timer1Label, constraints );

        constraints = new GridBagConstraints( 1, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets( 0, 20, 0, 0 ), 0, 0 );
        panel.add( timer1Field, constraints );

        // ---------------------------------------------------------------------

        JLabel timer2Label = UiUtils.createTextLabel( "Timer 2:" );

        constraints = new GridBagConstraints( 0, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.EAST, GridBagConstraints.NONE,
            new Insets( 10, 0, 0, 0 ), 0, 0 );
        panel.add( timer2Label, constraints );

        constraints = new GridBagConstraints( 1, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets( 10, 20, 0, 0 ), 0, 0 );
        panel.add( timer2Field, constraints );

        // ---------------------------------------------------------------------

        JLabel timer3Label = UiUtils.createTextLabel( "Timer 3:" );

        constraints = new GridBagConstraints( 0, 2, 1, 1, 0.0, 0.0,
            GridBagConstraints.EAST, GridBagConstraints.NONE,
            new Insets( 10, 0, 0, 0 ), 0, 0 );
        panel.add( timer3Label, constraints );

        constraints = new GridBagConstraints( 1, 2, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets( 10, 20, 0, 0 ), 0, 0 );
        panel.add( timer3Field, constraints );

        // ---------------------------------------------------------------------

        JLabel averageLabel = UiUtils.createTextLabel( "Average:" );

        constraints = new GridBagConstraints( 0, 3, 1, 1, 0.0, 0.0,
            GridBagConstraints.EAST, GridBagConstraints.NONE,
            new Insets( 30, 0, 0, 0 ), 0, 0 );
        panel.add( averageLabel, constraints );

        constraints = new GridBagConstraints( 1, 3, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets( 30, 20, 0, 0 ), 0, 0 );
        panel.add( averageField, constraints );

        // ---------------------------------------------------------------------

        JLabel targetLabel = UiUtils.createTextLabel( "Target:" );
        JLabel targetField = UiUtils.createNumLabel(
            competition.config.targetTime + ".0 s" );

        constraints = new GridBagConstraints( 0, 4, 1, 1, 0.0, 0.0,
            GridBagConstraints.EAST, GridBagConstraints.NONE,
            new Insets( 20, 0, 0, 0 ), 0, 0 );
        panel.add( targetLabel, constraints );

        constraints = new GridBagConstraints( 1, 4, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets( 20, 20, 0, 0 ), 0, 0 );
        panel.add( targetField, constraints );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createRunsPanel()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        panel.setOpaque( false );

        // ---------------------------------------------------------------------

        JLabel run1Label = UiUtils.createTextLabel( "Run 1:" );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.EAST, GridBagConstraints.NONE,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( run1Label, constraints );

        constraints = new GridBagConstraints( 1, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets( 0, 20, 0, 0 ), 0, 0 );
        panel.add( run1Field, constraints );

        // ---------------------------------------------------------------------

        JLabel run2Label = UiUtils.createTextLabel( "Run 1:" );

        constraints = new GridBagConstraints( 0, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.EAST, GridBagConstraints.NONE,
            new Insets( 10, 0, 0, 0 ), 0, 0 );
        panel.add( run2Label, constraints );

        constraints = new GridBagConstraints( 1, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets( 10, 20, 0, 0 ), 0, 0 );
        panel.add( run2Field, constraints );

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
            reset();

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

    /**
     * 
     */
    private void reset()
    {
        competition.reset();

        if( competition.config.teams.isEmpty() )
        {
            teamButton.setText( "No Teams Entered" );
            teamButton.setEnabled( false );
        }
        else if( competition.getAvailableTeams().isEmpty() )
        {
            teamButton.setText( "Teams Complete" );
            teamButton.setEnabled( false );
        }
        else
        {
            teamButton.setText( "Select Team" );
            teamButton.setEnabled( true );
        }
    }

    private void unloadTeam()
    {
        // TODO Auto-generated method stub
    }

    private void setTeamData( RcTeam t )
    {
        // TODO Auto-generated method stub
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

    private static final class TeamsDecorator
        implements IListCellLabelDecorator<RcTeam>
    {
        /**  */
        private final Font f = UiUtils.getTextFont( 36 );

        @Override
        public void decorate( JLabel label, JList<? extends RcTeam> list,
            RcTeam value, int index, boolean isSelected, boolean cellHasFocus )
        {
            label.setFont( f );
        }
    }
}
