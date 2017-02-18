package soh.ui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;

import org.jutils.SwingUtils;
import org.jutils.ui.event.ActionAdapter;
import org.jutils.ui.model.IDataView;

import soh.SohIcons;
import soh.data.*;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PinTestSuiteView implements IDataView<PinTestSuite>
{
    /**  */
    private final JPanel view;
    /**  */
    private final List<PinTestView> pinViews;

    /**  */
    private PinTestSuite suite;

    /***************************************************************************
     * 
     **************************************************************************/
    public PinTestSuiteView()
    {
        this.pinViews = createPinViews( createPins() );
        this.view = createView();

        setData( new PinTestSuite() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private static List<PinData> createPins()
    {
        List<PinData> pins = new ArrayList<>();

        for( Pi3Pin p : Pi3Pin.values() )
        {
            pins.add( new PinData( p ) );
        }

        return pins;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private static List<PinTestView> createPinViews( List<PinData> pins )
    {
        List<PinTestView> views = new ArrayList<>( pins.size() );

        for( int i = 0; i < pins.size(); i++ )
        {
            PinData pd = pins.get( i );
            PinTestView view = new PinTestView( ( i % 2 ) == 0 );

            view.setData( pd );

            views.add( view );
        }

        return views;
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
        return new ActionAdapter( ( e ) -> provisionAll(), "Provision All",
            SohIcons.getConnect16() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Action createUnprovAllAction()
    {
        return new ActionAdapter( ( e ) -> unprovisionAll(), "Unprovision All",
            SohIcons.getDisconnect16() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createTestView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        JComponent imgPane = new JLabel( SohIcons.getPinoutIcon() );
        GridBagConstraints constraints;

        constraints = new GridBagConstraints( 0, 0, 1, 0, 0.5, 1.0,
            GridBagConstraints.NORTHEAST, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( createPanels( true ), constraints );

        constraints = new GridBagConstraints( 1, 0, 1, 0, 0.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( imgPane, constraints );

        constraints = new GridBagConstraints( 2, 0, 1, 0, 0.5, 1.0,
            GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( createPanels( false ), constraints );

        return panel;
    }

    /***************************************************************************
     * @param pins
     * @param isLeft
     * @return
     **************************************************************************/
    private Component createPanels( boolean isLeft )
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;
        int pinIdxStart = isLeft ? 0 : 1;
        int row = 0;

        Border topBorder = new MatteBorder( 1, 0, 1, 0, Color.darkGray );
        Border border = new MatteBorder( 0, 0, 1, 0, Color.darkGray );

        JComponent box;

        box = ( JComponent )Box.createVerticalStrut( 6 );
        // box.setBorder( new LineBorder( Color.red ) );
        constraints = new GridBagConstraints( 0, row++, 1, 1, 0.0, 0.5,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( box, constraints );

        for( int i = 0; i < 20; i++ )
        {
            int idx = pinIdxStart + i * 2;
            Border b = i == 0 ? topBorder : border;
            PinTestView p = pinViews.get( idx );
            p.getView().setBorder( b );
            constraints = new GridBagConstraints( 0, row++, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 0, 0, 0, 0 ), 0, 0 );
            panel.add( p.getView(), constraints );
        }

        box = ( JComponent )Box.createVerticalStrut( 6 );
        // box.setBorder( new LineBorder( Color.red ) );
        constraints = new GridBagConstraints( 0, row++, 1, 1, 0.0, 0.5,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( box, constraints );

        // panel.setBorder( new LineBorder( Color.green ) );

        return panel;
    }

    private void provisionAll()
    {
        for( PinTestView ptv : pinViews )
        {
            ptv.provision();
        }
    }

    private void unprovisionAll()
    {
        for( PinTestView ptv : pinViews )
        {
            ptv.unprovision();
        }
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
    public PinTestSuite getData()
    {
        return suite;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( PinTestSuite suite )
    {
        this.suite = suite;

        if( suite.pins.size() == pinViews.size() )
        {
            for( int i = 0; i < pinViews.size(); i++ )
            {
                PinData pin = suite.pins.get( i );
                PinTestView pv = pinViews.get( i );

                if( pin.gpio != null )
                {
                    pv.setData( pin );
                }
            }
        }
    }
}
