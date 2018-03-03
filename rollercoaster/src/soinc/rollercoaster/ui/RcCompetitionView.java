package soinc.rollercoaster.ui;

import java.awt.Color;
import java.awt.Component;
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
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.LineBorder;

import org.jutils.SwingUtils;
import org.jutils.ui.ColorIcon;
import org.jutils.ui.OkDialogView;
import org.jutils.ui.OkDialogView.OkDialogButtons;
import org.jutils.ui.event.ActionAdapter;
import org.jutils.ui.event.RightClickListener;
import org.jutils.ui.fields.ItemsListField;
import org.jutils.ui.model.IView;
import org.jutils.ui.model.LabelListCellRenderer.IListCellLabelDecorator;

import soinc.lib.SciolyIcons;
import soinc.lib.UiUtils;
import soinc.rollercoaster.RcIcons;
import soinc.rollercoaster.RcMain;
import soinc.rollercoaster.data.RcCompetitionData;
import soinc.rollercoaster.data.RcCompetitionData.RunState;
import soinc.rollercoaster.data.RcTeam;
import soinc.rollercoaster.data.TimeDuration;
import soinc.rollercoaster.tasks.RcTeamCompetition;

/*******************************************************************************
 * 
 ******************************************************************************/
public class RcCompetitionView implements IView<JFrame>
{
    /**  */
    private static final float LRG_FONT = 64.0f;
    /**  */
    private static final float REG_FONT = UiUtils.DEFAULT_FONT_SIZE;

    /**  */
    private final RcTeamCompetition competition;

    /**  */
    private final Icon blankIcon;
    /**  */
    private final Icon checkIcon;
    /**  */
    private final Icon xIcon;

    /**  */
    private final JFrame frame;
    /**  */
    private final JButton teamButton;
    /**  */
    private final JLabel periodField;
    /**  */
    private final JLabel timerAField;
    /**  */
    private final JLabel timerSField;
    /**  */
    private final JLabel timerDField;
    /**  */
    private final JLabel officialField;
    /**  */
    private final JLabel stateField;
    /**  */
    private final JLabel run1Field;
    /**  */
    private final JLabel run1Icon;
    /**  */
    private final JLabel run2Field;
    /**  */
    private final JLabel run2Icon;
    /**  */
    private final JLabel scoreIconField;

    /**  */
    private final JComponent content;

    /**  */
    private RcCompetitionData compData;

    /***************************************************************************
     * @param competition
     * @param icons
     * @param dimension
     **************************************************************************/
    public RcCompetitionView( RcTeamCompetition competition, List<Image> icons,
        Dimension size )
    {
        this.competition = competition;

        this.blankIcon = new ColorIcon( new Color( 20, 20, 20 ), 36 );
        this.checkIcon = SciolyIcons.getCheckIcon( 36 );
        this.xIcon = SciolyIcons.getXIcon( 36 );

        this.frame = new JFrame( "Roller Coaster Competition" );

        this.teamButton = new JButton( "No Teams Entered" );
        this.periodField = UiUtils.createNumLabel( "-:--", LRG_FONT );
        this.timerAField = UiUtils.createNumLabel( "--.- s", REG_FONT );
        this.timerSField = UiUtils.createNumLabel( "--.- s", REG_FONT );
        this.timerDField = UiUtils.createNumLabel( "--.- s", REG_FONT );
        this.officialField = UiUtils.createNumLabel( "--.- s", REG_FONT );
        this.stateField = UiUtils.createTextLabel( "-----", REG_FONT );
        this.run1Field = UiUtils.createNumLabel( "--.- s", REG_FONT );
        this.run1Icon = new JLabel( blankIcon );
        this.run2Field = UiUtils.createNumLabel( "--.- s", REG_FONT );
        this.run2Icon = new JLabel( blankIcon );
        this.scoreIconField = new JLabel();

        run1Icon.setPreferredSize( new Dimension( 38, 38 ) );
        run1Icon.setMinimumSize( new Dimension( 38, 38 ) );
        run1Icon.setMaximumSize( new Dimension( 38, 38 ) );

        run2Icon.setPreferredSize( new Dimension( 38, 38 ) );
        run2Icon.setMinimumSize( new Dimension( 38, 38 ) );
        run2Icon.setMaximumSize( new Dimension( 38, 38 ) );

        this.content = createCompetitionPanel();

        frame.setIconImages( icons );
        frame.setContentPane( content );
        frame.setDefaultCloseOperation( JDialog.DO_NOTHING_ON_CLOSE );
        frame.addWindowListener( new CompetitionFrameListener( this ) );
        frame.setUndecorated( true );
        frame.setSize( size );
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
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( createBannerPanel(), constraints );

        // ---------------------------------------------------------------------

        constraints = new GridBagConstraints( 0, row++, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 10, 0, 0, 0 ), 0, 0 );
        panel.add( createPeriodPanel(), constraints );

        // ---------------------------------------------------------------------

        constraints = new GridBagConstraints( 0, row++, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 10, 0, 0, 0 ), 0, 0 );
        panel.add( createUpperPanel(), constraints );

        // ---------------------------------------------------------------------

        constraints = new GridBagConstraints( 0, row++, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 30, 0 ), 0, 0 );
        panel.add( createBottomPanel(), constraints );

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

        Component timersPanel = createTimersPanel();
        Component runsPanel = createRunsPanel();

        constraints = new GridBagConstraints( 0, row, 1, 1, 0.5, 0.0,
            GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL,
            new Insets( 10, 20, 10, 40 ), 0, 0 );
        panel.add( timersPanel, constraints );

        constraints = new GridBagConstraints( 1, row++, 1, 1, 0.5, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
            new Insets( 10, 40, 10, 20 ), 0, 0 );
        panel.add( runsPanel, constraints );

        SwingUtils.setMaxComponentSize( timersPanel, runsPanel );

        // ---------------------------------------------------------------------

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createBottomPanel()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;
        int row = 0;

        panel.setOpaque( false );

        // ---------------------------------------------------------------------

        Component officialPanel = createOfficialPanel();
        Component scorePanel = createScorePanel();

        constraints = new GridBagConstraints( 0, row, 1, 1, 0.5, 1.0,
            GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( officialPanel, constraints );

        constraints = new GridBagConstraints( 1, row++, 1, 1, 0.5, 1.0,
            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( scorePanel, constraints );

        SwingUtils.setMaxComponentSize( officialPanel, scorePanel );

        // ---------------------------------------------------------------------

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private static Component createBannerPanel()
    {
        Icon bannerIcon = RcIcons.getBannerImage();
        JLabel soLabel = new JLabel( bannerIcon );

        return soLabel;
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
            GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( periodLabel, constraints );

        constraints = new GridBagConstraints( 1, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE,
            new Insets( 0, 20, 0, 0 ), 0, 0 );
        panel.add( periodField, constraints );

        // ---------------------------------------------------------------------

        JLabel targetLabel = UiUtils.createTextLabel( "Target:", REG_FONT );
        JLabel targetField = UiUtils.createNumLabel(
            competition.config.getTargetTime() + ".0 s", REG_FONT );

        constraints = new GridBagConstraints( 0, 2, 1, 1, 0.0, 0.0,
            GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE,
            new Insets( 10, 0, 0, 0 ), 0, 0 );
        panel.add( targetLabel, constraints );

        constraints = new GridBagConstraints( 1, 2, 1, 1, 0.0, 0.0,
            GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE,
            new Insets( 10, 20, 0, 0 ), 0, 0 );
        panel.add( targetField, constraints );

        return panel;
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

        JLabel timer1Label = UiUtils.createTextLabel( "Timer A:", REG_FONT );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( timer1Label, constraints );

        constraints = new GridBagConstraints( 1, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE,
            new Insets( 0, 20, 0, 0 ), 0, 0 );
        panel.add( timerAField, constraints );

        // ---------------------------------------------------------------------

        JLabel timer2Label = UiUtils.createTextLabel( "Timer S:", REG_FONT );

        constraints = new GridBagConstraints( 0, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE,
            new Insets( 10, 0, 0, 0 ), 0, 0 );
        panel.add( timer2Label, constraints );

        constraints = new GridBagConstraints( 1, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE,
            new Insets( 10, 20, 0, 0 ), 0, 0 );
        panel.add( timerSField, constraints );

        // ---------------------------------------------------------------------

        JLabel timer3Label = UiUtils.createTextLabel( "Timer D:", REG_FONT );

        constraints = new GridBagConstraints( 0, 2, 1, 1, 0.0, 0.0,
            GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE,
            new Insets( 10, 0, 0, 0 ), 0, 0 );
        panel.add( timer3Label, constraints );

        constraints = new GridBagConstraints( 1, 2, 1, 1, 0.0, 0.0,
            GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE,
            new Insets( 10, 20, 0, 0 ), 0, 0 );
        panel.add( timerDField, constraints );

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
            new Insets( 0, 20, 0, 0 ), 0, 0 );
        panel.add( run1Field, constraints );

        constraints = new GridBagConstraints( 2, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets( 0, 20, 0, 10 ), 0, 0 );
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
        panel.add( run2Field, constraints );

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

        constraints = new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE,
            new Insets( 40, 0, 0, 0 ), 0, 0 );
        panel.add( officialLabel, constraints );

        constraints = new GridBagConstraints( 1, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE,
            new Insets( 30, 20, 0, 0 ), 0, 0 );
        panel.add( officialField, constraints );

        // ---------------------------------------------------------------------

        stateField.setText( competition.getState().name );
        stateField.setOpaque( true );
        stateField.setBackground( Color.black );

        constraints = new GridBagConstraints( 0, 1, 2, 1, 0.0, 0.0,
            GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE,
            new Insets( 10, 20, 0, 0 ), 0, 0 );
        panel.add( stateField, constraints );

        // ---------------------------------------------------------------------

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createScorePanel()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        panel.setOpaque( false );

        // ---------------------------------------------------------------------

        scoreIconField.setIcon( competition.getState().icon );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets( 10, 0, 0, 0 ), 0, 0 );
        panel.add( scoreIconField, constraints );

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

            if( teams.isEmpty() )
            {
                JMenuItem item = new JMenuItem( "No Teams Available" );
                item.setEnabled( false );
                menu.add( item );
            }
            else
            {
                for( RcTeam t : teams )
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
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JFrame getView()
    {
        return frame;
    }

    public JComponent getContent()
    {
        return content;
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

    /***************************************************************************
     * @param data
     **************************************************************************/
    public void setData( RcCompetitionData data )
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
                TimeDuration d = new TimeDuration( data.periodTime );
                String time = String.format( "%1d:%02d", d.totalMinutes,
                    d.seconds );
                periodField.setText( time );
            }
            else
            {
                periodField.setText( "-:--" );
            }
        }

        setTimer( data, timerAField, 0 );
        setTimer( data, timerSField, 1 );
        setTimer( data, timerDField, 2 );

        if( data.officialTime != compData.officialTime )
        {
            setTimeField( data.officialTime, officialField );
        }

        if( data.state != compData.state )
        {
            stateField.setText( data.state.name );
            stateField.setBackground( data.state.background );
            scoreIconField.setIcon( data.state.icon );
        }

        if( data.run1Time != compData.run1Time )
        {
            setTimeField( data.run1Time, run1Field );
        }

        if( data.run2Time != compData.run2Time )
        {
            setTimeField( data.run2Time, run2Field );
        }

        if( data.run1State != compData.run1State )
        {
            setFieldIcon( data.run1State, run1Icon );
        }

        if( data.run2State != compData.run2State )
        {
            setFieldIcon( data.run2State, run2Icon );
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
     * @param data
     * @param timerField
     * @param index
     **************************************************************************/
    private void setTimer( RcCompetitionData data, JLabel timerField,
        int index )
    {
        if( data.timers[index] != compData.timers[index] )
        {
            setTimeField( data.timers[index], timerField );
        }
    }

    private void setTimeField( long duration, JLabel timeField )
    {
        String time = null;

        if( duration < 0 )
        {
            time = "--.- s";
        }
        else
        {
            TimeDuration d = new TimeDuration( duration );
            time = String.format( "%02d.%1d s", d.totalSeconds,
                d.millis / 100 );
        }

        timeField.setText( time );
    }

    /**
     * 
     */
    private void reset()
    {
        competition.signalClearTeam();

        if( competition.config.getTeams().isEmpty() )
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
        competition.signalClearTeam();
        reset();
    }

    private void setTeamData( RcTeam team )
    {
        teamButton.setText( team.name );
        teamButton.setEnabled( false );

        competition.signalLoadTeam( team );

        // TODO Auto-generated method stub
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class CompetitionFrameListener extends WindowAdapter
    {
        /**  */
        private final RcCompetitionView view;

        /**
         * @param view
         */
        public CompetitionFrameListener( RcCompetitionView view )
        {
            this.view = view;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void windowClosing( WindowEvent e )
        {
            if( !view.competition.isRunning() )
            {
                RcMain.getOptions().write();
                UiUtils.setFullScreen( false, view.frame );
                view.frame.setVisible( false );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
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
