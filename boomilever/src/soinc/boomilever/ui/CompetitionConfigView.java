package soinc.boomilever.ui;

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
import org.jutils.ui.fields.ComboFormField;
import org.jutils.ui.fields.IntegerFormField;
import org.jutils.ui.model.IDataView;

import soinc.boomilever.BlMain;
import soinc.boomilever.data.BlOptions;
import soinc.boomilever.data.BlTeam;
import soinc.boomilever.data.CompetitionConfig;
import soinc.lib.ui.PhysicalKey;

/*******************************************************************************
 * 
 ******************************************************************************/
public class CompetitionConfigView implements IDataView<CompetitionConfig>
{
    /**  */
    private final JPanel view;

    /**  */
    private final IntegerFormField periodTimeField;
    /**  */
    private final IntegerFormField periodWarningField;
    /**  */
    private final IntegerFormField redRelayField;
    /**  */
    private final IntegerFormField greenRelayField;
    /**  */
    private final IntegerFormField blueRelayField;
    /**  */
    private final ComboFormField<PhysicalKey> startPauseKeyField;
    /**  */
    private final ComboFormField<PhysicalKey> clearKeyField;
    /**  */
    private final ItemListView<BlTeam> teamsView;

    /**  */
    private CompetitionConfig config;

    /***************************************************************************
     * 
     **************************************************************************/
    public CompetitionConfigView()
    {
        this.periodTimeField = new IntegerFormField( "Period Time", "seconds",
            8, 10, 60 * 60 );
        this.periodWarningField = new IntegerFormField( "Period Warning",
            "seconds", 8, 5, 60 * 60 );

        this.redRelayField = new IntegerFormField( "Red Relay", null, 8, 0, 8 );
        this.greenRelayField = new IntegerFormField( "Red Relay", null, 8, 0,
            8 );
        this.blueRelayField = new IntegerFormField( "Red Relay", null, 8, 0,
            8 );

        this.startPauseKeyField = new ComboFormField<PhysicalKey>( "Start Key",
            PhysicalKey.values() );
        this.clearKeyField = new ComboFormField<PhysicalKey>( "Clear Key",
            PhysicalKey.values() );

        this.teamsView = createTeamsView();

        this.view = createView();

        OptionsSerializer<BlOptions> options = BlMain.getOptions();

        this.config = options.getOptions().config;

        setData( config );

        periodTimeField.setUpdater( ( d ) -> config.periodTime = d );
        periodWarningField.setUpdater( ( d ) -> config.periodWarning = d );

        redRelayField.setUpdater( ( d ) -> config.redRelay = d );
        greenRelayField.setUpdater( ( d ) -> config.greenRelay = d );
        blueRelayField.setUpdater( ( d ) -> config.blueRelay = d );

        startPauseKeyField.setUpdater( ( d ) -> config.startPauseKey = d );
        clearKeyField.setUpdater( ( d ) -> config.clearKey = d );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private ItemListView<BlTeam> createTeamsView()
    {
        ItemListView<BlTeam> view = new ItemListView<>( new BlTeamView(),
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
        for( BlTeam team : teamsView.getData() )
        {
            team.reset();
        }

        deselectTeams();
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
        form.addField( periodWarningField );

        form.addField( redRelayField );
        form.addField( greenRelayField );
        form.addField( blueRelayField );

        form.addField( startPauseKeyField );
        form.addField( clearKeyField );

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
    public CompetitionConfig getData()
    {
        return config;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( CompetitionConfig config )
    {
        this.config = config;

        periodTimeField.setValue( config.periodTime );
        periodWarningField.setValue( config.periodWarning );

        redRelayField.setValue( config.redRelay );
        greenRelayField.setValue( config.greenRelay );
        blueRelayField.setValue( config.blueRelay );

        startPauseKeyField.setValue( config.startPauseKey );
        clearKeyField.setValue( config.clearKey );

        teamsView.setData( config.teams );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class RcTeamsModel implements IItemListModel<BlTeam>
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public String getTitle( BlTeam team )
        {
            return team.name;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public BlTeam promptForNew( ListView<BlTeam> view )
        {
            List<BlTeam> teams = view.getData();
            String name = view.promptForName( "Team" );
            BlTeam team = null;

            if( name != null )
            {
                name = name.toUpperCase();

                for( BlTeam t : teams )
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

                team = new BlTeam( name );
            }

            return team;
        }
    }

    public void deselectTeams()
    {
        teamsView.setSelected( null );
    }
}
