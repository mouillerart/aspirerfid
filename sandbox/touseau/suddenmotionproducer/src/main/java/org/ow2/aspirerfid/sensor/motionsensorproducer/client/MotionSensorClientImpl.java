/*
 * Copyright 2005-2008, Aspire
 * 
 * This library is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by 
 * the Free Software Foundation (the "LGPL"); either version 2.1 of the 
 * License, or (at your option) any later version. If you do not alter this 
 * notice, a recipient may use your version of this file under either the 
 * LGPL version 2.1, or (at his option) any later version.
 * 
 * You should have received a copy of the GNU Lesser General Public License 
 * along with this library; if not, write to the Free Software Foundation, 
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * 
 * This software is distributed on an "AS IS" basis, WITHOUT WARRANTY OF ANY 
 * KIND, either express or implied. See the GNU Lesser General Public 
 * License for the specific language governing rights and limitations.
 */
package org.ow2.aspirerfid.sensor.motionsensorproducer.client;

import org.osgi.service.component.ComponentContext;
import org.ow2.aspirerfid.sensor.motionsensorproducer.service.MotionSensorService;

/**
 * 
 * @author Arnaud Constancin
 */
public class MotionSensorClientImpl {

	MotionSensorService motionSensorService = null;

	public void activate(ComponentContext componentContext) {
		System.out.println("MotionSensorClient activated");

		test();
	}

	public void deactivate(ComponentContext componentContext) {
		System.out.println("MotionSensorClient deactivated");
	}

	public void setMotionSensorService(MotionSensorService motionSensorService) {
		System.out.println("MotionSensorClient setMotionSensorService");

		this.motionSensorService = motionSensorService;
	}

	public void unsetMotionSensorService(MotionSensorService motionSensorService) {
		System.out.println("MotionSensorClient unsetMotionSensorService");

		this.motionSensorService = null;
	}

	public void test() {
		try {

			for (int n = 0; n < 10; ++n) {
				// System.out.println(thisMotion.getOrientation());

				double x = motionSensorService.getSMSX();
				double y = motionSensorService.getSMSY();
				double z = motionSensorService.getSMSZ();

				System.out.println("X = " + x + " == " + (double) ((int) (100 * x) / 255) / 100 + "g    (lateral)");
				System.out.println("Y = " + y + " == " + (double) ((int) (100 * y) / 255) / 100 + "g    (profondeur)");
				System.out.println("Z = " + z + " == " + (double) ((int) (100 * z) / 255) / 100 + "g    (vertical)");

				double a = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
				System.out.println("Axyz = " + a + " == " + (double) ((int) (1000 * z) / 255) / 1000 + "g    (3d)");

				System.out.println();
				sleep(1000);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * @param millis
	 */
	private static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
