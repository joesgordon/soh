package soinc.lib.ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jutils.ui.event.updater.WrappedUpdater;
import org.jutils.ui.fields.ComboFormField;
import org.jutils.ui.fields.NamedItemDescriptor;
import org.jutils.ui.model.IDataView;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

import soinc.lib.SciolyIcons;
import soinc.lib.data.GpioPinDirection;
import soinc.lib.data.PinData;
import soinc.lib.data.PinLevel;
import soinc.lib.data.PinResistance;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PinDataTestView implements IDataView<PinData>
{
    /**  */
    private final JComponent view;
    /**  */
    private final JLabel pinoutLabel;
    /**  */
    private final JLabel pinLabel;
    /**  */
    private final ComboFormField<GpioPinDirection> dirField;
    /**  */
    private final ComboFormField<PinResistance> pullField;
    /**  */
    private final ComboFormField<PinLevel> levelField;
    /**  */
    private final JButton provisionButton;
    /**  */
    private final JButton levelButton;
    /**  */
    private final JLabel levelLabel;

    /**  */
    private final Icon lowIcon;
    /**  */
    private final Icon highIcon;
    /**  */
    private final Icon provisionIcon;
    /**  */
    private final Icon unprovisionIcon;

    /**  */
    private PinData data;
    /**  */
    private GpioPin gpin;

    /***************************************************************************
     * @param isLeft
     **************************************************************************/
    public PinDataTestView( boolean isLeft )
    {
        this.pinoutLabel = new JLabel();
        this.pinLabel = new JLabel();
        this.dirField = new ComboFormField<>( "", GpioPinDirection.values(),
            new NamedItemDescriptor<>() );
        this.pullField = new ComboFormField<>( "", PinResistance.values(),
            new NamedItemDescriptor<>() );
        this.levelField = new ComboFormField<>( "", PinLevel.values(),
            new NamedItemDescriptor<>() );
        this.provisionButton = new JButton();
        this.levelButton = new JButton();
        this.levelLabel = new JLabel( SciolyIcons.getLevelLow16() );

        this.lowIcon = SciolyIcons.getLevelLow16();
        this.highIcon = SciolyIcons.getLevelHigh16();

        this.provisionIcon = SciolyIcons.getConnect16();
        this.unprovisionIcon = SciolyIcons.getDisconnect16();

        this.view = createView( isLeft );

        // setData( new PinData( Pi3Pin.PIN_01 ) );

        dirField.setUpdater( new WrappedUpdater<>( ( d ) -> data.direction = d,
            ( d ) -> handleDirectionChanged( d ) ) );
        pullField.setUpdater( ( d ) -> data.pullRes = d );
        levelField.setUpdater( ( d ) -> data.defaultLevel = d );
        provisionButton.addActionListener( ( e ) -> toggleProvisioning() );
        levelButton.addActionListener( ( e ) -> toggleOutputLevel() );
    }

    /***************************************************************************
     * @param dir
     **************************************************************************/
    private void handleDirectionChanged( GpioPinDirection dir )
    {
        boolean isGpio = data.gpio != null;
        pullField.getView().setVisible(
            isGpio && dir == GpioPinDirection.INPUT );
        levelField.getView().setVisible(
            isGpio && dir == GpioPinDirection.OUTPUT );
    }

    /***************************************************************************
     * @param isLeft
     * @return
     **************************************************************************/
    private JComponent createView( boolean isLeft )
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;
        int alignment = isLeft ? GridBagConstraints.EAST
            : GridBagConstraints.WEST;
        List<Integer> indexes = new ArrayList<>();
        int idx = 0;

        for( int i = 0; i < 10; i++ )
        {
            indexes.add( i );
        }

        if( isLeft )
        {
            Collections.reverse( indexes );
        }

        // ----------------------------------------------------------------------

        constraints = new GridBagConstraints( indexes.get( idx++ ), 0, 1, 1,
            0.0, 0.0, alignment, GridBagConstraints.NONE,
            new Insets( 0, 4, 0, 2 ), 0, 0 );
        panel.add( pinoutLabel, constraints );

        // ---------------------------------------------------------------------

        // pinLabel.setVerticalAlignment( SwingConstants.CENTER );
        //
        // Dimension dim = pinLabel.getPreferredSize();
        //
        // dim.height = 30;
        //
        // pinLabel.setPreferredSize( dim );
        // pinLabel.setMinimumSize( dim );

        constraints = new GridBagConstraints( indexes.get( idx++ ), 0, 1, 1,
            0.0, 0.0, alignment, GridBagConstraints.NONE,
            new Insets( 0, 2, 0, 2 ), 0, 0 );
        panel.add( pinLabel, constraints );

        // ---------------------------------------------------------------------

        dirField.setValue( GpioPinDirection.UNALLOCATED );

        constraints = new GridBagConstraints( indexes.get( idx++ ), 0, 1, 1,
            0.0, 0.0, alignment, GridBagConstraints.NONE,
            new Insets( 0, 2, 0, 2 ), 0, 0 );
        panel.add( dirField.getView(), constraints );

        // ---------------------------------------------------------------------

        constraints = new GridBagConstraints( indexes.get( idx++ ), 0, 1, 1,
            0.0, 0.0, alignment, GridBagConstraints.NONE,
            new Insets( 0, 2, 0, 2 ), 0, 0 );
        panel.add( pullField.getView(), constraints );

        // ---------------------------------------------------------------------

        constraints = new GridBagConstraints( indexes.get( idx++ ), 0, 1, 1,
            0.0, 0.0, alignment, GridBagConstraints.NONE,
            new Insets( 0, 2, 0, 2 ), 0, 0 );
        panel.add( levelField.getView(), constraints );

        // ---------------------------------------------------------------------

        constraints = new GridBagConstraints( indexes.get( idx++ ), 0, 1, 1,
            0.0, 0.0, alignment, GridBagConstraints.NONE,
            new Insets( 0, 2, 0, 2 ), 0, 0 );
        panel.add( provisionButton, constraints );

        // ---------------------------------------------------------------------

        constraints = new GridBagConstraints( indexes.get( idx++ ), 0, 1, 1,
            0.0, 0.0, alignment, GridBagConstraints.NONE,
            new Insets( 0, 2, 0, 2 ), 0, 0 );
        panel.add( levelButton, constraints );

        // ---------------------------------------------------------------------

        constraints = new GridBagConstraints( indexes.get( idx++ ), 0, 1, 1,
            1.0, 0.0, alignment, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( Box.createHorizontalStrut( 0 ), constraints );

        // ---------------------------------------------------------------------

        constraints = new GridBagConstraints( indexes.get( idx++ ), 0, 1, 1,
            0.0, 0.0, alignment, GridBagConstraints.NONE,
            new Insets( 0, 2, 0, 4 ), 0, 0 );
        panel.add( levelLabel, constraints );

        // ---------------------------------------------------------------------

        panel.setPreferredSize( new Dimension( 30, 43 ) );
        panel.setMinimumSize( panel.getPreferredSize() );

        return panel;
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
    public PinData getData()
    {
        return data;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( PinData data )
    {
        this.data = data;

        pinoutLabel.setText( String.format( "%02d", data.pinout.pinout ) );
        pinLabel.setText( data.pinout.getName() );

        // LogUtils.printDebug( "name: %s", data.pinout.getName() );
        // LogUtils.printDebug( "text: %s (%s)", pinLabel.getText(),
        // data.pinout.getName() );

        if( data.gpio != null )
        {
            pullField.setValues( PinResistance.getValues(
                data.gpio.hwPin.getSupportedPinPullResistance() ) );
            dirField.setValues( GpioPinDirection.getValues(
                data.gpio.hwPin.getSupportedPinModes() ) );
        }

        dirField.setValue( data.direction );
        pullField.setValue( data.pullRes );
        levelField.setValue( data.defaultLevel );

        handleDirectionChanged( data.direction );

        dirField.getView().setVisible( data.gpio != null );
        provisionButton.setVisible( data.gpio != null );
        provisionButton.setIcon(
            data.provisioned ? unprovisionIcon : provisionIcon );

        dirField.setEditable( !data.provisioned );
        pullField.setEditable( !data.provisioned );
        levelField.setEditable( !data.provisioned );
        levelButton.setVisible(
            data.provisioned && data.direction == GpioPinDirection.OUTPUT );

        setLevelButtonState( data.defaultLevel.state );

        switch( data.pinout.type )
        {
            case PWR_3V3:
                levelLabel.setIcon( highIcon );
                break;
            case PWR_5V0:
                levelLabel.setIcon( highIcon );
                break;
            case GROUND:
                levelLabel.setIcon( lowIcon );
            default:
                levelLabel.setIcon( lowIcon );
                break;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void provision()
    {
        if( !data.provisioned && data.gpio != null &&
            data.direction != GpioPinDirection.UNALLOCATED )
        {
            GpioController gpio = GpioFactory.getInstance();

            // LogUtils.printDebug( "Provisioning %s", data.gpio.getName() );

            if( data.direction == GpioPinDirection.INPUT )
            {
                gpin = gpio.provisionDigitalInputPin( data.gpio.hwPin,
                    data.pullRes.res );
            }
            else
            {
                gpin = gpio.provisionDigitalOutputPin( data.gpio.hwPin,
                    data.gpio.getName(), data.defaultLevel.state );
            }
            data.provisioned = true;

            gpin.setShutdownOptions( true, PinState.LOW,
                PinPullResistance.OFF );

            GpioPinListenerDigital l = ( e ) -> handlePinStateChanged( e );
            gpin.addListener( l );

            setData( data );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void unprovision()
    {
        if( data.provisioned )
        {
            GpioController gpio = GpioFactory.getInstance();

            // LogUtils.printDebug( "Unprovisioning %s", data.gpio.getName() );

            gpio.unprovisionPin( gpin );
            data.provisioned = false;

            setData( data );
        }
    }

    /***************************************************************************
     * @param state
     **************************************************************************/
    private void setLevelButtonState( PinState state )
    {
        if( state == PinState.HIGH )
        {
            levelButton.setIcon( lowIcon );
            levelButton.setText( "-> Low" );
        }
        else
        {
            levelButton.setIcon( highIcon );
            levelButton.setText( "-> High" );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void toggleProvisioning()
    {
        if( data.provisioned )
        {
            unprovision();
        }
        else
        {
            provision();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void toggleOutputLevel()
    {
        if( gpin instanceof GpioPinDigitalOutput )
        {
            ( ( GpioPinDigitalOutput )gpin ).toggle();
        }
    }

    /***************************************************************************
     * @param event
     **************************************************************************/
    private void handlePinStateChanged( GpioPinDigitalStateChangeEvent event )
    {
        PinState state = event.getState();

        setLevelButtonState( state );

        if( state == PinState.HIGH )
        {
            levelLabel.setIcon( highIcon );
        }
        else
        {
            levelLabel.setIcon( lowIcon );
        }
    }
}
