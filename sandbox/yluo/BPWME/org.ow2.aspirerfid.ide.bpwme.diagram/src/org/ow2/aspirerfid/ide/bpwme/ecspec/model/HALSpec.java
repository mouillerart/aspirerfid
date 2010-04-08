package org.ow2.aspirerfid.ide.bpwme.ecspec.model;

import java.util.Hashtable;

import org.ow2.aspirerfid.commons.ale.model.alelr.LRProperty;

public class HALSpec extends Spec{
	public HALSpec(String name) {
		super(name);
		type = Type.HAL;
		iniHAL();
	}
	
	private void iniHAL() {
		LRProperty lrp = new LRProperty();
		lrp.setName("Description");
		lrp.setValue("A HAL Description");
		adf.getLRSpec().getProperties().getProperty().add(lrp);
	}
	
	public HALSpec getClone() {
		HALSpec newSpec = new HALSpec(this.getName());
		newSpec.adf.getLRSpec().getProperties().getProperty().clear();
		for(LRProperty lrp:adf.getLRSpec().getProperties().getProperty()) {
			newSpec.adf.getLRSpec().getProperties().getProperty().add(cloneLRProperty(lrp));
		}
		return newSpec;
	}

}
