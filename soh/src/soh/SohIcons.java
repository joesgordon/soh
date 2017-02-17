package soh;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.jutils.IconLoader;
import org.jutils.io.LogUtils;

public class SohIcons
{
    private static final IconLoader loader = new IconLoader( SohIcons.class,
        "icons" );

    public static Image getUahImage()
    {
        return loader.getImage( "combined.png" );
    }

    public static Image getScienceOlympiadImage()
    {
        return loader.getImage( "sci_oly.png" );
    }

    public static Icon getBannerImage()
    {
        return loader.getIcon( "banner.png" );
    }

    public static Icon getCheckIcon( int size )
    {
        return getScaledIcon( loader.getImage( "check.png" ), size, size );
    }

    public static Icon getXIcon( int size )
    {
        return getScaledIcon( loader.getImage( "fail2.png" ), size, size );
    }

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

    public static List<? extends Image> getSohIcons()
    {
        return loader.getImages( IconLoader.buildNameList( "fan" ) );
    }

    public static Icon getSoh16()
    {
        return loader.getIcon( "fan016.png" );
    }

    public static Icon getPinoutIcon()
    {
        return loader.getIcon( "pi3_pinout2.png" );
    }

    public static Icon getPinoutIcon_old()
    {
        Image img = loader.getImage( "pi3_pinout.png" );

        float scale = 0.401f;
        int h = Math.round( img.getHeight( null ) * scale );
        LogUtils.printDebug( "height = %d", h );
        int w = Math.round( img.getWidth( null ) * scale );

        return getScaledIcon( img, w, h );
    }
}
