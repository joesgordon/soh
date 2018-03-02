package soinc.hovercraft.ui.cfg;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.border.TitledBorder;

import org.jutils.ui.StandardFormView;
import org.jutils.ui.TitleView;
import org.jutils.ui.fields.IntegerFormField;
import org.jutils.ui.model.IDataView;

import soinc.hovercraft.data.HoverConfig;
import soinc.hovercraft.ui.HcTeamListView;
import soinc.lib.UiUtils;

/*******************************************************************************
 * 
 ******************************************************************************/
public class HoverConfigView implements IDataView<HoverConfig>
{
    /**  */
    private final JPanel view;
    /**  */
    private final IntegerFormField periodField;
    /**  */
    private final DivisionConfigView divbView;
    /**  */
    private final DivisionConfigView divcView;
    /**  */
    private final TrackCfgView track1View;
    /**  */
    private final TrackCfgView track2View;
    /**  */
    private final HcTeamListView teamsView;

    /**  */
    private HoverConfig config;

    /***************************************************************************
     * 
     **************************************************************************/
    public HoverConfigView()
    {
        this.periodField = new IntegerFormField( "Period Time", "s", 10, 1,
            null );
        this.divbView = new DivisionConfigView();
        this.divcView = new DivisionConfigView();

        this.track1View = new TrackCfgView();
        this.track2View = new TrackCfgView();

        this.teamsView = new HcTeamListView();

        this.view = createView();

        setData( new HoverConfig() );

        periodField.setUpdater( ( d ) -> config.periodTime = d );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new BorderLayout() );
        JTabbedPane pane = new JTabbedPane();

        pane.addTab( "Configuration", createControlsView() );
        pane.addTab( "Help", createHelpPanel() );

        panel.add( pane, BorderLayout.CENTER );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createControlsView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;
        int row = 0;

        // ---------------------------------------------------------------------

        constraints = new GridBagConstraints( 0, row, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 10, 10, 10, 10 ), 0, 0 );
        panel.add( createHoverForm(), constraints );

        // ---------------------------------------------------------------------

        TitleView teamsPane = new TitleView( "Teams", teamsView.getView() );

        teamsPane.getView().setPreferredSize( new Dimension( 600, 200 ) );
        teamsPane.getView().setMinimumSize( new Dimension( 600, 200 ) );

        constraints = new GridBagConstraints( 2, row++, 1, 3, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
            new Insets( 10, 0, 12, 10 ), 0, 0 );
        panel.add( teamsPane.getView(), constraints );

        // ---------------------------------------------------------------------

        divbView.getView().setBorder( new TitledBorder( "Division B" ) );
        constraints = new GridBagConstraints( 0, row, 1, 1, 0.0, 0.0,
            GridBagConstraints.NORTH, GridBagConstraints.NONE,
            new Insets( 0, 10, 10, 10 ), 0, 0 );
        panel.add( divbView.getView(), constraints );

        divcView.getView().setBorder( new TitledBorder( "Division C" ) );
        constraints = new GridBagConstraints( 1, row++, 1, 1, 0.0, 0.0,
            GridBagConstraints.NORTH, GridBagConstraints.NONE,
            new Insets( 0, 0, 10, 10 ), 0, 0 );
        panel.add( divcView.getView(), constraints );

        // ---------------------------------------------------------------------

        track1View.getView().setBorder( new TitledBorder( "Track 1" ) );
        constraints = new GridBagConstraints( 0, row, 1, 1, 0.0, 0.0,
            GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 10, 10, 10 ), 0, 0 );
        panel.add( track1View.getView(), constraints );

        track2View.getView().setBorder( new TitledBorder( "Track 2" ) );
        constraints = new GridBagConstraints( 1, row++, 1, 1, 0.0, 0.0,
            GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 10, 10 ), 0, 0 );
        panel.add( track2View.getView(), constraints );

        // ---------------------------------------------------------------------

        constraints = new GridBagConstraints( 0, row++, 3, 1, 1.0, 1.0,
            GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
            new Insets( 0, 10, 10, 10 ), 0, 0 );
        panel.add( Box.createHorizontalStrut( 0 ), constraints );

        // ---------------------------------------------------------------------

        return panel;
    }

    private Component createHoverForm()
    {
        StandardFormView form = new StandardFormView();

        form.addField( periodField );

        return form.getView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private static Component createHelpPanel()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        JLabel label;
        GridBagConstraints constraints;

        String [] startPeriod = { "F1", "F9",
            "Starts/Pauses the competition period timer" };

        String [] failCmd = { "F2", "F10", "Fails a run" };

        String [] resetCmd = { "F3", "F11", "Resets a run w/o failing" };

        String [] clearCmd = { "F4", "F12", "Clears the team info" };

        String [] [] commands = new String[][] { startPeriod, failCmd, resetCmd,
            clearCmd };

        int row = 0;

        label = createHelpLabel(
            "F8 - Toggles between the configuration and the competition scream" );
        constraints = new GridBagConstraints( 0, row++, 3, 1, 1.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets( 0, 0, 10, 0 ), 0, 0 );
        panel.add( label, constraints );

        // ---------------------------------------------------------------------

        label = createHelpLabel( "<html><center>Track 1<br>Key</center></html>",
            28 );
        constraints = new GridBagConstraints( 0, row, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 0, 0, 0, 20 ), 0, 0 );
        panel.add( label, constraints );

        label = createHelpLabel( "<html><center>Track 2<br>Key</center></html>",
            28 );
        constraints = new GridBagConstraints( 1, row, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 0, 0, 0, 20 ), 0, 0 );
        panel.add( label, constraints );

        label = createHelpLabel( "Description", 28 );
        constraints = new GridBagConstraints( 2, row++, 1, 1, 1.0, 0.0,
            GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( label, constraints );

        // ---------------------------------------------------------------------

        constraints = new GridBagConstraints( 0, row++, 3, 1, 1.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( new JSeparator(), constraints );

        // ---------------------------------------------------------------------

        for( String [] cmd : commands )
        {
            label = createHelpLabel( cmd[0] );
            constraints = new GridBagConstraints( 0, row, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets( 0, 0, 0, 20 ), 0, 0 );
            panel.add( label, constraints );

            label = createHelpLabel( cmd[1] );
            constraints = new GridBagConstraints( 1, row, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets( 0, 0, 0, 20 ), 0, 0 );
            panel.add( label, constraints );

            label = createHelpLabel( cmd[2] );
            constraints = new GridBagConstraints( 2, row++, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets( 0, 0, 0, 0 ), 0, 0 );
            panel.add( label, constraints );
        }

        // ---------------------------------------------------------------------

        return panel;
    }

    private static JLabel createHelpLabel( String text )
    {
        return createHelpLabel( text, 24 );
    }

    private static JLabel createHelpLabel( String text, int size )
    {
        JLabel label = UiUtils.createLabel( text, size, "Dialog" );

        Font f = label.getFont().deriveFont( Font.PLAIN );

        label.setFont( f );

        label.setForeground( Color.black );

        return label;
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
     * 
     **************************************************************************/
    @Override
    public HoverConfig getData()
    {
        return config;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( HoverConfig data )
    {
        this.config = data;

        periodField.setValue( data.periodTime );

        divbView.setData( data.divB );
        divcView.setData( data.divC );

        track1View.setData( data.track1 );
        track2View.setData( data.track2 );

        teamsView.setData( data.teams );
    }
}
