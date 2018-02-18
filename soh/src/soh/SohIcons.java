package soh;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.jutils.io.IconLoader;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SohIcons
{
    /**  */
    private static final IconLoader loader = new IconLoader( SohIcons.class,
        "icons" );

    /***************************************************************************
     * @return
     **************************************************************************/
    public static Image getUahImage()
    {
        return loader.getImage( "combined.png" );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static Image getScienceOlympiadImage()
    {
        return loader.getImage( "sci_oly.png" );
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
        return getScaledIcon( img, w, h );
    }

    /***************************************************************************
     * @param size
     * @return
     **************************************************************************/
    public static Icon getCheckIcon( int size )
    {
        return getScaledIcon( loader.getImage( "check.png" ), size, size );
    }

    /***************************************************************************
     * @param size
     * @return
     **************************************************************************/
    public static Icon getXIcon( int size )
    {
        return getScaledIcon( loader.getImage( "fail2.png" ), size, size );
    }

    /***************************************************************************
     * @param srcImg
     * @param w
     * @param h
     * @return
     **************************************************************************/
    private static Icon getScaledIcon( Image srcImg, int w, int h )
    {
        BufferedImage resizedImg = new BufferedImage( w, h,
            BufferedImage.TYPE_INT_ARGB );
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint( RenderingHints.KEY_INTERPOLATION,
            RenderingHints.VALUE_INTERPOLATION_BILINEAR );
        g2.drawImage( srcImg, 0, 0, w, h, null );
        g2.dispose();

        return new ImageIcon( resizedImg );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static List<? extends Image> getSohIcons()
    {
        return loader.getImages( IconLoader.buildNameList( "fan" ) );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static Icon getLevelHigh16()
    {
        return loader.getIcon( "level_high.png" );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static Icon getLevelLow16()
    {
        return loader.getIcon( "level_low.png" );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static Icon getSoh16()
    {
        return loader.getIcon( "fan016.png" );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static Icon getConnect16()
    {
        return loader.getIcon( "connect016.png" );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static Icon getDisconnect16()
    {
        return loader.getIcon( "disconnect016.png" );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static Icon getPinoutIcon()
    {
        return loader.getIcon( "pi3_pinout2.png" );
    }
}
