package org.ow2.aspirerfid.ide.bpwme.ecspec.model;


import java.util.Hashtable;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.model.IWorkbenchAdapter;
import org.eclipse.ui.views.properties.IPropertySource;
import org.ow2.aspirerfid.commons.ale.model.alelr.LRProperty;
import org.ow2.aspirerfid.commons.ale.model.alelr.LRSpec;
import org.ow2.aspirerfid.commons.ale.model.alelr.LRSpec.Properties;
import org.ow2.aspirerfid.commons.apdl.model.ApdlDataField;
import org.ow2.aspirerfid.ide.bpwme.utils.MainControl;

public class Spec implements IWorkbenchAdapter, IAdaptable{
	public enum Type{LLRP, RP, HAL}
	//public String name;
	//public String description;
	//public Hashtable<String, String> propertyTable;
	protected ApdlDataField adf;
	protected Type type;
//	public Spec() {
//		//this.name = "default spec";
//		//this.description = "no description";
//		
//		propertyTable = new Hashtable<String, String>();
//		//propertyTable.put("Name", name);
//		//propertyTable.put("Description", description);
//	}
	/*
	public Spec(String name) {
		//this.name = name;
		//this.description = "no description";
		propertyTable = new Hashtable<String, String>();
		propertyTable.put("Name", name);
		//propertyTable.put("Description", description);
	}*/
//	public Spec(Type type) {
//		this.type = type;
//	}
	
	public Spec(String name) {
		adf = new ApdlDataField();
		adf.setName(name);
		adf.setType("LRSpec");
		adf.setLRSpec(new LRSpec());
		adf.getLRSpec().setProperties(new Properties());
		//propertyTable = new Hashtable<String, String>();
	}
	
	public void setName(String name) {
		adf.setName(name);
		MainControl mc = MainControl.getMainControl();
		mc.lrsb.notifyListeners();
	}
	
	public String getName() {
		return adf.getName();
	}
	
	public void addProperty(LRProperty property) {
		adf.getLRSpec().getProperties().getProperty().add(property);
	}
	
	public void removeProperty(String name) {
		for(LRProperty lrp:adf.getLRSpec().getProperties().getProperty()) {
			if(lrp.getName().equals(name)) {
				adf.getLRSpec().getProperties().getProperty().remove(lrp);
			}
		}
	}
	
	public Spec getClone() {
		Spec newSpec = new Spec(adf.getName());
		for(LRProperty lrp:adf.getLRSpec().getProperties().getProperty()) {
			newSpec.adf.getLRSpec().getProperties().getProperty().add(lrp);
		}
		return newSpec;
	}
	
	protected LRProperty cloneLRProperty(LRProperty lrp) {
		LRProperty newLRP = new LRProperty();
		newLRP.setName(lrp.getName());
		newLRP.setValue(lrp.getValue());
		return newLRP;
	}
//	public static Spec createSpec(Type type) {
//		switch(type) {
//		case LLRP:
//			return new LLRPSpec();
//		case RP:
//			return new RPSpec();
//		case HAL:
//			return new HALSpec();
//		}
//		return null;
//	}
	
//	public static Spec createSpec(ApdlDataField adf) {
//		if(adf.getType().equals("LRSpec")) {
//			
//		}
//	}
	
//	public Spec(Spec item) {
//		this.propertyTable = (Hashtable<String, String>) item.propertyTable.clone();
//	}
	public String toString() {
		return null;
	}
	@Override
	public Object[] getChildren(Object o) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ImageDescriptor getImageDescriptor(Object object) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getLabel(Object o) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Object getParent(Object o) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public ApdlDataField getApdl() {
		return adf;
	}
	@Override
	public Object getAdapter(Class adapter) {
		// TODO Auto-generated method stub
        if (adapter == IWorkbenchAdapter.class)
            return this;
        if (adapter == IPropertySource.class)
            return new SpecProperties(this);
        return null;
	}
}
