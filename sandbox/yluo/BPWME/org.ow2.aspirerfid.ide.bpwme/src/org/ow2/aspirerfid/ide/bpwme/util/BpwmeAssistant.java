package org.ow2.aspirerfid.ide.bpwme.util;

public class BpwmeAssistant {
	private static int OLCBCounter = 0;
	private static int CLCBCounter = 0;
	private static int EBCounter = 0;
	
	public static String getUniqueOLCBID() {
		return "olcbproc:" + (OLCBCounter ++);
	}
	
	public static String getUniqueCLCBID() {
		return "clcbproc:" + (CLCBCounter ++);
	}
	
	public static String getUniqueEBID() {
		return "ebproc:" + (EBCounter ++);
	}
}
