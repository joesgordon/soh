package soh.gpio;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.PinListener;

/*******************************************************************************
 * 
 ******************************************************************************/
public class FauxGpioProvider implements GpioProvider
{
    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void unexport( Pin arg0 )
    {
        // TODO Auto-generated method stub
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void shutdown()
    {
        // TODO Auto-generated method stub
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setValue( Pin arg0, double arg1 )
    {
        // TODO Auto-generated method stub
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setState( Pin arg0, PinState arg1 )
    {
        // TODO Auto-generated method stub
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setPwmRange( Pin arg0, int arg1 )
    {
        // TODO Auto-generated method stub
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setPwm( Pin arg0, int arg1 )
    {
        // TODO Auto-generated method stub
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setPullResistance( Pin arg0, PinPullResistance arg1 )
    {
        // TODO Auto-generated method stub
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setMode( Pin arg0, PinMode arg1 )
    {
        // TODO Auto-generated method stub
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void removeListener( Pin arg0, PinListener arg1 )
    {
        // TODO Auto-generated method stub
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void removeAllListeners()
    {
        // TODO Auto-generated method stub
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean isShutdown()
    {
        // TODO Auto-generated method stub
        return false;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean isExported( Pin arg0 )
    {
        // TODO Auto-generated method stub
        return false;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean hasPin( Pin arg0 )
    {
        // TODO Auto-generated method stub
        return false;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public double getValue( Pin arg0 )
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public PinState getState( Pin arg0 )
    {
        // TODO Auto-generated method stub
        return null;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int getPwm( Pin arg0 )
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public PinPullResistance getPullResistance( Pin arg0 )
    {
        // TODO Auto-generated method stub
        return null;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getName()
    {
        return "RaspberryPi GPIO Provider";
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public PinMode getMode( Pin arg0 )
    {
        // TODO Auto-generated method stub
        return null;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void export( Pin arg0, PinMode arg1, PinState arg2 )
    {
        // TODO Auto-generated method stub
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void export( Pin arg0, PinMode arg1 )
    {
        // TODO Auto-generated method stub
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void addListener( Pin arg0, PinListener arg1 )
    {
        // TODO Auto-generated method stub
    }
}
