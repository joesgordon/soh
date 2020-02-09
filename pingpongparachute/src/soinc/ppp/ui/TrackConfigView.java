package soinc.ppp.ui;

import javax.swing.JPanel;

import org.jutils.ui.StandardFormView;
import org.jutils.ui.model.IDataView;

import soinc.lib.ui.Pi3InputPinField;
import soinc.lib.ui.Pi3OutputPinField;
import soinc.ppp.data.TrackConfig;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TrackConfigView implements IDataView<TrackConfig>
{
    /**  */
    private final JPanel view;

    /**  */
    private final Pi3OutputPinField timer1OutField;
    /**  */
    private final Pi3InputPinField timer1InField;

    /**  */
    private final Pi3OutputPinField timer2OutField;
    /**  */
    private final Pi3InputPinField timer2InField;

    /**  */
    private TrackConfig config;

    /***************************************************************************
     * 
     **************************************************************************/
    public TrackConfigView()
    {
        this.timer1InField = new Pi3InputPinField( "Timer 1 Input" );
        this.timer1OutField = new Pi3OutputPinField( "Timer 1 Output" );

        this.timer2InField = new Pi3InputPinField( "Timer 2 Input" );
        this.timer2OutField = new Pi3OutputPinField( "Timer 2 Output" );

        this.view = createView();

        setData( new TrackConfig() );

        timer1InField.setUpdater( ( d ) -> config.timer1In.set( d ) );
        timer1OutField.setUpdater( ( d ) -> config.timer1Out.set( d ) );

        timer2InField.setUpdater( ( d ) -> config.timer2In.set( d ) );
        timer2OutField.setUpdater( ( d ) -> config.timer2Out.set( d ) );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        StandardFormView form = new StandardFormView();

        form.addField( timer1OutField );
        form.addField( timer1InField );

        form.addField( timer2OutField );
        form.addField( timer2InField );

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

        timer1OutField.setValue( config.timer1Out );
        timer1InField.setValue( config.timer1In );

        timer2OutField.setValue( config.timer2Out );
        timer2InField.setValue( config.timer2In );
    }
}
