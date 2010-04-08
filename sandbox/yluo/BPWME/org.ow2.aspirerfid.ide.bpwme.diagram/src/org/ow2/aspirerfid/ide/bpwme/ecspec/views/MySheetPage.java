package org.ow2.aspirerfid.ide.bpwme.ecspec.views;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.views.properties.PropertySheetEntry;
import org.eclipse.ui.views.properties.PropertySheetPage;

public class MySheetPage extends PropertySheetPage{
	
	private PropertySheetEntry selection = null;
	
	public String getSelectProperty() {
		if(selection != null){
			return selection.getDisplayName();
		}
		return null;
	}
	
	public void handleEntrySelection(ISelection selection) {
		// TODO Auto-generated method stub
		if(selection != null) {
			//System.out.println(selection);
			StructuredSelection structuredSelection = (StructuredSelection)selection;
			if(!structuredSelection.isEmpty()) {
				this.selection = (PropertySheetEntry)structuredSelection.getFirstElement();
				//System.out.println(((PropertySheetEntry)sel.getFirstElement()).getDisplayName());
			}
			//System.out.println((StructuredSelection)selection);
			//System.out.println(((PropertySheetEntry)((StructuredSelection)selection).getFirstElement()).getDisplayName());
		}
		super.handleEntrySelection(selection);
	}
	
	
}
