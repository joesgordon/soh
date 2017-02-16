package soh.ui;

import java.awt.*;
import java.awt.Dialog.ModalityType;

import javax.swing.*;
import javax.swing.border.LineBorder;

import org.jutils.SwingUtils;
import org.jutils.ui.ColorIcon;
import org.jutils.ui.OkDialogView;
import org.jutils.ui.OkDialogView.OkDialogButtons;
import org.jutils.ui.model.IView;

import soh.SohIcons;
import soh.TrackCompetition;
import soh.data.*;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TrackView implements IView<JComponent>
{
    /**  */
    private static final Color WARNING_COLOR = new Color( 233, 153, 33 );
    /**  */
    private static final Color ERROR_COLOR = Color.RED;

    /**  */
    private final JPanel view;

    /**  */
    private final JButton teamButton;
    /**  */
    private final JLabel targetTimeField;
    /**  */
    private final JLabel targetLengthField;

    /**  */
    private final JLabel periodField;
    /**  */
    private final JLabel run1Field;
    /**  */
    private final JLabel run1Icon;
    /**  */
    private final JLabel run2Field;
    /**  */
    private final JLabel run2Icon;

    /**  */
    private final JLabel [] failedFields;

    /**  */
    private final JLabel finishedField;

    /**  */
    private final Icon blankIcon;
    /**  */
    private final Icon checkIcon;
    /**  */
    private final Icon xIcon;

    /**  */
    private TrackCompetition competition;
    /**  */
    private HoverConfig config;

    /***************************************************************************
     * @param trackName
     **************************************************************************/
    public TrackView( String trackName )
    {
        this.teamButton = new JButton( "Welcome Olympians" );
        this.targetTimeField = UiUtils.createNumLabel( "--.- s", 36 );
        this.targetLengthField = UiUtils.createNumLabel( "---.- cm", 36 );

        this.blankIcon = new ColorIcon( new Color( 10, 10, 10 ), 48 );
        this.checkIcon = SohIcons.getCheckIcon( 48 );
        this.xIcon = SohIcons.getXIcon( 48 );

        this.periodField = UiUtils.createNumLabel( "-:--", 124 );
        this.run1Field = UiUtils.createNumLabel( "--.- s" );
        this.run1Icon = new JLabel( blankIcon );
        this.run2Field = UiUtils.createNumLabel( "--.- s" );
        this.run2Icon = new JLabel( blankIcon );
        this.failedFields = new JLabel[5];
        this.finishedField = UiUtils.createTextLabel( " ", 36 );

        for( int i = 0; i < failedFields.length; i++ )
        {
            failedFields[i] = new JLabel( blankIcon );
        }

        teamButton.setName( trackName );

        this.view = createView();

        clearTeam();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public String getTrackName()
    {
        return teamButton.getName();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void clearTeam()
    {
        competition = null;

        teamButton.setText( "Select Team" );
        targetTimeField.setText( " --.- s" );
        targetLengthField.setText( "---.- s" );
        setPeriodTime( -1 );
        setRunaTime( -1, false );
        setRunbTime( -1, false );

        for( int i = 0; i < failedFields.length; i++ )
        {
            failedFields[i].setIcon( blankIcon );
            failedFields[i].invalidate();
            failedFields[i].repaint();
            failedFields[i].getParent().invalidate();
            failedFields[i].getParent().repaint();
        }

        setFinished( false );
    }

    /***************************************************************************
     * @param team
     **************************************************************************/
    private void setTeamData( Team team )
    {
        if( team == null )
        {
            clearTeam();
        }
        else
        {
            teamButton.setText( team.schoolCode );
            // + " (Division " + team.div.name + ")" );

            DivisionConfig dcv = config.getDivision( team.div );

            targetTimeField.setText(
                String.format( "%5.1f s", dcv.targetTime / 10.0 ) );
            targetLengthField.setText(
                String.format( "%5.1f cm   ", dcv.targetLength * 1.0 ) );

            initializeTrack( team );
        }
    }

    /***************************************************************************
     * @param seconds
     **************************************************************************/
    public void setPeriodTime( int seconds )
    {
        if( seconds < 0 )
        {
            periodField.setText( " -:-- " );
            periodField.setOpaque( false );
        }
        else
        {
            int minutes = seconds / 60;
            int sec = seconds % 60;

            periodField.setText( String.format( " %1d:%02d ", minutes, sec ) );

            if( seconds < 61 && seconds > 0 )
            {
                periodField.setOpaque( true );
                periodField.setBackground( WARNING_COLOR );
            }
            else if( seconds < 1 )
            {
                periodField.setOpaque( true );
                periodField.setBackground( ERROR_COLOR );
            }
            else
            {
                periodField.setOpaque( false );
            }
        }
    }

    /***************************************************************************
     * @param tenthsSeconds
     * @param complete
     **************************************************************************/
    public void setRunaTime( int tenthsSeconds, boolean complete )
    {
        setRunTime( run1Field, run1Icon, tenthsSeconds, complete );
    }

    /***************************************************************************
     * @param tenthsSeconds
     * @param complete
     **************************************************************************/
    public void setRunbTime( int tenthsSeconds, boolean complete )
    {
        setRunTime( run2Field, run2Icon, tenthsSeconds, complete );
    }

    /***************************************************************************
     * @param finished
     **************************************************************************/
    public void setFinished( boolean finished )
    {
        String text = finished ? "Team Complete" : "";

        finishedField.setText( text );
    }

    /***************************************************************************
     * @param runField
     * @param runIcon
     * @param tenthsSeconds
     * @param complete
     **************************************************************************/
    private void setRunTime( JLabel runField, JLabel runIcon, int tenthsSeconds,
        boolean complete )
    {
        if( tenthsSeconds < 0 )
        {
            runField.setText( "--.- s" );
            runIcon.setIcon( blankIcon );
        }
        else
        {
            int sec = tenthsSeconds / 10;
            int f = tenthsSeconds % 10;

            runField.setText( String.format( "%02d.%1d s", sec, f ) );
            if( complete )
            {
                runIcon.setIcon( checkIcon );
            }
        }
    }

    /***************************************************************************
     * @param cnt
     **************************************************************************/
    public void setFailCount( int cnt )
    {
        for( int i = 0; i < cnt; i++ )
        {
            failedFields[i].setIcon( xIcon );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void showTeamChooser()
    {
        if( competition != null &&
            competition.getState() != TrackState.FINISHED )
        {
            SwingUtils.showErrorMessage(
                getView(), "Cannot change teams until " +
                    competition.team.schoolCode + " has finished",
                "Input Error" );
            return;
        }

        clearTeam();

        TeamsView teamsView = new TeamsView( config );
        OkDialogView okDialog = new OkDialogView( teamButton,
            teamsView.getView(), ModalityType.MODELESS,
            OkDialogButtons.OK_CANCEL );

        okDialog.addOkListener( ( e ) -> {
            if( e.getItem() )
            {
                Team t = teamsView.getSelected();

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
    private JPanel createView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        JLabel label;
        GridBagConstraints constraints;
        int row = 0;

        panel.setOpaque( false );

        // ---------------------------------------------------------------------

        constraints = new GridBagConstraints( 0, row++, 2, 1, 1.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( createTeamPanel(), constraints );

        // ---------------------------------------------------------------------

        label = UiUtils.createTextLabel( "Time Trials",
            UiUtils.SMALL_FONT_SIZE );
        constraints = new GridBagConstraints( 0, row++, 2, 1, 1.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( label, constraints );

        // ---------------------------------------------------------------------

        constraints = new GridBagConstraints( 0, row++, 2, 1, 1.0, 1.0,
            GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( createTimingPanel(), constraints );

        // ---------------------------------------------------------------------

        finishedField.setForeground( UiUtils.UNI_MAIN_COLOR );

        constraints = new GridBagConstraints( 0, row++, 2, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( finishedField, constraints );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createTeamPanel()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;
        JLabel label;
        int row = 0;

        panel.setOpaque( false );

        teamButton.setFont( UiUtils.getTextFont() );
        teamButton.setBorder( new LineBorder( Color.lightGray, 2 ) );
        teamButton.setOpaque( false );
        teamButton.setForeground( Color.white );
        teamButton.addActionListener( ( e ) -> showTeamChooser() );
        teamButton.setBorderPainted( false );

        // ---------------------------------------------------------------------

        label = UiUtils.createTextLabel( teamButton.getName() + ":" );

        constraints = new GridBagConstraints( 0, row, 1, 1, 1.0, 0.0,
            GridBagConstraints.EAST, GridBagConstraints.NONE,
            new Insets( 4, 4, 4, 4 ), 0, 0 );
        panel.add( label, constraints );

        constraints = new GridBagConstraints( 1, row++, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 4, 0, 4, 4 ), 0, 0 );
        panel.add( teamButton, constraints );

        // ---------------------------------------------------------------------

        constraints = new GridBagConstraints( 0, row, 1, 1, 1.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( createDivisionPanel(), constraints );

        constraints = new GridBagConstraints( 1, row++, 1, 2, 1.0, 0.0,
            GridBagConstraints.SOUTH, GridBagConstraints.NONE,
            new Insets( 4, 0, 0, 4 ), 0, 0 );
        panel.add( periodField, constraints );

        // ---------------------------------------------------------------------

        label = UiUtils.createTextLabel( "Testing Period:",
            UiUtils.SMALL_FONT_SIZE );
        constraints = new GridBagConstraints( 0, row, 1, 1, 1.0, 0.0,
            GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE,
            new Insets( 4, 4, 8, 4 ), 0, 0 );
        panel.add( label, constraints );

        // ---------------------------------------------------------------------

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createDivisionPanel()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;
        JLabel label;
        int row = 0;

        panel.setOpaque( false );

        // ---------------------------------------------------------------------
        label = UiUtils.createTextLabel( "Target Time",
            UiUtils.SMALL_FONT_SIZE );

        constraints = new GridBagConstraints( 0, row, 1, 1, 1.0, 0.0,
            GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE,
            new Insets( 4, 4, 8, 0 ), 0, 0 );
        panel.add( label, constraints );

        label = UiUtils.createTextLabel( ":", UiUtils.SMALL_FONT_SIZE );

        constraints = new GridBagConstraints( 1, row, 1, 1, 1.0, 0.0,
            GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE,
            new Insets( 4, 0, 8, 4 ), 0, 0 );
        panel.add( label, constraints );

        constraints = new GridBagConstraints( 2, row++, 1, 1, 1.0, 0.0,
            GridBagConstraints.SOUTHWEST, GridBagConstraints.HORIZONTAL,
            new Insets( 4, 0, 0, 4 ), 0, 0 );
        panel.add( targetTimeField, constraints );

        // ---------------------------------------------------------------------
        label = UiUtils.createTextLabel( "Target Length",
            UiUtils.SMALL_FONT_SIZE );

        constraints = new GridBagConstraints( 0, row, 1, 1, 1.0, 0.0,
            GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE,
            new Insets( 4, 4, 8, 0 ), 0, 0 );
        panel.add( label, constraints );

        label = UiUtils.createTextLabel( ":", UiUtils.SMALL_FONT_SIZE );

        constraints = new GridBagConstraints( 1, row, 1, 1, 1.0, 0.0,
            GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE,
            new Insets( 4, 0, 8, 4 ), 0, 0 );
        panel.add( label, constraints );

        constraints = new GridBagConstraints( 2, row++, 1, 1, 1.0, 0.0,
            GridBagConstraints.SOUTHWEST, GridBagConstraints.HORIZONTAL,
            new Insets( 4, 0, 0, 4 ), 0, 0 );
        panel.add( targetLengthField, constraints );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createTimingPanel()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;
        JLabel label;
        int row = 0;

        panel.setOpaque( false );
        panel.setBorder( new LineBorder( Color.white, 2 ) );

        // ---------------------------------------------------------------------

        label = UiUtils.createTextLabel( "Successful Attempts", 36 );
        constraints = new GridBagConstraints( 0, row++, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 4, 4, 4, 0 ), 0, 0 );
        panel.add( label, constraints );

        // ---------------------------------------------------------------------
        constraints = new GridBagConstraints( 0, row++, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 20, 4, 4, 4 ), 0, 0 );
        panel.add( createRunPanel(), constraints );

        // ---------------------------------------------------------------------
        label = UiUtils.createTextLabel( "Failed Attempts", 36 );
        constraints = new GridBagConstraints( 0, row++, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 20, 4, 4, 4 ), 0, 0 );
        panel.add( label, constraints );

        // ---------------------------------------------------------------------
        constraints = new GridBagConstraints( 0, row++, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 20, 4, 4, 4 ), 0, 0 );
        panel.add( createFailedPanel(), constraints );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createRunPanel()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;
        JLabel label;
        int row = 0;

        panel.setOpaque( false );

        // ---------------------------------------------------------------------
        label = UiUtils.createTextLabel( "A: ", UiUtils.SMALL_FONT_SIZE );

        constraints = new GridBagConstraints( 0, row, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 4, 4, 12, 4 ), 0, 0 );
        panel.add( label, constraints );

        constraints = new GridBagConstraints( 1, row, 1, 1, 0.0, 0.0,
            GridBagConstraints.EAST, GridBagConstraints.NONE,
            new Insets( 0, 0, 12, 4 ), 0, 0 );
        panel.add( run1Field, constraints );

        constraints = new GridBagConstraints( 2, row++, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 0, 16, 16, 4 ), 0, 0 );
        panel.add( run1Icon, constraints );

        label = UiUtils.createTextLabel( "B: ", UiUtils.SMALL_FONT_SIZE );

        constraints = new GridBagConstraints( 0, row, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 4, 4, 4, 4 ), 0, 0 );
        panel.add( label, constraints );

        constraints = new GridBagConstraints( 1, row, 1, 1, 1.0, 0.0,
            GridBagConstraints.EAST, GridBagConstraints.NONE,
            new Insets( 0, 0, 4, 4 ), 0, 0 );
        panel.add( run2Field, constraints );

        constraints = new GridBagConstraints( 2, row, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 0, 16, 8, 4 ), 0, 0 );
        panel.add( run2Icon, constraints );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createFailedPanel()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        panel.setOpaque( false );

        for( int i = 0; i < failedFields.length; i++ )
        {
            constraints = new GridBagConstraints( i, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets( 4, 16, 8, 16 ), 0, 0 );
            panel.add( failedFields[i], constraints );
        }

        return panel;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return view;
    }

    /***************************************************************************
     * @param config
     **************************************************************************/
    public void setConfig( HoverConfig config )
    {
        this.config = config;
    }

    /***************************************************************************
     * @param team
     **************************************************************************/
    public void initializeTrack( Team team )
    {
        if( competition != null &&
            competition.getState() != TrackState.FINISHED )
        {
            String msg = String.format(
                "The current team (%s) must finish first",
                competition.team.schoolCode );
            SwingUtils.showErrorMessage( getView(), msg, "Input Error" );
            return;
        }
        else if( team == null )
        {
            SwingUtils.showErrorMessage( getView(), "No Team Chosen",
                "Input Error" );
            return;
        }

        competition = new TrackCompetition( this, config, team );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void startPeriod()
    {
        if( checkState( "start period", TrackState.INITIALIZED ) )
        {
            competition.startPeriod();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void startRun()
    {
        if( competition != null &&
            checkStates( TrackState.WAITING_A, TrackState.WAITING_B ) )
        {
            competition.startRun();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void stopRun()
    {
        if( competition != null &&
            checkStates( TrackState.RUNNING_A, TrackState.RUNNING_B ) )
        {
            competition.stopRun();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void failRun()
    {
        if( checkState( "fail a run ", TrackState.WAITING_A,
            TrackState.RUNNING_A, TrackState.WAITING_B, TrackState.RUNNING_B ) )
        {
            competition.failRun();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void clearTrack()
    {
        if( competition != null &&
            competition.getState() != TrackState.FINISHED )
        {
            SwingUtilities.invokeLater(
                () -> SwingUtils.showErrorMessage( getView(),
                    "Wait for " + competition.team.schoolCode +
                        " to finish before clearing track data",
                    "Input Error" ) );
            return;
        }

        if( competition != null )
        {
            competition.stop();
        }

        SwingUtilities.invokeLater( () -> setTeamData( null ) );
    }

    /***************************************************************************
     * @param state
     * @param track
     * @return
     **************************************************************************/
    private boolean checkState( String action, TrackState... states )
    {
        if( competition == null )
        {
            SwingUtilities.invokeLater( () -> SwingUtils.showErrorMessage(
                getView(), "No Team Chosen", "Input Error" ) );
            return false;
        }
        else if( states != null && action != null )
        {
            if( !checkStates( states ) )
            {
                SwingUtilities.invokeLater( () -> SwingUtils.showErrorMessage(
                    getView(),
                    "Cannot " + action + " in state " + competition.getState(),
                    "Input Error" ) );
                return false;
            }
        }

        return true;
    }

    private boolean checkStates( TrackState... states )
    {
        for( TrackState state : states )
        {
            if( competition.getState() == state )
            {
                return true;
            }
        }
        return false;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public boolean isRunning()
    {
        return competition != null &&
            competition.getState() != TrackState.FINISHED;
    }
}
