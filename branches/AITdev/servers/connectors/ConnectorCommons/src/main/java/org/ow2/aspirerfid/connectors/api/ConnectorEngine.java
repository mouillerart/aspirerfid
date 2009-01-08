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
 * This interface define the endpoints that the connector client should invoke
 * when it wants to register or unregister for a specific transaction.
 * @author Nektarios Leontiadis (nele@ait.edu.gr)
 *
 */
public interface ConnectorEngine {

    /**
     * Register an observator for a transaction
     * @param transactionId Transaction identifier 
     * @param transactionType The transaction type as defined in the EPCIS spec
     * @param sid Subscription identifier
     * @return True if the operations is completed without errors; false otherwise
     */
    public boolean startObservingTransaction(String transactionId, String transactionType, String sid);
    
    /**
     * Unsubscribe an observator for an already registered transaction 
     * @param sid The subscription identifier that should be unsubscribed
     * @return True if the operation completes withour errors; false otherwise (e.g. there is no such subscription)
     */
    public boolean stopObservingTransaction(String sid);
}
