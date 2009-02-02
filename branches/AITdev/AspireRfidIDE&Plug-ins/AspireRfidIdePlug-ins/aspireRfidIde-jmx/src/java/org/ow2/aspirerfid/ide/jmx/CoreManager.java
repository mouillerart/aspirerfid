/**
 * Copyright © 2008-2010, Aspire 
 * 
 * Aspire is free software; you can redistribute it and/or 
 * modify it under  the terms of the GNU Lesser General Public 
 * License version 2.1 as published by the Free Software Foundation (the 
 * "LGPL"). 
 * 
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this library in the file COPYING-LGPL-2.1; if not, write to the Free Software 
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301 USA. 
 * 
 * This software is distributed on an "AS IS" basis, WITHOUT WARRANTY 
 * OF ANY KIND, either express or implied. See the GNU Lesser General Public 
 * License for the specific language governing rights and limitations. 
 * 
 */

package org.ow2.aspirerfid.ide.jmx;

import javax.management.MBeanOperationInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.ow2.aspirerfid.ide.jmx.preferences.PreferenceConstants;

/**
 * @author Nektarios Leontiadis (nele@ait.edu.gr)
 *
 */
public class CoreManager {

    private static MBeanServerConnection connection;
    private static String mBeanName = "rfid:type=service,SymbolicName=BundleManager";


    public static void restartCore() throws Exception
    {
	ObjectName bean = new ObjectName(mBeanName);
	String url = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.JMX_CONNECTION_URL);
	JMXServiceURL u = new JMXServiceURL(url);
	JMXConnector c = JMXConnectorFactory.connect(u);
	Object [] params = {"org.ow2.aspirerfid.ReaderProtocolImpl"};
	String [] sig = {"java.lang.String"};
	connection = c.getMBeanServerConnection();
	MBeanOperationInfo []info = connection.getMBeanInfo(bean).getOperations();
	for(int i=0; i<info.length;i++)
	    System.out.println(info[i]);
	
	connection.invoke(bean, "restartBundle",params,sig);
	c.close();
    }

}
