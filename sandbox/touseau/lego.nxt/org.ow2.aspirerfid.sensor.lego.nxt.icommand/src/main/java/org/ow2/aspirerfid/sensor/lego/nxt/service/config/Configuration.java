/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.ow2.aspirerfid.sensor.lego.nxt.service.config;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * configuration utility
 * 
 * @author <a href="mailto:dev@felix.apache.org">Felix Project Team</a>
 */
public class Configuration {
	static String DEFAULT_PROPERTIES_FILE = "config.properties";

	/**
	 * loads properties from a file
	 * 
	 * @param filename
	 *            the properties file name
	 * @return the properties
	 */
	public static Properties loadProperties(String filename) {
		if (filename == null) {
			filename = System.getProperty(
					"org.apache.felix.sandbox.util.configuration.file",
					DEFAULT_PROPERTIES_FILE);
		}
		try {
			return loadProperties(new FileInputStream(filename));
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * loads properties from a input stream
	 * 
	 * @param in
	 *            the imput stream
	 * @return the properties
	 */
	public static Properties loadProperties(InputStream in) {
		Properties prop = new Properties();
		try {
			prop.load(in);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		return prop;
	}

	/**
	 * builds a map from properties. the key list is in the property
	 * keyListPropertyName <br>
	 * Example getMap(props,"keylist") returns a hashtable with 3 key-value
	 * entry
	 * 
	 * <pre>
	 *  keylist=bar;bubba;foo
	 *  bar=BAR
	 *  foo=1.5;float
	 *  bubba=100;int
	 * </pre>
	 * 
	 * @param props
	 *            the properties
	 * @param keyListPropertyName
	 *            the property name of the key list (separed by ;)
	 * @return The map
	 */
	public static Map getMap(Properties props, String keyListPropertyName) {
		return getHashtable(props, keyListPropertyName);
	}

	/**
	 * builds a hashtable from properties. the key list is in the property
	 * keyListPropertyName <br>
	 * Example getHashtable(props,"keylist") returns a hashtable with 3
	 * key-value entry
	 * 
	 * <pre>
	 *  keylist=bar;bubba;foo
	 *  bar=BAR
	 *  foo=1.5;float
	 *  bubba=100;int
	 * </pre>
	 * 
	 * @param props
	 *            the properties
	 * @param keyListPropertyName
	 *            the property name of the key list (separed by ;)
	 * @return The hashtable
	 */
	public static Hashtable getHashtable(Properties props,
			String keyListPropertyName) {

		StringTokenizer st = new StringTokenizer(props
				.getProperty(keyListPropertyName), ";");
		Hashtable map = new Hashtable();

		while (st.hasMoreTokens()) {
			String key = st.nextToken();
			StringTokenizer stvaluetype = new StringTokenizer(props
					.getProperty(key), ";");
			Object obj = null;
			if (stvaluetype.hasMoreTokens()) {
				String value = stvaluetype.nextToken();
				if (stvaluetype.hasMoreTokens()) {
					String type = stvaluetype.nextToken();
					if (type.equals("int")) {
						obj = new Integer(value);
					} else if (type.equals("long")) {
						obj = new Long(value);
					} else if (type.equals("float")) {
						obj = new Float(value);
					} else if (type.equals("double")) {
						obj = new Double(value);
					} else if (type.equals("string")) {
						obj = value;
					} else {
						// default : string
						obj = value;
					}
				} else {
					// no type means String
					obj = value;
				}

			}
			map.put(key, obj);
		}
		return map;
	}

	/**
	 * builds a (ordered) vector from properties. the index list is in the
	 * property keyListPropertyName <br>
	 * Example getVector(props,"tab") returns a 4 elements vector (tab.5 is
	 * ignored since tab.4 does not exist)
	 * 
	 * <pre>
	 *  tab.0=bar
	 *  tab.1=foo
	 *  tab.2=1.5;float
	 *  tab.3=100;int
	 *  tab.5=10000;long
	 * </pre>
	 * 
	 * @param props
	 *            the properties
	 * @param vectorPropertyName
	 *            the property name of the key list (separed by ;)
	 * @return The vector
	 */
	public static Vector getVector(Properties props, String vectorPropertyName) {

		Vector vect = new Vector();

		for (int i = 0; true; i++) {
			String valuetype = props.getProperty(vectorPropertyName) + "." + i;
			if (valuetype == null)
				break;
			StringTokenizer stvaluetype = new StringTokenizer(valuetype, ";");
			Object obj = null;
			if (stvaluetype.hasMoreTokens()) {
				String value = stvaluetype.nextToken();
				if (stvaluetype.hasMoreTokens()) {
					String type = stvaluetype.nextToken();
					if (type.equals("int")) {
						obj = new Integer(value);
					} else if (type.equals("long")) {
						obj = new Long(value);
					} else if (type.equals("float")) {
						obj = new Float(value);
					} else if (type.equals("double")) {
						obj = new Double(value);
					} else if (type.equals("string")) {
						obj = value;
					} else {
						// default : string
						obj = value;
					}
				} else {
					// no type means String
					obj = value;
				}

			}
			vect.addElement(obj);
		}
		return vect;
	}
}
