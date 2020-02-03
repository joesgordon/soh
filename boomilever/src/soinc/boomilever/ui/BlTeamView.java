package soinc.boomilever.ui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

import org.jutils.ui.StandardFormView;
import org.jutils.ui.fields.BooleanFormField;
import org.jutils.ui.fields.StringFormField;
import org.jutils.ui.model.IDataView;

import soinc.boomilever.data.BlTeam;

public class BlTeamView implements IDataView<BlTeam>
{
    private final JPanel panel;

    private final StringFormField nameField;
    private final BooleanFormField loadedField;
    private final BooleanFormField completeField;

    private BlTeam team;

    /***************************************************************************
     * 
     **************************************************************************/
    public BlTeamView()
    {
        this.nameField = new StringFormField( "Name", 2, null );
        this.loadedField = new BooleanFormField( "Loaded" );
        this.completeField = new BooleanFormField( "Complete" );

        this.panel = createView();

        setData( new BlTeam( "Test" ) );

        nameField.setUpdater( ( d ) -> team.name = d );
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
    public BlTeam getData()
    {
        return team;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( BlTeam team )
    {
        this.team = team;

        nameField.setValue( team.name );
        loadedField.setValue( team.loaded );
        completeField.setValue( team.complete );
    }
}
