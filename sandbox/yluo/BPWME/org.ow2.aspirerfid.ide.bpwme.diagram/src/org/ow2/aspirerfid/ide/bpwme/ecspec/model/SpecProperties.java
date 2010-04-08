package org.ow2.aspirerfid.ide.bpwme.ecspec.model;


import java.util.Hashtable;
import java.util.Iterator;

import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.ow2.aspirerfid.commons.ale.model.alelr.LRProperty;

public class SpecProperties implements IPropertySource {
	
	private Spec spec;
	private Hashtable<String, LRProperty> pTable;
	
	public SpecProperties(Spec spec) {
		// TODO Auto-generated constructor stub
		this.spec = spec;
		pTable = new Hashtable();
	}

	@Override
	public Object getEditableValue() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
        // Create the property vector.
        IPropertyDescriptor[] propertyDescriptors = new IPropertyDescriptor[spec.adf.getLRSpec().getProperties().getProperty().size()];
        Iterator<LRProperty> iter = spec.adf.getLRSpec().getProperties().getProperty().iterator();
        int i = 0;
        while(iter.hasNext()) {
        	LRProperty lrp = iter.next();
        	String name = lrp.getName();
        	pTable.put(name, lrp);
        	PropertyDescriptor descriptor = new TextPropertyDescriptor(name, name);
        	//descriptor.setCategory("Basic");
        	propertyDescriptors[i] = descriptor;
        	i ++;
        }
        //propertyDescriptors[i] = new ComboBoxPropertyDescriptor("foo", "foo", null);
        return propertyDescriptors;
	}

	@Override
	public Object getPropertyValue(Object id) {		
		return pTable.get(id).getValue();
	}

	@Override
	public boolean isPropertySet(Object id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void resetPropertyValue(Object id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		// TODO Auto-generated method stub
        //if(id.equals(obj))
		//if(id.equals("Name")) {
		//	spec.name = (String)value;
		//}	
		LRProperty lrp = pTable.get(id);
		lrp.setValue((String) value);
	}
}
