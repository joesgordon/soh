package soinc.lib.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.jutils.ui.StandardFormView;
import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.event.updater.UpdaterList;
import org.jutils.ui.fields.ComboFormField;
import org.jutils.ui.fields.IDataFormField;
import org.jutils.ui.fields.NamedItemDescriptor;
import org.jutils.ui.validation.AggregateValidityChangedManager;
import org.jutils.ui.validation.IValidityChangedListener;
import org.jutils.ui.validation.Validity;

import soinc.lib.data.Pi3GpioPin;
import soinc.lib.data.Pi3InputPin;
import soinc.lib.data.PinResistance;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Pi3InputPinField implements IDataFormField<Pi3InputPin>
{
    /**  */
    private final JPanel view;
    /**  */
    private final Pi3GpioPinField pinField;
    /**  */
    private final ComboFormField<PinResistance> resistanceField;

    /**  */
    private final AggregateValidityChangedManager validityManager;

    /**  */
    private UpdaterList<Pi3InputPin> updaters;

    /**  */
    private Pi3InputPin pin;

    /***************************************************************************
     * 
     **************************************************************************/
    public Pi3InputPinField( String name )
    {
        this.pinField = new Pi3GpioPinField( name );
        this.resistanceField = new ComboFormField<>( "", PinResistance.values(),
            new NamedItemDescriptor<>() );
        this.view = createView();

        this.validityManager = new AggregateValidityChangedManager();

        this.updaters = new UpdaterList<>();
        this.pin = new Pi3InputPin();

        validityManager.addField( pinField );
        validityManager.addField( resistanceField );

        pinField.setUpdater( ( d ) -> updatePin( d ) );
        resistanceField.setUpdater( ( d ) -> updateResistance( d ) );
    }

    /***************************************************************************
     * @param pin
     **************************************************************************/
    private void updatePin( Pi3GpioPin pin )
    {
        this.pin.gpioPin = pin;
        updaters.fire( this.pin );
    }

    /***************************************************************************
     * @param resistance
     **************************************************************************/
    private void updateResistance( PinResistance resistance )
    {
        this.pin.resistance = resistance;
        updaters.fire( this.pin );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        int pad = StandardFormView.DEFAULT_FIELD_MARGIN;

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( pinField.getView(), constraints );

        constraints = new GridBagConstraints( 1, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, pad, 0, 0 ), 0, 0 );
        panel.add( resistanceField.getView(), constraints );

        return panel;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Pi3InputPin getValue()
    {
        return pin;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setValue( Pi3InputPin pin )
    {
        this.pin = pin;

        pinField.setValue( pin.gpioPin );
        resistanceField.setValue( pin.resistance );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setUpdater( IUpdater<Pi3InputPin> updater )
    {
        updaters.removeAll();
        updaters.add( updater );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public IUpdater<Pi3InputPin> getUpdater()
    {
        return updaters.size() > 0 ? updaters.get( 0 ) : null;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setEditable( boolean editable )
    {
        pinField.setEditable( editable );
        resistanceField.setEditable( editable );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return pinField.getName();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return view;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void addValidityChanged( IValidityChangedListener l )
    {
        validityManager.addValidityChanged( l );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void removeValidityChanged( IValidityChangedListener l )
    {
        validityManager.removeValidityChanged( l );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Validity getValidity()
    {
        return validityManager.getValidity();
    }
}
