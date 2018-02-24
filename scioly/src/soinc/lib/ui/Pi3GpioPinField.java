package soinc.lib.ui;

import javax.swing.JComponent;

import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.fields.ComboFormField;
import org.jutils.ui.fields.IDataFormField;
import org.jutils.ui.fields.IDescriptor;
import org.jutils.ui.validation.IValidityChangedListener;
import org.jutils.ui.validation.Validity;

import soinc.lib.data.Pi3GpioPin;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Pi3GpioPinField implements IDataFormField<Pi3GpioPin>
{
    /**  */
    private final Pi3GpioPinDescriptor pinDescriptor;
    /**  */
    private final ComboFormField<Pi3GpioPin> startPinField;

    /***************************************************************************
     * @param name
     **************************************************************************/
    public Pi3GpioPinField( String name )
    {
        this.pinDescriptor = new Pi3GpioPinDescriptor();
        this.startPinField = new ComboFormField<>( name, Pi3GpioPin.values(),
            pinDescriptor );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Pi3GpioPin getValue()
    {
        return startPinField.getValue();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setValue( Pi3GpioPin value )
    {
        startPinField.setValue( value );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setUpdater( IUpdater<Pi3GpioPin> updater )
    {
        startPinField.setUpdater( updater );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public IUpdater<Pi3GpioPin> getUpdater()
    {
        return startPinField.getUpdater();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setEditable( boolean editable )
    {
        startPinField.setEditable( editable );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return startPinField.getName();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return startPinField.getView();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void addValidityChanged( IValidityChangedListener l )
    {
        startPinField.addValidityChanged( l );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void removeValidityChanged( IValidityChangedListener l )
    {
        startPinField.removeValidityChanged( l );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Validity getValidity()
    {
        return startPinField.getValidity();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class Pi3GpioPinDescriptor
        implements IDescriptor<Pi3GpioPin>
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public String getDescription( Pi3GpioPin item )
        {
            return item == null ? ""
                : "Pin " + item.pin.pinout + " (" + item.getName() + ")";
        }
    }
}
