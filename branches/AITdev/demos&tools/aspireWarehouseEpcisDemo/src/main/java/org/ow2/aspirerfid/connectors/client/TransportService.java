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

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

import org.apache.log4j.Logger;
import org.apache.xmlrpc.webserver.ServletWebServer;
import org.apache.xmlrpc.webserver.XmlRpcServlet;
import org.ow2.aspirerfid.connectors.api.ConnectorAdapter;
import org.ow2.aspirerfid.connectors.api.ConnectorClient;
import org.ow2.aspirerfid.connectors.api.Event;

/**
 * Handles incomin event data through XML-RPC
 * 
 * @author Nektarios Leontiadis (nele@ait.edu.gr)
 *
 */
public class TransportService implements ConnectorAdapter {


    private static ConnectorClient client;
    
    private static Logger logger;
    private static boolean servletStarted;
    private static ServletWebServer webServer;
    private static XmlRpcServlet servlet;
    
    static {
	logger = Logger.getLogger(TransportService.class);
	servletStarted = false;
    }
    
    static
    {
	client = null;
    }
    
    /* (non-Javadoc)
     * @see org.ow2.aspirerfid.connectors.api.ConnectorAdapter#transactionObserved(byte [])
     */
    public boolean transactionObserved(byte [] evt) {
	Event event;
	ByteArrayInputStream in = new ByteArrayInputStream(evt); 
	try {
	    ObjectInputStream oIn = new ObjectInputStream(in);
	    event =  (Event)oIn.readObject();
	    logger.debug(event.toString());
	    
	    if(client != null)
		client.handleEvent(event);
	} catch (Exception e) {
	    e.printStackTrace();
	} 

	return true;
    }

    /* (non-Javadoc)
     * @see org.ow2.aspirerfid.connectors.api.ConnectorAdapter#startStandaloneMode(int)
     */
    public boolean startStandaloneMode(int port) {
	if(!servletStarted)
	    servletStarted = startServlet(port);
	
	return servletStarted;
    }

    /* (non-Javadoc)
     * @see org.ow2.aspirerfid.connectors.api.ConnectorAdapter#setTransactionHandler(org.ow2.aspirerfid.connectors.api.ConnectorClient)
     */
    public void setTransactionHandler(ConnectorClient handler) {
	client = handler;
    }

    /**
     * Starts a standalone servlet to handle incoming XML-RPC requests
     * @param port The port for the servlet engine to start
     * @return True if it start successfully; false otherwise
     */
    private synchronized boolean startServlet(int port)
    {
	logger.info("Starting standalone XmlRpc Servlet");
	    try {
		servlet = new XmlRpcServlet();
		webServer = new ServletWebServer(servlet, port);
		webServer.start();
		 logger.info("Standalone XmlRpc Servlet started");
		return true;
	    } catch (Exception e) {
		logger.error(e);
	    }
	    return false;
	   
    }

}
