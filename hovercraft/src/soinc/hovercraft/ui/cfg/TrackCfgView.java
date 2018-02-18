package soinc.hovercraft.ui.cfg;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.jutils.ui.StandardFormView;
import org.jutils.ui.event.updater.ReflectiveUpdater;
import org.jutils.ui.fields.ComboFormField;
import org.jutils.ui.fields.NamedItemDescriptor;
import org.jutils.ui.model.IDataView;

import soinc.hovercraft.data.TrackCfg;
import soinc.lib.data.Pi3GpioPin;
import soinc.lib.data.PinLevel;
import soinc.lib.data.PinResistance;

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
    private final ComboFormField<Pi3GpioPin> redPinField;
    /**  */
    private final ComboFormField<PinLevel> redLevelField;
    /**  */
    private final ComboFormField<Pi3GpioPin> greenPinField;
    /**  */
    private final ComboFormField<PinLevel> greenLevelField;
    /**  */
    private final ComboFormField<Pi3GpioPin> bluePinField;
    /**  */
    private final ComboFormField<PinLevel> blueLevelField;

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

        this.redPinField = new ComboFormField<>( "Red Pin", Pi3GpioPin.values(),
            new NamedItemDescriptor<>() );
        this.redLevelField = new ComboFormField<>( "Red Off Level",
            PinLevel.values(), new NamedItemDescriptor<>() );

        this.greenPinField = new ComboFormField<>( "Green Pin",
            Pi3GpioPin.values(), new NamedItemDescriptor<>() );
        this.greenLevelField = new ComboFormField<>( "Green Off Level",
            PinLevel.values(), new NamedItemDescriptor<>() );

        this.bluePinField = new ComboFormField<>( "Blue Pin",
            Pi3GpioPin.values(), new NamedItemDescriptor<>() );
        this.blueLevelField = new ComboFormField<>( "Blue Off Level",
            PinLevel.values(), new NamedItemDescriptor<>() );

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

        redPinField.setUpdater(
            new ReflectiveUpdater<>( this, "track.redPin" ) );
        redLevelField.setUpdater(
            new ReflectiveUpdater<>( this, "track.redDefaultLevel" ) );

        greenPinField.setUpdater(
            new ReflectiveUpdater<>( this, "track.greenPin" ) );
        greenLevelField.setUpdater(
            new ReflectiveUpdater<>( this, "track.greenDefaultLevel" ) );

        bluePinField.setUpdater(
            new ReflectiveUpdater<>( this, "track.bluePin" ) );
        blueLevelField.setUpdater(
            new ReflectiveUpdater<>( this, "track.blueDefaultLevel" ) );
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

        form.addField( redPinField );
        form.addField( redLevelField );
        form.addField( greenPinField );
        form.addField( greenLevelField );
        form.addField( bluePinField );
        form.addField( blueLevelField );

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

        redPinField.setValue( track.redPin );
        redLevelField.setValue( track.redDefaultLevel );

        greenPinField.setValue( track.greenPin );
        greenLevelField.setValue( track.greenDefaultLevel );

        bluePinField.setValue( track.bluePin );
        blueLevelField.setValue( track.blueDefaultLevel );
    }
}
