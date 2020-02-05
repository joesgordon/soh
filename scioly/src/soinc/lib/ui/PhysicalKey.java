package soinc.lib.ui;

/**
 * @author
 */
public enum PhysicalKey
{
    ENTER( "ENTER" ),
    SPACE( "SPACE" ),
    F1( "F1" ),
    F2( "F2" ),
    F3( "F3" ),
    F4( "F4" ),
    F5( "F5" ),
    F6( "F6" ),
    F7( "F7" ),
    F8( "F8" ),
    F9( "F9" ),
    F10( "F10" ),
    F11( "F11" ),
    F12( "F12" ),
    Q( "Q" ),
    W( "W" ),
    E( "E" ),
    R( "R" ),
    T( "T" ),
    Y( "Y" ),
    U( "U" ),
    I( "I" ),
    O( "O" ),
    P( "P" ),
    A( "A" ),
    S( "S" ),
    D( "D" ),
    F( "F" ),
    G( "G" ),
    H( "H" ),
    J( "J" ),
    K( "K" ),
    L( "L" ),
    Z( "Z" ),
    X( "X" ),
    C( "C" ),
    V( "V" ),
    B( "B" ),
    N( "N" ),
    M( "M" ),
    K1( "1" ),
    K2( "2" ),
    K3( "3" ),
    K4( "4" ),
    K5( "5" ),
    K6( "6" ),
    K7( "7" ),
    K8( "8" ),
    K9( "9" ),
    K0( "0" );

    public final String keystroke;

    private PhysicalKey( String keystroke )
    {
        this.keystroke = keystroke;
    }
}
