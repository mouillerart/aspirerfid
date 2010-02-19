/**
 * Copyright (c) 2008-2010, AspireRFID
 *
 * This library is free software; you can redistribute it and/or
 * modify it either under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation
 * (the "LGPL"). If you do not alter this
 * notice, a recipient may use your version of this file under the LGPL.
 *
 * You should have received a copy of the LGPL along with this library
 * in the file COPYING-LGPL-2.1; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * This software is distributed on an "AS IS" basis, WITHOUT WARRANTY
 * OF ANY KIND, either express or implied. See the LGPL  for
 * the specific language governing rights and limitations.
 *
 * Contact: AspireRFID mailto:aspirerfid@ow2.org
 */

package org.ow2.aspirerfid.util.mbean;

import java.util.ArrayList;
import java.util.Iterator;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * provides a utility class to register/unregister MBeans when the servlet is deployed/undeployed
 * @author Didier Donsez
 */
public abstract class AbstractWebappMBean implements ServletContextListener {

	protected ServletContext servletContext;

	private static boolean trace=true;
	private static boolean log=false;
	
	final public void contextInitialized(ServletContextEvent event) {
		servletContext=event.getServletContext();
		listServers();
		register(getServer());
		start();
	}

	abstract protected void start();
	abstract protected void stop();

	final public void contextDestroyed(ServletContextEvent event) {
		stop();
		unregister(getServer());
	}

	protected ServletContext getServletContext() {
		return servletContext;	
	}
	
	protected abstract ObjectName getObjectName();

	protected void print(String msg){
		if(trace) System.out.println(msg);
		if(log) servletContext.log(msg);
	}

	protected void print(String msg, Throwable t){
		if(trace) {
			System.err.println(msg + " : " + t.getMessage());
			t.printStackTrace();
		}
		if(log) {
			servletContext.log(msg + " : " + t.getMessage());
		}
	}

	private void register(MBeanServer server) {
			try {
				server.registerMBean(this, getObjectName());
			} catch (Exception e) {
				print("Exception while registering",e);
			}
	}

	private void unregister(MBeanServer server) {
		try {
			server.unregisterMBean(getObjectName());
		} catch (Exception e) {
			print("Exception while unregistering",e);
		}
	}
	
	private void listServers() {
		ArrayList mbservers = MBeanServerFactory.findMBeanServer(null);
		if (mbservers == null || mbservers.size()==0) {
			print("No MBeanServer found");
		} else {
			for (Iterator iterator = mbservers.iterator(); iterator.hasNext();) {
				MBeanServer mBeanServer = (MBeanServer) iterator.next();
				print("MBeanServer:"+mBeanServer.toString());
			}
		}
		MBeanServer platformMBeanServer = java.lang.management.ManagementFactory.getPlatformMBeanServer();
		print("Platform MBeanServer:"+platformMBeanServer.toString());	
	}
	
	private MBeanServer getServer() {
		MBeanServer mbeanserver = null;

		ArrayList mbservers = MBeanServerFactory.findMBeanServer(null);

		if (mbservers.size() > 0) {
			mbeanserver = (MBeanServer) mbservers.get(0);
		}

		if (mbeanserver != null) {
			print("Found MBeanServer:"+mbeanserver.toString());
		} else {
			mbeanserver = MBeanServerFactory.createMBeanServer();
			print("Create MBeanServer:"+mbeanserver.toString());
		}

		return mbeanserver;
	}
}
