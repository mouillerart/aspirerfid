package org.ow2.aspirerfid.sandbox.calmant.btbundle;

import org.ow2.aspirerfid.sandbox.calmant.bluetooth.BluetoothServerService;
import org.ow2.aspirerfid.sandbox.calmant.bluetooth.CommunicationListener;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

public class BTSActivator implements BundleActivator, CommunicationListener {
	private ServiceTracker m_serviceTracker;
	
	public void start(BundleContext context) {
		System.out.println("Starting...");
		
		m_serviceTracker = new ServiceTracker(context,
				BluetoothServerService.class.getName(), null);
		
		m_serviceTracker.open();
		
		BluetoothServerService m_server = (BluetoothServerService)m_serviceTracker.getService();
		
		if(m_server == null)
		{
			System.out.println("Server not found...");
			return;
		}
		
		m_server.prepareServer();
		m_server.addCommunicationListener(this);
		System.out.println("Server prepared.");
		new Thread(m_server).start();
		System.out.println("Server started.");
	}
	
	public void stop(BundleContext context) {
		BluetoothServerService m_server = (BluetoothServerService)m_serviceTracker.getService();
		if(m_server == null)
		{
			System.out.println("Server is not running");
			return;
		}
		
		m_server.removeCommunicationListener(this);
		m_server.stop();
		System.out.println("Server stopped.");
	}

	public void commBegin(String logicalName) {
		System.out.println("[BTTest] Communication started with " + logicalName);
	}

	public void commEnd(String logicalName) {
		System.out.println("[BTTest] Communication ended with " + logicalName);
	}

	public void commRead(String logicalName, String data) {
		System.out.println("[BTTest] " + logicalName + " -> " + data);
	}
}
