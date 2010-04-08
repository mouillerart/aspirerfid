package org.ow2.aspirerfid.ide.bpwme.ecspec.utils;

import java.util.ArrayList;

import org.eclipse.jface.viewers.Viewer;
import org.ow2.aspirerfid.commons.ale.model.alelr.LRSpec;
import org.ow2.aspirerfid.commons.apdl.model.ApdlDataField;
import org.ow2.aspirerfid.commons.apdl.model.EBProc;
import org.ow2.aspirerfid.ide.bpwme.ecspec.model.*;

public class LRSpecBuilder {
	
	
	ArrayList<Viewer> listeners;
	ArrayList<Spec> lspecList;
	ArrayList<Spec> rspecList;
	EBProc ebp;
	
	/*for test use*/
	public LRSpecBuilder() {
		listeners = new ArrayList<Viewer>();
		lspecList = new ArrayList<Spec>();
		rspecList = new ArrayList<Spec>();
	}
	public void test() {
		lspecList.add(new LLRPSpec("123"));
		lspecList.add(new LLRPSpec("456"));
		lspecList.add(new LLRPSpec("789"));
	}
	
	
	public LRSpecBuilder(ApdlDataField adf) {
		//lrsbList = new ArrayList<ApdlDataField>();
		listeners = new ArrayList<Viewer>();
//		if((lrsb = adf.getLRSpec()) == null) {
//			
//		}		
	}
	
	public LRSpecBuilder(EBProc ebp) {
		this.ebp = ebp;
		listeners = new ArrayList<Viewer>();
		lspecList = new ArrayList<Spec>();
		rspecList = new ArrayList<Spec>();
	}
	
	
	public void addLRSpec(ApdlDataField adf) {
//		LRSpec lrsb;
//		if((lrsb = adf.getLRSpec()) != null) {
//			//lrsbList.add(adf);
//		} else {
//			System.err.println("This is not a LRSpec ApdlDataField" + adf);
//		}
		ebp.getApdlDataFields().getApdlDataField().add(adf);
	}
	
	public void removeLRSpec(ApdlDataField adf) {
		ebp.getApdlDataFields().getApdlDataField().remove(adf);
	}
	
	public void addListener(Viewer listener) {
		listeners.add(listener);
	}
	
	public void notifyListeners() {
		for(Viewer v : listeners) {
			//System.out.println(v);
			v.refresh();
		}
	}
	
	public void addProperty(){}
	public void changeProperty(){}
	public void removeProperty() {}
	public ArrayList<Spec> getLeftSpecList() {
		return lspecList;
	}
	public ArrayList<Spec> getRightSpecList() {
		return rspecList;
	}
	
//	public void setName(String value) {
//		adf.setName(value);		
//	}
//	
//	public String getName() {
//		return adf.getName();
//	}
	
}
