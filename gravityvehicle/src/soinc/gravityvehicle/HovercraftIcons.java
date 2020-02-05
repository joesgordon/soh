package soinc.gravityvehicle;

import java.awt.Image;
import java.util.List;

import javax.swing.Icon;

import org.jutils.io.IconLoader;

import soinc.lib.SciolyIcons;

/*******************************************************************************
 * 
 ******************************************************************************/
public class HovercraftIcons
{
    /**  */
    private static final IconLoader loader = new IconLoader(
        HovercraftIcons.class, "icons" );

    /***************************************************************************
     * Private constructor to prevent instantiation.
     **************************************************************************/
    private HovercraftIcons()
    {
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
     * @return
     **************************************************************************/
    public static List<? extends Image> getHovercraftIcons()
    {
        return loader.getImages( IconLoader.buildNameList( "fan" ) );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static Icon getHovercraft16()
    {
        return loader.getIcon( "fan016.png" );
    }
}
