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

package org.ow2.aspirerfid.ide.lrspec.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import org.ow2.aspirerfid.ide.lrspec.preferences.PreferenceConstants;
import org.ow2.aspirerfid.ide.lrspec.Activator;

/**
 * Class used to initialize default preference values.
 */
/**
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
		store.setDefault(PreferenceConstants.P_HAL_LRSpecsPATH, System.getProperty("user.home") + "\\AspireRFID\\IDE\\LRSpecs\\HAL\\");
		store.setDefault(PreferenceConstants.P_RP_LRSpecsPATH, System.getProperty("user.home") + "\\AspireRFID\\IDE\\LRSpecs\\RP\\");
		store.setDefault(PreferenceConstants.P_LLRP_LRSpecsPATH, System.getProperty("user.home") + "\\AspireRFID\\IDE\\LRSpecs\\LLRP\\");
		store.setDefault(PreferenceConstants.P_Composite_LRSpecsPATH, System.getProperty("user.home") + "\\AspireRFID\\IDE\\LRSpecs\\Composite\\");
		store.setDefault(PreferenceConstants.P_ConnectionPoints, "http://localhost:8080,http://localhost:7070,http://localhost:6060");
		store.setDefault(PreferenceConstants.P_NotificationPoints, "http://localhost:8080,http://localhost:7070,http://localhost:6060");
		store.setDefault(PreferenceConstants.P_DynamicReaders, "DynamicImpinjLlrpReader,DynamicAccadaSimulatorRpReader,DynamicIntermecRpReader");
	}

}
