package soinc.boomilever.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

import org.jutils.ui.StandardFormView;
import org.jutils.ui.fields.ComboFormField;
import org.jutils.ui.fields.IntegerFormField;
import org.jutils.ui.model.IDataView;

import soinc.boomilever.data.CompetitionConfig;
import soinc.lib.ui.PhysicalKey;

/*******************************************************************************
 * 
 ******************************************************************************/
public class CompetitionConfigView implements IDataView<CompetitionConfig>
{
    /**  */
    private final JPanel view;

    /**  */
    private final IntegerFormField redRelayField;
    /**  */
    private final IntegerFormField greenRelayField;
    /**  */
    private final IntegerFormField blueRelayField;
    /**  */
    private final ComboFormField<PhysicalKey> loadKeyField;
    /**  */
    private final ComboFormField<PhysicalKey> startPauseKeyField;
    /**  */
    private final ComboFormField<PhysicalKey> clearKeyField;

    /**  */
    private CompetitionConfig config;

    /***************************************************************************
     * 
     **************************************************************************/
    public CompetitionConfigView()
    {
        this.redRelayField = new IntegerFormField( "Red Relay", null, 8, 0, 8 );
        this.greenRelayField = new IntegerFormField( "Red Relay", null, 8, 0,
            8 );
        this.blueRelayField = new IntegerFormField( "Red Relay", null, 8, 0,
            8 );

        this.loadKeyField = new ComboFormField<PhysicalKey>( "Load Key",
            PhysicalKey.values() );
        this.startPauseKeyField = new ComboFormField<PhysicalKey>( "Start Key",
            PhysicalKey.values() );
        this.clearKeyField = new ComboFormField<PhysicalKey>( "Clear Key",
            PhysicalKey.values() );

        this.view = createView();

        setData( new CompetitionConfig() );

        redRelayField.setUpdater( ( d ) -> config.redRelay = d );
        greenRelayField.setUpdater( ( d ) -> config.greenRelay = d );
        blueRelayField.setUpdater( ( d ) -> config.blueRelay = d );

        loadKeyField.setUpdater( ( d ) -> config.loadKey = d );
        startPauseKeyField.setUpdater( ( d ) -> config.startPauseKey = d );
        clearKeyField.setUpdater( ( d ) -> config.clearKey = d );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        int pad = StandardFormView.DEFAULT_FORM_MARGIN;

        constraints = new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( createForm(), constraints );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createForm()
    {
        StandardFormView form = new StandardFormView();

        form.addField( redRelayField );
        form.addField( greenRelayField );
        form.addField( blueRelayField );

        form.addField( loadKeyField );
        form.addField( startPauseKeyField );
        form.addField( clearKeyField );

        return form.getView();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JPanel getView()
    {
        return view;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public CompetitionConfig getData()
    {
        return config;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( CompetitionConfig config )
    {
        this.config = config;

        redRelayField.setValue( config.redRelay );
        greenRelayField.setValue( config.greenRelay );
        blueRelayField.setValue( config.blueRelay );

        loadKeyField.setValue( config.loadKey );
        startPauseKeyField.setValue( config.startPauseKey );
        clearKeyField.setValue( config.clearKey );
    }
}
