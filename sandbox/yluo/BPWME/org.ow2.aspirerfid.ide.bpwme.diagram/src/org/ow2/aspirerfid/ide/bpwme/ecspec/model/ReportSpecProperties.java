package org.ow2.aspirerfid.ide.bpwme.ecspec.model;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

public class ReportSpecProperties implements IPropertySource{

	ReportSpec reportSpec;
	public ReportSpecProperties(ReportSpec reportSpec) {
		this.reportSpec = reportSpec;
	}

	@Override
	public Object getEditableValue() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		IPropertyDescriptor[] propertyDescriptors = new IPropertyDescriptor[1];
		propertyDescriptors[0] = new TextPropertyDescriptor("Name", "Name");
		return propertyDescriptors;
	}

	@Override
	public Object getPropertyValue(Object id) {
		// TODO Auto-generated method stub
		return reportSpec.getName();
	}

	@Override
	public boolean isPropertySet(Object id) {
		
		return false;
	}

	@Override
	public void resetPropertyValue(Object id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		// TODO Auto-generated method stub
		
	}

}
