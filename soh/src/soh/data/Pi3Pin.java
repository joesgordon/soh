package soh.data;

import org.jutils.INamedItem;

/*******************************************************************************
 * 
 ******************************************************************************/
public enum Pi3Pin implements INamedItem
{
    PIN_01( 1, PinType.PWR_3V3, "3.3 V",
        "3v3 supply w/ up to ~500 mA current" ),
    PIN_02( 2, PinType.PWR_5V0, "5 V", "5v supply w/ main supply current" ),
    PIN_03( 3, PinType.GPIO, "GPIO 02", "I2C SDA (fixed 1.8kohms pull-up)" ),
    PIN_04( 4, PinType.PWR_5V0, PIN_02.name, PIN_02.description ),
    PIN_05( 5, PinType.GPIO, "GPIO 03", "I2C SCL (fixed 1.8kohms pull-up)" ),
    PIN_06( 6, PinType.GROUND, "GND", "Ground plane" ),
    PIN_07( 7, PinType.GPIO, "GPIO 04", "GPCLK0" ),
    PIN_08( 8, PinType.GPIO, "GPIO 14", "UART TXD" ),
    PIN_09( 9, PinType.GROUND, "GND", "Ground plane" ),
    PIN_10( 10, PinType.GPIO, "GPIO 15", "UART RXD" ),
    PIN_11( 11, PinType.GPIO, "GPIO 17", "" ),
    PIN_12( 12, PinType.GPIO, "GPIO 18", "PWM0" ),
    PIN_13( 13, PinType.GPIO, "GPIO 27", "" ),
    PIN_14( 14, PinType.GROUND, "GND", "Ground plane" ),
    PIN_15( 15, PinType.GPIO, "GPIO 22", "" ),
    PIN_16( 16, PinType.GPIO, "GPIO 23", "" ),
    PIN_17( 17, PinType.PWR_3V3, "3.3 V", "" ),
    PIN_18( 18, PinType.GPIO, "GPIO 24", "" ),
    PIN_19( 19, PinType.GPIO, "GPIO 10", "SPI0 MOSI" ),
    PIN_20( 20, PinType.GROUND, "GND", "Ground plane" ),
    PIN_21( 21, PinType.GPIO, "GPIO 09", "SPI0 MISO" ),
    PIN_22( 22, PinType.GPIO, "GPIO 26", "" ),
    PIN_23( 23, PinType.GPIO, "GPIO 11", "SPI0 SCLK" ),
    PIN_24( 24, PinType.GPIO, "GPIO 08", "" ),
    PIN_25( 25, PinType.GROUND, "GND", "Ground plane" ),
    PIN_26( 26, PinType.GPIO, "GPIO 07", "" ),
    PIN_27( 27, PinType.I2C_EEPROM, "ID SD", "" ),
    PIN_28( 28, PinType.I2C_EEPROM, "ID SC", "" ),
    PIN_29( 29, PinType.GPIO, "GPIO 05", "" ),
    PIN_30( 30, PinType.GROUND, "GND", "Ground plane" ),
    PIN_31( 31, PinType.GPIO, "GPIO 06", "" ),
    PIN_32( 32, PinType.GPIO, "GPIO 12", "" ),
    PIN_33( 33, PinType.GPIO, "GPIO 13", "" ),
    PIN_34( 34, PinType.GROUND, "GND", "Ground plane" ),
    PIN_35( 35, PinType.GPIO, "GPIO 19", "PCM FS" ),
    PIN_36( 36, PinType.GPIO, "GPIO 16", "" ),
    PIN_37( 37, PinType.GPIO, "GPIO 26", "" ),
    PIN_38( 38, PinType.GPIO, "GPIO 20", "PCM DIN" ),
    PIN_39( 39, PinType.GROUND, "GND", "Ground plane" ),
    PIN_40( 40, PinType.GPIO, "GPIO 21", "PCM DOUT" );

    /**  */
    public final int pinout;
    /**  */
    public final PinType type;
    /**  */
    public final String name;
    /**  */
    public final String description;

    /***************************************************************************
     * @param pinout
     * @param type
     * @param name
     * @param description
     **************************************************************************/
    private Pi3Pin( int pinout, PinType type, String name, String description )
    {
        this.pinout = pinout;
        this.type = type;
        this.name = name;
        this.description = description.isEmpty() ? name : description;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getName()
    {
        return name;
    }
}
