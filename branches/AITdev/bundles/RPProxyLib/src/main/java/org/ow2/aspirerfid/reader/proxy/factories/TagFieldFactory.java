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

import org.ow2.aspirerfid.rp.proxy.RPProxyException;
import org.ow2.aspirerfid.rp.proxy.ReaderDevice;
import org.ow2.aspirerfid.rp.proxy.TagField;
import org.ow2.aspirerfid.rp.proxy.invocationHandlers.ProxyInvocationHandler;
import org.ow2.aspirerfid.rp.proxy.msg.ParameterTypeException;
import org.ow2.aspirerfid.rp.proxy.msg.ProxyConnection;

/**
 * This class creates new tag fields and new tag field proxies. 
 * 
 * @author regli
 */
public class TagFieldFactory {

	/**
	 * This method returns a new tag field proxy which belongs to an existing tag field.
	 * 
	 * @param name of the existing tag field
	 * @param proxyConnection of the corresponding reader device proxy
	 * @return new tag field proxy
	 */
	public static TagField getTagField(String name, ProxyConnection proxyConnection) {
		
		InvocationHandler handler = new ProxyInvocationHandler("TagField", name, proxyConnection);
		return (TagField) Proxy.newProxyInstance(TagField.class.getClassLoader(), new Class[] { TagField.class }, handler);
		
	}

	/**
	 * This method creates a new tag field and returns a corresponding proxy.
	 * 
	 * @param name of the new tag field
	 * @param readerDevice to which the new tag field should be added
	 * @return corresponding tag field proxy
	 * @throws RPProxyException if the new tag field could not be created
	 */
	public static TagField createTagField(String name, ReaderDevice readerDevice) throws RPProxyException {
		
		try {
			ProxyConnection proxyConnection = ReaderDeviceFactory.getConnection(readerDevice);
			proxyConnection.executeCommand("TagField", "create", new Class[] {String.class}, new Object[] {name}, "", false);
			return getTagField(name, proxyConnection);
		} catch (ParameterTypeException e) {
			throw new RPProxyException(e.getClass() + ": " + e.getMessage());
		}
		
	}

}