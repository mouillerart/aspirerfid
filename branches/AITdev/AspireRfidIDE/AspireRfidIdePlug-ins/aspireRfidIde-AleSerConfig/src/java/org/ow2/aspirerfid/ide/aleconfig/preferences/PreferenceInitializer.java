/*
 * Copyright © 2008-2010, Aspire
 * 
 * Aspire is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License version 2.1 as published by
 * the Free Software Foundation (the "LGPL").
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library in the file COPYING-LGPL-2.1; if not, write to the
 * Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301 USA.
 * 
 * This software is distributed on an "AS IS" basis, WITHOUT WARRANTY OF ANY
 * KIND, either express or implied. See the GNU Lesser General Public License
 * for the specific language governing rights and limitations.
 */

package org.ow2.aspirerfid.ide.aleconfig.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import org.ow2.aspirerfid.ide.aleconfig.Activator;
import org.ow2.aspirerfid.ide.aleconfig.preferences.PreferenceConstants;

/**
 * Class used to initialize default preference values.

 * @author Vasso Koletti e-mail: vkol@ait.edu.gr
 * @author Nikos Kefalakis (nkef) e-mail: nkef@ait.edu.gr
 *
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#
	 * initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		
		store.setDefault(PreferenceConstants.P_ECSpecsPATH,
				"C:\\ASPIRE_APPLICATION_FILES\\ECSpecs\\");
		store.setDefault(PreferenceConstants.P_HAL_LRSpecsPATH,
				"C:\\ASPIRE_APPLICATION_FILES\\LRSpecs\\HAL\\");
		store.setDefault(PreferenceConstants.P_RP_LRSpecsPATH,
				"C:\\ASPIRE_APPLICATION_FILES\\LRSpecs\\RP\\");
		store.setDefault(PreferenceConstants.P_LLRP_LRSpecsPATH,
				"C:\\ASPIRE_APPLICATION_FILES\\LRSpecs\\LLRP\\");
		store.setDefault(PreferenceConstants.P_Composite_LRSpecsPATH,
				"C:\\ASPIRE_APPLICATION_FILES\\LRSpecs\\Composite\\");
		store.setDefault(PreferenceConstants.P_ALEClientEndPointSTRING,
				"http://localhost:8080/aspireALE0.3.1m/services/ALEService");
		store.setDefault(PreferenceConstants.P_ALELRClientEndPointSTRING,
				"http://localhost:8080/aspireALE0.3.1m/services/ALELRService");
		store
				.setDefault(PreferenceConstants.P_NotificationURI,
						"http://localhost:8080,http://localhost:7070,http://localhost:6060");

		store
				.setDefault(
						PreferenceConstants.P_ReaderNames,
						"AccadaSimulatorWithRPProxy,SmartLabIntermecLogicalReader");

		store
				.setDefault(PreferenceConstants.P_ConnectionPoints,
						"http://localhost:8080,http://localhost:7070,http://localhost:6060");
		store
				.setDefault(PreferenceConstants.P_NotificationPoints,
						"http://localhost:8080,http://localhost:7070,http://localhost:6060");
		
		store.setDefault(PreferenceConstants.P_ECSpecNames,
				"ECSpec_additions,ECSpec_current,ECSpec_deletions");
	}

}
