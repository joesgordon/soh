package soinc.gravityvehicle.ui;

import javax.swing.JPanel;

import org.jutils.ui.StandardFormView;
import org.jutils.ui.fields.IntegerFormField;
import org.jutils.ui.model.IDataView;

import soinc.gravityvehicle.data.TrackConfig;
import soinc.lib.ui.PhysicalKeyField;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TrackConfigView implements IDataView<TrackConfig>
{
    /**  */
    private final JPanel view;

    /**  */
    private final IntegerFormField powerRelayField;
    /**  */
    private final IntegerFormField redRelayField;
    /**  */
    private final IntegerFormField greenRelayField;
    /**  */
    private final IntegerFormField blueRelayField;
    /**  */
    private final PhysicalKeyField loadKeyField;
    /**  */
    private final PhysicalKeyField startPauseKeyField;
    /**  */
    private final PhysicalKeyField clearKeyField;

    /**  */
    private TrackConfig config;

    /***************************************************************************
     * 
     **************************************************************************/
    public TrackConfigView()
    {
        this.powerRelayField = new IntegerFormField( "Power Relay", null, 8, 0,
            8 );
        this.redRelayField = new IntegerFormField( "Red Relay", null, 8, 0, 8 );
        this.greenRelayField = new IntegerFormField( "Red Relay", null, 8, 0,
            8 );
        this.blueRelayField = new IntegerFormField( "Red Relay", null, 8, 0,
            8 );

        this.loadKeyField = new PhysicalKeyField( "Load Key" );
        this.startPauseKeyField = new PhysicalKeyField( "Start Key" );
        this.clearKeyField = new PhysicalKeyField( "Clear Key" );

        this.view = createForm();

        setData( new TrackConfig() );

        powerRelayField.setUpdater( ( d ) -> config.powerRelay = d );
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
    private JPanel createForm()
    {
        StandardFormView form = new StandardFormView();

        form.addField( powerRelayField );
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
    public TrackConfig getData()
    {
        return config;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( TrackConfig config )
    {
        this.config = config;

        powerRelayField.setValue( config.powerRelay );
        redRelayField.setValue( config.redRelay );
        greenRelayField.setValue( config.greenRelay );
        blueRelayField.setValue( config.blueRelay );

        loadKeyField.setValue( config.loadKey );
        startPauseKeyField.setValue( config.startPauseKey );
        clearKeyField.setValue( config.clearKey );
    }
}
