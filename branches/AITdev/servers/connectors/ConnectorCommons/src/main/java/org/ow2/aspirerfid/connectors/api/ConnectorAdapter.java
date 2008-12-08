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

package org.ow2.aspirerfid.connectors.api;

/**
 * This interface should be implemented by the client side of the connector
 * @author Nektarios Leontiadis (nele@ait.edu.gr)
 * 
 */
public interface ConnectorAdapter {

    /**
     * Signals that a new event has been received and should be processed
     * @param evt This is an Event object in byte array representation
     * @return True if the operation has been completed sucessfully; false otherwise
     */
    public boolean transactionObserved(byte[] evt);

    /**
     * This defines the handler application of the event
     * @param handler
     */
    public void setTransactionHandler(ConnectorClient handler);

    /**
     * This method defines if the connector client will be started within the WMS application of within an application server
     * @param port The port that the connector client will be listening when started within the WMS application
     * @return True is it starts sucessfully; false otherwise
     */
    public boolean startStandaloneMode(int port);
}
