package soinc.rollercoaster;

public interface IRcSignals
{
    public void connect();

    public void disconnect();

    public void setLights( boolean red, boolean green, boolean blue );

    public int getTimerCount();

    public void setTimerCallback( int index, Runnable callback );
}
