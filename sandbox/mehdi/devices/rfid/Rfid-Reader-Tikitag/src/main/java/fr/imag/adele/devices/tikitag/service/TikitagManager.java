package fr.imag.adele.devices.tikitag.service;

import java.util.Map;


/**
 * TikitagManager
 * @author Daniel Lovera and Cl�ment Deschamps and Mehdi Damou
 **/

public interface TikitagManager {

	public Map<String, TikitagDescriptor> getListTikitag();
	
	public TikitagDescriptor getTikitagbyID(String i);

}