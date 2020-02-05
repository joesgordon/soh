package soinc.boomilever.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog.ModalityType;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import org.jutils.OptionUtils;
import org.jutils.ui.OkDialogView;
import org.jutils.ui.OkDialogView.OkDialogButtons;
import org.jutils.ui.event.ActionAdapter;
import org.jutils.ui.event.RightClickListener;
import org.jutils.ui.fields.ItemsListField;
import org.jutils.ui.model.IView;
import org.jutils.ui.model.LabelListCellRenderer.IListCellLabelDecorator;

import soinc.boomilever.data.CompetitionData;
import soinc.boomilever.data.CompetitionState;
import soinc.boomilever.data.Team;
import soinc.boomilever.data.TimeDuration;
import soinc.boomilever.tasks.TeamCompetition;
import soinc.lib.UiUtils;

/***************************************************************************
 * 
 **************************************************************************/
public class CompetitionView implements IView<JComponent>
{
    /**  */
    private static final float LRG_FONT = 128.0f;
    /**  */
    private static final float REG_FONT = 36.0f;

    /**  */
    private final JComponent view;
    /**  */
    private final JButton teamButton;
    /**  */
    private final JLabel periodField;
    /**  */
    private final JLabel stateField;

    /**  */
    private TeamCompetition competition;
    /**  */
    private CompetitionData compData;

    /***************************************************************************
     * 
     **************************************************************************/
    public CompetitionView()
    {
        this.teamButton = new JButton( "No Teams Entered" );
        this.periodField = UiUtils.createNumLabel( "-:-- s", LRG_FONT );
        this.stateField = UiUtils.createTextLabel( "-----", REG_FONT );

        this.view = createView();
    }

    /**
     * @return
     */
    private JComponent createView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;
        int row = 0;

        // ---------------------------------------------------------------------

        constraints = new GridBagConstraints( 0, row++, 1, 1, 0.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 10, 0, 0, 0 ), 0, 0 );
        panel.add( createPeriodPanel(), constraints );

        // ---------------------------------------------------------------------

        constraints = new GridBagConstraints( 0, row++, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 10, 0, 0, 0 ), 0, 0 );
        panel.add( createUpperPanel(), constraints );

        return panel;
    }

    @Override
    public JComponent getView()
    {
        return view;
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
        teamButton.setMinimumSize( teamButton.getPreferredSize() );
        teamButton.setMaximumSize( teamButton.getPreferredSize() );
        teamButton.setPreferredSize( teamButton.getPreferredSize() );

        constraints = new GridBagConstraints( 0, 0, 2, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 0, 0, 10, 0 ), 0, 0 );
        panel.add( teamButton, constraints );

        // ---------------------------------------------------------------------

        JLabel periodLabel = UiUtils.createTextLabel( "Testing Period:",
            REG_FONT );

        constraints = new GridBagConstraints( 0, 1, 1, 1, 0.0, 1.0,
            GridBagConstraints.EAST, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( periodLabel, constraints );

        periodField.setOpaque( true );
        periodField.setBackground( Color.black );

        constraints = new GridBagConstraints( 1, 1, 1, 1, 0.0, 1.0,
            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 20, 0, 0 ), 0, 0 );
        panel.add( periodField, constraints );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createUpperPanel()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;
        int row = 0;

        panel.setOpaque( false );

        // ---------------------------------------------------------------------

        Component officialPanel = createOfficialPanel();

        constraints = new GridBagConstraints( 0, row, 1, 1, 0.5, 1.0,
            GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE,
            new Insets( 10, 20, 20, 0 ), 0, 0 );
        panel.add( officialPanel, constraints );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createOfficialPanel()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        panel.setOpaque( false );

        // ---------------------------------------------------------------------

        stateField.setText( competition.getState().name );
        stateField.setOpaque( true );
        stateField.setBackground( Color.black );

        constraints = new GridBagConstraints( 0, 1, 2, 1, 0.0, 0.0,
            GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE,
            new Insets( 10, 20, 0, 10 ), 0, 0 );
        panel.add( stateField, constraints );

        // ---------------------------------------------------------------------

        return panel;
    }

    /***************************************************************************
     * @param e
     **************************************************************************/
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
            List<Team> teams = competition.getAvailableTeams();

            if( teams.isEmpty() )
            {
                JMenuItem item = new JMenuItem( "No Teams Available" );
                item.setEnabled( false );
                menu.add( item );
            }
            else
            {
                for( Team t : teams )
                {
                    ActionListener listener = ( evt ) -> setTeamData( t );
                    Action a = new ActionAdapter( listener, t.name, null );
                    menu.add( a );
                }
            }

            menu.show( e.getComponent(), e.getX(), e.getY() );
        }
    }

    /**
     * 
     */
    private void showTeamChooser()
    {
        if( competition.isRunning() )
        {
            OptionUtils.showErrorMessage(
                getView(), "Cannot change teams until " +
                    competition.getTeam().name + " has finished",
                "Input Error" );
            return;
        }

        List<Team> teams = competition.getAvailableTeams();
        ItemsListField<Team> teamsField = new ItemsListField<>( "Teams", teams,
            ( t ) -> t.name );
        teamsField.setDecorator( new TeamsDecorator() );

        OkDialogView okDialog = new OkDialogView( teamButton,
            teamsField.getView(), ModalityType.DOCUMENT_MODAL,
            OkDialogButtons.OK_CANCEL );

        okDialog.addOkListener( ( e ) -> {
            if( e.getItem() )
            {
                Team t = teamsField.getValue();

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
     * @param data
     **************************************************************************/
    public void setData( CompetitionData data )
    {
        if( this.compData == null )
        {
            this.compData = data;
        }

        if( data.team != compData.team )
        {
            boolean enabled = data.team == null ? true : false;
            String name = data.team == null ? "Select Team" : data.team.name;

            if( data.team == null )
            {
                if( competition.getAvailableTeams().isEmpty() )
                {
                    name = "Teams Complete";
                    enabled = false;
                }
            }

            teamButton.setText( name );
            teamButton.setEnabled( enabled );
        }

        if( data.periodTime != compData.periodTime )
        {
            if( data.periodTime > -1 )
            {
                // LogUtils.printDebug( "Time %d of %d", data.periodTime,
                // competition.config.periodTime );
                long millis = competition.config.periodTime * 1000 -
                    data.periodTime;
                TimeDuration d = new TimeDuration( millis );
                String time = String.format( " %1d:%02d ", d.totalMinutes,
                    d.seconds );
                periodField.setText( time );
            }
            else
            {
                periodField.setText( " -:-- " );
            }

            if( data.state == CompetitionState.WARNING )
            {
                if( periodField.getBackground() != Color.orange )
                {
                    periodField.setBackground( Color.orange );
                    periodField.setForeground( Color.black );
                }
            }
            else if( data.state == CompetitionState.COMPLETE )
            {
                if( periodField.getBackground() != Color.red )
                {
                    periodField.setBackground( Color.red );
                    periodField.setForeground( Color.white );
                }
            }
            else if( periodField.getBackground() != Color.black )
            {
                periodField.setBackground( Color.black );
                periodField.setForeground( Color.white );
            }
        }

        if( data.state != compData.state )
        {
            stateField.setText( " " + data.state.name + " " );
            stateField.setBackground( data.state.background );
            stateField.setForeground( data.state.foreground );
        }

        this.compData = data;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public boolean isRunning()
    {
        return competition.isRunning();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void showPlayersChoices()
    {
        showTeamChooser();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void reset()
    {
        competition.signalClearTeam();

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

    /***************************************************************************
     * 
     **************************************************************************/
    private void unloadTeam()
    {
        competition.signalClearTeam();
        reset();
    }

    /***************************************************************************
     * @param team
     **************************************************************************/
    private void setTeamData( Team team )
    {
        teamButton.setText( team.name );
        teamButton.setEnabled( false );

        competition.signalLoadTeam( team );

        // TODO Auto-generated method stub
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class TeamsDecorator
        implements IListCellLabelDecorator<Team>
    {
        /**  */
        private final Font f = UiUtils.getTextFont( 36 );

        /**
         * {@inheritDoc}
         */
        @Override
        public void decorate( JLabel label, JList<? extends Team> list,
            Team value, int index, boolean isSelected, boolean cellHasFocus )
        {
            label.setFont( f );
        }
    }
}
