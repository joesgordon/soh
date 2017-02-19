package soh.ui;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.jutils.ui.StandardFormView;
import org.jutils.ui.event.updater.ReflectiveUpdater;
import org.jutils.ui.fields.ComboFormField;
import org.jutils.ui.fields.NamedItemDescriptor;
import org.jutils.ui.model.IDataView;

import soh.data.*;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TrackCfgView implements IDataView<TrackCfg>
{
    /**  */
    private final JPanel view;
    /**  */
    private final ComboFormField<Pi3GpioPin> startPinField;
    /**  */
    private final ComboFormField<PinResistance> startResField;
    /**  */
    private final ComboFormField<Pi3GpioPin> stopPinField;
    /**  */
    private final ComboFormField<PinResistance> stopResField;

    /**  */
    private TrackCfg track;

    /***************************************************************************
     * 
     **************************************************************************/
    public TrackCfgView()
    {
        this.startPinField = new ComboFormField<>( "Start Pin",
            Pi3GpioPin.values(), new NamedItemDescriptor<>() );
        this.startResField = new ComboFormField<>( "Start Resistance",
            PinResistance.values(), new NamedItemDescriptor<>() );
        this.stopPinField = new ComboFormField<>( "Stop Pin",
            Pi3GpioPin.values(), new NamedItemDescriptor<>() );
        this.stopResField = new ComboFormField<>( "Stop Resistance",
            PinResistance.values(), new NamedItemDescriptor<>() );

        this.view = createView();

        setData( new TrackCfg() );

        startPinField.setUpdater(
            new ReflectiveUpdater<>( this, "track.startPin" ) );
        startResField.setUpdater(
            new ReflectiveUpdater<>( this, "track.startRes" ) );

        stopPinField.setUpdater(
            new ReflectiveUpdater<>( this, "track.stopPin" ) );
        stopResField.setUpdater(
            new ReflectiveUpdater<>( this, "track.stopRes" ) );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        StandardFormView form = new StandardFormView();

        form.addField( startPinField );
        form.addField( startResField );
        form.addField( stopPinField );
        form.addField( stopResField );

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
    public TrackCfg getData()
    {
        return track;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( TrackCfg track )
    {
        this.track = track;

        startPinField.setValue( track.startPin );
        startResField.setValue( track.startRes );

        stopPinField.setValue( track.stopPin );
        stopResField.setValue( track.stopRes );
    }
}
