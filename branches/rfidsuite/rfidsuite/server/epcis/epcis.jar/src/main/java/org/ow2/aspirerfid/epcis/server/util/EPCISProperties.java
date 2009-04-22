/*
 * Copyright 2005-2008, Aspire This library is free software; you can
 * redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation (the "LGPL");
 * either version 2.1 of the License, or (at your option) any later version. If
 * you do not alter this notice, a recipient may use your version of this file
 * under either the LGPL version 2.1, or (at his option) any later version. You
 * should have received a copy of the GNU Lesser General Public License along
 * with this library; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA. This software is
 * distributed on an "AS IS" basis, WITHOUT WARRANTY OF ANY KIND, either express
 * or implied. See the GNU Lesser General Public License for the specific
 * language governing rights and limitations.
 */
package org.ow2.aspirerfid.epcis.server.util;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.management.loading.MLet;
import javax.management.loading.MLetMBean;

/**
 * TODO Javadoc
 * 
 * @author Unknown
 */
public class EPCISProperties implements EPCISPropertiesMBean {
	private static EPCISProperties instance = new EPCISProperties();
	private Properties properties;

	private EPCISProperties() {
		System.out.println("=======================Properties==============");
		try {
			if (properties == null) {
				properties = new Properties();
				properties.load(EPCISProperties.class
						.getResourceAsStream("/epcis.properties"));
			}
			registerMBean();

		} catch (IOException e) {
			e.printStackTrace();
			properties = new Properties();
		}
	}

	private void registerMBean() {
		ObjectName name;
		try {
			name = new ObjectName("aspire:Name=epcisprops");

			MBeanServer server = lookForExistingServer();
			if (server != null && !server.isRegistered(name)) {
				System.out
						.println("Creating new MBean for epcis properties");
				
				server.registerMBean(this, name);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get a property from the property file
	 * 
	 * @param key
	 *            : the key property
	 * @return the associated value
	 */
	public static String getProperty(String key) {
		return instance.getValue(key);
	}

	public String getValue(String key) {
		return properties.getProperty(key);
	}

	public void setValue(String key, String value) {
		System.out.println("MBean setting " + key + " to " + value);
		properties.put(key, value);
	}

	private MBeanServer lookForExistingServer() {
		List mbeanServers = MBeanServerFactory.findMBeanServer(null);
		if (mbeanServers != null && mbeanServers.size() > 0) {
			return (MBeanServer) mbeanServers.get(0);
		}
		System.out.println("NO MBeanServer found!");
		return null;
	}

}
