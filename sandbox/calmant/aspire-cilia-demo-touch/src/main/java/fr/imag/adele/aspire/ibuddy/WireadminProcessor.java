/*
 * Copyright 2009 OW2 Chameleon
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.imag.adele.aspire.ibuddy;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.wireadmin.Consumer;
import org.osgi.service.wireadmin.Wire;
import org.osgi.service.wireadmin.WireConstants;
import org.osgi.util.measurement.Measurement;
import org.osgi.util.measurement.State;
import org.osgi.util.position.Position;

import fr.liglab.adele.cilia.Data;
import fr.liglab.adele.cilia.framework.IProcessor;

public class WireadminProcessor implements Consumer, Runnable, IProcessor {

	private String m_PID;

	private long m_pollDelay;

	@SuppressWarnings("unchecked")
	private static final Class[] m_flavors = new Class[] { Measurement.class,
			State.class, Image.class, Position.class, Double.class,
			Float.class, Long.class, Integer.class, Short.class, Boolean.class,
			double[].class, float[].class, long[].class, int[].class,
			short[].class, byte[].class, boolean[].class };

	private BundleContext m_bundleContext;

	/**
	 * registration of the Consumer service
	 */
	private ServiceRegistration m_serviceRegistration;

	private boolean m_end;

	private Wire[] m_wires;

	private Thread m_thread;

	private HashMap<String, LinkedList<Dictionary<String, Object>>> m_data = new HashMap<String, LinkedList<Dictionary<String, Object>>>();

	public WireadminProcessor(BundleContext bc) {
		m_bundleContext = bc;
	}

	public void activate() {
		// registration of the Consumer service
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put(Constants.SERVICE_PID, m_PID);
		props.put(WireConstants.WIREADMIN_CONSUMER_FLAVORS, m_flavors);
		m_serviceRegistration = m_bundleContext.registerService(
				new String[] { Consumer.class.getName() }, this, props);

		// start the thread
		m_end = false;
		m_thread = new Thread(this);
		m_thread.setName("Wire2EventBridge poll");
		m_thread.start();
	}

	public void deactivate() {
		// stop the thread
		m_end = true;
		m_thread.interrupt();

		// unregistration of the consumer service
		m_serviceRegistration.unregister();
	}

	/**
	 * @see org.osgi.service.wireadmin.Consumer#updated(org.osgi.service.wireadmin.Wire,
	 *      java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public void updated(Wire wire, Object o) {
		// m_lastData=o;

		Dictionary d = new Properties();

		String source = (String) wire.getProperties().get(
				WireConstants.WIREADMIN_PRODUCER_PID);
		if (source != null)
			d.put("source", source);

		if (o instanceof Measurement) {
			Measurement m = (Measurement) o;
			d.put("value", new Double(m.getValue()));
			d.put("timestamp", new Long(m.getTime()));
		} else if (o instanceof State) {
			State s = (State) o;
			d.put("value", new Integer(s.getValue()));
			d.put("state", s.getName());
			d.put("timestamp", new Long(s.getTime()));
		} else if (o instanceof Position) {
			// TODO test nullity of lat,long and alt
			Position p = (Position) o;
			Measurement m;
			Long t = null;

			m = p.getLatitude();
			if (m != null) {
				d.put("latitude", new Double(m.getValue()));
				if (t == null)
					t = new Long(m.getTime());
			}

			m = p.getLongitude();
			if (m != null) {
				d.put("longitude", new Double(m.getValue()));
				if (t == null)
					t = new Long(m.getTime());
			}

			m = p.getAltitude();
			if (m != null) {
				d.put("altitude", new Double(m.getValue()));
				if (t == null)
					t = new Long(m.getTime());
			}

			m = p.getSpeed();
			if (m != null) {
				d.put("speed", new Double(m.getValue()));
				if (t == null)
					t = new Long(m.getTime());
			}

			m = p.getTrack();
			if (m != null) {
				d.put("track", new Double(m.getValue()));
				if (t == null)
					t = new Long(m.getTime());
			}

			if (t != null) {
				d.put("timestamp", t);
			}
		} else {
			d.put("timestamp", new Long(System.currentTimeMillis()));
			d.put("value", o);
		}

		// Store dictionary
		if (!m_data.containsKey(source))
			m_data.put(source, new LinkedList());

		m_data.get(source).addLast(d);
	}

	public void run() {
		while (!m_end) {
			synchronized (this) {
				if (m_wires != null) {
					for (Wire wire : m_wires) {
						if (wire.isValid() && wire.isConnected()) {
							updated(wire, wire.poll());
						}
					}
				}
			}
			try {
				Thread.sleep(m_pollDelay);
			} catch (InterruptedException e) {
			}
		}
	}

	public void producersConnected(Wire[] wires) {
		synchronized (this) {
			m_wires = wires;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Data> process(List listSet) {
		List<Data> result = new ArrayList<Data>();

		for (String key : m_data.keySet())
			result.add(new Data(m_data.get(key), key));

		return result;
	}
}
