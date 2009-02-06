/**
 * Copyright (c) 2008-2010, Aspire 
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

package org.ow2.aspirerfid.demos.warehouse.connector;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashSet;

import org.apache.log4j.Logger;
import org.ow2.aspirerfid.connectors.api.ConnectorClient;
import org.ow2.aspirerfid.connectors.client.RegistrationClient;
import org.ow2.aspirerfid.connectors.client.TransportService;
import org.ow2.aspirerfid.connectors.tools.Configurator;

/**
 * This class contains operations to communicate with the connector client module
 * @author Nektarios Leontiadis (nele@ait.edu.gr)
 * 
 */
public class ConnectorManager {

    private String endpoint;
    private boolean started;
    private HashSet<String> subscr;
    private TransportService service;
    
    private static ConnectorManager manager;
    
    public static final String transactionId;
    public static final boolean IS_STANDALONE;
    private static final int DEFAULT_STANDALONE_PORT = 8089;
    private static final Logger logger;

    static {
	transactionId = Configurator.getProperty("transactionId", "urn:epc:id:gid:");
	IS_STANDALONE = Configurator.getProperty("isConnectorClientStandaloneModeOn","true").equalsIgnoreCase("true");
	logger = Logger.getLogger(ConnectorManager.class);
    }

    public static ConnectorManager getInstance() {
	if (manager == null) {
	    manager = new ConnectorManager();
	}

	return manager;
    }

    private ConnectorManager() {
	endpoint = null;
	started = false;
	subscr = new HashSet<String>();
	service = new TransportService();

    }

    public void setEndpoint(String queryEndpoint) {
	endpoint = queryEndpoint;
    }

    public void setConnectorClient(ConnectorClient client) {
	service.setTransactionHandler(client);
    }

    private boolean startService() {
	int port;
	
	if (started)
	    return true;
	try
	{
	    port = Integer.parseInt(Configurator.getProperty("standaloneConnectorClientPort"));
	}catch (Exception e)
	{
	    port = DEFAULT_STANDALONE_PORT;
	}
	started = service.startStandaloneMode(port);
	return started;
    }

    /**
     * Sends a registration message either to the embedded or the remote connector client 
     * @param invoiceId The invoice ID to be registered.
     * @return True if operation suceeds; false otherwise.
     */
    public boolean registerForTransaction(String invoiceId) {
	boolean response = false;
	HttpURLConnection conn;
	URL url;
	String regUrl;

	if (subscr.contains(invoiceId))
	    return true;

	if (!IS_STANDALONE) {

	    regUrl = endpoint + "register?sub&sid=" + transactionId + "&tid="
		    + Configurator.getProperty("transactionId", "urn:epc:id:gid:")+invoiceId;
	    try {
		// regUrl = URLEncoder.encode(regUrl, "UTF-8");
		url = new URL(regUrl);
		conn = (HttpURLConnection) url.openConnection();
		conn.connect();
		BufferedReader reader = new BufferedReader(
			new InputStreamReader(conn.getInputStream()));
		char respCh = (char) reader.read();
		if (respCh == 't') {
		    subscr.add(invoiceId);
		    response = true;
		}
		reader.close();
		conn.disconnect();

	    } catch (Exception e) {
		logger.error(e);
	    }
	    return response;
	} else {
	    //Start local XmlRpc servlet
	    if (!startService())
		    return false;
	    if (RegistrationClient.register(invoiceId, Configurator.getProperty("bizTransType", "urn:epcglobal:fmcg:btt:receiving"), invoiceId)) {
		subscr.add(invoiceId);
		return true;
	    } else
		return false;
	}

    }

    /**
     * Sends an unregister message either to the embedded or the remote connector client 
     * @param invoiceId The registered invoice ID
     * @return True if operation suceeds; false otherwise.
     */
    public boolean unregisterForTransaction(String invoiceId) {
	boolean response = false;
	HttpURLConnection conn;
	URL url;
	String regUrl;

	if (!subscr.contains(invoiceId))
	    return false;

	if (!IS_STANDALONE) {
	    regUrl = endpoint + "register?unsub&sid=" + invoiceId;
	    try {
		regUrl = URLEncoder.encode(regUrl, "UTF-8");
		url = new URL(regUrl);
		conn = (HttpURLConnection) url.openConnection();
		BufferedReader reader = new BufferedReader(
			new InputStreamReader(conn.getInputStream()));
		char respCh = (char) reader.read();
		if (respCh == 't') {
		    subscr.remove(invoiceId);
		    response = true;
		}
		reader.close();
		conn.disconnect();

	    } catch (Exception e) {
		logger.error(e);
	    }
	    return response;
	} else {
	    if (RegistrationClient.unregister(invoiceId)) {
		subscr.remove(invoiceId);
		return true;
	    } else
		return false;
	}
    }

    public void cancelAllSubscriptions() {
	logger.warn("Unsubscribing from all pending subscriptions");
	for (String ids : subscr) {
	    unregisterForTransaction(ids);
	}
    }

    // private String generateRandomSubscriptionId()
    // {
    // SecureRandom sr;
    // try {
    // sr = SecureRandom.getInstance("SHA1PRNG");
    // } catch (NoSuchAlgorithmException e) {
    // sr = new SecureRandom();
    // }
    // // Get 128 random bits
    // byte[] bytes = new byte[128/8];
    // sr.nextBytes(bytes);
    //        
    // return new String(bytes);
    // }
}
