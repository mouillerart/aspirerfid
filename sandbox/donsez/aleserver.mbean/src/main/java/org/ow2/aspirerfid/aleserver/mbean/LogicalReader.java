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
 * provides the MBean implementation for managing logical readers in the ALEServer
 * @author Didier Donsez
 */
public class LogicalReader extends AbstractWebappMBean implements LogicalReaderMBean {
	private ObjectName name = null;

	public LogicalReader() {
		try {
			name = new ObjectName(OBJECTNAME);
		} catch (Exception e) {
			getServletContext().log(e.getMessage()+" "+OBJECTNAME,e);
		}
	}
	
	protected void start() {
	}

	protected void stop() {
	}
	
	protected ObjectName getObjectName() {
		return name;
	}

	// interface method implementations
	
	public String[] getLogicalReaderNames() {
    	return new String[]{"dockdoor1","dockdoor2","walkinrefrigerator","store"};
	}
	
	public void addLogicalReader(String name, String lrSpecDescription) {
		// TODO parse the lrSpecDescription
		// TODO add org.ow2.aspirerfid.ale.server.readers.LogicalReaderManager.addReaders(String name, java.util.List<String> readers)
	}

	public void removeLogicalReader(String name) {
		// TODO Auto-generated method stub
		
	}



}
