/*
   Copyright 2005-2008, OW2 Aspire RFID project 
   
   This library is free software; you can redistribute it and/or modify it 
   under the terms of the GNU Lesser General Public License as published by 
   the Free Software Foundation (the "LGPL"); either version 2.1 of the 
   License, or (at your option) any later version. If you do not alter this 
   notice, a recipient may use your version of this file under either the 
   LGPL version 2.1, or (at his option) any later version.
   
   You should have received a copy of the GNU Lesser General Public License 
   along with this library; if not, write to the Free Software Foundation, 
   Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
   
   This software is distributed on an "AS IS" basis, WITHOUT WARRANTY OF ANY 
   KIND, either express or implied. See the GNU Lesser General Public 
   License for the specific language governing rights and limitations.

   Contact: OW2 Aspire RFID project <X AT Y DOT org> (with X=aspirerfid and Y=ow2)

   LGPL version 2.1 full text http://www.gnu.org/licenses/lgpl-2.1.txt    
*/
package org.ow2.aspirerfid.tool.pcscshell.bundle;

import java.io.File;
import java.io.PrintStream;
import java.net.URL;

import javax.smartcardio.CardException;

import org.apache.felix.shell.Command;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.ow2.aspirerfid.tool.pcscshell.impl.PCSCShell;

/**
 * This class provides a command shell for Felix
 * @author  Didier DONSEZ <X.Y@imag.fr> where X=Didier and Y=Donsez
 */
public class PCSCCommand implements BundleActivator, Command {

	private ServiceRegistration serviceRegistration;
	private PCSCShell pcscShell;
	/**
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		serviceRegistration=bundleContext.registerService(Command.class.getName(), this, null);
		pcscShell=new PCSCShell();
		// load ATRs ?
	}

	/**
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		pcscShell.close();
		pcscShell=null;
		serviceRegistration.unregister();
	}

	public void execute(String line, PrintStream out, PrintStream err) {
		try {
			int i=getName().length();
			while(line.charAt(i)!=' ') i++;
			pcscShell.execute(line.substring(i),out,err);
		} catch (CardException e) {
			e.printStackTrace();
		}		
	}

	public String getName() {
		return "pcsc";
	}

	public String getShortDescription() {
		return "simple utility to send/receive APDU with a smartcard connected to a PC/SC smartcard reader";
	}

	public String getUsage() {
		return null;
	}
}
