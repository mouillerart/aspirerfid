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

import java.io.IOException;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.apache.log4j.Logger;
import org.fosstrak.epcis.model.ArrayOfString;
import org.fosstrak.epcis.model.QueryParam;
import org.fosstrak.epcis.model.QueryParams;
import org.fosstrak.epcis.model.QuerySchedule;
import org.fosstrak.epcis.model.Subscribe;
import org.fosstrak.epcis.model.SubscriptionControls;
import org.fosstrak.epcis.queryclient.Query;
import org.fosstrak.epcis.queryclient.QueryControlClient;
import org.ow2.aspirerfid.connectors.api.ConnectorEngine;
import org.ow2.aspirerfid.connectors.tools.Configurator;

/**
 * This is the entry point of the Connector service. This class implements the
 * Connector Engine API and exposes the functionality that implements though
 * XML-RPC.
 * 
 * @author Nektarios Leontiadis (nele@ait.edu.gr)
 * 
 */
public class ConnectorEngineImpl implements ConnectorEngine {

    private static Logger logger;
    private static boolean loaded = false;
    private String destinationUrl;
    private Query query;
    private String querySec, queryMin, queryHour, queryDayOfMonth, queryMonth, quertDayOfWeek;

    private static final String queryName;
    private QueryCallbackListener listener;
    private QueryControlClient client;
    private QueryResultsProcessor processor;

    static {
	logger = Logger.getLogger(ConnectorEngineImpl.class);
	try {
	    Configurator.loadProperties("application.properties");
	    loaded = true;
	} catch (Exception e) {
	    logger.error(e);
	}
	queryName = Configurator.getProperty("queryName", "SimpleEventQuery");
    }

    public ConnectorEngineImpl() {
	loadProperties();
	initialize(Configurator.getProperty("epcisQueryIfceUrl", "http://localhost:8080/epcis/query"));
    }

    public ConnectorEngineImpl(String queryUrl) {
	loadProperties();
	initialize(queryUrl);
    }

    private void loadProperties() {
	if (!loaded) {
	    try {
		Configurator.loadProperties("application.properties");
	    } catch (Exception e) {
		logger.error(e);
	    }
	    loaded = true;
	}
    }

    private void initialize(String queryUrl) {
	querySec = Configurator.getProperty("querySeconds", "1");
	destinationUrl = Configurator.getProperty("callbackDestinationUrl", "http://localhost:8899");
	configureClient(queryUrl);

	try {
	    listener = QueryCallbackListener.getInstance();
	    processor = QueryResultsProcessor.getInstance();
	    listener.setResultsProcessor(processor);
	} catch (IOException e) {
	    logger.error(e);
	}
    }

    private void configureClient(String queryUrl) {
	logger.info("Setting epci url: " + queryUrl);
	client = new QueryControlClient(queryUrl);
    }

    public boolean startObservingTransaction(String tid, String transactionType, String sid) {

	logger.info("Registering for tid:" + tid + " with sid:" + sid);

	Subscribe s = prepareSubscription(tid, transactionType, sid);
	return subscribe(s);
    }

    public boolean stopObservingTransaction(String sid) {

	boolean result = false;

	try {
	    if (sid != null) {
		client.unsubscribe(sid);

		result = true;

		if (client.getSubscriptionIds(queryName).size() == 0)
		    listener.stopRunning();
	    }
	} catch (Exception e) {
	    logger.error(e);
	}
	return result;
    }

    private Subscribe prepareSubscription(String tid, String transactionType, String sid) {
	Subscribe sub = new Subscribe();
	QueryParam param = new QueryParam();
	ArrayOfString arr = new ArrayOfString();
	logger.info("Preparing subscription");
	sub.setDest(destinationUrl);
	sub.setQueryName("SimpleEventQuery");
	sub.setSubscriptionID(sid);

	query = new Query();

	query.setReturnAggregationEvents(true);
	query.setReturnObjectEvents(true);
	query.setReturnQuantityEvents(true);
	query.setReturnTransactionEvents(true);

	if (transactionType != null)
	    param.setName("EQ_bizTransaction_" + transactionType);
	else
	    param.setName("EQ_bizTransaction");
	logger.info("bizTransactionType:"+param.getName());
	logger.info("bizTransactionId:"+tid);
	arr.getString().add(tid);
	param.setValue(arr);

	query.getQueryParameters().add(param);
	QueryParams queryParams = new QueryParams();
	queryParams.getParam().add(param);
	sub.setParams(queryParams);

	// Poll poll = new Poll();
	// poll.setQueryName("SimpleEventQuery");
	// poll.setParams(queryParams);
	// poll(poll);

	SubscriptionControls controls = new SubscriptionControls();

	DatatypeFactory factory;
	try {
	    factory = DatatypeFactory.newInstance();
	    controls.setInitialRecordTime(factory.newXMLGregorianCalendar(new GregorianCalendar()));
	} catch (DatatypeConfigurationException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	controls.setReportIfEmpty(true);

	QuerySchedule sched = new QuerySchedule();

	sched.setSecond(querySec);
	controls.setSchedule(sched);
	sub.setControls(controls);

	return sub;
    }

    // private void poll(Poll poll) {
    // try {
    // logger.info(client.poll(poll));
    // } catch (ImplementationExceptionResponse e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // } catch (QueryTooComplexExceptionResponse e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // } catch (QueryTooLargeExceptionResponse e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // } catch (SecurityExceptionResponse e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // } catch (ValidationExceptionResponse e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // } catch (NoSuchNameExceptionResponse e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // } catch (QueryParameterExceptionResponse e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // }

    private boolean subscribe(final Subscribe sub) {
	try {
	    client.subscribe(sub.getQueryName(), sub.getParams(), sub.getDest(), sub.getControls(), sub.getSubscriptionID());

	    if (!listener.isRunning())
		listener.start();

	    return true;

	} catch (Exception e) {
	    e.printStackTrace();
	    logger.error(e);
	}
	return false;
    }

}
