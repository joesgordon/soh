package soinc.lib.ui;

import javax.swing.JComponent;

import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.fields.ComboFormField;
import org.jutils.ui.fields.IDataFormField;
import org.jutils.ui.validation.IValidityChangedListener;
import org.jutils.ui.validation.Validity;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PhysicalKeyField implements IDataFormField<PhysicalKey>
{
    /**  */
    private final ComboFormField<PhysicalKey> field;

    /***************************************************************************
     * @param name
     **************************************************************************/
    public PhysicalKeyField( String name )
    {
        this.field = new ComboFormField<PhysicalKey>( name,
            PhysicalKey.values() );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public PhysicalKey getValue()
    {
        return field.getValue();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setValue( PhysicalKey value )
    {
        field.setValue( value );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setUpdater( IUpdater<PhysicalKey> updater )
    {
        field.setUpdater( updater );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public IUpdater<PhysicalKey> getUpdater()
    {
        return field.getUpdater();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setEditable( boolean editable )
    {
        field.setEditable( editable );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return field.getName();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return field.getView();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void addValidityChanged( IValidityChangedListener l )
    {
        field.addValidityChanged( l );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void removeValidityChanged( IValidityChangedListener l )
    {
        field.removeValidityChanged( l );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Validity getValidity()
    {
        return field.getValidity();
    }
}
