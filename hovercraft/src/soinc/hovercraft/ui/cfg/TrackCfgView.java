package soinc.hovercraft.ui.cfg;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.jutils.ui.StandardFormView;
import org.jutils.ui.fields.ComboFormField;
import org.jutils.ui.fields.NamedItemDescriptor;
import org.jutils.ui.model.IDataView;

import soinc.hovercraft.data.Division;
import soinc.hovercraft.data.TrackCfg;
import soinc.lib.ui.Pi3InputPinField;
import soinc.lib.ui.Pi3OutputPinField;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TrackCfgView implements IDataView<TrackCfg>
{
    /**  */
    private final JPanel view;

    /**  */
    private final ComboFormField<Division> divisionField;

    /**  */
    private final Pi3InputPinField startPinField;
    /**  */
    private final Pi3InputPinField stopPinField;

    /**  */
    private final Pi3OutputPinField redPinField;
    /**  */
    private final Pi3OutputPinField greenPinField;
    /**  */
    private final Pi3OutputPinField bluePinField;

    /**  */
    private TrackCfg track;

    /***************************************************************************
     * 
     **************************************************************************/
    public TrackCfgView()
    {
        this.divisionField = new ComboFormField<>( "Division",
            Division.values(), new NamedItemDescriptor<>() );

        this.startPinField = new Pi3InputPinField( "Start Pin" );
        this.stopPinField = new Pi3InputPinField( "Stop Pin" );

        this.redPinField = new Pi3OutputPinField( "Red Pin" );
        this.greenPinField = new Pi3OutputPinField( "Green Pin" );
        this.bluePinField = new Pi3OutputPinField( "Blue Pin" );

        this.view = createView();

        setData( new TrackCfg() );

        divisionField.setUpdater( ( d ) -> track.division = d );

        startPinField.setUpdater( ( d ) -> track.startPin.set( d ) );
        stopPinField.setUpdater( ( d ) -> track.stopPin.set( d ) );

        redPinField.setUpdater( ( d ) -> track.redPin.set( d ) );
        greenPinField.setUpdater( ( d ) -> track.greenPin.set( d ) );
        bluePinField.setUpdater( ( d ) -> track.bluePin.set( d ) );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        StandardFormView form = new StandardFormView();

        form.addField( divisionField );

        form.addField( startPinField );
        form.addField( stopPinField );

        form.addField( redPinField );
        form.addField( greenPinField );
        form.addField( bluePinField );

        return form.getView();
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
    public TrackCfg getData()
    {
        return track;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( TrackCfg track )
    {
        this.track = track;

        divisionField.setValue( track.division );

        startPinField.setValue( track.startPin );
        stopPinField.setValue( track.stopPin );

        redPinField.setValue( track.redPin );
        greenPinField.setValue( track.greenPin );
        bluePinField.setValue( track.bluePin );
    }
}
