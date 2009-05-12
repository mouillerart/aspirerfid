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
package org.ow2.aspirerfid.ibuddy.example;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.ow2.aspirerfid.ibuddy.service.IBuddyDescriptor;
import org.ow2.aspirerfid.ibuddy.service.IIBuddy;

/**
 * Ibuddy
 * @author Daniel Lovera and Clément Deschamps and Mehdi Damou
 **/

public class IBuddyHandler {

	private IIBuddy m_bud;
	public static final short IBUDDY_VENDOR_ID = 0x1130;
	public static final short IBUDDY_PRODUCT_ID = 0x0004;



	private List<MyBuddy> ibuddys;

	public void start() {
//		try {
			System.out.println("Starting IbuddyHandler...");
			ibuddys = new ArrayList<MyBuddy>();
			if (m_bud != null) {
				Map<Long, IBuddyDescriptor> listBuddy = m_bud.getListIbuddy();
				for (Iterator<Long> iterator = listBuddy.keySet().iterator(); iterator
						.hasNext();) {
					Long ll = (Long) iterator.next();
					System.out.println(">>> " + ll + " -- "
							+ listBuddy.get(ll).isOpened());
					
					MyBuddy bud = new MyBuddy(listBuddy.get(ll));
					
					ibuddys.add(bud);

				}

			} else {
				System.out.println("IBuddy not found");
			}

	}

	public void stop() {		
		System.out.println("bye bye");
		System.out.println("Stopped IBuddyHandler.");
	}

}
