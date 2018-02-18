package soinc.hovercraft.ui.cfg;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.jutils.ui.StandardFormView;
import org.jutils.ui.event.updater.ReflectiveUpdater;
import org.jutils.ui.event.updater.WrappedUpdater;
import org.jutils.ui.fields.IntegerFormField;
import org.jutils.ui.fields.StringFormField;
import org.jutils.ui.model.IDataView;

import soinc.hovercraft.data.Division;
import soinc.hovercraft.data.DivisionConfig;

/*******************************************************************************
 * 
 ******************************************************************************/
public class DivisionConfigView implements IDataView<DivisionConfig>
{
    /**  */
    private final JPanel view;
    /**  */
    private final StringFormField divField;
    /**  */
    private final IntegerFormField lengthField;
    /**  */
    private final IntegerFormField timeField;
    /**  */
    private final StringFormField mphField;
    /**  */
    private final StringFormField mpsField;

    private DivisionConfig config;

    /***************************************************************************
     * 
     **************************************************************************/
    public DivisionConfigView()
    {
        this.divField = new StringFormField( "Division", 10, 1, null );
        this.lengthField = new IntegerFormField( "Target Length", "cm", 10, 100,
            195 );
        this.timeField = new IntegerFormField( "Target Time", "0.1 seconds", 10,
            50, 250 );
        this.mphField = new StringFormField( "Speed (mph)", 10, null, null );
        this.mpsField = new StringFormField( "Speed (m/s)", 10, null, null );

        this.view = createView();

        setData( new DivisionConfig( Division.DIVISION_B ) );

        divField.setEditable( false );
        lengthField.setUpdater( new WrappedUpdater<>(
            new ReflectiveUpdater<>( this, "config.targetLength" ),
            ( e ) -> updateSpeed() ) );
        timeField.setUpdater( new WrappedUpdater<>(
            new ReflectiveUpdater<>( this, "config.targetTime" ),
            ( e ) -> updateSpeed() ) );
        mphField.setEditable( false );
        mpsField.setEditable( false );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        StandardFormView form = new StandardFormView();

        // form.addField( divField );
        form.addField( lengthField );
        form.addField( timeField );
        form.addField( mphField );
        form.addField( mpsField );

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
    public DivisionConfig getData()
    {
        return config;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( DivisionConfig data )
    {
        this.config = data;

        divField.setValue( data.div.name );
        lengthField.setValue( data.targetLength );
        timeField.setValue( data.targetTime );

        updateSpeed();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void updateSpeed()
    {
        double mps = config.targetLength / 100.0 / ( config.targetTime / 10.0 );
        double mph = mps / 0.44704;

        mphField.setValue( String.format( "%.5f", mph ) );
        mpsField.setValue( String.format( "%.5f", mps ) );
    }
}
