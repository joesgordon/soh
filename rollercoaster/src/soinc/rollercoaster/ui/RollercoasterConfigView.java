package soinc.rollercoaster.ui;

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

import soinc.rollercoaster.RollercoasterMain;
import soinc.rollercoaster.data.RollercoasterConfig;
import soinc.rollercoaster.data.RollercoasterUserData;

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
    private final ListView<String> teamsView;

    /**  */
    private RollercoasterConfig config;

    /***************************************************************************
     * 
     **************************************************************************/
    public RollercoasterConfigView()
    {
        this.periodTimeField = new IntegerFormField( "Period Time", "seconds",
            10, 60 * 60 );
        this.targetTimeField = new IntegerFormField( "TargetTime", "seconds",
            20, 45 );
        this.trailTimeoutField = new IntegerFormField( "Trial Timeout",
            "seconds", 10, 2 * 60 );
        this.teamsView = new ListView<>( new TeamsModel() );

        this.view = createView();

        OptionsSerializer<RollercoasterUserData> options = RollercoasterMain.getUserOptions();

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
        StandardFormView form = new StandardFormView();
        TitleView titleView = new TitleView( "Teams", teamsView.getView() );

        form.addField( periodTimeField );
        form.addField( trailTimeoutField );
        form.addField( targetTimeField );
        form.addComponent( titleView.getView() );

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
