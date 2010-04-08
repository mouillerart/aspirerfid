package org.ow2.aspirerfid.ide.bpwme.ecspec.model;

import org.ow2.aspirerfid.ide.bpwme.ecspec.utils.ECSpecBuilder;

public class BoundaryContent{
	String name;
	long value;
	ECSpecBuilder ecsb;
	public BoundaryContent(ECSpecBuilder ecsb, String name, long value) {
		this.name = name;
		this.value = value;
		this.ecsb = ecsb;
	}
	public String getName() {
		return name;
	}
	
	public String getValue() {
		return Long.toString(value);
	}
	public void setValue(String value) {
		if(name.equals("Repeat Period")) {
			//System.out.println("set repeat");
			ecsb.setRepeatPeriod(Long.parseLong(value));
		} else if(name.equals("Duration")) {
			ecsb.setDuration(Long.parseLong(value));
		} else if(name.equals("Stable Set Interval")) {
			ecsb.setStableSetInterval(Long.parseLong(value));
		}
	}
}
