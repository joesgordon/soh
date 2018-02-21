package soinc.rollercoaster;

import java.awt.Image;
import java.util.List;

import javax.swing.Icon;

import org.jutils.io.IconLoader;

public class RollercoasterIcons
{
    /**  */
    private static final IconLoader loader = new IconLoader(
        RollercoasterIcons.class, "icons" );

    /***************************************************************************
     * Private constructor to prevent instantiation.
     **************************************************************************/
    private RollercoasterIcons()
    {
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static List<? extends Image> getRollercoasterIcons()
    {
        return loader.getImages( IconLoader.buildNameList( "rc_" ) );
    }

    /**
     * @return
     */
    public static Icon getRollercoaster16()
    {
        return loader.getIcon( "rc_016.png" );
    }

}
