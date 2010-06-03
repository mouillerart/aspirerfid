/*
 * Copyright 2005-2008, Aspire
 * 
 * This library is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by 
 * the Free Software Foundation (the "LGPL"); either version 2.1 of the 
 * License, or (at your option) any later version. If you do not alter this 
 * notice, a recipient may use your version of this file under either the 
 * LGPL version 2.1, or (at his option) any later version.
 * 
 * You should have received a copy of the GNU Lesser General Public License 
 * along with this library; if not, write to the Free Software Foundation, 
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * 
 * This software is distributed on an "AS IS" basis, WITHOUT WARRANTY OF ANY 
 * KIND, either express or implied. See the GNU Lesser General Public 
 * License for the specific language governing rights and limitations.
 */
package org.ow2.aspirerfid.smsreceiver.bundle;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.ow2.aspirerfid.smsreceiver.LocalUserThread;

/**
 * The class starts and stops the SMS receiver 
 * @author Didier Donsez
 */
public class Activator implements BundleActivator {

	private LocalUserThread userThread = null;

	public void start(BundleContext bundleContext) throws Exception {

		// get the EventAdmin service
		
		
		// get the configuration
		
		// Launching a thread
//		try
//		{
//			userThread = new LocalUserThread(modemPort.getInputStream(),
//					modemPort.getOutputStream(), guiPort.getOutputStream(),
//					args[2], args[3], args[4]);
//		}
//		catch (IOException e)
//		{
//			e.printStackTrace();
//		}
		userThread.start();		
	}

	public void stop(BundleContext bundleContext) throws Exception {
		userThread.end();
		// join the thread ?
	}
}