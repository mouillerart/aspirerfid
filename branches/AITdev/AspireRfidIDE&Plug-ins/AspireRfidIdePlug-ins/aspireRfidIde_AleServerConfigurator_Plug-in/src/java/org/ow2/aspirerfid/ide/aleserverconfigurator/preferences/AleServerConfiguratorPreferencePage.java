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

package org.ow2.aspirerfid.ide.aleserverconfigurator.preferences;

import org.eclipse.jface.preference.*;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.ow2.aspirerfid.ide.aleserverconfigurator.Activator;
import org.ow2.aspirerfid.ide.aleserverconfigurator.preferences.PreferenceConstants;

/**
 * This class represents a preference page that is contributed to the
 * Preferences dialog. By subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows us to create a page
 * that is small and knows how to save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They are stored in the
 * preference store that belongs to the main plug-in class. That way,
 * preferences can be accessed directly via the preference store.
 * 
 * @author nkef
 * 
 */

public class AleServerConfiguratorPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public AleServerConfiguratorPreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("ALE Server Configurator Preferences");
	}

	/**
	 * Creates the field editors. Field editors are abstractions of the common
	 * GUI blocks needed to manipulate various types of preferences. Each field
	 * editor knows how to save and restore itself.
	 */
	public void createFieldEditors() {

		// addField(new DirectoryFieldEditor(PreferenceConstants.P_PATH,
		// "&Directory preference:", getFieldEditorParent()));
		//		
		// addField(
		// new BooleanFieldEditor(
		// PreferenceConstants.P_BOOLEAN,
		// "&An example of a boolean preference",
		// getFieldEditorParent()));
		//
		// addField(new RadioGroupFieldEditor(
		// PreferenceConstants.P_CHOICE,
		// "An example of a multiple-choice preference",
		// 1,
		// new String[][] { { "&Choice 1", "choice1" }, {
		// "C&hoice 2", "choice2" }
		// }, getFieldEditorParent()));

		{
			addField(new DirectoryFieldEditor(PreferenceConstants.P_ECSpecsPATH, "EC Specs Directory:", getFieldEditorParent()));
		}

		{
			addField(new StringFieldEditor(PreferenceConstants.P_ALEClientEndPointSTRING, "ALE Client End Point:", getFieldEditorParent()));
		}

		{
			addField(new DirectoryFieldEditor(PreferenceConstants.P_LRSpecsPATH, "LR Specs Directory:", getFieldEditorParent()));
		}

		{
			addField(new StringFieldEditor(PreferenceConstants.P_ALELRClientEndPointSTRING, "ALE LR Client End Point", getFieldEditorParent()));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}

}