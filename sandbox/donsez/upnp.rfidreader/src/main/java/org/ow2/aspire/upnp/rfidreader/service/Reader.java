
/*
__BANNER__
*/
// this file was generated at 21-January-2011 12:56 AM by ${author}
package org.ow2.aspire.upnp.rfidreader.service;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.osgi.framework.*;
import org.osgi.service.upnp.*;
import org.apache.felix.upnp.devicegen.util.*;
import org.apache.felix.upnp.devicegen.holder.*;

import org.ow2.aspire.upnp.rfidreader.model.*;
import org.ow2.aspire.upnp.rfidreader.impl.*;

	

public class Reader extends AbstractUPnPService {
	
		
	private ReaderModel model;	
		
	/**
	 * constructor
	 */
	public Reader(
		UPnPDevice upnpDevice,
		String serviceId,
		String serviceType,
		String version,
		ReaderModel model
	){
		super(	
			upnpDevice,
			serviceId,
			serviceType,
			version
		);
		this.model=model;
	
		
	stateVariables=new HashMap();
	

	
	stateVariables.put(
		"Properties",
		new PropertiesStateVariable(this,model)
	);


	
	stateVariables.put(
		"Duration",
		new DurationStateVariable(this,model)
	);


	
	stateVariables.put(
		"ReportMembers",
		new ReportMembersStateVariable(this,model)
	);

	actions=new HashMap();
	


	actions.put(
		"SetDuration",
		new SetDurationAction(this,model)
	);



	actions.put(
		"GetDuration",
		new GetDurationAction(this,model)
	);



	actions.put(
		"GetProperties",
		new GetPropertiesAction(this,model)
	);
	
	}

	// UPnPStateVariable classes
	
	

// class PropertiesStateVariable
public class PropertiesStateVariable
  extends AbstractUPnPStateVariable
			
	{

	public final static String NAME="Properties";
	public final static String DATATYPE="string";
	
	// HERE specific state variable members
	private ReaderModel model;
	
	public PropertiesStateVariable(
		UPnPService upnpService,
		ReaderModel model
	){	
		super(
					upnpService,
					NAME,
					DATATYPE,
					null, // TODO defaultValue changed for 
					null, // TODO step changed for 
					null, // TODO step changed for 
					null, // TODO minimum changed for 
					null, // TODO maximum changed for 
					// sendEventsAttribute
	false,
					true  // TODO required changed for  OR 
		);
		
		this.model=model;

		
	}
	
	// TODO
	
	public void start(BundleContext bundleContext) throws Exception {
		// TODO
	}
	
	public void stop(BundleContext bundleContext) throws Exception {
		// TODO
	}
	
			
	/**
	 * invoked for the first notification just after a subscription
	 * @see it.cnr.isti.niche.osgi.upnp.util.UPnPStateVariableDescriptor#getValue()
	 */
	public Object getValue(){
	return null;
	}
};


// class DurationStateVariable
public class DurationStateVariable
  extends AbstractUPnPStateVariable
			
	  implements UPnPStateVariableDescriptor{

	public final static String NAME="Duration";
	public final static String DATATYPE="ui4";
	
	// HERE specific state variable members
	private ReaderModel model;
	
	public DurationStateVariable(
		UPnPService upnpService,
		ReaderModel model
	){	
		super(
					upnpService,
					NAME,
					DATATYPE,
					null, // TODO defaultValue changed for 1000
					null, // TODO step changed for 
					null, // TODO step changed for 
					null, // TODO minimum changed for 
					null, // TODO maximum changed for 
					// sendEventsAttribute
	true,
					true  // TODO required changed for  OR 
		);
		
		this.model=model;

		
	}
	
	// TODO
	
	public void start(BundleContext bundleContext) throws Exception {
		// TODO
	}
	
	public void stop(BundleContext bundleContext) throws Exception {
		// TODO
	}
	
			
	/**
	 * invoked for the first notification just after a subscription
	 * @see it.cnr.isti.niche.osgi.upnp.util.UPnPStateVariableDescriptor#getValue()
	 */
	public Object getValue(){
	
		return model.getDurationStateVariableValue();
		
	}
};


// class ReportMembersStateVariable
public class ReportMembersStateVariable
  extends AbstractUPnPStateVariable
			
	  implements UPnPStateVariableDescriptor{

	public final static String NAME="ReportMembers";
	public final static String DATATYPE="string";
	
	// HERE specific state variable members
	private ReaderModel model;
	
	public ReportMembersStateVariable(
		UPnPService upnpService,
		ReaderModel model
	){	
		super(
					upnpService,
					NAME,
					DATATYPE,
					null, // TODO defaultValue changed for 
					null, // TODO step changed for 
					null, // TODO step changed for 
					null, // TODO minimum changed for 
					null, // TODO maximum changed for 
					// sendEventsAttribute
	true,
					true  // TODO required changed for  OR 
		);
		
		this.model=model;

		
	}
	
	// TODO
	
	public void start(BundleContext bundleContext) throws Exception {
		// TODO
	}
	
	public void stop(BundleContext bundleContext) throws Exception {
		// TODO
	}
	
			
	/**
	 * invoked for the first notification just after a subscription
	 * @see it.cnr.isti.niche.osgi.upnp.util.UPnPStateVariableDescriptor#getValue()
	 */
	public Object getValue(){
	
		return model.getReportMembersStateVariableValue();
		
	}
};
	

	// UPnPAction classes
	
	

public class SetDurationAction extends AbstractUPnPAction {

	ReaderModel model;
	
	// HERE specific action members
	
	public SetDurationAction(
	    UPnPService upnpService,
		ReaderModel model
	){
		super(
			upnpService,
			"SetDuration",
			""
		);
		this.model=model;
		
			addInArg(
				"newDuration",
				upnpService.getStateVariable("Duration")
			);
		
	}

	/**
	 * @see org.osgi.service.upnp.UPnPAction#invoke(java.util.Dictionary)
	 */
	public Dictionary invoke(Dictionary _args) throws Exception {

	java.lang.Long newDuration = (java.lang.Long) _args.get("newDuration");
		

		// invoke model
		
		 model.setDuration(
			LongHolder.toValue(
			newDuration)
		
		 );
		
		// build returned Dictionary		
		Dictionary _result = new Hashtable();
		
		return _result.isEmpty()?null:_result;
	}


	public void start(BundleContext bundleContext) throws Exception {
		// TODO
	}
	
	public void stop(BundleContext bundleContext) throws Exception {
		// TODO
	}
};


public class GetDurationAction extends AbstractUPnPAction {

	ReaderModel model;
	
	// HERE specific action members
	
	public GetDurationAction(
	    UPnPService upnpService,
		ReaderModel model
	){
		super(
			upnpService,
			"GetDuration",
			""
		);
		this.model=model;
		
			addOutArg(
				"currentDuration",
				upnpService.getStateVariable("Duration")
			);
		
	}

	/**
	 * @see org.osgi.service.upnp.UPnPAction#invoke(java.util.Dictionary)
	 */
	public Dictionary invoke(Dictionary _args) throws Exception {

	LongHolder currentDuration = new LongHolder();
		

		// invoke model
		
		 model.getDuration(
			currentDuration
		 );
		
		// build returned Dictionary		
		Dictionary _result = new Hashtable();
		
			_result.put("currentDuration",currentDuration.getObject());
			// java.lang.Long
		return _result.isEmpty()?null:_result;
	}


	public void start(BundleContext bundleContext) throws Exception {
		// TODO
	}
	
	public void stop(BundleContext bundleContext) throws Exception {
		// TODO
	}
};


public class GetPropertiesAction extends AbstractUPnPAction {

	ReaderModel model;
	
	// HERE specific action members
	
	public GetPropertiesAction(
	    UPnPService upnpService,
		ReaderModel model
	){
		super(
			upnpService,
			"GetProperties",
			""
		);
		this.model=model;
		
			addOutArg(
				"currentProperties",
				upnpService.getStateVariable("Properties")
			);
		
	}

	/**
	 * @see org.osgi.service.upnp.UPnPAction#invoke(java.util.Dictionary)
	 */
	public Dictionary invoke(Dictionary _args) throws Exception {

	StringHolder currentProperties = new StringHolder();
		

		// invoke model
		
		 model.getProperties(
			currentProperties
		 );
		
		// build returned Dictionary		
		Dictionary _result = new Hashtable();
		
			_result.put("currentProperties",currentProperties.getObject());
			// java.lang.String
		return _result.isEmpty()?null:_result;
	}


	public void start(BundleContext bundleContext) throws Exception {
		// TODO
	}
	
	public void stop(BundleContext bundleContext) throws Exception {
		// TODO
	}
};
	
}
