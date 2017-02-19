package soh.data;

import org.jutils.INamedItem;

import com.pi4j.io.gpio.PinState;

public enum PinLevel implements INamedItem
{
    HIGH( PinState.HIGH, "High" ),
    LOW( PinState.LOW, "Low" );

    public final PinState state;
    public final String name;

    private PinLevel( PinState state, String name )
    {
        this.state = state;
        this.name = name;
    }

    @Override
    public String getName()
    {
        return name;
    }
}
