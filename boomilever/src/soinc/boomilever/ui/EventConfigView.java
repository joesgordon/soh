package soinc.boomilever.ui;

import java.awt.Window;
import java.util.List;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.jutils.IconConstants;
import org.jutils.OptionUtils;
import org.jutils.SwingUtils;
import org.jutils.ui.ItemListView;
import org.jutils.ui.ListView;
import org.jutils.ui.ListView.IItemListModel;
import org.jutils.ui.StandardFormView;
import org.jutils.ui.event.ActionAdapter;
import org.jutils.ui.fields.IntegerFormField;
import org.jutils.ui.model.IDataView;

import soinc.boomilever.data.EventConfig;
import soinc.boomilever.data.Team;

/*******************************************************************************
 *
 ******************************************************************************/
public class EventConfigView implements IDataView<EventConfig>
{
    /**  */
    private final JPanel view;

    /**  */
    private final IntegerFormField periodTimeField;
    /**  */
    private final IntegerFormField periodWarningField;
    /**  */
    private final ItemListView<Team> teamsView;

    /**  */
    private EventConfig config;

    /***************************************************************************
     * 
     **************************************************************************/
    public EventConfigView()
    {
        this.periodTimeField = new IntegerFormField( "Period Time", "seconds",
            8, 10, 60 * 60 );
        this.periodWarningField = new IntegerFormField( "Period Warning",
            "seconds", 8, 5, 60 * 60 );

        this.teamsView = createTeamsView();

        this.view = createView();

        setData( new EventConfig() );

        periodTimeField.setUpdater( ( d ) -> config.periodTime = d );
        periodWarningField.setUpdater( ( d ) -> config.periodWarning = d );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        StandardFormView form = new StandardFormView();

        form.addField( periodTimeField );
        form.addField( periodWarningField );

        return form.getView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JButton createInitButton()
    {
        Action action;
        Icon icon;
        JButton button;

        icon = IconConstants.getIcon( IconConstants.UNDO_16 );
        action = new ActionAdapter( ( e ) -> initAll(), "Initialize All",
            icon );
        button = new JButton( action );

        button.setToolTipText( button.getText() );
        button.setText( "" );

        return button;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void initAll()
    {
        for( Team team : teamsView.getData() )
        {
            team.reset();
        }

        deselectTeams();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private ItemListView<Team> createTeamsView()
    {
        ItemListView<Team> view = new ItemListView<>( new TeamView(),
            new RcTeamsModel() );
        view.addSeparatorToToolbar();
        view.addToToolbar( createInitButton() );
        return view;
    }

    @Override
    public JComponent getView()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public EventConfig getData()
    {
        return config;
    }

    @Override
    public void setData( EventConfig data )
    {
        this.config = data;

        periodTimeField.setValue( config.periodTime );
        periodWarningField.setValue( config.periodWarning );
        teamsView.setData( config.teams );
    }

    public void deselectTeams()
    {
        teamsView.setSelected( null );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class RcTeamsModel implements IItemListModel<Team>
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public String getTitle( Team team )
        {
            return team.name;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Team promptForNew( ListView<Team> view )
        {
            List<Team> teams = view.getData();
            String name = view.promptForName( "Team" );
            Team team = null;

            if( name != null )
            {
                name = name.toUpperCase();

                for( Team t : teams )
                {
                    if( t.name.equalsIgnoreCase( name ) )
                    {
                        Window w = SwingUtils.getComponentsWindow(
                            view.getView() );
                        OptionUtils.showErrorMessage( w,
                            "Duplicate Team name: " + name, "Input Error" );
                        name = null;
                        break;
                    }
                }

                team = new Team( name );
            }

            return team;
        }
    }
}
