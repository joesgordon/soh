package soh.gpio;

import java.util.ArrayList;
import java.util.List;

import org.jutils.task.*;

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
import com.pi4j.io.gpio.*;

/**
 * This example code demonstrates how to perform simple state control of a GPIO
 * pin on the Raspberry Pi.
 * @author Robert Savage
 */
public class GpioOutputExample implements ITask
{
    private static void doit( ITaskHandler handler ) throws InterruptedException
    {
        handler.signalMessage( "<--Pi4J--> GPIO Control Example ... started." );

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
        Pin [] gpios = new Pin[] { RaspiPin.GPIO_21, RaspiPin.GPIO_22,
            RaspiPin.GPIO_23, RaspiPin.GPIO_24, RaspiPin.GPIO_25,
            RaspiPin.GPIO_26, RaspiPin.GPIO_27 };
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
        return "Control GPIO Example";
    }
}
// END SNIPPET: control-gpio-snippet
