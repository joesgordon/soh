package soh.ui;

import java.awt.*;
import java.util.List;

import javax.swing.*;

import org.jutils.ui.model.CollectionListModel;
import org.jutils.ui.model.IView;

import soh.data.Team;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TeamsView implements IView<JComponent>
{
    /** The main component. */
    private final JPanel view;
    /** The model of the list. */
    private final CollectionListModel<Team> itemsListModel;
    /** The component to display the list. */
    private final JList<Team> itemsList;

    /***************************************************************************
     * @param config
     **************************************************************************/
    public TeamsView( List<Team> teams )
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
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        itemsList.setCellRenderer( new TeamCellRenderer() );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 8, 8, 8, 8 ), 0, 0 );
        panel.add( itemsList, constraints );

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
     * @return
     **************************************************************************/
    public Team getSelected()
    {
        return itemsList.getSelectedValue();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class TeamCellRenderer
        implements ListCellRenderer<Team>
    {
        private final DefaultListCellRenderer renderer;

        private final Font f;

        public TeamCellRenderer()
        {
            this.renderer = new DefaultListCellRenderer();

            f = ( UiUtils.getTextFont( 36 ) );
        }

        @Override
        public Component getListCellRendererComponent(
            JList<? extends Team> list, Team value, int index,
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
