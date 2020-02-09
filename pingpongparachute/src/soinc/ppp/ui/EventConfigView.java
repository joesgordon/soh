package soinc.ppp.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.util.List;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.jutils.IconConstants;
import org.jutils.OptionUtils;
import org.jutils.SwingUtils;
import org.jutils.io.options.OptionsSerializer;
import org.jutils.ui.ItemListView;
import org.jutils.ui.ListView;
import org.jutils.ui.ListView.IItemListModel;
import org.jutils.ui.StandardFormView;
import org.jutils.ui.TitleView;
import org.jutils.ui.event.ActionAdapter;
import org.jutils.ui.fields.IntegerFormField;
import org.jutils.ui.model.IDataView;

import soinc.ppp.PppMain;
import soinc.ppp.data.EventConfig;
import soinc.ppp.data.PppOptions;
import soinc.ppp.data.Team;

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
    private final TrackConfigView trackAView;
    /**  */
    private final TrackConfigView trackBView;
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

        this.trackAView = new TrackConfigView();
        this.trackBView = new TrackConfigView();

        this.teamsView = createTeamsView();

        this.view = createView();

        OptionsSerializer<PppOptions> options = PppMain.getOptions();

        this.config = options.getOptions().config;

        setData( config );

        periodTimeField.setUpdater( ( d ) -> config.periodTime = d );
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
     * 
     **************************************************************************/
    public void deselectTeams()
    {
        teamsView.setSelected( null );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        int pad = StandardFormView.DEFAULT_FORM_MARGIN;

        constraints = new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( createForm(), constraints );

        TitleView titleView = new TitleView( "Teams", teamsView.getView() );

        constraints = new GridBagConstraints( 1, 0, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( pad, 0, pad, pad ), 0, 0 );
        panel.add( titleView.getView(), constraints );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createForm()
    {
        StandardFormView form = new StandardFormView();

        form.addField( periodTimeField );

        form.addComponent(
            new TitleView( "Track A", trackAView.getView() ).getView() );
        form.addComponent(
            new TitleView( "Track B", trackBView.getView() ).getView() );

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
    public EventConfig getData()
    {
        return config;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( EventConfig config )
    {
        this.config = config;

        periodTimeField.setValue( config.periodTime );

        trackAView.setData( config.trackA );
        trackBView.setData( config.trackB );

        teamsView.setData( config.teams );
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
