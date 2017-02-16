package soh.ui;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import org.jutils.ui.StandardFormView;
import org.jutils.ui.TitleView;
import org.jutils.ui.event.updater.ReflectiveUpdater;
import org.jutils.ui.fields.IntegerFormField;
import org.jutils.ui.model.IDataView;

import soh.data.HoverConfig;

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
    private final TeamsConfigView teamsView;

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
        this.teamsView = new TeamsConfigView();

        this.view = createView();

        setData( new HoverConfig() );

        periodField.setUpdater(
            new ReflectiveUpdater<>( this, "config.periodTime" ) );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;
        int row = 0;

        // ---------------------------------------------------------------------

        constraints = new GridBagConstraints( 0, row++, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 10, 10, 10, 10 ), 0, 0 );
        panel.add( createHoverForm(), constraints );

        // ---------------------------------------------------------------------

        divbView.getView().setBorder( new TitledBorder( "Division B" ) );
        constraints = new GridBagConstraints( 0, row, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 0, 10, 10, 10 ), 0, 0 );
        panel.add( divbView.getView(), constraints );

        divcView.getView().setBorder( new TitledBorder( "Division C" ) );
        constraints = new GridBagConstraints( 1, row++, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 0, 10, 10, 10 ), 0, 0 );
        panel.add( divcView.getView(), constraints );

        // ---------------------------------------------------------------------

        TitleView teamsPane = new TitleView( "Teams", teamsView.getView() );

        constraints = new GridBagConstraints( 0, row++, 2, 1, 0.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 10, 10, 10, 10 ), 0, 0 );
        panel.add( teamsPane.getView(), constraints );

        // ---------------------------------------------------------------------

        constraints = new GridBagConstraints( 0, row++, 3, 1, 1.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets( 0, 10, 10, 10 ), 0, 0 );
        panel.add( createHelpPanel(), constraints );

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
    private Component createHelpPanel()
    {
        StandardFormView form = new StandardFormView();
        JLabel label;

        int size = 24;
        String font = "Dialog";

        label = UiUtils.createLabel(
            "F1 - Starts the competition for the Track 1 Team", size, font );
        label.setForeground( Color.black );
        form.addField( null, label );

        label = UiUtils.createLabel( "F2 - Fails a run for the Track 1 Team",
            size, font );
        label.setForeground( Color.black );
        form.addField( null, label );

        label = UiUtils.createLabel( "F3 - Clears the team from Track 1", size,
            font );
        label.setForeground( Color.black );
        form.addField( null, label );

        label = UiUtils.createLabel(
            "F8 - Toggles between the configuration and the competition scream",
            size, font );
        label.setForeground( Color.black );
        form.addField( null, label );

        label = UiUtils.createLabel(
            "F10 - Starts the competition for the Track 2 Team", size, font );
        label.setForeground( Color.black );
        form.addField( null, label );

        label = UiUtils.createLabel( "F11 - Fails a run for the Track 2 Team",
            size, font );
        label.setForeground( Color.black );
        form.addField( null, label );

        label = UiUtils.createLabel( "F12 - Clears the team from Track 2", size,
            font );
        label.setForeground( Color.black );
        form.addField( null, label );

        return form.getView();
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
        teamsView.setData( data.teams );
    }
}
