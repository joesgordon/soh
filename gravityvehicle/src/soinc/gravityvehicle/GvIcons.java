package soinc.gravityvehicle;

import java.awt.Image;
import java.util.List;

import javax.swing.Icon;

import org.jutils.io.IconLoader;

import soinc.lib.SciolyIcons;

/*******************************************************************************
 * 
 ******************************************************************************/
public class GvIcons
{
    /**  */
    private static final IconLoader loader = new IconLoader( GvIcons.class,
        "icons" );

    /***************************************************************************
     * Private constructor to prevent instantiation.
     **************************************************************************/
    private GvIcons()
    {
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static List<? extends Image> getRollercoasterIcons()
    {
        return loader.getImages( IconLoader.buildNameList( "rc_" ) );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static Icon getRollercoaster16()
    {
        return loader.getIcon( "rc_016.png" );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static Icon getBannerImage()
    {
        Image img = loader.getImage( "banner.png" );
        int w = 1200;
        float scale = w / ( float )img.getWidth( null );
        int h = Math.round( img.getHeight( null ) * scale );
        return SciolyIcons.getScaledIcon( img, w, h );
    }

    /***************************************************************************
     * @param name
     * @return
     **************************************************************************/
    public static Icon getIcon( String name )
    {
        return loader.getIcon( name );
    }

    public static Icon getResizedIcon( String iconName, float percent )
    {
        Image img = loader.getImage( iconName );
        int w = Math.round( img.getWidth( null ) * percent );
        float scale = w / ( float )img.getWidth( null );
        int h = Math.round( img.getHeight( null ) * scale );
        return SciolyIcons.getScaledIcon( img, w, h );
    }
}
