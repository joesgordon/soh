package soinc.lib;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.jutils.io.IconLoader;

/***************************************************************************
 *
 **************************************************************************/
public class SciolyIcons
{
    /**  */
    private static final IconLoader loader = new IconLoader( SciolyIcons.class,
        "icons" );

    /***************************************************************************
     * Private constructor to prevent instantiation.
     **************************************************************************/
    private SciolyIcons()
    {
    }

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
     * @param size
     * @return
     **************************************************************************/
    public static Icon getCheckIcon( int size )
    {
        return getScaledIcon( loader.getImage( "check36.png" ), size, size );
    }

    /***************************************************************************
     * @param size
     * @return
     **************************************************************************/
    public static Icon getXIcon( int size )
    {
        return getScaledIcon( loader.getImage( "fail36.png" ), size, size );
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

    /***************************************************************************
     * @param srcImg
     * @param w
     * @param h
     * @return
     **************************************************************************/
    public static Icon getScaledIcon( Image srcImg, int w, int h )
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
}
