/*
 *  Copyright (C) Aspire
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */package org.ow2.aspirerfid.nfc.server.message.request;

import org.ow2.aspirerfid.nfc.server.message.response.AbstractResponseMessage;

/**
 * Message received from the telephone.
 * 
 * @author <a href="mailto:angoca@yahoo.com">Andres Gomez</a>
 * @version 1.0 07/07/2008
 */
public abstract class AbstractRequestMessage {

	/**
	 * Creates the message to return to the telephone.
	 * 
	 * @param receivedMess
	 *            Received message.
	 * @return Specific response message.
	 */
	public abstract AbstractResponseMessage getResponseMessage(
			String receivedMess);

}