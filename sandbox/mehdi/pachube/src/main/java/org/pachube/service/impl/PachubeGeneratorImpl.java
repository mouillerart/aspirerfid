package org.pachube.service.impl;

import org.pachube.service.ApiVersion;
import org.pachube.service.GenericPachube;
import org.pachube.service.PachubeGenerator;

import Pachube.Pachube;
import Pachube2.Pachube2;

public class PachubeGeneratorImpl implements PachubeGenerator{
	
	public void start(){
		System.out.println(">>> Starting PACHUBE service... ");
	}
	
	public void stop(){
		System.out.println(">>> Stopping PACHUBE service... ");
	}
	
	public GenericPachube createPachube(String apiKey,ApiVersion version) {
		GenericPachube pachube = null;
		switch (version) {
		
			case V1:
					pachube = new GenericPachubeImpl(new Pachube(apiKey));
				break;
				
			case V2:
					pachube = new GenericPachubeImpl(new Pachube2(apiKey));
				break;
	
			default:
				break;
		}
		return pachube;
	}

}
