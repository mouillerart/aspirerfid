/*
 * Copyright © 2008-2010, Aspire 
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

package org.ow2.aspirerfid.reader.rp.impl;

import java.util.List;
import org.apache.log4j.Logger;
import org.ow2.aspirerfid.reader.rp.impl.core.ReaderDevice;
import org.ow2.aspirerfid.reader.rp.impl.core.msg.MessageLayer;
import org.ow2.aspirerfid.reader.rp.impl.core.util.ResourceLocator;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.ow2.aspirerfid.reader.rp.RmRpMBean;
import java.io.File;

/**
 * @author Nektarios Leontiadis nele@ait.edu.gr
 * 
 */
public class AccadaRmRp implements AccadaRmRpMBean {

	private static XMLConfiguration conf;
	
	private ReaderDevice device;
	private MessageLayer ml;
	private static Logger log;
	private static File configFile;

	static {
		log = Logger.getLogger(AccadaRmRp.class);
		configFile = new File(System.getProperty("user.home"),ReaderDevice.PROPERTIES_FILE);
	}

	public AccadaRmRp() {

		conf = new XMLConfiguration();
		try {
			if (configFile.exists()) {
				conf.load(configFile);
			} else {
				conf.load(ResourceLocator.getURL(null,
						ReaderDevice.DEFAULT_PROPERTIES_FILE));
				conf.save(configFile);
				conf = new XMLConfiguration();
				conf.load(configFile);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		conf.setAutoSave(true);
	}

	public void resetConfig() {
		conf = new XMLConfiguration();
		try {
			conf.load(ResourceLocator.getURL(null,ReaderDevice.DEFAULT_PROPERTIES_FILE));
			conf.save(configFile);
			conf = new XMLConfiguration();
			conf.load(configFile);

		} catch (Exception e) {
			log.error(e.getMessage());
		}
		conf.setAutoSave(true);
	}

	public void loadConfig() {
		try {
			conf.save(configFile);
		} catch (ConfigurationException e) {
			log.error("Error while saving configuration:" + e.getMessage());
		}

		if (ml != null)
			ml.reset();
	}

	public void start() {
		ml = new MessageLayer();
	}

	public void stop() {
		if (ml != null) {
			try {
				conf.save(configFile);
			} catch (ConfigurationException e) {
				log.warn("Problem while saving configuration:"+ e.getMessage());
			}
			ml.stop();
			ml = null;
		}
	}

	public void addAlarmChannel(String name, String host, int port) {
		conf.addProperty("alarmChannels.alarmChannel(-1).name", name);
		conf.addProperty("alarmChannels.alarmChannel.host", host);
		conf.addProperty("alarmChannels.alarmChannel.port", port);
	}

	public void addIOEdgeTriggerPortManager(String port) {
		conf.addProperty("IOEdgeTriggerPortManager.port(-1)", port);
	}

	public void addIOValueTriggerPortManager(String port) {
		conf.addProperty("IOValueTriggerPortManager.port(-1)", port);
	}

	public void addReader(String name, String className, String propertiesFile) {
		conf.addProperty("readers.reader(-1).name", name);
		conf.addProperty("readers.reader.class", className);
		conf.addProperty("readers.reader.properties", propertiesFile);

	}

	public void addReaderReadpoint(String readerName, String readpoint) {
		List list = conf.getList("readers.reader.name");
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).equals(readerName)) {
				
				conf.addProperty("readers.reader(" + i + ").readpoint(-1)",readpoint);
				break;
			}
		}

	}

	public void addSource(String name, boolean fixed, String readpoint) {
		conf.addProperty("sources.source(-1).name", name);
		conf.addProperty("sources.source.fixed", fixed);
		conf.addProperty("sources.source.readpoint", readpoint);
	}

	public String getAlarmChannelHost(String channelName) {
		List list = conf.getList("alarmChannels");
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).equals(channelName)) {
				return conf.getString("alarmChannels.alarmChannel(" + i
						+ ").host");
			}
		}

		return null;
	}

	public int getAlarmChannelPort(String channelName) {
		List list = conf.getList("alarmChannels");
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).equals(channelName)) {
				return conf
						.getInt("alarmChannels.alarmChannel(" + i + ").port");
			}
		}

		return -1;
	}

	public String[] getAlarmChannels() {
		List list = conf.getList("alarmChannels");
		final String COMMA = ",";
		String channels[] = new String[list.size()];
		String name, host, port;
		for (int i = 0; i < list.size(); i++) {
			name = conf.getString("alarmChannels.alarmChannel(" + i + ").name");
			host = conf.getString("alarmChannels.alarmChannel(" + i + ").host");
			port = conf.getString("alarmChannels.alarmChannel(" + i + ").port");
			channels[i] = name + COMMA + host + COMMA + port;
		}

		return channels;
	}

	public String getCurrentSource() {
		return conf.getString("currentSource");
	}

	public String getEPC() {
		return conf.getString("epc");
	}

	public long getGlimpsedTimeout() {
		return conf.getLong("glimpsedTimeout");
	}

	public int getHandle() {
		return conf.getInt("handle");
	}

	public int getHttpPort() {
		return conf.getInt("httpPort");
	}

	public boolean getHttpServerConnectionEnabled() {
		return conf.getBoolean("httpServerConnection");
	}

	public String[] getIOEdgeTriggerPortManager() {
		List list = conf.getList("IOEdgeTriggerPortManager");
		final String COMMA = ",";
		String ports[] = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			ports[i] = conf.getString("IOEdgeTriggerPortManager.port(" + i
					+ ")");
		}

		return ports;
	}

	public String[] getIOValueTriggerPortManager() {
		List list = conf.getList("IOValueTriggerPortManager");
		final String COMMA = ",";
		String ports[] = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			ports[i] = conf.getString("IOValueTriggerPortManager.port(" + i
					+ ")");
		}

		return ports;
	}

	public boolean getIsSourceFixed(String sourceName) {
		List list = conf.getList("sources.source.name");
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).equals(sourceName)) {
				return conf.getBoolean("sources.source(" + i + ").fixed");
			}
		}

		return false;
	}

	public long getLostTimeout() {
		return conf.getLong("lostTimeout");
	}

	public String getMacAddress() {
		return conf.getString("macAddress");
	}

	public String getManufacturer() {
		return conf.getString("manufacturer");
	}

	public String getManufacturerDescription() {
		return conf.getString("manufacturerDescription");
	}

	public int getMaxReadDutyCycles() {
		return conf.getInt("maxReadDutyCycles");
	}

	public int getMaxSourceNumber() {
		return conf.getInt("maxReadDutyCycles");
	}

	public int getMaxTagSelectorNumber() {
		return conf.getInt("maxTagSelectorNumber");
	}

	public int getMaxTriggerNumber() {
		return conf.getInt("maxTriggerNumber");
	}

	public String getModel() {
		return conf.getString("model");
	}

	public String getName() {
		return conf.getString("name");
	}

	public long getNotificationListenTimeout() {
		return conf.getLong("notificationListenTimeout");
	}

	public long getObservedThreshold() {
		return conf.getLong("observedThreshold");
	}

	public long getObservedTimeout() {
		return conf.getLong("observedTimeout");
	}

	public String getRMAgentAddress() {
		return conf.getString("mgmtAgentAddress");
	}

	public int getRMAgentPort() {
		return conf.getInt("mgmtAgentPort");
	}

	public String getRMAgentType() {
		return conf.getString("mgmtAgentType");
	}

	public String getRMContact() {
		return conf.getString("contact");
	}

	public String getRMDescription() {
		return conf.getString("description");
	}

	public String getRMLocationDescription() {
		return conf.getString("locationDescription");
	}

	public String getRMSerialNumber() {
		return conf.getString("serialNumber");
	}

	public int getReadCyclesPerTrigger() {
		return conf.getInt("readCyclesPerTrigger");
	}

	public String[] getReadPointNames(String readerName) {
		List list;
		int pos = -1;

		list = conf.getList("readers.reader.name");
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).equals(readerName)) {
				pos = i;
				break;
			}
		}
		if (pos != -1) {
			list = conf.getList("readers.reader(" + pos + ").readpoint");
			String readpoints[] = new String[list.size()];
			for (int i = 0; i < list.size(); i++) {
				readpoints[i] = (String) list.get(i);
			}

			return readpoints;
		} else
			return null;
	}

	public long getReadTimeout() {
		return conf.getLong("readTimeout");
	}

	public String getReaderClassName(String readerName) {
		List list = conf.getList("readers.reader.name");
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).equals(readerName)) {
				return conf.getString("readers.reader(" + i + ").class");
			}
		}

		return null;
	}

	public String getReaderPropertiesFile(String readerName) {
		List list = conf.getList("readers.reader.name");
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).equals(readerName)) {
				return conf.getString("readers.reader(" + i + ").properties");
			}
		}

		return null;
	}

	public String[] getReaders() {
		List list = conf.getList("readers.reader.name");
		return (String[]) list.toArray(new String[list.size()]);
	}

	public String getRole() {
		return conf.getString("role");
	}

	public String getSourceReadpoint(String sourceName) {
		List list = conf.getList("sources.source.name");
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).equals(sourceName)) {
				return conf.getString("readers.reader(" + i + ").readpoint");
			}
		}

		return null;
	}

	public String[] getSources() {
		List list = conf.getList("sources.source.name");
		return (String[]) list.toArray(new String[list.size()]);
	}

	public int getTcpPort() {
		return conf.getInt("tcpPort");
	}

	public boolean getTcpServerConnection() {
		return conf.getBoolean("tcpServerConnection");
	}

	public int getThreadPoolSize() {
		return conf.getInt("threadPoolSize");
	}

	public void setCurrentSource(String source) {
		conf.setProperty("currentSource", source);
	}

	public void setEPC(String epc) {
		conf.setProperty("epc", epc);
	}

	public void setGlimpsedTimeout(long timeout) {
		conf.setProperty("glimpsedTimeout", timeout);
	}

	public void setHandle(int handle) {
		conf.setProperty("handle", handle);
	}

	public void setHttpPort(int port) {
		conf.setProperty("httpPort", port);
	}

	public void setHttpServerConnectionEnabled(boolean isEnabled) {
		conf.setProperty("tcpServerConnection", isEnabled);
	}

	public void setIsSourceFized(boolean isFixed) {
		conf.setProperty("isFixed", isFixed);
	}

	public void setLostTimeout(long timeout) {
		conf.setProperty("lostTimeout", timeout);
	}

	public void setMacAddress(String mac) {
		conf.setProperty("macAddress", mac);
	}

	public void setManufacturer(String manufacturer) {
		conf.setProperty("manufacturer", manufacturer);
	}

	public void setManufacturerDescription(String desc) {
		conf.setProperty("manufacturerDescription", desc);
	}

	public void setMaxReadDutyCycles(int cycles) {
		conf.setProperty("maxReadDutyCycles", cycles);
	}

	public void setMaxSourceNumber(int number) {
		conf.setProperty("maxSourceNumber", number);
	}

	public void setMaxTagSelectorNumber(int number) {
		conf.setProperty("maxTagSelectorNumber", number);
	}

	public void setMaxTriggerNumber(int number) {
		conf.setProperty("maxTriggerNumber", number);
	}

	public void setModel(String model) {
		conf.setProperty("model", model);
	}

	public void setName(String name) {
		conf.setProperty("name", name);
	}

	public void setNotificationListenTimeout(long timeout) {
		conf.setProperty("notificationListenTimeout", timeout);
	}

	public void setObservedThreshold(long threshold) {
		conf.setProperty("observedThreshold", threshold);
	}

	public void setObservedTimeout(long timeout) {
		conf.setProperty("observedTimeout", timeout);
	}

	public void setRMAgentAddress(String address) {
		conf.setProperty("mgmtAgentAddress", address);
	}

	public void setRMAgentType(String type) {
		conf.setProperty("mgmtAgentType", type);
	}

	public void setRMContact(String contact) {
		conf.setProperty("contact", contact);
	}

	public void setRMDescription(String description) {
		conf.setProperty("description", description);
	}

	public void setRMLocationDescription(String desc) {
		conf.setProperty("description", desc);
	}

	public void setRMSerialNumber(String serial) {
		conf.setProperty("serialNumber", serial);
	}

	public void setReadCyclesPerTrigger(int cycles) {
		conf.setProperty("readCyclesPerTrigger", cycles);
	}

	public void setReadTimeout(long timeout) {
		conf.setProperty("readTimeout", timeout);
	}

	public void setRole(String role) {
		conf.setProperty("role", role);
	}

	public void setTcpPort(int port) {
		conf.setProperty("tcpPort", port);
	}

	public void setTcpServerConnection(boolean isEnabled) {
		conf.setProperty("tcpServerConnection", isEnabled);
	}

	public void setThreadPoolSize(int size) {
		conf.setProperty("threadPoolSize", size);
	}

}
