package soinc.hovercraft.ui.live;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jutils.ui.model.IView;

import soinc.hovercraft.HovercraftIcons;
import soinc.hovercraft.data.HoverConfig;
import soinc.hovercraft.tasks.HovercraftCompetition;
import soinc.hovercraft.tasks.TrackCompetition;
import soinc.lib.UiUtils;

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
    public CompetitionView( HoverConfig config, HovercraftCompetition hc )
    {
        this.track1View = new TrackView( "Track 1", config, hc.track1 );
        this.track2View = new TrackView( "Track 2", config, hc.track2 );

        this.view = createView();

        setupHotkeys( hc );
    }

    /***************************************************************************
     * @param hc
     **************************************************************************/
    private void setupHotkeys( HovercraftCompetition hc )
    {
        addHotKeys( track1View.getView(), hc.track1, "F1", "F2", "F3", "F4",
            "control 1", "control 2" );

        addHotKeys( track2View.getView(), hc.track2, "F9", "F10", "F11", "F12",
            "control 9", "control 0" );
    }

    /***************************************************************************
     * @param comp
     * @param track
     * @param startPeriodKey
     * @param failKey
     * @param clearKey
     * @param startRunKey
     * @param stopRunKey
     **************************************************************************/
    private static void addHotKeys( JComponent comp, TrackCompetition track,
        String startPeriodKey, String failKey, String resetKey, String clearKey,
        String startRunKey, String stopRunKey )
    {
        UiUtils.addHotKey( comp, startPeriodKey,
            ( e ) -> track.signalStartPauseTrack() );

        UiUtils.addHotKey( comp, failKey, ( e ) -> track.signalFailRun() );

        UiUtils.addHotKey( comp, resetKey, ( e ) -> track.signalResetRun() );

        UiUtils.addHotKey( comp, clearKey, ( e ) -> track.signalClearTrack() );

        UiUtils.addHotKey( comp, startRunKey, ( e ) -> track.signalStartRun() );

        UiUtils.addHotKey( comp, stopRunKey,
            ( e ) -> track.signalCompleteRun() );
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
    private static Component createHeader()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        Icon bannerIcon = HovercraftIcons.getBannerImage();

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
     * @return
     **************************************************************************/
    public boolean isRunning()
    {
        return track1View.isRunning() || track2View.isRunning();
    }
}
