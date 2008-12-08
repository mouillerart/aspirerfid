/**
 * Copyright © 2008-2010, Aspire 
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

package org.ow2.aspirerfid.connectors.engine;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.ow2.aspirerfid.connectors.api.ConnectorAdapter;
import org.ow2.aspirerfid.connectors.api.Event;
import org.ow2.aspirerfid.connectors.tools.Configurator;

/**
 * This class handles the operation of sending an event message to the a connector client
 * using XML-RPC
 * @author Nektarios Leontiadis (nele@ait.edu.gr)
 * 
 */
public class ConnectorTransport extends Thread {

    private Event event;
    private static final String serverUrl; 
    private static final Logger logger;
    
    private static XmlRpcClient client;
    
    static
    {
	logger = Logger.getLogger(ConnectorTransport.class);
	serverUrl = Configurator.getProperty("connectorClientEndpoint","http://localhost:8089/listen");
	XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
	try {
	    config.setServerURL(new URL(serverUrl));
	} catch (MalformedURLException e) {
	    logger.error(e);
	}
	client = new XmlRpcClient();
	client.setConfig(config);
    }
    
    private ConnectorTransport(Event evt) {
	event = evt;
    }

    public void run() {
	this.transmit();
    }
    
    private void transmit()
    {
	boolean result = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream(); 
	try {
	    ObjectOutputStream oOutput = new ObjectOutputStream(out);
	    oOutput.writeObject(event);
	} catch (IOException e1) {
	    logger.error(e1);
	} 
	Object[] params = new Object[] { out.toByteArray() };
	try {
	    synchronized(client)
	    {
		result = (Boolean) client.execute(ConnectorAdapter.class.getName()+".transactionObserved", params);
	    }
	} catch (XmlRpcException e) {
	    logger.error(e);
	}
	logger.info("Transmition "+ (result?"succeded":"failed"));
    }

    public static void transmit(Event evt) {
	new ConnectorTransport(evt).start();
    }

}
