package soh.ui;

import java.awt.Component;

import javax.swing.JPanel;

import org.jutils.ui.StandardFormView;
import org.jutils.ui.event.updater.ReflectiveUpdater;
import org.jutils.ui.fields.ComboFormField;
import org.jutils.ui.fields.StringFormField;
import org.jutils.ui.model.IDataView;

import soh.data.Division;
import soh.data.Team;

public class TeamConfigView implements IDataView<Team>
{
    private final JPanel view;
    private final StringFormField nameField;
    private final ComboFormField<Division> divField;
    private Team team;

    public TeamConfigView()
    {
        this.nameField = new StringFormField( "School Code" );
        this.divField = new ComboFormField<>( "Division", Division.values() );
        this.view = createView();

        setData( new Team() );

        nameField.setUpdater(
            new ReflectiveUpdater<>( this, "team.schoolCode" ) );
        divField.setUpdater( new ReflectiveUpdater<>( this, "team.div" ) );
    }

    private JPanel createView()
    {
        StandardFormView form = new StandardFormView();

        form.addField( nameField );
        form.addField( divField );

        return form.getView();
    }

    @Override
    public Component getView()
    {
        return view;
    }

    @Override
    public Team getData()
    {
        return team;
    }

    @Override
    public void setData( Team data )
    {
        this.team = data;

        nameField.setValue( team.schoolCode );
        divField.setValue( team.div );
    }

}
