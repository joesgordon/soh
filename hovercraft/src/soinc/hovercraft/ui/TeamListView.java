package soinc.hovercraft.ui;

import java.awt.Component;
import java.awt.Window;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;

import org.jutils.IconConstants;
import org.jutils.SwingUtils;
import org.jutils.ui.ItemListView;
import org.jutils.ui.ListView;
import org.jutils.ui.ListView.IItemListModel;
import org.jutils.ui.event.ActionAdapter;
import org.jutils.ui.model.IDataView;

import soinc.hovercraft.data.Division;
import soinc.hovercraft.data.Team;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TeamListView implements IDataView<List<Team>>
{
    /**  */
    private final ItemListView<Team> view;

    /**  */
    private List<Team> teams;

    /***************************************************************************
     * 
     **************************************************************************/
    public TeamListView()
    {
        this.view = new ItemListView<>( new TeamView(), new TeamsModel() );

        view.addSeparatorToToolbar();
        view.addToToolbar( createInitButton() );

        setData( new ArrayList<>() );
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
        for( Team t : teams )
        {
            t.initTrials();
        }

        view.setSelected( null );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Component getView()
    {
        return view.getView();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public List<Team> getData()
    {
        return view.getData();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( List<Team> data )
    {
        this.teams = data;

        view.setData( data );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class TeamsModel implements IItemListModel<Team>
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public String getTitle( Team item )
        {
            return item.schoolCode;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Team promptForNew( ListView<Team> view )
        {
            List<Team> teams = view.getData();
            String name = view.promptForName( "Team" );
            Team newTeam = null;

            if( name != null )
            {
                name = name.toUpperCase();

                for( Team t : teams )
                {
                    if( t.schoolCode.equalsIgnoreCase( name ) )
                    {
                        Window w = SwingUtils.getComponentsWindow(
                            view.getView() );
                        SwingUtils.showErrorMessage( w,
                            "Duplicate Team name: " + name, "Input Error" );
                        return null;
                    }
                }

                newTeam = new Team();
                newTeam.schoolCode = name;

                for( Division d : Division.values() )
                {
                    if( d.designation == name.charAt( 0 ) )
                    {
                        newTeam.div = d;
                        break;
                    }
                }
            }

            return newTeam;
        }
    }
}
