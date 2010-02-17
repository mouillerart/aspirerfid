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

import javax.management.ObjectName;

import org.ow2.aspirerfid.util.mbean.AbstractWebappMBean;

/**
 * provides a sample implementation of the ALE Server MBean
 * @author Didier Donsez
 */
public class AleServer extends AbstractWebappMBean implements AleServerMBean {
	public static final String OBJECTNAME = "org.ow2.aspirerfid:type=aleserver";
	private ObjectName name = null;

	private long _startTime = 0L;

	public AleServer() {
		_startTime = System.currentTimeMillis();
		try {
			name = new ObjectName(OBJECTNAME);
		} catch (Exception e) {
			getServletContext().log(e.getMessage()+" "+OBJECTNAME,e);
		}
	}
	
	protected ObjectName getObjectName() {
		return name;
	}

	// interface method implementations

	public long getUptime() {
		return System.currentTimeMillis() - _startTime;
	}
	
	public String[] getReaderNames() {
    	return new String[]{"foo","bar"};
	}
    public String[] getECSpecNames(){
    	return new String[]{"tic","tac","toe"};
    }

	public void gc() {
		System.gc();
	}

	public long getEventCycles() {
		// TODO Auto-generated method stub
		return getEventCycles()/1000;
	}

	public long getReadCycles() {
		// TODO Auto-generated method stub
		return getEventCycles()/10;
	}
}
