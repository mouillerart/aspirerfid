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

import org.ow2.aspirerfid.ibuddy.service.IBuddyDescriptor;
import org.ow2.aspirerfid.ibuddy.service.IIBuddy;

/**
 * TODO
 * @author Daniel Lovera and Clément Deschamps and Mehdi Damou
 **/

public class MyBuddy implements Runnable {

	private static int counter=0;
	
	private IBuddyDescriptor buddy;

	public MyBuddy(IBuddyDescriptor buddyDescriptor) {
		this.buddy = buddyDescriptor;
		Thread t = new Thread(this);
		t.setName(MyBuddy.class.getName()+"#"+(++counter));
		t.start();
	}

	public void sendAccessGranted() throws InterruptedException {

		buddy.sendHeadColor(IIBuddy.HeadColor.GREEN);
		buddy.sendHeartState(IIBuddy.HeartState.ON);
		for (int i = 0; i < 10; i++) {
			buddy.sendWingsState(IIBuddy.WingsState.UP);
			Thread.sleep(75);
			buddy.sendWingsState(IIBuddy.WingsState.DOWN);
			Thread.sleep(75);
		}

		Thread.sleep(500);

		buddy.sendReset();
	}

	public void sendAccessDenied() throws InterruptedException {

		buddy.sendHeadColor(IIBuddy.HeadColor.RED);
		buddy.sendHeartState(IIBuddy.HeartState.OFF);
		for (int i = 0; i < 5; i++) {
			buddy.sendOrientation(IIBuddy.Orientation.LEFT);
			Thread.sleep(225);
			buddy.sendOrientation(IIBuddy.Orientation.RIGHT);
			Thread.sleep(225);
		}

		Thread.sleep(500);

		buddy.sendReset();
	}

	public void sendConnectFailed() throws InterruptedException {

		buddy.sendHeadColor(IIBuddy.HeadColor.YELLOW);
		buddy.sendHeartState(IIBuddy.HeartState.OFF);
		for (int i = 0; i < 5; i++) {
			buddy.sendOrientation(IIBuddy.Orientation.LEFT);
			Thread.sleep(225);
			buddy.sendOrientation(IIBuddy.Orientation.RIGHT);
			Thread.sleep(225);
		}

		Thread.sleep(500);

		buddy.sendReset();

	}

	public void run() {
		try {
			buddy.open();
			buddy.sendReset();
			this.sendAccessDenied();
			this.sendAccessGranted();
			buddy.close();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
