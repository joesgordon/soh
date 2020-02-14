package soinc.ppp.ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.util.List;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import org.jutils.IconConstants;
import org.jutils.OptionUtils;
import org.jutils.SwingUtils;
import org.jutils.ui.ItemListView;
import org.jutils.ui.ListView;
import org.jutils.ui.ListView.IItemListModel;
import org.jutils.ui.StandardFormView;
import org.jutils.ui.TitleView;
import org.jutils.ui.event.ActionAdapter;
import org.jutils.ui.fields.IntegerFormField;
import org.jutils.ui.model.IDataView;

import soinc.lib.ui.PhysicalKeyField;
import soinc.lib.ui.Pi3InputPinField;
import soinc.lib.ui.Pi3OutputPinField;
import soinc.ppp.data.EventConfig;
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
    private final IntegerFormField periodWarningField;

    /**  */
    private final Pi3OutputPinField timer1OutField;
    /**  */
    private final Pi3InputPinField timer1InField;
    /**  */
    private final PhysicalKeyField timer1StartStopKeyField;
    /**  */
    private final PhysicalKeyField timer1ClearKeyField;

    /**  */
    private final Pi3OutputPinField timer2OutField;
    /**  */
    private final Pi3InputPinField timer2InField;
    /**  */
    private final PhysicalKeyField timer2StartStopKeyField;
    /**  */
    private final PhysicalKeyField timer2ClearKeyField;

    /**  */
    private final PhysicalKeyField timer3StartStopKeyField;
    /**  */
    private final PhysicalKeyField timer3ClearKeyField;

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
        this.periodWarningField = new IntegerFormField( "Period Warning",
            "seconds", 8, 5, 60 * 60 );

        this.timer1OutField = new Pi3OutputPinField( "Timer 1 Output" );
        this.timer1InField = new Pi3InputPinField( "Timer 1 Input" );
        this.timer1StartStopKeyField = new PhysicalKeyField(
            "Timer 1 Start/Stop Key" );
        this.timer1ClearKeyField = new PhysicalKeyField( "Timer 1 Clear Key" );

        this.timer2OutField = new Pi3OutputPinField( "Timer 2 Output" );
        this.timer2InField = new Pi3InputPinField( "Timer 2 Input" );
        this.timer2StartStopKeyField = new PhysicalKeyField(
            "Timer 2 Start/Stop Key" );
        this.timer2ClearKeyField = new PhysicalKeyField( "Timer 2 Clear Key" );

        this.timer3StartStopKeyField = new PhysicalKeyField(
            "Timer 3 Start/Stop Key" );
        this.timer3ClearKeyField = new PhysicalKeyField( "Timer 3 Clear Key" );

        this.trackAView = new TrackConfigView();
        this.trackBView = new TrackConfigView();

        this.teamsView = createTeamsView();

        this.view = createView();

        this.config = new EventConfig( new EventConfig() );

        setData( config );

        periodTimeField.setUpdater( ( d ) -> config.periodTime = d );
        periodWarningField.setUpdater( ( d ) -> config.periodWarning = d );

        timer1OutField.setUpdater( ( d ) -> config.timer1Out.set( d ) );
        timer1InField.setUpdater( ( d ) -> config.timer1In.set( d ) );
        timer1StartStopKeyField.setUpdater(
            ( d ) -> config.timer1StartStopKey = d );
        timer1ClearKeyField.setUpdater( ( d ) -> config.timer1ClearKey = d );

        timer2OutField.setUpdater( ( d ) -> config.timer2Out.set( d ) );
        timer2InField.setUpdater( ( d ) -> config.timer2In.set( d ) );
        timer2StartStopKeyField.setUpdater(
            ( d ) -> config.timer2StartStopKey = d );
        timer2ClearKeyField.setUpdater( ( d ) -> config.timer2ClearKey = d );

        timer3StartStopKeyField.setUpdater(
            ( d ) -> config.timer3StartStopKey = d );
        timer3ClearKeyField.setUpdater( ( d ) -> config.timer3ClearKey = d );
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
    private JComponent createForm()
    {
        StandardFormView form = new StandardFormView();

        form.addField( periodTimeField );
        form.addField( periodWarningField );

        form.addField( timer1OutField );
        form.addField( timer1InField );
        form.addField( timer1StartStopKeyField );
        form.addField( timer1ClearKeyField );

        form.addField( timer2OutField );
        form.addField( timer2InField );
        form.addField( timer2StartStopKeyField );
        form.addField( timer2ClearKeyField );

        form.addField( timer3StartStopKeyField );
        form.addField( timer3ClearKeyField );

        form.addComponent(
            new TitleView( "Track A", trackAView.getView() ).getView() );
        form.addComponent(
            new TitleView( "Track B", trackBView.getView() ).getView() );

        JScrollPane pane = new JScrollPane( form.getView() );

        pane.setBorder( new EmptyBorder( 0, 0, 0, 0 ) );
        pane.getVerticalScrollBar().setUnitIncrement( 10 );

        pane.setMinimumSize(
            new Dimension( form.getView().getPreferredSize().width + 18, 0 ) );

        return pane;
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
        periodWarningField.setValue( config.periodWarning );

        timer1OutField.setValue( config.timer1Out );
        timer1InField.setValue( config.timer1In );
        timer1ClearKeyField.setValue( config.timer1ClearKey );

        timer2OutField.setValue( config.timer2Out );
        timer2InField.setValue( config.timer2In );
        timer2ClearKeyField.setValue( config.timer2ClearKey );

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
