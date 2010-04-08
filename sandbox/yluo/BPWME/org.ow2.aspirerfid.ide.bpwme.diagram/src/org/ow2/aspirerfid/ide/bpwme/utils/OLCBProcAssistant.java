package org.ow2.aspirerfid.ide.bpwme.utils;

import org.ow2.aspirerfid.commons.apdl.model.CLCBProc;
import org.ow2.aspirerfid.commons.apdl.model.EBProc;
import org.ow2.aspirerfid.commons.apdl.model.OLCBProc;

public class OLCBProcAssistant {
	OLCBProc olcb;
	public OLCBProcAssistant(OLCBProc olcb) {
		this.olcb = olcb;
	}
	
	public CLCBProc getCLCB(String id) {
		for (CLCBProc clcb : olcb.getCLCBProc()) {
			if(clcb.getId().equals(id)) {
				return clcb;
			}
		}
		return null;
	}
	
	public EBProc getEBProc(CLCBProc clcb, String id) {
		for (EBProc ebp : clcb.getEBProc()) {
			if(ebp.getId().equals(id)) {
				return ebp;
			}
		}
		return null;
	}
}
