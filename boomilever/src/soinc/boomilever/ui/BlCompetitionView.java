package soinc.boomilever.ui;

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

import org.jutils.OptionUtils;
import org.jutils.ui.OkDialogView;
import org.jutils.ui.OkDialogView.OkDialogButtons;
import org.jutils.ui.event.ActionAdapter;
import org.jutils.ui.event.RightClickListener;
import org.jutils.ui.fields.ItemsListField;
import org.jutils.ui.model.IView;
import org.jutils.ui.model.LabelListCellRenderer.IListCellLabelDecorator;

import soinc.boomilever.BlIcons;
import soinc.boomilever.BlMain;
import soinc.boomilever.data.BlCompetitionData;
import soinc.boomilever.data.BlTeam;
import soinc.boomilever.data.CompetitionState;
import soinc.boomilever.data.TimeDuration;
import soinc.boomilever.tasks.BlTeamCompetition;
import soinc.lib.UiUtils;

/*******************************************************************************
 * 
 ******************************************************************************/
public class BlCompetitionView implements IView<JFrame>
{
    /**  */
    private static final float LRG_FONT = 128.0f;
    /**  */
    private static final float REG_FONT = 36.0f;

    /**  */
    private final BlTeamCompetition competition;

    /**  */
    private final JFrame frame;
    /**  */
    private final JButton teamButton;
    /**  */
    private final JLabel periodField;
    /**  */
    private final JLabel stateField;

    /**  */
    private final JComponent content;

    /**  */
    private BlCompetitionData compData;

    /***************************************************************************
     * @param competition
     * @param icons
     * @param size
     **************************************************************************/
    public BlCompetitionView( BlTeamCompetition competition, List<Image> icons,
        Dimension size )
    {
        this.competition = competition;

        this.frame = new JFrame( "Roller Coaster Competition" );

        this.teamButton = new JButton( "No Teams Entered" );
        this.periodField = UiUtils.createNumLabel( "-:-- s", LRG_FONT );
        this.stateField = UiUtils.createTextLabel( "-----", REG_FONT );

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

        constraints = new GridBagConstraints( 0, row++, 1, 1, 0.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 10, 0, 0, 0 ), 0, 0 );
        panel.add( createPeriodPanel(), constraints );

        // ---------------------------------------------------------------------

        constraints = new GridBagConstraints( 0, row++, 1, 1, 1.0, 0.0,
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
    private static Component createBannerPanel()
    {
        Icon bannerIcon = BlIcons.getBannerImage();
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
            List<BlTeam> teams = competition.getAvailableTeams();

            if( teams.isEmpty() )
            {
                JMenuItem item = new JMenuItem( "No Teams Available" );
                item.setEnabled( false );
                menu.add( item );
            }
            else
            {
                for( BlTeam t : teams )
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

        List<BlTeam> teams = competition.getAvailableTeams();
        ItemsListField<BlTeam> teamsField = new ItemsListField<>( "Teams",
            teams, ( t ) -> t.name );
        teamsField.setDecorator( new TeamsDecorator() );

        OkDialogView okDialog = new OkDialogView( teamButton,
            teamsField.getView(), ModalityType.DOCUMENT_MODAL,
            OkDialogButtons.OK_CANCEL );

        okDialog.addOkListener( ( e ) -> {
            if( e.getItem() )
            {
                BlTeam t = teamsField.getValue();

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
    public void setData( BlCompetitionData data )
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

    /**
     * 
     */
    private void reset()
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

    private void unloadTeam()
    {
        competition.signalClearTeam();
        reset();
    }

    private void setTeamData( BlTeam team )
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
        private final BlCompetitionView view;

        /**
         * @param view
         */
        public CompetitionFrameListener( BlCompetitionView view )
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
                BlMain.getOptions().write();
                UiUtils.setFullScreen( false, view.frame );
                view.frame.setVisible( false );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class TeamsDecorator
        implements IListCellLabelDecorator<BlTeam>
    {
        /**  */
        private final Font f = UiUtils.getTextFont( 36 );

        @Override
        public void decorate( JLabel label, JList<? extends BlTeam> list,
            BlTeam value, int index, boolean isSelected, boolean cellHasFocus )
        {
            label.setFont( f );
        }
    }
}
