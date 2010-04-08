package org.ow2.aspirerfid.ide.bpwme.ecspec.model;

import java.util.Hashtable;

import org.ow2.aspirerfid.commons.ale.model.alelr.LRProperty;


public class LLRPSpec extends Spec{
	//initiate several default properties LLRP Spec has
	public LLRPSpec(String name) {
		super(name);
		type = Type.LLRP;
		iniLLRP();
	}
	
	protected void iniLLRP() {
		LRProperty lrp = new LRProperty();
		lrp.setName("Description");
		lrp.setValue("This Logical Reader consists of read point 1,2,3");
		adf.getLRSpec().getProperties().getProperty().add(lrp);
		lrp = new LRProperty();
		lrp.setName("ReaderType");
		lrp.setValue("org.ow2.aspirerfid.ale.server.readers.llrp.LLRPAdaptor");
		adf.getLRSpec().getProperties().getProperty().add(lrp);
	}
	
	public LLRPSpec getClone() {
		LLRPSpec newSpec = new LLRPSpec(this.getName());
		newSpec.adf.getLRSpec().getProperties().getProperty().clear();
		for(LRProperty lrp:adf.getLRSpec().getProperties().getProperty()) {
			newSpec.adf.getLRSpec().getProperties().getProperty().add(cloneLRProperty(lrp));
		}
		return newSpec;
	}
}
