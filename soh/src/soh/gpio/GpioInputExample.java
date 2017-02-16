package soh.gpio;
//START SNIPPET: listen-gpio-snippet

import org.jutils.task.*;

/*
* #%L
* **********************************************************************
* ORGANIZATION  :  Pi4J
* PROJECT       :  Pi4J :: Java Examples
* FILENAME      :  ListenGpioExample.java
*
* This file is part of the Pi4J project. More information about
* this project can be found here:  http://www.pi4j.com/
* **********************************************************************
* %%
* Copyright (C) 2012 - 2017 Pi4J
* %%
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU Lesser General Public License as
* published by the Free Software Foundation, either version 3 of the
* License, or (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Lesser Public License for more details.
*
* You should have received a copy of the GNU General Lesser Public
* License along with this program.  If not, see
* <http://www.gnu.org/licenses/lgpl-3.0.html>.
* #L%
*/

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

/**
 * This example code demonstrates how to setup a listener for GPIO pin state
 * changes on the Raspberry Pi.
 * @author Robert Savage
 */
public class GpioInputExample implements ITask
{
    private static void doit( ITaskHandler handler ) throws InterruptedException
    {
        handler.signalPercent( 0 );
        handler.signalPercent( -1 );
        handler.signalMessage( "GPIO listen example started." );

        try
        {
            SohGpio.startup();
        }
        catch( IllegalStateException ex )
        {
            handler.signalError(
                new TaskError( "Setup Error", "Pi4j library was not found" ) );
            return;
        }

        // create gpio controller
        GpioController gpio = GpioFactory.getInstance();
        GpioPinDigitalInput pin = null;

        try
        {
            // provision gpio pin #02 as an input pin with its internal pull
            // down
            // resistor enabled
            pin = gpio.provisionDigitalInputPin( RaspiPin.GPIO_02,
                PinPullResistance.OFF );

            // set shutdown state for this input pin
            pin.setShutdownOptions( true, PinState.LOW, PinPullResistance.OFF );

            // create and register gpio pin listener
            pin.addListener( new GpioPinListenerDigital()
            {
                @Override
                public void handleGpioPinDigitalStateChangeEvent(
                    GpioPinDigitalStateChangeEvent event )
                {
                    // display pin state on console
                    handler.signalMessage( " --> GPIO PIN STATE CHANGE: " +
                        event.getPin() + " = " + event.getState() );
                }

            } );

            handler.signalMessage(
                "Complete the GPIO #02 circuit to see the pin state change..." );

            // keep program running until user aborts (CTRL-C)
            while( handler.canContinue() )
            {
                Thread.sleep( 200 );
            }
        }
        finally
        {
            // stop all GPIO activity/threads by shutting down the GPIO
            // controller (this method will forcefully shutdown all GPIO
            // monitoring threads and scheduled tasks)
            // gpio.shutdown(); // <--- implement this method call if you wish
            // to terminate the Pi4J GPIO controller
            if( pin != null )
            {
                gpio.unprovisionPin( pin );
            }
            // LogUtils.printDebug( "Shutting Down" );
        }
    }

    @Override
    public void run( ITaskHandler handler )
    {
        try
        {
            doit( handler );
        }
        catch( InterruptedException e )
        {
        }
    }

    @Override
    public String getName()
    {
        return "Listen GPIO Example";
    }
}

// END SNIPPET: listen-gpio-snippet
