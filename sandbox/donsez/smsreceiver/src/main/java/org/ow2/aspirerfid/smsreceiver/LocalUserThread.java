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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.ow2.aspirerfid.smsreceiver.data.MalformedMessageException;
import org.ow2.aspirerfid.smsreceiver.data.ballon.SMSBalloonMessage;
import org.ow2.aspirerfid.smsreceiver.data.ballon.SMSBalloonMessageProcessor;

/**
 * Implementation of a thread controlling a TELIT GSM modem, user-side.
 * 
 * @author Sebastien Jean, IUT Valence (sebastien.jean@iut-valence.fr), Didier Donsez (for refactoring)
 * 
 */
public class LocalUserThread extends Thread
{

	private static final long SLEEPTIME = 30000;

	/**
	 * Stream used as input.
	 */
	private InputStream in;

	/**
	 * Stream used as output.
	 */
	private OutputStream out;

	/**
	 * Stream used as output for gui host.
	 */
	private OutputStream guiOut;

	/**
	 * Path of the HMTL file to be produced
	 */
	private String path;

	/**
	 * Local GSM modem phone number.
	 */
	private String localPhoneNumber;

	/**
	 * Remote GSM modem phone number
	 */
	private String remotePhoneNumber;

	/**
	 * Logger
	 */
	private Logger logger;

	/**
	 * flag to end the thread loop
	 */
	private boolean ended=false;

	/**
	 * Constructor with parameters.
	 * 
	 * @param in local GSM modem input stream
	 * @param out local GSM modem output stream
	 * @param guiOut gui host output stream
	 * @param path path of the HMTL file to be produced
	 * @param localNum local GSM modem phone number
	 * @param remoteNum remote GSM modem phone number
	 */
	public LocalUserThread(InputStream in, OutputStream out,
			OutputStream guiOut, String path, String localNum, String remoteNum)
	{
		super("SMSReceiver");
		this.in = in;
		this.out = out;
		this.guiOut = guiOut;
		this.path = path;
		this.localPhoneNumber = localNum;
		this.remotePhoneNumber = remoteNum;		   
	}

	/**
	 * Processing of a command dedicated to suppress echo on local GSM modem.
	 * 
	 * @throws ModemCommandProcessingException if command processing fails.
	 */
	public void invalidateEcho() throws ModemCommandProcessingException
	{
		logger.log(Level.INFO,"LU - sending invalidateEcho command");
		try
		{
			this.out.write("ATE".getBytes("US-ASCII"));
			this.out.write(CommUtils.CR);
			this.out.flush();
			if (CommUtils.waitForLineEndedByCRLFOccurringSubstring(this.in,
					"OK") == null)
				throw new ModemCommandProcessingException();
		}
		catch (Exception e)
		{
			throw new ModemCommandProcessingException();
		}
		logger.log(Level.INFO,"LU - invalidateEcho <done>");
	}

	/**
	 * Processing of a command dedicated to toggle local GSM modem "text mode".
	 * 
	 * @throws ModemCommandProcessingException if command processing fails.
	 */
	public void setTextMode() throws ModemCommandProcessingException
	{
		logger.log(Level.INFO,"LU - sending setTextMode command");
		try
		{
			this.out.write("AT+CMGF=1".getBytes("US-ASCII"));
			this.out.write(CommUtils.CR);
			this.out.flush();
			if (CommUtils.waitForLineEndedByCRLFOccurringSubstring(this.in,
					"OK") == null)
				throw new ModemCommandProcessingException();
		}
		catch (Exception e)
		{
			throw new ModemCommandProcessingException();
		}
		logger.log(Level.INFO,"LU - setTextMode <done>");
	}

	/**
	 * Processing of a command dedicated to ask local GSM modem to notify each
	 * SMS reception.
	 * 
	 * @throws ModemCommandProcessingException if command processing fails.
	 */
	public void notifySMS() throws ModemCommandProcessingException
	{
		logger.log(Level.INFO,"LU - sending notifySMS command");
		try
		{
			this.out.write("AT+CNMI=1,1".getBytes("US-ASCII"));
			this.out.write(CommUtils.CR);
			this.out.flush();
			if (CommUtils.waitForLineEndedByCRLFOccurringSubstring(this.in,
					"OK") == null)
				throw new ModemCommandProcessingException();
		}
		catch (Exception e)
		{
			throw new ModemCommandProcessingException();
		}
		logger.log(Level.INFO,"LU - notifySMS <done>");
	}

	/**
	 * Processing of a command dedicated to ask local GSM modem to send an SMS
	 * to a remote GSM modem.
	 * 
	 * @param number the remote phone number.
	 * @param message the message to embed in the SMS.
	 * @throws ModemCommandProcessingException if command processing fails.
	 */
	public void sendSMS(String number, String message)
			throws ModemCommandProcessingException
	{
		logger.log(Level.INFO,"LU - sending sendSMS command");
		logger.log(Level.INFO,"number = " + number);
		logger.log(Level.INFO,"message = " + message);
		try
		{
			this.out.write(("AT+CMGS=\"" + number + "\"").getBytes("US-ASCII"));
			this.out.write(CommUtils.CR);
			this.out.flush();
		}
		catch (Exception e)
		{
			throw new ModemCommandProcessingException();
		}

		byte[] line = CommUtils.readLineEndedByAFlag(this.in, CommUtils.SP);
		if (line == null)
			throw new ModemCommandProcessingException();
		logger.log(Level.INFO,"LU - sendSMS <prompt received>");
		try
		{
			this.out.write(message.getBytes("US-ASCII"));
			this.out.write(CommUtils.CTRL_Z);
			this.out.flush();
			if (CommUtils.waitForLineEndedByCRLFOccurringSubstring(this.in,
					"+CMGS:") == null)
				throw new ModemCommandProcessingException();
		}
		catch (Exception e)
		{
			throw new ModemCommandProcessingException();
		}

		logger.log(Level.INFO,"LU - sendSMS <+CMGS received>");
		if (CommUtils.waitForLineEndedByCRLFOccurringSubstring(this.in, "OK") == null)
			throw new ModemCommandProcessingException();
		logger.log(Level.INFO,"LU - sendSMS <done>");
	}

	/**
	 * Waiting for an incoming SMS to be notified by local GSM modem, and
	 * processing of commands dedicated to retrieve and delete this SMS.
	 * 
	 * @return the received SMS.
	 * @throws ModemCommandProcessingException if command processing fails.
	 */
	public String waitForSMS() throws ModemCommandProcessingException
	{
		String sline = null;
		while (true)
		{
			logger.log(Level.INFO,"LU - waiting for an SMS to be notified");
			sline = CommUtils.waitForLineEndedByCRLFOccurringSubstring(this.in,
					"+CMTI");
			if (sline == null)
				continue;
			break;
		}
		int smsNumber = 0;
		try
		{
			logger.log(Level.INFO,sline
					+ "#"
					+ sline.substring(sline.indexOf(',') + 1,
							sline.length() - 2));
			smsNumber = Integer.parseInt(sline.substring(
					sline.indexOf(',') + 1, sline.length() - 2));
			logger.log(Level.INFO,"LU - <SMS " + smsNumber
					+ " reception notified>");
		}
		catch (Exception e)
		{

			throw new ModemCommandProcessingException();
		}

		logger.log(Level.INFO,"LU - sending SMS retrieval command");
		try
		{
			this.out.write(("AT+CMGR=" + smsNumber).getBytes("US-ASCII"));
			this.out.write(CommUtils.CR);
			this.out.flush();
			if (CommUtils.waitForLineEndedByCRLFOccurringSubstring(this.in,
					"+CMGR:") == null)
				throw new ModemCommandProcessingException();
		}
		catch (Exception e)
		{
			throw new ModemCommandProcessingException();
		}

		sline = CommUtils.waitForLineEndedByCRLFOccurringSubstring(this.in, "");
		if (sline == null)
			throw new ModemCommandProcessingException();

		logger.log(Level.INFO,"LU - <SMS retrieved> : " + sline);

		if (CommUtils.waitForLineEndedByCRLFOccurringSubstring(this.in, "OK") == null)
			throw new ModemCommandProcessingException();
		logger.log(Level.INFO,"LU - <SMS reception done>");
		logger.log(Level.INFO,"LU - sending SMS deletion command");
		try
		{
			this.out.write(("AT+CMGD=" + smsNumber).getBytes("US-ASCII"));
			this.out.write(CommUtils.CR);
			this.out.flush();
			if (CommUtils.waitForLineEndedByCRLFOccurringSubstring(this.in,
					"OK") == null)
				throw new ModemCommandProcessingException();
		}
		catch (Exception e)
		{
			throw new ModemCommandProcessingException();
		}
		logger.log(Level.INFO,"LU - <SMS deletion done>");
		return sline;
	}

	public void end() {
		ended=true;
	}
	
	/**
	 * @see java.lang.Thread#run()
	 */
	public void run()
	{
		
		
		logger = Logger.getLogger(this.getName());
		
		logger.log(Level.INFO,"LU thread started...");

		try
		{
			this.invalidateEcho();
		}
		catch (ModemCommandProcessingException e)
		{
			logger.log(Level.WARNING,"LU - failed to process 'invalidate echo command', exiting...");
			return;
		}

		try
		{
			this.setTextMode();
		}
		catch (ModemCommandProcessingException e)
		{
			logger.log(Level.WARNING,"LU - failed to process 'set text mode' command, exiting...");
			return;
		}
		try
		{
			this.notifySMS();
		}
		catch (ModemCommandProcessingException e)
		{
			logger.log(Level.WARNING,"LU - failed to process 'notify SMS' command, exiting...");
			return;
		}

		try
		{
			this.sendSMS(this.remotePhoneNumber, "TEL "+ this.localPhoneNumber);
		}
		catch (ModemCommandProcessingException e)
		{
			logger.log(Level.WARNING,"LU - failed to process 'send SMS' command, exiting...");
			return;
		}

		logger.log(Level.INFO,"Waiting "+SLEEPTIME/1000+"s");
		// Waiting 60s between two SMS to be sure that the remote modem will
		// have
		// enough time to process each one correctly
		try
		{
			Thread.sleep(SLEEPTIME);
		}
		catch (InterruptedException e)
		{}

		while (ended)
		{
			// Sending REQ
			try
			{
				this.sendSMS(this.remotePhoneNumber, "REQ");
			}
			catch (ModemCommandProcessingException e)
			{
				logger.log(Level.WARNING,"LU - failed to send 'REQ' SMS");
			}

			// Waiting for SMS
			String sms = "";
			try
			{
				sms = this.waitForSMS();
			}
			catch (ModemCommandProcessingException e)
			{
				logger.log(Level.WARNING,"LU - failed to receive SMS");
				continue;
			}

			System.out.println(sms);

			try
			{
				this.guiOut.write(sms.getBytes("US-ASCII"));
				this.guiOut.flush();
			}
			catch (Exception e)
			{
				logger.log(Level.WARNING,"SMS forwarding failed : "
						+ e.getClass().getName());
			}

			// Generating HMTL file

			try
			{
				generateHMTLFile(sms, this.path);
			}
			catch (Exception e)
			{
				logger.log(Level.WARNING,"LU - failed to generate HTML file : "
						+ e.getClass().getName());
			}

			logger.log(Level.INFO,"Waiting "+SLEEPTIME/1000+"s");
			// Waiting  XX s between two SMS to be sure that the remote modem will have enough time to process each one correctly
			try
			{
				Thread.sleep(SLEEPTIME);
			}
			catch (InterruptedException e)
			{}

			logger.log(Level.INFO,"Type return to send another REQ command");
			try
			{
				new BufferedReader(new InputStreamReader(System.in)).readLine();
			}
			catch (IOException e)
			{
				logger.log(Level.WARNING,"LU - unable to readLine from standard input...");
			}
		}
	}

	/**
	 * Generating HTML file with informations received from remote GSM modem.
	 * 
	 * @param sms the SMS containing the informations
	 * @param path the path of the HTML file to be created
	 * @throws FileNotFoundException if path is invalid
	 * @throws UnsupportedEncodingException if UTF-8 character encoding is not
	 *         available on host platform
	 * @throws MalformedMessageException if SMS is corrupted
	 */
	private void generateHMTLFile(String sms, String path)
			throws FileNotFoundException, UnsupportedEncodingException,
			MalformedMessageException
	{		
		SMSBalloonMessage smsMessage = (SMSBalloonMessage)(new SMSBalloonMessageProcessor()).parse(sms);
		FileOutputStream fos = new FileOutputStream(path);
		PrintStream ps = new PrintStream(fos, true, "UTF-8");
		ps.println(smsMessage.toHTML());
	}
}
