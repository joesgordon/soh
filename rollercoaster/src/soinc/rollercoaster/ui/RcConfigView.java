package soinc.rollercoaster.ui;

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

import soinc.lib.ui.Pi3InputPinField;
import soinc.lib.ui.Pi3OutputPinField;
import soinc.rollercoaster.RcMain;
import soinc.rollercoaster.data.RcConfig;
import soinc.rollercoaster.data.RcOptions;
import soinc.rollercoaster.data.RcTeam;

/*******************************************************************************
 * 
 ******************************************************************************/
public class RcConfigView implements IDataView<RcConfig>
{
    /**  */
    private final JPanel view;
    /**  */
    private final IntegerFormField periodTimeField;
    /**  */
    private final IntegerFormField targetTimeField;
    /**  */
    private final IntegerFormField trailTimeoutField;
    /**  */
    private final Pi3OutputPinField timerAOutField;
    /**  */
    private final Pi3OutputPinField timerSOutField;
    /**  */
    private final Pi3OutputPinField timerDOutField;
    /**  */
    private final Pi3InputPinField timerAInField;
    /**  */
    private final Pi3InputPinField timerSInField;
    /**  */
    private final Pi3InputPinField timerDInField;
    /**  */
    private final ItemListView<RcTeam> teamsView;

    /**  */
    private RcConfig config;

    /***************************************************************************
     * 
     **************************************************************************/
    public RcConfigView()
    {
        this.periodTimeField = new IntegerFormField( "Period Time", "seconds",
            8, 10, 60 * 60 );
        this.targetTimeField = new IntegerFormField( "TargetTime", "seconds", 8,
            20, 45 );
        this.trailTimeoutField = new IntegerFormField( "Trial Timeout",
            "seconds", 8, 10, 2 * 60 );

        this.timerAInField = new Pi3InputPinField( "Timer A Input" );
        this.timerSInField = new Pi3InputPinField( "Timer S Input" );
        this.timerDInField = new Pi3InputPinField( "Timer D Input" );

        this.timerAOutField = new Pi3OutputPinField( "Timer A Output" );
        this.timerSOutField = new Pi3OutputPinField( "Timer S Output" );
        this.timerDOutField = new Pi3OutputPinField( "Timer D Output" );

        this.teamsView = createTeamsView();

        this.view = createView();

        OptionsSerializer<RcOptions> options = RcMain.getOptions();

        this.config = options.getOptions().config;

        setData( config );

        periodTimeField.setUpdater( ( d ) -> config.periodTime = d );
        targetTimeField.setUpdater( ( d ) -> config.targetTime = d );
        trailTimeoutField.setUpdater( ( d ) -> config.runTimeout = d );

        timerAInField.setUpdater( ( d ) -> config.timerAIn.set( d ) );
        timerSInField.setUpdater( ( d ) -> config.timerSIn.set( d ) );
        timerDInField.setUpdater( ( d ) -> config.timerDIn.set( d ) );

        timerAOutField.setUpdater( ( d ) -> config.timerAOut.set( d ) );
        timerSOutField.setUpdater( ( d ) -> config.timerSOut.set( d ) );
        timerDOutField.setUpdater( ( d ) -> config.timerDOut.set( d ) );
    }

    private ItemListView<RcTeam> createTeamsView()
    {
        ItemListView<RcTeam> view = new ItemListView<>( new RcTeamView(),
            new RcTeamsModel() );
        view.addSeparatorToToolbar();
        view.addToToolbar( createInitButton() );
        return view;
    }

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
        for( RcTeam team : teamsView.getData() )
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
        form.addField( trailTimeoutField );
        form.addField( targetTimeField );

        form.addField( timerAOutField );
        form.addField( timerAInField );

        form.addField( timerSOutField );
        form.addField( timerSInField );

        form.addField( timerDOutField );
        form.addField( timerDInField );

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
    public RcConfig getData()
    {
        return config;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( RcConfig config )
    {
        this.config = config;

        periodTimeField.setValue( config.periodTime );
        trailTimeoutField.setValue( config.runTimeout );
        targetTimeField.setValue( config.targetTime );

        timerAInField.setValue( config.timerAIn );
        timerSInField.setValue( config.timerSIn );
        timerDInField.setValue( config.timerDIn );

        timerAOutField.setValue( config.timerAOut );
        timerSOutField.setValue( config.timerSOut );
        timerDOutField.setValue( config.timerDOut );

        teamsView.setData( config.teams );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class RcTeamsModel implements IItemListModel<RcTeam>
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public String getTitle( RcTeam team )
        {
            return team.name;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public RcTeam promptForNew( ListView<RcTeam> view )
        {
            List<RcTeam> teams = view.getData();
            String name = view.promptForName( "Team" );
            RcTeam team = null;

            if( name != null )
            {
                name = name.toUpperCase();

                for( RcTeam t : teams )
                {
                    if( t.name.equalsIgnoreCase( name ) )
                    {
                        Window w = SwingUtils.getComponentsWindow(
                            view.getView() );
                        SwingUtils.showErrorMessage( w,
                            "Duplicate Team name: " + name, "Input Error" );
                        name = null;
                        break;
                    }
                }

                team = new RcTeam( name );
            }

            return team;
        }
    }

    public void deselectTeams()
    {
        teamsView.setSelected( null );
    }
}
