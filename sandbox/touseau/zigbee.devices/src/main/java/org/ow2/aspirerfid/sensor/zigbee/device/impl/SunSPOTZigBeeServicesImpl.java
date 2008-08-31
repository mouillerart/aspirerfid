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
package org.ow2.aspirerfid.sensor.zigbee.device.impl;

import java.io.IOException;
import java.util.Dictionary;

import org.osgi.service.log.LogService;
import org.ow2.aspirerfid.sensor.zigbee.device.NodeDiscoveryService;
import org.ow2.aspirerfid.sensor.zigbee.device.NodeListener;
import org.ow2.aspirerfid.sensor.zigbee.device.NodePacketService;
import org.ow2.aspirerfid.sensor.zigbee.device.PacketReceiver;

/**
 * This class porvides the implementation of the discovery and packet services based on the SunSPOT host libraries.
 * @author Didier Donsez
 *
 */
public class SunSPOTZigBeeServicesImpl implements NodeDiscoveryService, NodePacketService {

	private LogService logService;
		
	/**
	 * @param logService the logService to bind
	 */
	public void bindLogService(LogService logService) {
		this.logService = logService;
	}
	
	/**
	 * @param logService the logService to unbind
	 */
	public void unbindLogService(LogService logService) {
		this.logService = null;
	}

	public void addNodeListener(NodeListener nodeListener) {
		// TODO Auto-generated method stub
		
	}

	public byte[][] getAvailableNodeAddresses(long timeout) {
		// TODO Auto-generated method stub
		return null;
	}

	public byte[][] getKnownNodeAddresses(long timeout) {
		// TODO Auto-generated method stub
		return null;
	}

	public Dictionary getNodeInfo(byte[] nodeAddress) {
		// TODO Auto-generated method stub
		return null;
	}

	public void removeAllNodeListeners() {
		// TODO Auto-generated method stub
		
	}

	public void removeNodeListener(NodeListener nodeListener) {
		// TODO Auto-generated method stub
		
	}

	public void addPacketReceiver(byte[] nodeAddress, int port, PacketReceiver p) {
		// TODO Auto-generated method stub
		
	}

	public void addPacketReceiver(int port, PacketReceiver p) {
		// TODO Auto-generated method stub
		
	}

	public void broadcastPacket(int port, int maxHop, byte[] packetData)
			throws IOException {
		// TODO Auto-generated method stub
		
	}

	public void removeAllPacketReceivers() {
		// TODO Auto-generated method stub
		
	}

	public void removePacketReceiver(byte[] nodeAddress, int port,
			PacketReceiver p) {
		// TODO Auto-generated method stub
		
	}

	public void removePacketReceiver(int port, PacketReceiver p) {
		// TODO Auto-generated method stub
		
	}

	public void sendPacket(byte[] nodeAddress, int port, byte[] packetData)
			throws IOException {
		// TODO Auto-generated method stub
		
	}

}
