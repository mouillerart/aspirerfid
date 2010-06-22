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
 */
package org.ow2.aspirerfid.sandbox.calmant.bluetooth;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import javax.bluetooth.DiscoveryAgent;

/**
 * These are the properties of the bluetooh connection.
 * 
 * @author <a href="mailto:angoca@yahoo.com">Andres Gomez</a>
 * @author Thomas Calmant
 * @version 1.0 07/07/2008
 * @version 1.1 08/06/2010
 */
public class BluetoothSettings {

	/**
	 * Remote device authentication.
	 */
	public static final String AUTHENTICATE = "authenticate";
	/**
	 * Remote device authentication par default.
	 */
	private static final String AUTHENTICATE_VALUE = "false";
	/**
	 * Encrypt connection.
	 */
	public static final String ENCRYPT = "encrypt";
	/**
	 * Encrypt connection par default.
	 */
	private static final String ENCRYPT_VALUE = "false";
	/**
	 * Inquiry mode par default.
	 */
	public static final String INQUIRY_MODE = "inquiryMode";
	/**
	 * Inquiry mode par default.
	 */
	private static final String INQUIRY_MODE_VALUE = "" + DiscoveryAgent.GIAC;
	/**
	 * Is inquiry scanning allowed during a connection: "true" or "false".
	 */
	public static final String INQUIRY_SCAN = "bluetooth.connected.inquiry.scan";
	/**
	 * Is master/slave switch allowed: "true" or "false".
	 */
	public static final String MASTER_SLAVE = "bluetooth.master.switch";

	/**
	 * The maximum number of connected devices supported.
	 */
	public static final String MAX_DEVICES = "bluetooth.connected.devices.max";

	/**
	 * The L2CAP maximum transmission unit.
	 */
	public static final String MAX_MTU = "bluetooth.l2cap.receiveMTU.max";

	/**
	 * The maximum number of service-record attributes that can be retrieved at
	 * a time.
	 */
	public static final String MAX_RETRIEVABLE = "bluetooth.sd.attr.retrievable.max";

	/**
	 * The maximum number of concurrent service-discovery transactions.
	 */
	public static final String MAX_TRANS = "bluetooth.sd.trans.max";

	/**
	 * Is paging allowed during a connection: "true" or "false".
	 */
	public static final String PAGE = "bluetooth.connected.page";

	/**
	 * Is page scanning allowed during a connection: "true" or "false".
	 */
	public static final String PAGE_SCAN = "bluetooth.connected.page.scan";

	/**
	 * Properties file name.
	 */
	private static String propertiesFileName = "bluetooth.xml";

	/**
	 * Service name.
	 */
	public static final String SERVICE_NAME = "serviceName";

	/**
	 * Default Service name.
	 */
	private static final String SERVICE_NAME_VALUE = "Echo Server";

	/**
	 * Service uuid.
	 */
	public static final String UUID = "uuid";

	/**
	 * Default Service uuid.
	 */
	private static final String UUID_VALUE = "9ee2b296da2448399658cbc0a7ee69e2";

	/**
	 * Max number of threads
	 */
	public static final String MAX_THREADS = "max_threads";

	/**
	 * Default thread maximum count
	 */
	private static final String MAX_THREADS_VALUE = "5";

	/**
	 * Bluetooth properties.
	 */
	private final Properties m_bluetoothProperties;

	/**
	 * Default constructor. Set default values, then try to read the default
	 * configuration file
	 */
	public BluetoothSettings() {
		this(BluetoothSettings.propertiesFileName);
	}

	/**
	 * Loads the bluetooth properties from the given file name.
	 * 
	 * @param fileName
	 *            File name of the bluetooth properties.
	 */
	public BluetoothSettings(final String fileName) {
		// It cannot be re factored because the attribute defaultProps is final.
		Properties defaultProps = setDefault();
		this.m_bluetoothProperties = new Properties(defaultProps);

		try {
			loadXML(fileName);
		} catch (IOException error) {
			System.err
					.println("Error reading configuration file : " + fileName);
		}
	}

	/**
	 * Loads the bluetooth properties from a given input stream
	 * 
	 * @param in
	 *            Input stream where the properties can be read.
	 * @throws IOException
	 */
	public BluetoothSettings(final InputStream in) throws IOException {
		Properties defaultProps = setDefault();
		this.m_bluetoothProperties = new Properties(defaultProps);
		loadXML(in);
	}

	private void loadXML(final String fileName) throws IOException {
		File propertiesFile = new File(fileName);
		InputStream in = new FileInputStream(propertiesFile);
		loadXML(in);
	}

	/**
	 * @param in
	 * @throws IOException
	 */
	private void loadXML(final InputStream in) throws IOException {
		try {
			this.m_bluetoothProperties.loadFromXML(in);
		} catch (InvalidPropertiesFormatException e) {
			throw new IOException("Bad file format : " + e.getMessage());
		}
	}

	/**
	 * @return Properties with the default settings.
	 */
	private Properties setDefault() {
		Properties defaultProps = new Properties();
		defaultProps.setProperty(BluetoothSettings.UUID,
				BluetoothSettings.UUID_VALUE);
		defaultProps.setProperty(BluetoothSettings.SERVICE_NAME,
				BluetoothSettings.SERVICE_NAME_VALUE);
		defaultProps.setProperty(BluetoothSettings.AUTHENTICATE,
				BluetoothSettings.AUTHENTICATE_VALUE);
		defaultProps.setProperty(BluetoothSettings.ENCRYPT,
				BluetoothSettings.ENCRYPT_VALUE);
		defaultProps.setProperty(BluetoothSettings.INQUIRY_MODE,
				BluetoothSettings.INQUIRY_MODE_VALUE);
		defaultProps.setProperty(MAX_THREADS, MAX_THREADS_VALUE);
		return defaultProps;
	}

	/**
	 * Returns a given string, according to the given key.
	 * 
	 * @param key
	 *            Object's key.
	 * @return String related to the given key.
	 */
	public final String getProperty(final String key) {
		String value = "";
		value = this.m_bluetoothProperties.getProperty(key);
		System.out.println("[PARAM] " + key + ": " + value);

		return value;
	}
}
