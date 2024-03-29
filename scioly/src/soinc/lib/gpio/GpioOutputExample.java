package soinc.lib.gpio;

import java.util.ArrayList;
import java.util.List;

import org.jutils.concurrent.ITaskHandler;
import org.jutils.task.IStatusTask;
import org.jutils.task.ITaskStatusHandler;
import org.jutils.task.TaskError;

//START SNIPPET: control-gpio-snippet

/*
* #%L
* **********************************************************************
* ORGANIZATION  :  Pi4J
* PROJECT       :  Pi4J :: Java Examples
* FILENAME      :  ControlGpioExample.java
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
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;

import soinc.lib.data.Pi3GpioPin;

/**
 * This example code demonstrates how to perform simple state control of a GPIO
 * pin on the Raspberry Pi.
 * @author Robert Savage
 */
public class GpioOutputExample implements IStatusTask
{
    private static void doit( ITaskStatusHandler handler )
        throws InterruptedException
    {
        handler.signalMessage( "<--Pi4J--> GPIO Control Example ... started." );

        try
        {
            SciolyGpio.startup();
        }
        catch( IllegalStateException ex )
        {
            handler.signalError(
                new TaskError( "Setup Error", "Pi4j library was not found" ) );
            return;
        }

        // create gpio controller
        GpioController gpio = GpioFactory.getInstance();
        Pin [] gpios = new Pin[] { Pi3GpioPin.GPIO_05.hwPin,
            Pi3GpioPin.GPIO_06.hwPin, Pi3GpioPin.GPIO_13.hwPin,
            Pi3GpioPin.GPIO_19.hwPin, Pi3GpioPin.GPIO_26.hwPin,
            Pi3GpioPin.GPIO_12.hwPin, Pi3GpioPin.GPIO_16.hwPin,
            Pi3GpioPin.GPIO_20.hwPin, Pi3GpioPin.GPIO_21.hwPin };
        List<GpioPinDigitalOutput> pins = new ArrayList<>();

        try
        {
            for( int i = 0; i < gpios.length; i++ )
            {
                // provision gpio pin #01 as an output pin and turn on
                String name = "Output " + ( i + 21 );
                Pin gpiop = gpios[i];
                GpioPinDigitalOutput pin = gpio.provisionDigitalOutputPin(
                    gpiop, name, PinState.HIGH );

                // set shutdown state for this pin
                pin.setShutdownOptions( true, PinState.LOW,
                    PinPullResistance.OFF );
                pins.add( pin );
            }

            handler.signalPercent( 20 );
            handler.signalMessage( "--> GPIO state should be: ON" );

            if( !waitCheck( handler, 5 * 5 ) )
            {
                return;
            }

            // turn off gpio pin #01
            for( GpioPinDigitalOutput gpdo : pins )
            {
                gpdo.low();
            }
            handler.signalPercent( 40 );
            handler.signalMessage( "--> GPIO state should be: OFF" );

            if( !waitCheck( handler, 5 * 5 ) )
            {
                return;
            }

            // toggle the current state of gpio pin #01 (should turn on)
            for( GpioPinDigitalOutput gpdo : pins )
            {
                gpdo.toggle();
            }
            handler.signalPercent( 60 );
            handler.signalMessage( "--> GPIO state should be: ON" );

            if( !waitCheck( handler, 5 * 5 ) )
            {
                return;
            }

            // toggle the current state of gpio pin #01 (should turn off)
            for( GpioPinDigitalOutput gpdo : pins )
            {
                gpdo.toggle();
            }
            handler.signalPercent( 80 );
            handler.signalMessage( "--> GPIO state should be: OFF" );

            if( !waitCheck( handler, 5 * 5 ) )
            {
                return;
            }

            // turn on gpio pin #01 for 1 second and then off
            handler.signalPercent( 99 );
            handler.signalMessage(
                "--> GPIO state should be: ON for only 1 second" );
            for( GpioPinDigitalOutput gpdo : pins )
            {
                gpdo.pulse( 1000, true );
            } // set second argument to 'true' use a
              // blocking
              // call
        }
        finally
        {
            // stop all GPIO activity/threads by shutting down the GPIO
            // controller
            // (this method will forcefully shutdown all GPIO monitoring threads
            // and
            // scheduled tasks)
            for( GpioPinDigitalOutput gpdo : pins )
            {
                gpio.unprovisionPin( gpdo );
            }
            handler.signalMessage( "Exiting ControlGpioExample" );
        }
    }

    private static boolean waitCheck( ITaskHandler handler, int fifthSeconds )
        throws InterruptedException
    {
        for( int i = 0; i < fifthSeconds && handler.canContinue(); i++ )
        {
            Thread.sleep( 200 );
        }

        return handler.canContinue();
    }

    @Override
    public void run( ITaskStatusHandler handler )
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
        return "Control GPIO Example";
    }
}
// END SNIPPET: control-gpio-snippet
