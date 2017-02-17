package soh.ui;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;

import org.jutils.INamedItem;
import org.jutils.SwingUtils;
import org.jutils.ui.event.ActionAdapter;
import org.jutils.ui.fields.ComboFormField;
import org.jutils.ui.model.IView;

import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;

import soh.SohIcons;
import soh.data.Pi3GpioPin;
import soh.data.Pi3Pin;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PinTestView implements IView<JComponent>
{
    /**  */
    private final JPanel view;

    /***************************************************************************
     * 
     **************************************************************************/
    public PinTestView()
    {
        view = createView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new BorderLayout() );

        JScrollPane pane = new JScrollPane( createTestView() );

        panel.add( createToolbar(), BorderLayout.NORTH );
        panel.add( pane, BorderLayout.CENTER );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createToolbar()
    {
        JToolBar toolbar = new JToolBar();
        Action action;
        JButton button;

        SwingUtils.setToolbarDefaults( toolbar );

        // toolbar.setRollover( false );

        action = createProvAllAction();
        button = SwingUtils.addActionToToolbar( toolbar, action );
        button.setText( action.getValue( Action.NAME ).toString() );

        action = createUnprovAllAction();
        button = SwingUtils.addActionToToolbar( toolbar, action );
        button.setText( action.getValue( Action.NAME ).toString() );

        return toolbar;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Action createProvAllAction()
    {
        // TODO Auto-generated method stub
        return new ActionAdapter( ( e ) -> {
        }, "Provision All", null );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Action createUnprovAllAction()
    {
        // TODO Auto-generated method stub
        return new ActionAdapter( ( e ) -> {
        }, "Unprovision All", null );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createTestView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        JComponent imgPane = new JLabel( SohIcons.getPinoutIcon() );
        GridBagConstraints constraints;

        Pi3Pin [] pins = Pi3Pin.values();

        constraints = new GridBagConstraints( 0, 0, 1, 0, 0.5, 1.0,
            GridBagConstraints.EAST, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( createPanels( pins, true ), constraints );

        constraints = new GridBagConstraints( 1, 0, 1, 0, 0.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( imgPane, constraints );

        constraints = new GridBagConstraints( 2, 0, 1, 0, 0.5, 1.0,
            GridBagConstraints.WEST, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( createPanels( pins, false ), constraints );

        return panel;
    }

    /***************************************************************************
     * @param pins
     * @param isLeft
     * @return
     **************************************************************************/
    private Component createPanels( Pi3Pin [] pins, boolean isLeft )
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;
        int pinIdxStart = isLeft ? 0 : 1;
        int row = 0;

        Border topBorder = new MatteBorder( 1, 0, 1, 0, Color.darkGray );
        Border border = new MatteBorder( 0, 0, 1, 0, Color.darkGray );

        constraints = new GridBagConstraints( 0, row++, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( Box.createVerticalStrut( 6 ), constraints );

        for( int i = 0; i < 20; i++ )
        {
            int idx = pinIdxStart + i * 2;
            Border b = i == 0 ? topBorder : border;
            Pi3Pin pin = pins[idx];
            JPanel p = createPinPanel( pin, isLeft );
            p.setBorder( b );
            constraints = new GridBagConstraints( 0, row++, 1, 1, 1.0, 0.05,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 0, 0, 0, 0 ), 0, 0 );
            panel.add( p, constraints );
        }

        constraints = new GridBagConstraints( 0, row++, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( Box.createVerticalStrut( 8 ), constraints );

        return panel;
    }

    /***************************************************************************
     * @param pin
     * @param isLeft
     * @return
     **************************************************************************/
    private JPanel createPinPanel( Pi3Pin pin, boolean isLeft )
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;
        int alignment = isLeft ? GridBagConstraints.EAST
            : GridBagConstraints.WEST;
        List<Integer> indexes = new ArrayList<>();
        int idx = 0;

        for( int i = 0; i < 4; i++ )
        {
            indexes.add( i );
        }

        if( isLeft )
        {
            Collections.reverse( indexes );
        }

        // ----------------------------------------------------------------------

        // JLabel pinoutLabel = new RotLabel( String.format( "%02d", pin.pinout
        // ), 90 );
        JLabel pinoutLabel = new JLabel( String.format( "%02d", pin.pinout ) );
        pinoutLabel.setVerticalAlignment( SwingConstants.CENTER );

        constraints = new GridBagConstraints( indexes.get( idx++ ), 0, 1, 1,
            0.0, 0.0, alignment, GridBagConstraints.NONE,
            new Insets( 0, 4, 0, 4 ), 0, 0 );
        panel.add( pinoutLabel, constraints );

        // ----------------------------------------------------------------------

        JLabel pinLabel = new JLabel( pin.getName() );
        pinLabel.setVerticalAlignment( SwingConstants.CENTER );

        Dimension dim = pinLabel.getPreferredSize();

        dim.height = 30;

        pinLabel.setPreferredSize( dim );
        pinLabel.setMinimumSize( dim );

        constraints = new GridBagConstraints( indexes.get( idx++ ), 0, 1, 1,
            0.0, 0.0, alignment, GridBagConstraints.NONE,
            new Insets( 0, 4, 0, 4 ), 0, 0 );
        panel.add( pinLabel, constraints );

        // ----------------------------------------------------------------------
        {
            Component comp = Box.createHorizontalStrut( 0 );
            if( Pi3GpioPin.getPin( pin ) != null )
            {
                ComboFormField<IoDirection> dirField = new ComboFormField<>( "",
                    IoDirection.values() );

                dirField.setValue( IoDirection.UNALLOCATED );

                comp = dirField.getView();
            }
            constraints = new GridBagConstraints( indexes.get( idx++ ), 0, 1, 1,
                0.0, 0.0, alignment, GridBagConstraints.NONE,
                new Insets( 0, 4, 0, 4 ), 0, 0 );
            panel.add( comp, constraints );
        }
        // ----------------------------------------------------------------------

        constraints = new GridBagConstraints( indexes.get( idx++ ), 0, 1, 1,
            1.0, 0.0, alignment, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( Box.createHorizontalStrut( 0 ), constraints );

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
    private static final class RotLabel extends JLabel
    {
        /**  */
        private static final long serialVersionUID = 2201205266483152244L;

        /**  */
        private final int degrees;

        public RotLabel( String text, int degrees )
        {
            super( text );

            this.degrees = degrees;
        }

        protected void paintComponent( Graphics g )
        {
            Graphics2D g2 = ( Graphics2D )g;

            g2.setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON );

            AffineTransform aT = g2.getTransform();
            Shape oldshape = g2.getClip();

            aT.rotate( Math.toRadians( degrees ) );
            g2.setTransform( aT );
            g2.setClip( oldshape );

            super.paintComponent( g );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static enum IoDirection
    {
        UNALLOCATED,
        INPUT,
        OUTPUT;
    }

    private static enum PinRes implements INamedItem
    {
        OFF( PinPullResistance.OFF ),
        PULL_UP( PinPullResistance.PULL_UP ),
        PULL_DOWN( PinPullResistance.PULL_DOWN );

        private final PinPullResistance res;

        private PinRes( PinPullResistance res )
        {
            this.res = res;
        }

        @Override
        public String getName()
        {
            return res.getName();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static enum PinLevel implements INamedItem
    {
        HIGH( PinState.HIGH ),
        LOW( PinState.LOW );

        private final PinState state;

        private PinLevel( PinState state )
        {
            this.state = state;
        }

        @Override
        public String getName()
        {
            return state.getName();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class PinData
    {
        public final Pi3Pin pinout;
        public final Pi3GpioPin gpio;
        public IoDirection direction;
        public PinRes pullRes;
        public PinLevel defaultLevel;
        public boolean provisioned;

        public PinData( Pi3Pin pinout )
        {
            this.pinout = pinout;
            this.gpio = Pi3GpioPin.getPin( pinout );
            this.direction = IoDirection.INPUT;
            this.pullRes = PinRes.PULL_UP;
            this.defaultLevel = PinLevel.LOW;
            this.provisioned = false;
        }
    }
}
