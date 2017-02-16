package soh.ui;

import java.awt.*;

import javax.swing.*;

import org.jutils.ui.model.IView;

import soh.SohIcons;
import soh.data.HoverConfig;
import soh.data.TrackType;

/*******************************************************************************
 * 
 ******************************************************************************/
public class CompetitionView implements IView<JComponent>
{
    /**  */
    private final JPanel view;
    /**  */
    private final TrackView track1View;
    /**  */
    private final TrackView track2View;

    /***************************************************************************
     * 
     **************************************************************************/
    public CompetitionView()
    {
        this.track1View = new TrackView( "Track 1" );
        this.track2View = new TrackView( "Track 2" );

        this.view = createView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public JPanel createView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        panel.setOpaque( true );
        panel.setBackground( new Color( 10, 10, 10 ) );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( createHeader(), constraints );

        constraints = new GridBagConstraints( 0, 1, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( createContent(), constraints );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createHeader()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        Icon bannerIcon = SohIcons.getBannerImage();

        JLabel soLabel = new JLabel( bannerIcon );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( soLabel, constraints );

        panel.setOpaque( false );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createContent()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        panel.setOpaque( false );
        // panel.setBorder( new LineBorder( Color.red, 4 ) );

        JComponent sep = Box.createVerticalBox();
        sep.setOpaque( true );
        sep.setBackground( Color.white );
        sep.setPreferredSize( new Dimension( 5, 30 ) );
        sep.setMinimumSize( sep.getPreferredSize() );

        track1View.getView().setPreferredSize( new Dimension( 50, 50 ) );
        track1View.getView().setMinimumSize( new Dimension( 50, 50 ) );
        track1View.getView().setMaximumSize( new Dimension( 50, 50 ) );

        track2View.getView().setPreferredSize( new Dimension( 50, 50 ) );
        track2View.getView().setMinimumSize( new Dimension( 50, 50 ) );
        track2View.getView().setMaximumSize( new Dimension( 50, 50 ) );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 0.5, 1.0,
            GridBagConstraints.NORTH, GridBagConstraints.BOTH,
            new Insets( 20, 40, 20, 40 ), 0, 0 );
        panel.add( track1View.getView(), constraints );

        constraints = new GridBagConstraints( 1, 0, 1, 1, 0.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
            new Insets( 20, 0, 20, 0 ), 0, 0 );
        panel.add( sep, constraints );

        constraints = new GridBagConstraints( 2, 0, 1, 1, 0.5, 1.0,
            GridBagConstraints.NORTH, GridBagConstraints.BOTH,
            new Insets( 20, 40, 20, 40 ), 0, 0 );
        panel.add( track2View.getView(), constraints );

        constraints = new GridBagConstraints( 0, 1, 3, 1, 1.0, 0.0,
            GridBagConstraints.NORTH, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( Box.createHorizontalStrut( 0 ), constraints );

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
     * @param config
     **************************************************************************/
    public void setConfig( HoverConfig config )
    {
        track1View.setConfig( config );
        track2View.setConfig( config );

        // track1View.setPeriodTime( 59 );
        // track1View.setRunaTime( 145, true );
        // track1View.setRunbTime( 63, false );
        // track1View.setFailCount( 2 );
        // track1View.setTeamData( config.teams.get( 0 ) );
    }

    public void startPeriod( TrackType track )
    {
        switch( track )
        {
            case TRACK_1:
            {
                track1View.startPeriod();
                break;
            }
            case TRACK_2:
            {
                track2View.startPeriod();
                break;
            }
        }
    }

    public void failRun( TrackType track )
    {
        switch( track )
        {
            case TRACK_1:
            {
                track1View.failRun();
                break;
            }
            case TRACK_2:
            {
                track2View.failRun();
                break;
            }
        }
    }

    public void clearTrack( TrackType track )
    {
        switch( track )
        {
            case TRACK_1:
            {
                track1View.clearTrack();
                break;
            }
            case TRACK_2:
            {
                track2View.clearTrack();
                break;
            }
        }
    }

    public void startRun( TrackType track )
    {
        switch( track )
        {
            case TRACK_1:
            {
                track1View.startRun();
                break;
            }
            case TRACK_2:
            {
                track2View.startRun();
                break;
            }
        }
    }

    public void stopRun( TrackType track )
    {
        switch( track )
        {
            case TRACK_1:
            {
                track1View.stopRun();
                break;
            }
            case TRACK_2:
            {
                track2View.stopRun();
                break;
            }
        }
    }

    public boolean isRunning()
    {
        return track1View.isRunning() || track2View.isRunning();
    }
}
