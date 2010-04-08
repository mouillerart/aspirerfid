package org.ow2.aspirerfid.ide.bpwme.ecspec.model;

public class ExtraProperty {
	public String name;
	int type;
	public static final int LLRP_TYPE = 0;
	public static final int RP_TYPE = 1;
	public static final int HAL_TYPE = 2;
	private static final int TYPE_MIN = LLRP_TYPE;
	private static final int TYPE_MAX = HAL_TYPE;
	
	public ExtraProperty(String name, int type) {
		this.name = name;
		if((type < TYPE_MIN) | (type > TYPE_MAX)) {
			System.out.println("Type Def Out of Range");
			return;
		}
		this.type = type;
	}
	
	public String toString() {
		return name;
	}
	
}
