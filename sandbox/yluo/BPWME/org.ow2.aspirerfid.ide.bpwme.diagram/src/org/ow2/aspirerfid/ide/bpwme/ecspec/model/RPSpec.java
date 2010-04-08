package org.ow2.aspirerfid.ide.bpwme.ecspec.model;

import org.ow2.aspirerfid.commons.ale.model.alelr.LRProperty;

public class RPSpec extends Spec{
	
	public RPSpec(String name) {
		super(name);
		type = Type.RP;
		iniRP();
	}
	
//	public RPSpec() {
//		super();
//	}

	
	
	private void iniRP() {
		LRProperty lrp = new LRProperty();
		lrp.setName("Description");
		lrp.setValue("This Logical Reader consists of shelf 1,2,3,4 of the physical reader named AccadaRPSimulator");
		adf.getLRSpec().getProperties().getProperty().add(lrp);
		lrp = new LRProperty();
		lrp.setName("ReaderType");
		lrp.setValue("org.ow2.aspirerfid.ale.server.readers.rp.RPAdaptor");
		adf.getLRSpec().getProperties().getProperty().add(lrp);
	}

	public RPSpec getClone() {
		RPSpec newSpec = new RPSpec(this.getName());
		newSpec.adf.getLRSpec().getProperties().getProperty().clear();
		for(LRProperty lrp:adf.getLRSpec().getProperties().getProperty()) {
			newSpec.adf.getLRSpec().getProperties().getProperty().add(cloneLRProperty(lrp));
		}
		return newSpec;
	}

}
