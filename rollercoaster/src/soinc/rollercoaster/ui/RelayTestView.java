package soinc.rollercoaster.ui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jutils.ui.LedIcon;
import org.jutils.ui.LedLabel;
import org.jutils.ui.StandardFormView;
import org.jutils.ui.model.IView;

import soinc.rollercoaster.RcMain;
import soinc.rollercoaster.relay.IRelays;

public class RelayTestView implements IView<JComponent>
{
    private final IRelays relay;
    private final JPanel view;

    public RelayTestView()
    {
        this.relay = RcMain.getRelay();
        this.view = createView();
    }

    private JPanel createView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        int pad = StandardFormView.DEFAULT_FORM_MARGIN;

        for( int i = 0; i < relay.getRelayCount(); i++ )
        {
            RelayView rv = new RelayView( relay, i );

            int p = i > 0 ? pad : 0;

            constraints = new GridBagConstraints( 0, i, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets( p, 0, 0, 0 ), 0, 0 );
            panel.add( rv.getView(), constraints );

        }
        // TODO Auto-generated method stub

        return panel;
    }

    public JComponent getView()
    {
        return view;
    }

    private static final class RelayView implements IView<JComponent>
    {
        private static final Color ON_COLOR = Color.GREEN;
        private static final Color OFF_COLOR = ON_COLOR.darker().darker().darker();

        private final IRelays relay;
        private final int index;
        private final JPanel view;
        private final LedLabel statusLabel;

        public RelayView( IRelays relay, int index )
        {
            this.relay = relay;
            this.index = index;

            this.statusLabel = new LedLabel( ON_COLOR, OFF_COLOR, 16 );

            this.view = createView();
        }

        private JPanel createView()
        {
            JPanel panel = new JPanel( new GridBagLayout() );
            GridBagConstraints constraints;

            int pad = StandardFormView.DEFAULT_FORM_MARGIN;

            JButton onButton = new JButton( "Turn On",
                new LedIcon( ON_COLOR ) );
            onButton.addActionListener( ( e ) -> setRelay( true ) );

            JButton offButton = new JButton( "Turn Off",
                new LedIcon( OFF_COLOR ) );
            offButton.addActionListener( ( e ) -> setRelay( false ) );

            constraints = new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets( 0, 0, 0, 0 ), 0, 0 );
            panel.add( new JLabel( "Relay " + index ), constraints );

            constraints = new GridBagConstraints( 1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets( 0, pad, 0, 0 ), 0, 0 );
            panel.add( onButton, constraints );

            constraints = new GridBagConstraints( 2, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets( 0, pad, 0, 0 ), 0, 0 );
            panel.add( offButton, constraints );

            constraints = new GridBagConstraints( 3, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets( 0, pad, 0, 0 ), 0, 0 );
            panel.add( statusLabel.getView(), constraints );

            return panel;
        }

        private void setRelay( boolean isOn )
        {
            relay.setRelay( index, isOn );
            statusLabel.setLit( relay.isRelayOn( index ) );
        }

        @Override
        public JComponent getView()
        {
            return view;
        }
    }
}
