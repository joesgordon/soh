package soh.data;

import org.jutils.INamedItem;

import com.pi4j.io.gpio.PinPullResistance;

public enum PinResistance implements INamedItem
{
    OFF( PinPullResistance.OFF, "Off" ),
    PULL_UP( PinPullResistance.PULL_UP, "Pull Up" ),
    PULL_DOWN( PinPullResistance.PULL_DOWN, "Pull Down" );

    public final PinPullResistance res;
    public final String name;

    private PinResistance( PinPullResistance res, String name )
    {
        this.res = res;
        this.name = name;
    }

    @Override
    public String getName()
    {
        return name;
    }
}
