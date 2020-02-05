package soinc.ppp.ui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

import org.jutils.ui.StandardFormView;
import org.jutils.ui.fields.BooleanFormField;
import org.jutils.ui.fields.IntegerFormField;
import org.jutils.ui.fields.StringFormField;
import org.jutils.ui.model.IDataView;

import soinc.ppp.data.Team;

public class TeamView implements IDataView<Team>
{
    private final JPanel panel;

    private final StringFormField nameField;
    private final IntegerFormField run1TimeField;
    private final IntegerFormField run2TimeField;
    private final BooleanFormField loadedField;
    private final BooleanFormField completeField;

    private Team team;

    /***************************************************************************
     * 
     **************************************************************************/
    public TeamView()
    {
        this.nameField = new StringFormField( "Name", 2, null );
        this.run1TimeField = new IntegerFormField( "Run 1 Time", "0.1 s" );
        this.run2TimeField = new IntegerFormField( "Run 2 Time", "0.1 s" );
        this.loadedField = new BooleanFormField( "Loaded" );
        this.completeField = new BooleanFormField( "Complete" );

        this.panel = createView();

        setData( new Team( "Test" ) );

        nameField.setUpdater( ( d ) -> team.name = d );
        run1TimeField.setUpdater( ( d ) -> team.run1Time = d );
        run2TimeField.setUpdater( ( d ) -> team.run2Time = d );
        loadedField.setUpdater( ( d ) -> team.loaded = d );
        completeField.setUpdater( ( d ) -> team.complete = d );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0,
            GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( createForm(), constraints );

        return panel;
    }

    private JPanel createForm()
    {
        StandardFormView form = new StandardFormView();

        form.addField( nameField );
        form.addField( run1TimeField );
        form.addField( run2TimeField );
        form.addField( loadedField );
        form.addField( completeField );

        return form.getView();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Component getView()
    {
        return panel;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Team getData()
    {
        return team;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( Team team )
    {
        this.team = team;

        nameField.setValue( team.name );
        run1TimeField.setValue( team.run1Time );
        run2TimeField.setValue( team.run2Time );
        loadedField.setValue( team.loaded );
        completeField.setValue( team.complete );
    }
}
