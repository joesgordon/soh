package soinc.lib.data;

import org.jutils.INamedItem;

import com.pi4j.io.gpio.PinPullResistance;

/*******************************************************************************
 * Defines the resistance of an input pin.
 ******************************************************************************/
public enum PinResistance implements INamedItem
{
    /** No resistance or floating. */
    OFF( PinPullResistance.OFF, "Off" ),
    /**
     * Defines pull-up resistance for general purpose input. See <a
     * href="https://en.wikipedia.org/wiki/Pull-up_resistor">Wikipedia</a>.
     */
    PULL_UP( PinPullResistance.PULL_UP, "Pull Up" ),
    /**
     * Defines pull-down resistance for general purpose input. See <a
     * href="https://en.wikipedia.org/wiki/Pull-up_resistor">Wikipedia</a>.
     */
    PULL_DOWN( PinPullResistance.PULL_DOWN, "Pull Down" );

    /** The pi4j resistance coupled to this enumerated value. */
    public final PinPullResistance res;
    /** The name of this resistance. */
    public final String name;

    /***************************************************************************
     * Creates a new resistance value with the provided parameters.
     * @param res the pi4j resistance coupled to this enumerated value.
     * @param name the name of this resistance.
     **************************************************************************/
    private PinResistance( PinPullResistance res, String name )
    {
        this.res = res;
        this.name = name;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return name;
    }
}
