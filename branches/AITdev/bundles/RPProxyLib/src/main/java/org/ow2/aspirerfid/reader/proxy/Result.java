/*
 * Copyright © 2008-2010, Aspire 
 * 
 * This file contains the source code of the Accada library by ETH Zurich (www.accada.org), 
 * licensed under the terms of the GNU Lesser General Public License version 2.1 in 2007 and 
 * modified for the needs of the Aspire project.
 * 
 * Aspire is free software; you can redistribute it and/or 
 * modify it under  the terms of the GNU Lesser General Public 
 * License version 2.1 as published by the Free Software Foundation (the 
 * "LGPL"). 
 * 
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this library in the file COPYING-LGPL-2.1; if not, write to the Free Software 
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301 USA. 
 * 
 * This software is distributed on an "AS IS" basis, WITHOUT WARRANTY 
 * OF ANY KIND, either express or implied. See the GNU Lesser General Public 
 * License for the specific language governing rights and limitations.
 *  
 */

package org.ow2.aspirerfid.rp.proxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;

import org.ow2.aspirerfid.rp.proxy.msg.Handshake;
import org.ow2.aspirerfid.rp.proxy.util.ResourceLocator;
import org.ow2.aspirerfid.reader.rp.impl.core.msg.Context;
import org.ow2.aspirerfid.reader.rp.impl.core.msg.MessageFactory;
import org.ow2.aspirerfid.reader.rp.impl.core.msg.MessageFormat;
import org.ow2.aspirerfid.reader.rp.impl.core.msg.MessageParser;
import org.ow2.aspirerfid.reader.rp.impl.core.msg.MessageParsingException;
import org.ow2.aspirerfid.reader.rp.impl.core.msg.MessagingConstants;
import org.ow2.aspirerfid.reader.rp.impl.core.msg.reply.Reply;
import org.ow2.aspirerfid.reader.rp.impl.core.msg.transport.HttpMessageHeaderParser;
import org.ow2.aspirerfid.reader.rp.impl.core.msg.transport.TcpMessageHeaderParser;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.Logger;

/**
 * This class listen to the the ProxyConnection and parse and process result messages from reader.
 * If the result message will not be received within the timeout time, a error will be created.
 * 
 * @author regli
 * @author Nektarios Leontiadis (nele@ait.edu.gr)
 */
public class Result {

	/** the logger */
	private static final Logger LOG = Logger.getLogger(Result.class);
	
	/** path to the properties file */
	private static final String PROPERTIES_FILE = "/props/RPProxy.xml";
   private static final String DEFAULT_PROPERTIES_FILE = "/props/RPProxy_default.xml";
	
	/** the handshake stores message format and transport protocol */
	private final Handshake handshake;

	/** contains the header data of the result message */
	private StringBuffer header = new StringBuffer();
	
	/** contains the body data of the result message */
	private StringBuffer body = new StringBuffer();
	
	/** indicates if the end of the header is reached */
	private boolean isHeaderFragment = true;
	
	/** stores the length of the message body */
	private int bodyLength = -1;
	
	/** the parsed reply message */
	private Reply reply = null;
	
	/** indicates if the whole message is already parsed */
	private boolean complete = false;
	
	/** stores the error if message parsing fail */
	private MessageParsingException error;
	
	/** time in ms, the result waits max for a return message */
	private int timeout;

	/**
	 * Constructor set handshake and load timeout from properties file.
	 * 
	 * @param handshake stores message format and transport protocol
	 */
	public Result(Handshake handshake) {
		
		this.handshake = handshake;
		
      XMLConfiguration conf;
      URL fileurl = ResourceLocator.getURL(PROPERTIES_FILE, DEFAULT_PROPERTIES_FILE);
      try {
         conf = new XMLConfiguration(fileurl);
         timeout = conf.getInt("timeout");
      } catch (Exception e) {
			timeout = 60000;
			LOG.warn("Invalid property file '" + PROPERTIES_FILE + "'. Timeout set to 60 000 ms");
		}
			
	}
	
	/**
	 * This method adds a new message fragment to the result message.
	 * 
	 * @param fragment message fragment
	 * @throws IOException if fragment could not be read properly
	 */
	public synchronized void addMsgFragment(String fragment) throws IOException {
		
		if (isHeaderFragment) {
			if (handshake.getTransportProtocol() == Handshake.HTTP) {
				processHttpHeaderFragment(fragment);
			} else if (handshake.getTransportProtocol() == Handshake.TCP) {
				processTcpHeaderFragment(fragment);
			}
		} else {
			processBodyFragment(fragment);
		}
		
	}


	/**
	 * this method initializes the result.
	 */
	public synchronized void init() {
		
		header = new StringBuffer();
		body = new StringBuffer();
		isHeaderFragment = true;
		bodyLength = -1;
		complete = false;
		reply = null;
		
	}

	/**
	 * This method returns the reply.
	 * 
	 * @return reply
	 */
	public synchronized Reply get() {
		
		int counter = 0;
		while (complete == false && counter < timeout / 100) {
			counter++;
			try {
				wait(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return reply;
		
	}
	
	/**
	 * This method returns the header.
	 * 
	 * @return header
	 */
	public synchronized String getHeader() {
		
		return header.toString();
		
	}
	
	/**
	 * This method returns the body.
	 * 
	 * @return body
	 */
	public synchronized String getBody() {
		
		return body.toString();
		
	}
	
	/**
	 * This method returns the error code.
	 * 
	 * @return error code
	 */
	public synchronized int getErrorCode() {
		
		int counter = 0;
		while (complete == false && counter < timeout / 100) {
			counter++;
			try {
				wait(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return counter >= timeout / 100 ? 55555 : reply == null ? error.getResultCode() : reply.getResultCode();
		
	}

	/**
	 * This method returns the error name.
	 * 
	 * @return error name
	 */
	public synchronized String getErrorName() {
		
		int counter = 0;
		while (complete == false && counter < timeout / 100) {
			counter++;
			try {
				wait(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return counter >= timeout / 100 ? "No Reply available after " + (timeout / 1000) + "s." : reply == null ? error.getErrorName() : reply.getError().getName();
		
	}
	
	/**
	 * This method returns the error description.
	 * 
	 * @return error description
	 */
	public synchronized String getErrorDescription() {
		
		int counter = 0;
		while (complete == false && counter < timeout / 100) {
			counter++;
			try {
				wait(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return counter >= timeout / 100 ?  "No Reply available after  " + (timeout / 1000) + "s." : reply == null ? error.getErrorDescription() : reply.getError().getDescription();
		
	}
	
	//
	// private methods
	//
	
	/**
	 * This method processes a http message header fragment.
	 * 
	 * @param fragment http message header fragment
	 * @throws IOException if the http message header fragment could not be processed properly
	 */
	private void processHttpHeaderFragment(String fragment) throws IOException {
		
		int eolPos = fragment.indexOf(MessagingConstants.EOL);
		if (eolPos > -1) {
			if (eolPos == 0 && header.toString().endsWith(MessagingConstants.EOL)) {
				header.append(MessagingConstants.EOL);
				parseHttpHeader();
				processBodyFragment(fragment.substring(MessagingConstants.EOL.length()));
			} else {
				int lastEolPos;
				do {
					lastEolPos = eolPos;
					eolPos = fragment.indexOf(MessagingConstants.EOL, lastEolPos + 1);
				} while (eolPos > -1 && eolPos - lastEolPos > MessagingConstants.EOL.length());
				if (eolPos > -1) {
					header.append(fragment.substring(0, eolPos + MessagingConstants.EOL.length()));
					parseHttpHeader();
					processBodyFragment(fragment.substring(eolPos + MessagingConstants.EOL.length()));
				} else {
					header.append(fragment);
				}
			}
		} else {
			header.append(fragment);
		}
		
	}
	
	/**
	 * This method processes a tcp message header fragment.
	 * 
	 * @param fragment tcp message header fragment
	 * @throws IOException if the tcp message header fragment could not be processed properly
	 */
	private void processTcpHeaderFragment(String fragment) throws IOException {
		
		int eohPos = fragment.indexOf(MessagingConstants.EOH);
		if (eohPos > -1) {
			header.append(fragment.substring(0, eohPos + MessagingConstants.EOH.length()));
			parseTcpHeader();
			processBodyFragment(fragment.substring(eohPos + MessagingConstants.EOH.length()));
		} else {
			header.append(fragment);
		}
		
	}
	
	/**
	 * This method processes a message body fragment.
	 * 
	 * @param fragment message body fragment
	 */
	private void processBodyFragment(String fragment) {
		
		body.append(fragment);
		if (body.length() >= bodyLength) {
			parseBody();
		}
		
	}

	/**
	 * This method parses the http header.
	 * 
	 * @throws IOException if the http header could not be parsed properly
	 */
	private void parseHttpHeader() throws IOException {
		
		isHeaderFragment = false;
		bodyLength = HttpMessageHeaderParser.readHandshake(new BufferedReader(new StringReader(header.toString()))).getContentLength();
		
	}
	
	/**
	 * This method parses the tcp message header.
	 * 
	 * @throws IOException if the tcp message header could not be parsed properly
	 */
	private void parseTcpHeader() throws IOException {
		
		isHeaderFragment = false;
		bodyLength = TcpMessageHeaderParser.readTcpMessageHeader(new BufferedReader(new StringReader(header.toString()))).getLength();
		
	}
	
	/**
	 * This method parses the message body.
	 */
	private void parseBody() {
		
		MessageFormat format;
		if (handshake.getMessageFormat() == Handshake.FORMAT_XML) {
			format = MessageFormat.XML;
		} else {
			format = MessageFormat.TEXT;
		}
		MessageFactory messageFactory = new MessageFactory();
		try {
			MessageParser parser = messageFactory.createParser(format, Context.REPLY);
			reply = (Reply)parser.parseReplyMessage(body.toString());
		} catch (MessageParsingException e) {
			error = e;
		}
		complete = true;
		
	}
	
}