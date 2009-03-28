/*
   Copyright 2005-2008, OW2 Aspire RFID project 
   
   This library is free software; you can redistribute it and/or modify it 
   under the terms of the GNU Lesser General Public License as published by 
   the Free Software Foundation (the "LGPL"); either version 2.1 of the 
   License, or (at your option) any later version. If you do not alter this 
   notice, a recipient may use your version of this file under either the 
   LGPL version 2.1, or (at his option) any later version.
   
   You should have received a copy of the GNU Lesser General Public License 
   along with this library; if not, write to the Free Software Foundation, 
   Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
   
   This software is distributed on an "AS IS" basis, WITHOUT WARRANTY OF ANY 
   KIND, either express or implied. See the GNU Lesser General Public 
   License for the specific language governing rights and limitations.

   Contact: OW2 Aspire RFID project <X AT Y DOT org> (with X=aspirerfid and Y=ow2)

   LGPL version 2.1 full text http://www.gnu.org/licenses/lgpl-2.1.txt    
*/
package org.ow2.aspirerfid.smsreceiver;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;

/**
 * This application is intended to manage the communication with a mobile object
 * (eg sonding balloon, plane, container, truck, ...)
 * using a GSM modem attached to a local serial port. This application
 * also forwards SMS received from the balloon (via the local GSM modem) to an
 * host (connected on another local serial port) managing data presentation by
 * the way of a GUI. Additionally, the application produces an html file that is
 * stored on a Web server's filesystem in order to allow remote web-friendly
 * data access. The HTML file contains a script that displays a map to locate
 * the balloon, using google maps API.
 * 
 * @author Sebastien Jean, IUT Valence (sebastien.jean AT iut-valence DOT fr), Didier Donsez
 * 
 */
public class Main
{
	
	private static void printUsage(){
		System.err.println(
		"Command-line arguments are:\n"+
		"\t[O] : Symbolic name of the serial port connected to GSM modem\n"+
		"\t[1] : Symbolic name of the serial port connected to GUI host\n"+
		"\t[2] : path of the index.html file to be produced\n"+
		"\t[3] : Local GSM modem phone number\n"+
		"\t[4] : Remote sender GSM modem phone number\n"
		);
	}
	
	/**
	 * Main method
	 * @param args Command-line arguments :<br/>
	 *        <ul>
	 *        <li>[0] : Symbolic name of the serial port connected to GSM modem</li>
	 *        <li>[1] : Symbolic name of the serial port connected to GUI host</li>
	 *        <li>[2] : path of the index.html file to be produced</li>
	 *        <li>[3] : Local receiver GSM modem phone number</li>
	 *        <li>[4] : Remote sender GSM modem phone number</li>
	 *        </ul>
	 */
	public static void main(String[] args)
	{
		
		if(args.length!=5){
			printUsage();
			System.exit(1);			
		}
		
		// Initialization of the serial port on which GSM modem is attached
		CommPortIdentifier modemPortID = null;
		try
		{
			modemPortID = CommPortIdentifier.getPortIdentifier(args[0]);
		}
		catch (NoSuchPortException e1)
		{
			System.err.println("Specified port (" + args[0]
					+ ") does not exist, " + "... exiting");
			printUsage();
			System.exit(1);
		}

		SerialPort modemPort = null;
		try
		{
			modemPort = (SerialPort) modemPortID.open("SMSReceiverModem",2000);

			modemPort.setSerialPortParams(9600, SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
			// The following configuration step is a work-around for a known bug
			// of RXTX lib on windows platform
			modemPort.enableReceiveTimeout(650000);
			modemPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);

		}
		catch (PortInUseException e2)
		{
			System.err.println("Specified port (" + args[0]
					+ ") is already in use by " + modemPortID.getCurrentOwner()
					+ ",... exiting");
			printUsage();
			System.exit(1);
		}
		catch (UnsupportedCommOperationException e)
		{
			System.err.println("Specified port (" + args[0]
					+ ") can not be correctly initialized,... exiting");
			printUsage();
			System.exit(1);
		}

		// Initialization of the serial port on which host managing GUI is
		// attached
		CommPortIdentifier guiPortID = null;
		try
		{
			guiPortID = CommPortIdentifier.getPortIdentifier(args[1]);
		}
		catch (NoSuchPortException e1)
		{
			System.out.println("Specified port (" + args[1]
					+ ") does not exist, " + "... exiting");
			printUsage();
			System.exit(1);
		}

		SerialPort guiPort = null;
		try
		{
			guiPort = (SerialPort) guiPortID.open("BallonWatcherGUI", 2000);
			guiPort.setSerialPortParams(9600, SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
			// The following configuration step is a work-around for a known
			// bug of RXTX lib on windows platform
			guiPort.enableReceiveTimeout(650000);

			guiPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
		}
		catch (PortInUseException e2)
		{
			System.err.println("Specified port (" + args[1]
					+ ") is already in use by " + guiPortID.getCurrentOwner()
					+ ",... exiting");
			printUsage();
			System.exit(1);
		}
		catch (UnsupportedCommOperationException e)
		{
			System.err.println("Specified port (" + args[1]
					+ ") can not be correctly initialized,... exiting");
			printUsage();
			System.exit(1);
		}

		// Launching a thread
		LocalUserThread userThread = null;
		try
		{
			userThread = new LocalUserThread(modemPort.getInputStream(),
					modemPort.getOutputStream(), guiPort.getOutputStream(),
					args[2], args[3], args[4]);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		userThread.start();
	}
}
