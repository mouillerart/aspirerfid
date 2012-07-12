package com.odelia.osgi.dosgi;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.ServiceRegistration;
import org.osgi.framework.BundleContext;
import java.util.Dictionary;
import java.util.Hashtable;

import sample.Helloworld;


public class SimpleActivator implements BundleActivator {
    final private static String PROPERTY_BASE_NAME = "org.apache.cxf.ws";

    private ServiceRegistration registration;

    public void start(BundleContext context) {
        Dictionary props = new Hashtable();

        props.put("service.exported.interfaces", "sample.Helloworld");
        props.put("service.exported.configs", PROPERTY_BASE_NAME);

        // If the property bellow is not used, the URL used for the web service should be: 
        // http://localhost:9000/sample/Helloworld
        props.put(PROPERTY_BASE_NAME + ".address", "http://localhost:9090/SimpleService");

        props.put(PROPERTY_BASE_NAME + ".frontend", "jaxws");
//        props.put(PROPERTY_BASE_NAME + ".databinding","jaxb");
        props.put(PROPERTY_BASE_NAME + ".service.ns", "http://sample/"); // service namespace 
        props.put(PROPERTY_BASE_NAME + ".service.name", "Helloworld"); // service name
        props.put(PROPERTY_BASE_NAME + ".port.name", "HelloworldSOAP11Port"); // port/endpoint name 
        props.put(PROPERTY_BASE_NAME + ".location", "wsdl/Helloworld.wsdl");

        registration = context.registerService(Helloworld.class.getName(), new SimpleServiceImpl(), props);
    }

    public void stop(BundleContext context) {
        registration.unregister();
    }
 
}