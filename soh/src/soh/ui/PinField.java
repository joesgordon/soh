package soh.ui;

import org.jutils.ui.fields.ComboFormField;
import org.jutils.ui.fields.NamedItemDescriptor;

import soh.data.GpioPin;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PinField
{
    /**  */
    private final ComboFormField<GpioPin> pinsField;

    /***************************************************************************
     * 
     **************************************************************************/
    public PinField()
    {
        this.pinsField = new ComboFormField<>( "Pin", GpioPin.values(),
            new NamedItemDescriptor<>() );
    }
}
