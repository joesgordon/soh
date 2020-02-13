package soinc.ppp.ui;

import javax.swing.JPanel;

import org.jutils.ui.StandardFormView;
import org.jutils.ui.model.IDataView;

import soinc.lib.ui.PhysicalKeyField;
import soinc.ppp.data.TrackConfig;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TrackConfigView implements IDataView<TrackConfig>
{
    /**  */
    private final JPanel view;

    /**  */
    private final PhysicalKeyField loadKeyField;
    /**  */
    private final PhysicalKeyField startPauseKeyField;
    /**  */
    private final PhysicalKeyField goodKeyField;
    /**  */
    private final PhysicalKeyField badKeyField;
    /**  */
    private final PhysicalKeyField clearTeamKeyField;

    /**  */
    private TrackConfig config;

    /***************************************************************************
     * 
     **************************************************************************/
    public TrackConfigView()
    {
        this.loadKeyField = new PhysicalKeyField( "Load Key" );
        this.startPauseKeyField = new PhysicalKeyField( "Start/Pause Key" );
        this.goodKeyField = new PhysicalKeyField( "Good Key" );
        this.badKeyField = new PhysicalKeyField( "Bad Key" );
        this.clearTeamKeyField = new PhysicalKeyField( "Clear Team Key" );

        this.view = createView();

        setData( new TrackConfig() );

        loadKeyField.setUpdater( ( d ) -> config.loadKey = d );
        startPauseKeyField.setUpdater( ( d ) -> config.startPauseKey = d );
        goodKeyField.setUpdater( ( d ) -> config.goodRunKey = d );
        badKeyField.setUpdater( ( d ) -> config.badRunKey = d );
        clearTeamKeyField.setUpdater( ( d ) -> config.clearTeamKey = d );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        StandardFormView form = new StandardFormView();

        form.addField( loadKeyField );
        form.addField( startPauseKeyField );
        form.addField( goodKeyField );
        form.addField( badKeyField );
        form.addField( clearTeamKeyField );

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

        loadKeyField.setValue( config.loadKey );
        startPauseKeyField.setValue( config.startPauseKey );
        goodKeyField.setValue( config.goodRunKey );
        badKeyField.setValue( config.badRunKey );
        clearTeamKeyField.setValue( config.clearTeamKey );
    }
}
