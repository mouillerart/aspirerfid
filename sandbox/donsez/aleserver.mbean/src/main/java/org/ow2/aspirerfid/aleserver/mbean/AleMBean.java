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

/**
 * provides the MBean interface to expose ALE operations of the ALEServer
 * @author Didier Donsez
 */
public interface AleMBean {

	public static final String OBJECTNAME = Common.ALESERVER_OBJECTNAME_PREFIX+",name=ale";	

    /**
     * get the ECSpec names
     */
     public String[] getECSpecNames();
    
    /**
     * create (ie define) a new ECSpec
     * @param name the ECSpec name
     * @param ecSpecDescription the XML document of the ECSpec
     */
    public void createECSpec(String name, String ecSpecDescription);
    /**
     * remove ECSpec
     * @param name the ECSpec name
     */
    public void removeECSpec(String name);
    
    
	public long getEventCycles();
	
	public long getReadCycles();
    
}
