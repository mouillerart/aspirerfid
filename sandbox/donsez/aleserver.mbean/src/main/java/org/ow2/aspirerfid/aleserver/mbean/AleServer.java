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
package org.ow2.aspirerfid.aleserver.mbean;

import java.util.HashMap;
import java.util.Map;

import javax.management.ObjectName;

import org.ow2.aspirerfid.util.mbean.AbstractWebappMBean;

/**
 * provides the implementation of the MBean to manage the ALEServer
 * @author Didier Donsez
 */
public class AleServer extends AbstractWebappMBean implements AleServerMBean {
	private ObjectName name = null;

	private long _startTime = 0L;

	public AleServer() {
		try {
			name = new ObjectName(OBJECTNAME);
		} catch (Exception e) {
			getServletContext().log(e.getMessage()+" "+OBJECTNAME,e);
		}
	}
	
	protected void start() {
		_startTime = System.currentTimeMillis();
	}

	protected void stop() {
	}
	
	protected ObjectName getObjectName() {
		return name;
	}

	// interface method implementations

	public long getUptime() {
		return System.currentTimeMillis() - _startTime;
	}

	public Map getProperties() {
		Map dummy=new HashMap();
		dummy.put("foo", "bar");
		dummy.put("hello","world");
		return dummy;
	}	
}