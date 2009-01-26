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
package org.ow2.aspirerfid.connectors.client;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.ow2.aspirerfid.connectors.api.ConnectorEngine;
import org.ow2.aspirerfid.connectors.tools.Configurator;

/**
 * This class is used to handle event registration operations
 * through XML-RPC.
 * 
 * @author Nektarios Leontiadis (nele@ait.edu.gr)
 *
 */
public class RegistrationClient {

    private static final String connectorServerUrl; 
    private static XmlRpcClient client;
    private static Logger logger;
    
    static
    {
	logger = Logger.getLogger(RegistrationClient.class);
	connectorServerUrl = Configurator.getProperty("connectorServerUrl","http://localhost:8080/connector/subscribe");

	logger.info("Subscribing to:"+connectorServerUrl);
	XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
	try {
	    config.setServerURL(new URL(connectorServerUrl));
	} catch (MalformedURLException e) {
	    logger.fatal(e);
	}
	client = new XmlRpcClient();
	client.setConfig(config);
    }
    
    public static boolean register(String tid, String transactionType, String sid)
    {
	boolean result = false;
	Object[] params = new Object[] { tid, transactionType, sid };
	try {
	    synchronized(client)
	    {
		result = (Boolean) client.execute(ConnectorEngine.class.getName()+".startObservingTransaction", params);
	    }
	} catch (XmlRpcException e) {
	    logger.error(e);
	}
	return result;
    }
    
    public static boolean unregister(String sid)
    {
	boolean result = false;
	Object[] params = new Object[] { sid };
	try {
	    synchronized(client)
	    {
		result = (Boolean) client.execute(ConnectorEngine.class.getName()+".stopObservingTransaction", params);
	    }
	} catch (XmlRpcException e) {
	    logger.error(e);
	}
	return result;
    }
}
