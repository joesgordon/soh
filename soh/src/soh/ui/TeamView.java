package soh.ui;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.*;

import org.jutils.IconConstants;
import org.jutils.SwingUtils;
import org.jutils.ui.StandardFormView;
import org.jutils.ui.event.ActionAdapter;
import org.jutils.ui.event.updater.ReflectiveUpdater;
import org.jutils.ui.fields.*;
import org.jutils.ui.model.IDataView;

import soh.data.Division;
import soh.data.Team;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TeamView implements IDataView<Team>
{
    /**  */
    private final JPanel view;
    /**  */
    private final StringFormField nameField;
    /**  */
    private final ComboFormField<Division> divField;
    /**  */
    private final IntegerFormField failedCountField;
    /**  */
    private final IntegerFormField time1Field;
    /**  */
    private final IntegerFormField time2Field;
    /**  */
    private final BooleanFormField loadedField;

    /**  */
    private Team team;

    /***************************************************************************
     * 
     **************************************************************************/
    public TeamView()
    {
        this.nameField = new StringFormField( "School Code", 15, 1, null );
        this.divField = new ComboFormField<>( "Division", Division.values() );
        this.failedCountField = new IntegerFormField( "Failed Count" );
        this.time1Field = new IntegerFormField( "Time 1", "0.1 seconds", 15, -1,
            null );
        this.time2Field = new IntegerFormField( "Time 2", "0.1 seconds", 15, -1,
            null );
        this.loadedField = new BooleanFormField( "Loaded" );
        this.view = createView();

        setData( new Team() );

        nameField.setUpdater(
            new ReflectiveUpdater<>( this, "team.schoolCode" ) );
        divField.setUpdater( new ReflectiveUpdater<>( this, "team.div" ) );
        failedCountField.setUpdater(
            new ReflectiveUpdater<>( this, "team.failedCount" ) );
        time1Field.setUpdater( new ReflectiveUpdater<>( this, "team.time1" ) );
        time2Field.setUpdater( new ReflectiveUpdater<>( this, "team.time2" ) );
        loadedField.setUpdater(
            new ReflectiveUpdater<>( this, "team.loaded" ) );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new BorderLayout() );

        panel.add( createToolbar(), BorderLayout.NORTH );
        panel.add( createForm(), BorderLayout.CENTER );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createToolbar()
    {
        JToolBar toolbar = new JToolBar();
        Action action;
        Icon icon;

        SwingUtils.setToolbarDefaults( toolbar );

        icon = IconConstants.getIcon( IconConstants.UNDO_16 );
        action = new ActionAdapter( ( e ) -> initTeam(), "Initialize", icon );
        SwingUtils.addActionToToolbar( toolbar, action );

        return toolbar;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void initTeam()
    {
        team.initTrials();
        setData( team );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createForm()
    {
        StandardFormView form = new StandardFormView();

        form.addField( nameField );
        form.addField( divField );
        form.addField( failedCountField );
        form.addField( time1Field );
        form.addField( time2Field );
        form.addField( loadedField );

        return form.getView();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Component getView()
    {
        return view;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Team getData()
    {
        return team;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( Team data )
    {
        this.team = data;

        nameField.setValue( team.schoolCode );
        divField.setValue( team.div );
        failedCountField.setValue( team.failedCount );
        time1Field.setValue( team.run1Time );
        time2Field.setValue( team.run2Time );
        loadedField.setValue( team.loaded );
    }
}
