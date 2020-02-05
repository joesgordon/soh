package soinc.gravityvehicle.ui;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ScrollPaneConstants;

import org.jutils.ui.model.CollectionListModel;
import org.jutils.ui.model.IView;

import soinc.gravityvehicle.data.HcTeam;
import soinc.lib.UiUtils;

/*******************************************************************************
 * 
 ******************************************************************************/
public class HcTeamsView implements IView<JComponent>
{
    /** The main component. */
    private final JPanel view;
    /** The model of the list. */
    private final CollectionListModel<HcTeam> itemsListModel;
    /** The component to display the list. */
    private final JList<HcTeam> itemsList;

    /***************************************************************************
     * @param config
     **************************************************************************/
    public HcTeamsView( List<HcTeam> teams )
    {
        this.itemsListModel = new CollectionListModel<>();
        this.itemsList = new JList<>( itemsListModel );

        this.view = createView();

        itemsListModel.addAll( teams );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JScrollPane pane = new JScrollPane( itemsList );
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        itemsList.setCellRenderer( new TeamCellRenderer() );

        pane.getVerticalScrollBar().setUnitIncrement( 12 );
        pane.setVerticalScrollBarPolicy(
            ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 8, 8, 8, 8 ), 0, 0 );
        panel.add( pane, constraints );

        return panel;
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
     * @return
     **************************************************************************/
    public HcTeam getSelected()
    {
        return itemsList.getSelectedValue();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class TeamCellRenderer
        implements ListCellRenderer<HcTeam>
    {
        /**  */
        private final DefaultListCellRenderer renderer;
        /**  */
        private final Font f;

        /**
         * 
         */
        public TeamCellRenderer()
        {
            this.renderer = new DefaultListCellRenderer();

            f = UiUtils.getTextFont( 36 );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Component getListCellRendererComponent(
            JList<? extends HcTeam> list, HcTeam value, int index,
            boolean isSelected, boolean cellHasFocus )
        {
            renderer.getListCellRendererComponent( list, value, index,
                isSelected, cellHasFocus );

            renderer.setFont( f );

            if( value != null )
            {
                renderer.setText( value.schoolCode );
            }

            return renderer;
        }
    }
}
