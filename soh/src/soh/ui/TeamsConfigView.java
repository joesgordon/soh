package soh.ui;

import java.awt.Component;
import java.awt.Window;
import java.util.List;

import org.jutils.SwingUtils;
import org.jutils.ui.ItemListView;
import org.jutils.ui.ListView;
import org.jutils.ui.ListView.IItemListModel;
import org.jutils.ui.model.IDataView;

import soh.data.Team;

public class TeamsConfigView implements IDataView<List<Team>>
{
    private final ItemListView<Team> view;

    public TeamsConfigView()
    {
        this.view = new ItemListView<>( new TeamConfigView(),
            new TeamsModel() );
    }

    @Override
    public Component getView()
    {
        return view.getView();
    }

    @Override
    public List<Team> getData()
    {
        return view.getData();
    }

    @Override
    public void setData( List<Team> data )
    {
        view.setData( data );
    }

    private static final class TeamsModel implements IItemListModel<Team>
    {
        @Override
        public String getTitle( Team item )
        {
            return item.schoolCode;
        }

        @Override
        public Team promptForNew( ListView<Team> view )
        {
            List<Team> teams = view.getData();
            String name = view.promptForName( "Team" );
            Team newTeam = null;

            if( name != null )
            {
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
            }

            return newTeam;
        }
    }
}