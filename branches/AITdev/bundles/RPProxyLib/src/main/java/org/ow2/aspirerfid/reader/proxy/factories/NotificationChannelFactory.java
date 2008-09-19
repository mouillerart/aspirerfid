/*
 * Copyright (C) 2007 ETH Zurich
 *
 * This file is part of Accada (www.accada.org).
 *
 * Accada is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1, as published by the Free Software Foundation.
 *
 * Accada is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Accada; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA  02110-1301  USA
 */

package org.ow2.aspirerfid.rp.proxy.factories;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import org.ow2.aspirerfid.rp.proxy.NotificationChannel;
import org.ow2.aspirerfid.rp.proxy.RPProxyException;
import org.ow2.aspirerfid.rp.proxy.ReaderDevice;
import org.ow2.aspirerfid.rp.proxy.invocationHandlers.ProxyInvocationHandler;
import org.ow2.aspirerfid.rp.proxy.msg.ParameterTypeException;
import org.ow2.aspirerfid.rp.proxy.msg.ProxyConnection;

/**
 * This class creates new notification channels and new notification channel proxies. 
 * 
 * @author regli
 */
public class NotificationChannelFactory {

	/**
	 * This method returns a new notification channel proxy which belongs to an existing notification channel.
	 * 
	 * @param name of the existing notification channel
	 * @param proxyConnection of the corresponding reader device proxy
	 * @return new notification channel proxy
	 */
	public static NotificationChannel getNotificationChannel(String name, ProxyConnection proxyConnection) {
		
		InvocationHandler handler = new ProxyInvocationHandler("NotificationChannel", name, proxyConnection);
		return (NotificationChannel) Proxy.newProxyInstance(NotificationChannel.class.getClassLoader(), new Class[] { NotificationChannel.class }, handler);
		
	}

	/**
	 * This method creates a new notification channel and returns a corresponding proxy.
	 * 
	 * @param name of the new notification channel
	 * @param address of the new notification channel
	 * @param readerDevice to which the new notification channel should be added
	 * @return corresponding notification channel proxy
	 * @throws RPProxyException if the new notification channel could not be created
	 */
	public static NotificationChannel createNotificationChannel(String name, String address, ReaderDevice readerDevice) throws RPProxyException {
		
		try {
			ProxyConnection proxyConnection = ReaderDeviceFactory.getConnection(readerDevice);
			proxyConnection.executeCommand("NotificationChannel", "create", new Class[] {String.class, String.class}, new Object[] {name, address}, "", false);
			return getNotificationChannel(name, proxyConnection);
		} catch (ParameterTypeException e) {
			throw new RPProxyException(e.getClass() + ": " + e.getMessage());
		}
		
	}

}