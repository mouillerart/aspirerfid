package com.odelia.osgi.dosgi;

import sample.Helloworld;


public class SimpleServiceImpl implements Helloworld {

	public String sayHello(String msg) {
		return "Hello " + msg;
	}

	public String saluer(String name) {
		return "Bonjour " + name;
	}
}