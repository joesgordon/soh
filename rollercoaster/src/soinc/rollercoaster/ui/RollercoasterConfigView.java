package soinc.rollercoaster.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.util.List;

import javax.swing.JPanel;

import org.jutils.SwingUtils;
import org.jutils.io.options.OptionsSerializer;
import org.jutils.ui.ListView;
import org.jutils.ui.ListView.IItemListModel;
import org.jutils.ui.StandardFormView;
import org.jutils.ui.TitleView;
import org.jutils.ui.fields.IntegerFormField;
import org.jutils.ui.model.IDataView;

import soinc.lib.ui.Pi3InputPinField;
import soinc.lib.ui.Pi3OutputPinField;
import soinc.rollercoaster.RollercoasterMain;
import soinc.rollercoaster.data.RollercoasterConfig;
import soinc.rollercoaster.data.RollercoasterOptions;

/*******************************************************************************
 * 
 ******************************************************************************/
public class RollercoasterConfigView implements IDataView<RollercoasterConfig>
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
    private final Pi3InputPinField timerAInField;
    /**  */
    private final Pi3InputPinField timerSInField;
    /**  */
    private final Pi3InputPinField timerDInField;
    /**  */
    private final Pi3OutputPinField timerAOutField;
    /**  */
    private final Pi3OutputPinField timerSOutField;
    /**  */
    private final Pi3OutputPinField timerDOutField;
    /**  */
    private final ListView<String> teamsView;

    /**  */
    private RollercoasterConfig config;

    /***************************************************************************
     * 
     **************************************************************************/
    public RollercoasterConfigView()
    {
        this.periodTimeField = new IntegerFormField( "Period Time", "seconds",
            8, 10, 60 * 60 );
        this.targetTimeField = new IntegerFormField( "TargetTime", "seconds", 8,
            20, 45 );
        this.trailTimeoutField = new IntegerFormField( "Trial Timeout",
            "seconds", 8, 10, 2 * 60 );

        this.timerAInField = new Pi3InputPinField( "Timer A Input" );
        this.timerSInField = new Pi3InputPinField( "Timer B Input" );
        this.timerDInField = new Pi3InputPinField( "Timer C Input" );

        this.timerAOutField = new Pi3OutputPinField( "Timer A Output" );
        this.timerSOutField = new Pi3OutputPinField( "Timer B Output" );
        this.timerDOutField = new Pi3OutputPinField( "Timer C Output" );

        this.teamsView = new ListView<>( new TeamsModel() );

        this.view = createView();

        OptionsSerializer<RollercoasterOptions> options = RollercoasterMain.getOptions();

        this.config = options.getOptions().config;

        setData( config );

        periodTimeField.setUpdater( ( d ) -> config.periodTime = d );
        targetTimeField.setUpdater( ( d ) -> config.targetTime = d );
        trailTimeoutField.setUpdater( ( d ) -> config.trialTimeout = d );
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

        form.addField( timerAInField );
        form.addField( timerAOutField );

        form.addField( timerSInField );
        form.addField( timerSOutField );

        form.addField( timerDInField );
        form.addField( timerDOutField );

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
    public RollercoasterConfig getData()
    {
        return config;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( RollercoasterConfig config )
    {
        this.config = config;

        periodTimeField.setValue( config.periodTime );
        trailTimeoutField.setValue( config.trialTimeout );
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
    private static final class TeamsModel implements IItemListModel<String>
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public String getTitle( String item )
        {
            return item;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String promptForNew( ListView<String> view )
        {
            List<String> teams = view.getData();
            String name = view.promptForName( "Team" );

            if( name != null )
            {
                name = name.toUpperCase();

                for( String t : teams )
                {
                    if( t.equalsIgnoreCase( name ) )
                    {
                        Window w = SwingUtils.getComponentsWindow(
                            view.getView() );
                        SwingUtils.showErrorMessage( w,
                            "Duplicate Team name: " + name, "Input Error" );
                        name = null;
                        break;
                    }
                }
            }

            return name;
        }
    }
}
