package soinc.ppp.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.Action;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.LineBorder;

import org.jutils.OptionUtils;
import org.jutils.ui.ColorIcon;
import org.jutils.ui.LedLabel;
import org.jutils.ui.OkDialogView;
import org.jutils.ui.OkDialogView.OkDialogButtons;
import org.jutils.ui.event.ActionAdapter;
import org.jutils.ui.event.RightClickListener;
import org.jutils.ui.fields.ItemsListField;
import org.jutils.ui.model.IView;
import org.jutils.ui.model.LabelListCellRenderer.IListCellLabelDecorator;

import soinc.lib.SciolyIcons;
import soinc.lib.UiUtils;
import soinc.lib.ui.MinSecLabel;
import soinc.lib.ui.SecondsLabel;
import soinc.ppp.data.EventConfig;
import soinc.ppp.data.Team;
import soinc.ppp.data.TrackData;
import soinc.ppp.data.TrackData.RunState;
import soinc.ppp.tasks.Track;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TrackView implements IView<JComponent>
{
    /**  */
    public static final float LRG_FONT = 64.0f;
    /**  */
    public static final float REG_FONT = UiUtils.DEFAULT_FONT_SIZE;

    /**  */
    private final JComponent view;

    /**  */
    private final Icon blankIcon;
    /**  */
    private final Icon checkIcon;
    /**  */
    private final Icon xIcon;

    /**  */
    private final JButton teamButton;
    /**  */
    private final MinSecLabel periodField;
    /**  */
    private final SecondsLabel officialField;
    /**  */
    private final JLabel stateField;
    /**  */
    private final SecondsLabel run1Field;
    /**  */
    private final JLabel run1Icon;
    /**  */
    private final SecondsLabel run2Field;
    /**  */
    private final JLabel run2Icon;
    /**  */
    private final LedLabel selectedLabel;

    /**  */
    private final EventConfig eventCfg;
    /**  */
    private Track track;

    /***************************************************************************
     * @param eventCfg
     * @param track
     **************************************************************************/
    public TrackView( EventConfig eventCfg, Track track )
    {
        this.eventCfg = eventCfg;
        this.track = track;

        this.blankIcon = new ColorIcon( new Color( 20, 20, 20 ), 36 );
        this.checkIcon = SciolyIcons.getCheckIcon( 36 );
        this.xIcon = SciolyIcons.getXIcon( 36 );

        this.teamButton = new JButton( "No Teams Entered" );
        this.periodField = new MinSecLabel( "-:-- s", LRG_FONT );
        this.officialField = new SecondsLabel( " --.-- s ", REG_FONT );
        this.stateField = UiUtils.createTextLabel( "-----", REG_FONT );
        this.run1Field = new SecondsLabel( " --.-- s ", REG_FONT );
        this.run1Icon = new JLabel( blankIcon );
        this.run2Field = new SecondsLabel( " --.-- s ", REG_FONT );
        this.run2Icon = new JLabel( blankIcon );
        this.selectedLabel = new LedLabel( Color.green, Color.red, 24,
            "Clear to Fire" );

        run1Icon.setPreferredSize( new Dimension( 38, 38 ) );
        run1Icon.setMinimumSize( new Dimension( 38, 38 ) );
        run1Icon.setMaximumSize( new Dimension( 38, 38 ) );

        run2Icon.setPreferredSize( new Dimension( 38, 38 ) );
        run2Icon.setMinimumSize( new Dimension( 38, 38 ) );
        run2Icon.setMaximumSize( new Dimension( 38, 38 ) );

        this.view = createCompetitionPanel();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createCompetitionPanel()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;
        int row = 0;

        // panel.setBackground( new Color( 10, 10, 10 ) );
        panel.setBackground( Color.black );
        // panel.setOpaque( false );

        // ---------------------------------------------------------------------

        constraints = new GridBagConstraints( 0, row++, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 10, 0, 0, 0 ), 0, 0 );
        panel.add( createPeriodPanel(), constraints );

        // ---------------------------------------------------------------------

        constraints = new GridBagConstraints( 0, row++, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 10, 0, 0, 0 ), 0, 0 );
        panel.add( createUpperPanel(), constraints );

        // ---------------------------------------------------------------------

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

        Component runsPanel = createRunsPanel();

        constraints = new GridBagConstraints( 0, row++, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 10, 20, 10, 20 ), 0, 0 );
        panel.add( runsPanel, constraints );

        // ---------------------------------------------------------------------

        Component officialPanel = createOfficialPanel();

        constraints = new GridBagConstraints( 0, row++, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 10, 20, 10, 0 ), 0, 0 );
        panel.add( officialPanel, constraints );

        // ---------------------------------------------------------------------

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

        constraints = new GridBagConstraints( 0, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.EAST, GridBagConstraints.NONE,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( periodLabel, constraints );

        constraints = new GridBagConstraints( 1, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE,
            new Insets( 0, 20, 0, 0 ), 0, 0 );
        panel.add( periodField.view, constraints );

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

        panel.setBorder( new LineBorder( Color.white, 2 ) );

        // ---------------------------------------------------------------------

        JLabel run1Label = UiUtils.createTextLabel( "Run 1:", REG_FONT );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.EAST, GridBagConstraints.NONE,
            new Insets( 10, 10, 0, 0 ), 0, 0 );
        panel.add( run1Label, constraints );

        constraints = new GridBagConstraints( 1, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets( 10, 20, 0, 0 ), 0, 0 );
        panel.add( run1Field.view, constraints );

        constraints = new GridBagConstraints( 2, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets( 10, 20, 0, 10 ), 0, 0 );
        panel.add( run1Icon, constraints );

        // ---------------------------------------------------------------------

        JLabel run2Label = UiUtils.createTextLabel( "Run 2:", REG_FONT );

        constraints = new GridBagConstraints( 0, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.EAST, GridBagConstraints.NONE,
            new Insets( 10, 0, 0, 0 ), 0, 0 );
        panel.add( run2Label, constraints );

        constraints = new GridBagConstraints( 1, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets( 10, 20, 0, 0 ), 0, 0 );
        panel.add( run2Field.view, constraints );

        constraints = new GridBagConstraints( 2, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets( 10, 20, 10, 10 ), 0, 0 );
        panel.add( run2Icon, constraints );

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

        JLabel officialLabel = UiUtils.createTextLabel( "Official Time:",
            REG_FONT );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 0.5, 0.0,
            GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE,
            new Insets( 40, 20, 0, 10 ), 0, 0 );
        panel.add( officialLabel, constraints );

        constraints = new GridBagConstraints( 1, 0, 1, 1, 0.5, 0.0,
            GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE,
            new Insets( 30, 10, 0, 20 ), 0, 0 );
        panel.add( officialField.view, constraints );

        // ---------------------------------------------------------------------

        constraints = new GridBagConstraints( 0, 1, 2, 1, 1.0, 1.0,
            GridBagConstraints.SOUTHWEST, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( Box.createHorizontalStrut( 1 ), constraints );

        // ---------------------------------------------------------------------

        selectedLabel.getView().setForeground( Color.white );
        ( ( JLabel )selectedLabel.getView() ).setFont(
            officialLabel.getFont().deriveFont( 18.0f ) );

        constraints = new GridBagConstraints( 0, 2, 1, 1, 0.0, 0.0,
            GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE,
            new Insets( 10, 20, 10, 20 ), 0, 0 );
        panel.add( selectedLabel.getView(), constraints );

        stateField.setText( track.getState().name );
        stateField.setOpaque( true );
        stateField.setBackground( Color.black );

        constraints = new GridBagConstraints( 1, 2, 1, 1, 0.0, 0.0,
            GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE,
            new Insets( 10, 20, 10, 20 ), 0, 0 );
        panel.add( stateField, constraints );

        // ---------------------------------------------------------------------

        return panel;
    }

    /***************************************************************************
     * @param e
     **************************************************************************/
    private void showTeamPopup( MouseEvent e )
    {
        if( track.isRunning() )
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
            List<Team> teams = track.getAvailableTeams();

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

    /***************************************************************************
     * 
     **************************************************************************/
    public void showTeamChooser()
    {
        if( track.isRunning() )
        {
            OptionUtils.showErrorMessage( getView(),
                "Cannot change teams until " + track.getTeam().name +
                    " has finished",
                "Input Error" );
            return;
        }

        List<Team> teams = track.getAvailableTeams();
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
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return view;
    }

    /***************************************************************************
     * @param track
     **************************************************************************/
    public void setData( Track track )
    {
        this.track = track;

        TrackData data = track.data;

        boolean enabled = data.team == null ? true : false;
        String name = data.team == null ? "Select Team" : data.team.name;

        if( data.team == null )
        {
            if( track.getAvailableTeams().isEmpty() )
            {
                name = "Teams Complete";
                enabled = false;
            }
        }

        teamButton.setText( name );
        teamButton.setEnabled( enabled );

        if( data.periodTime > -1 )
        {
            long curTime = data.periodTime;
            long time = eventCfg.periodTime * 1000 - curTime;
            periodField.setTime( time );
        }
        else
        {
            periodField.reset();
        }

        if( data.periodTime > track.config.periodTime * 7000 / 8 &&
            data.periodTime > track.config.periodTime )
        {
            if( periodField.view.getBackground() != Color.orange )
            {
                periodField.view.setBackground( Color.orange );
                periodField.view.setForeground( Color.black );
            }
        }
        else if( data.periodTime > track.config.periodTime * 1000 )
        {
            if( periodField.view.getBackground() != Color.red )
            {
                periodField.view.setBackground( Color.red );
                periodField.view.setForeground( Color.white );
            }
        }
        else if( periodField.view.getBackground() != Color.black )
        {
            periodField.view.setBackground( Color.black );
            periodField.view.setForeground( Color.white );
        }

        stateField.setText( " " + data.state.name + " " );
        stateField.setBackground( data.state.background );
        stateField.setForeground( data.state.foreground );

        if( data.isRunning() )
        {
            officialField.setTime( data.officialTime );
        }
        else
        {
            officialField.reset();
        }

        if( data.run1State.isComplete )
        {
            run1Field.setTime( data.run1Time );
        }
        else
        {
            run1Field.reset();
        }

        if( data.run2State.isComplete )
        {
            run2Field.setTime( data.run2Time );
        }
        else
        {
            run2Field.reset();
        }

        setFieldIcon( data.run1State, run1Icon );
        setFieldIcon( data.run2State, run2Icon );

        // selectedLabel.setStatus( track.isSelected,
        // track.isSelected ? "Clear to Fire" : "Fire not Granted" );

        selectedLabel.setLit( track.isSelected );
        selectedLabel.getView().setVisible( track.isSelected );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public boolean isRunning()
    {
        return track.isRunning();
    }

    /***************************************************************************
     * @param state
     * @param iconField
     **************************************************************************/
    private void setFieldIcon( RunState state, JLabel iconField )
    {
        Icon icon = null;

        switch( state )
        {
            case FAILED:
                icon = xIcon;
                break;

            case SUCCESS:
                icon = checkIcon;
                break;

            default:
                icon = blankIcon;
                break;
        }

        iconField.setIcon( icon );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void reset()
    {
        track.signalClearTeam();

        if( track.config.teams.isEmpty() )
        {
            teamButton.setText( "No Teams Entered" );
            teamButton.setEnabled( false );
        }
        else if( track.getAvailableTeams().isEmpty() )
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
        track.signalClearTeam();
        reset();
    }

    /***************************************************************************
     * @param team
     **************************************************************************/
    private void setTeamData( Team team )
    {
        teamButton.setText( team.name );
        teamButton.setEnabled( false );

        track.signalLoadTeam( team );
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
