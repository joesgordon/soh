package soh.ui;

import java.awt.*;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import org.jutils.SwingUtils;
import org.jutils.ui.ColorIcon;
import org.jutils.ui.OkDialogView;
import org.jutils.ui.OkDialogView.OkDialogButtons;
import org.jutils.ui.event.ActionAdapter;
import org.jutils.ui.model.IView;

import soh.SohIcons;
import soh.data.*;
import soh.tasks.TrackCompetition;

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
    private final HoverConfig config;
    /**  */
    private final TrackCompetition competition;
    /**  */
    private final javax.swing.Timer updateTimer;
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
    private final JTextArea errorField;

    /**  */
    private final Icon blankIcon;
    /**  */
    private final Icon checkIcon;
    /**  */
    private final Icon xIcon;

    /**  */
    private long errTime;
    /**  */
    private TrackData lastData;

    /**  */
    private Team team;

    /***************************************************************************
     * @param trackName
     **************************************************************************/
    public TrackView( String trackName, HoverConfig config,
        TrackCompetition competition )
    {
        this.config = config;
        this.competition = competition;
        this.updateTimer = new Timer( 50,
            ( e ) -> edtUpdateUI( competition.updateData() ) );

        this.teamButton = new JButton( "Welcome Olympians" );
        this.targetTimeField = UiUtils.createNumLabel( "--.- s", 24 );
        this.targetLengthField = UiUtils.createNumLabel( "---.- cm", 24 );

        this.blankIcon = new ColorIcon( new Color( 20, 20, 20 ), 36 );
        this.checkIcon = SohIcons.getCheckIcon( 36 );
        this.xIcon = SohIcons.getXIcon( 36 );

        this.periodField = UiUtils.createNumLabel( "-:--", 64 );
        this.run1Field = UiUtils.createNumLabel( "--.- s" );
        this.run1Icon = new JLabel( blankIcon );
        this.run2Field = UiUtils.createNumLabel( "--.- s" );
        this.run2Icon = new JLabel( blankIcon );
        this.failedFields = new JLabel[5];
        this.finishedField = UiUtils.createTextLabel( " ", 36 );
        this.errorField = new JTextArea();

        this.team = null;

        competition.addDataListener( ( e ) -> edtUpdateUI( e.getItem() ) );

        errorField.setFont(
            errorField.getFont().deriveFont( 36.0f ).deriveFont( Font.BOLD ) );
        errorField.setLineWrap( true );
        errorField.setWrapStyleWord( true );
        errorField.setOpaque( false );
        errorField.setBorder( new EmptyBorder( 0, 0, 0, 0 ) );
        errorField.setForeground( Color.red );

        for( int i = 0; i < failedFields.length; i++ )
        {
            failedFields[i] = new JLabel( blankIcon );
        }

        teamButton.setName( trackName );

        this.view = createView();
        this.lastData = new TrackData();
        this.errTime = -1;

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
        competition.signalClearTrack();

        teamButton.setText( "Select Team" );
        teamButton.setEnabled( true );

        if( config.getAvailableTeams().isEmpty() )
        {
            teamButton.setText( "Teams Complete" );
            teamButton.setEnabled( false );
        }

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
        setError( "" );
    }

    /***************************************************************************
     * @param team
     **************************************************************************/
    private void setTeamData( Team team )
    {
        this.team = team;

        if( team == null )
        {
            clearTeam();
            teamButton.setEnabled( true );
        }
        else
        {
            team.loaded = true;
            teamButton.setText( team.schoolCode );
            // + " (Division " + team.div.name + ")" );

            DivisionConfig dcv = config.getDivision( team.div );

            targetTimeField.setText(
                String.format( "%5.1f s", dcv.targetTime / 10.0 ) );
            targetLengthField.setText(
                String.format( "%5.1f cm   ", dcv.targetLength * 1.0 ) );

            initializeTrack( team );

            updateTimer.start();
            teamButton.setEnabled( false );
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
    private void setFinished( boolean finished )
    {
        String text = finished ? "Team Complete" : "";

        finishedField.setText( text );

        if( finished && config.getAvailableTeams().isEmpty() )
        {
            teamButton.setEnabled( false );
        }
    }

    /***************************************************************************
     * @param paused
     **************************************************************************/
    private void setPaused( boolean paused )
    {
        if( paused )
        {
            errorField.setForeground( Color.ORANGE );
            errorField.setText( "Paused" );
        }
        else
        {
            errorField.setText( "" );
        }
    }

    /***************************************************************************
     * @param errorMsg
     **************************************************************************/
    private void setError( String errorMsg )
    {
        errorField.setForeground( Color.red );
        errorField.setText( errorMsg );

        errTime = System.currentTimeMillis();
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
        for( int i = cnt; i < 5; i++ )
        {
            failedFields[i].setIcon( blankIcon );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void showTeamChooser()
    {
        TrackData data = competition.updateData();
        TrackState state = data.state;

        if( state != TrackState.FINISHED && state != TrackState.UNINITIALIZED )
        {
            SwingUtils.showErrorMessage( getView(),
                "Cannot change teams until " + data.getTeamCode() +
                    " has finished",
                "Input Error" );
            return;
        }

        clearTeam();

        List<Team> teams = config.getAvailableTeams();
        TeamsView teamsView = new TeamsView( teams );
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

        constraints = new GridBagConstraints( 0, row, 2, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( finishedField, constraints );

        constraints = new GridBagConstraints( 0, row++, 2, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( errorField, constraints );

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
        teamButton.addMouseListener( new RightClickMouseListener(
            ( e ) -> showTeamPopup( e.getItem() ) ) );
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
        label.addMouseListener( new RightClickMouseListener(
            ( e ) -> showResetSuccessPopup( e.getItem() ) ) );
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
        label.addMouseListener( new RightClickMouseListener(
            ( e ) -> showResetFailurePopup( e.getItem() ) ) );
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
     * @param team
     **************************************************************************/
    private void initializeTrack( Team team )
    {
        TrackData data = competition.updateData();

        if( data.state != TrackState.FINISHED &&
            data.state != TrackState.UNINITIALIZED )
        {
            String msg = String.format(
                "The current team (%s) must finish first", data.getTeamCode() );
            SwingUtils.showErrorMessage( getView(), msg, "Input Error" );
            return;
        }
        else if( team == null )
        {
            SwingUtils.showErrorMessage( getView(), "No Team Chosen",
                "Input Error" );
            return;
        }

        competition.signalLoadTrack( team );
    }

    /***************************************************************************
     * Returns {@code true} if either track is currently running; {@code false}
     * otherwise.
     **************************************************************************/
    public boolean isRunning()
    {
        return competition.isRunning();
    }

    /***************************************************************************
     * Run on the EDT
     **************************************************************************/
    private void edtUpdateUI( TrackData data )
    {
        if( data.checkTimeFail() )
        {
            competition.signalFailRun();
        }

        data = competition.updateData();

        if( data.state == TrackState.FINISHED )
        {
            updateTimer.stop();
        }
        else if( lastData.state == TrackState.FINISHED &&
            data.state != TrackState.UNINITIALIZED )
        {
            updateTimer.start();
        }

        if( lastData.state != data.state &&
            ( lastData.state == TrackState.PAUSE_A ||
                lastData.state == TrackState.PAUSE_B ||
                data.state == TrackState.PAUSE_A ||
                data.state == TrackState.PAUSE_B ) )
        {
            setPaused( data.state == TrackState.PAUSE_A ||
                data.state == TrackState.PAUSE_B );
        }

        // LogUtils.printDebug( "Updating times for track @ " + data.periodTime
        // );

        if( lastData.periodTime != data.periodTime )
        {
            setPeriodTime( data.periodTime );
        }

        if( !lastData.errorMsg.equals( data.errorMsg ) )
        {
            setError( data.errorMsg );
        }
        else if( errTime > 0 )
        {
            long now = System.currentTimeMillis();
            long delta = now - errTime;

            if( delta > 3000 )
            {
                errTime = -1;
            }
            else
            {
                float percent = 1.0f - delta / 3000.0f;

                // LogUtils.printDebug( "percent: %f", percent );

                Color fg = new Color( 1.0f, 0.0f, 0.0f, percent );

                errorField.setForeground( fg );
            }
        }

        if( lastData.state != data.state &&
            data.state == TrackState.UNINITIALIZED )
        {
            clearTeam();
        }

        setFinished( data.state == TrackState.FINISHED );

        setRunaTime( data.run1Time, data.state.runaComplete );
        setRunbTime( data.run2Time, data.state.runbComplete );

        setFailCount( data.failedCount );

        this.lastData = new TrackData( data );
    }

    /***************************************************************************
     * @param e
     **************************************************************************/
    private void showResetSuccessPopup( MouseEvent e )
    {
        TrackData data = competition.updateData();
        if( data.run1Time > -1 && data.state != TrackState.RUNNING_A &&
            data.failedCount < 5 )
        {
            JPopupMenu menu = new JPopupMenu();

            ActionListener listener = (
                evt ) -> competition.signalResetSuccess();
            Action a = new ActionAdapter( listener, "Reset Last Success",
                null );
            menu.add( a );

            menu.show( e.getComponent(), e.getX(), e.getY() );
        }
    }

    /***************************************************************************
     * @param e
     **************************************************************************/
    private void showResetFailurePopup( MouseEvent e )
    {
        if( competition.updateData().failedCount > 0 )
        {
            JPopupMenu menu = new JPopupMenu();

            ActionListener listener = (
                evt ) -> competition.signalResetFailure();
            Action a = new ActionAdapter( listener, "Reset Last Failure",
                null );
            menu.add( a );

            menu.show( e.getComponent(), e.getX(), e.getY() );
        }
    }

    /***************************************************************************
     * @param e
     **************************************************************************/
    private void showTeamPopup( MouseEvent e )
    {
        if( team == null )
        {
            JPopupMenu menu = new JPopupMenu();
            List<Team> teams = config.getAvailableTeams();

            for( Team t : teams )
            {
                ActionListener listener = ( evt ) -> setTeamData( t );
                Action a = new ActionAdapter( listener, t.schoolCode, null );
                menu.add( a );
            }

            menu.show( e.getComponent(), e.getX(), e.getY() );
        }
    }
}
