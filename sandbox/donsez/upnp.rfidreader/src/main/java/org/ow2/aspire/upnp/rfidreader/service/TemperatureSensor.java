
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

	

public class TemperatureSensor extends AbstractUPnPService {
	
		
	private TemperatureSensorModel model;	
		
	/**
	 * constructor
	 */
	public TemperatureSensor(
		UPnPDevice upnpDevice,
		String serviceId,
		String serviceType,
		String version,
		TemperatureSensorModel model
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
		"CurrentTemperature",
		new CurrentTemperatureStateVariable(this,model)
	);


	
	stateVariables.put(
		"Application",
		new ApplicationStateVariable(this,model)
	);


	
	stateVariables.put(
		"Name",
		new NameStateVariable(this,model)
	);

	actions=new HashMap();
	


	actions.put(
		"GetApplication",
		new GetApplicationAction(this,model)
	);



	actions.put(
		"SetApplication",
		new SetApplicationAction(this,model)
	);



	actions.put(
		"GetCurrentTemperature",
		new GetCurrentTemperatureAction(this,model)
	);



	actions.put(
		"GetName",
		new GetNameAction(this,model)
	);



	actions.put(
		"SetName",
		new SetNameAction(this,model)
	);
	
	}

	// UPnPStateVariable classes
	
	

// class CurrentTemperatureStateVariable
public class CurrentTemperatureStateVariable
  extends AbstractUPnPStateVariable
			
	  implements UPnPStateVariableDescriptor{

	public final static String NAME="CurrentTemperature";
	public final static String DATATYPE="i4";
	
	// HERE specific state variable members
	private TemperatureSensorModel model;
	
	public CurrentTemperatureStateVariable(
		UPnPService upnpService,
		TemperatureSensorModel model
	){	
		super(
					upnpService,
					NAME,
					DATATYPE,
					null, // TODO defaultValue changed for 2000
					null, // TODO step changed for 
					null, // TODO step changed for 
					null, // TODO minimum changed for 0
					null, // TODO maximum changed for 4000
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
	
		return model.getCurrentTemperatureStateVariableValue();
		
	}
};


// class ApplicationStateVariable
public class ApplicationStateVariable
  extends AbstractUPnPStateVariable
			
	  implements UPnPStateVariableDescriptor{

	public final static String NAME="Application";
	public final static String DATATYPE="string";
	
	// HERE specific state variable members
	private TemperatureSensorModel model;
	
	public ApplicationStateVariable(
		UPnPService upnpService,
		TemperatureSensorModel model
	){	
		super(
					upnpService,
					NAME,
					DATATYPE,
					null, // TODO defaultValue changed for Room
					null, // TODO step changed for 
					null, // TODO step changed for 
					null, // TODO minimum changed for 
					null, // TODO maximum changed for 
					// sendEventsAttribute
	true,
					true  // TODO required changed for  OR 
		);
		
		this.model=model;

		
	/*
		List allowedValueList=new LinkedList();
		
		allowedValueList.add(UPnPDataTypeUtil.instanciateObject("string","Room"));

		allowedValueList.add(UPnPDataTypeUtil.instanciateObject("string","Outdoor"));

		allowedValueList.add(UPnPDataTypeUtil.instanciateObject("string","Pipe"));

		allowedValueList.add(UPnPDataTypeUtil.instanciateObject("string","AirDuct"));

		this.setallowedValueList(allowedValueList);
	*/

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
	
		return model.getApplicationStateVariableValue();
		
	}
};


// class NameStateVariable
public class NameStateVariable
  extends AbstractUPnPStateVariable
			
	  implements UPnPStateVariableDescriptor{

	public final static String NAME="Name";
	public final static String DATATYPE="string";
	
	// HERE specific state variable members
	private TemperatureSensorModel model;
	
	public NameStateVariable(
		UPnPService upnpService,
		TemperatureSensorModel model
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
	
		return model.getNameStateVariableValue();
		
	}
};
	

	// UPnPAction classes
	
	

public class GetApplicationAction extends AbstractUPnPAction {

	TemperatureSensorModel model;
	
	// HERE specific action members
	
	public GetApplicationAction(
	    UPnPService upnpService,
		TemperatureSensorModel model
	){
		super(
			upnpService,
			"GetApplication",
			""
		);
		this.model=model;
		
			addOutArg(
				"CurrentApplication",
				upnpService.getStateVariable("Application")
			);
		
	}

	/**
	 * @see org.osgi.service.upnp.UPnPAction#invoke(java.util.Dictionary)
	 */
	public Dictionary invoke(Dictionary _args) throws Exception {

	StringHolder currentApplication = new StringHolder();
		

		// invoke model
		
		 model.getApplication(
			currentApplication
		 );
		
		// build returned Dictionary		
		Dictionary _result = new Hashtable();
		
			_result.put("CurrentApplication",currentApplication.getObject());
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


public class SetApplicationAction extends AbstractUPnPAction {

	TemperatureSensorModel model;
	
	// HERE specific action members
	
	public SetApplicationAction(
	    UPnPService upnpService,
		TemperatureSensorModel model
	){
		super(
			upnpService,
			"SetApplication",
			""
		);
		this.model=model;
		
			addInArg(
				"NewApplication",
				upnpService.getStateVariable("Application")
			);
		
	}

	/**
	 * @see org.osgi.service.upnp.UPnPAction#invoke(java.util.Dictionary)
	 */
	public Dictionary invoke(Dictionary _args) throws Exception {

	java.lang.String newApplication = (java.lang.String) _args.get("NewApplication");
		

		// invoke model
		
		 model.setApplication(
			StringHolder.toValue(
			newApplication)
		
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


public class GetCurrentTemperatureAction extends AbstractUPnPAction {

	TemperatureSensorModel model;
	
	// HERE specific action members
	
	public GetCurrentTemperatureAction(
	    UPnPService upnpService,
		TemperatureSensorModel model
	){
		super(
			upnpService,
			"GetCurrentTemperature",
			""
		);
		this.model=model;
		
			addOutArg(
				"CurrentTemp",
				upnpService.getStateVariable("CurrentTemperature")
			);
		
	}

	/**
	 * @see org.osgi.service.upnp.UPnPAction#invoke(java.util.Dictionary)
	 */
	public Dictionary invoke(Dictionary _args) throws Exception {

	IntegerHolder currentTemp = new IntegerHolder();
		

		// invoke model
		
		 model.getCurrentTemperature(
			currentTemp
		 );
		
		// build returned Dictionary		
		Dictionary _result = new Hashtable();
		
			_result.put("CurrentTemp",currentTemp.getObject());
			// java.lang.Integer
		return _result.isEmpty()?null:_result;
	}


	public void start(BundleContext bundleContext) throws Exception {
		// TODO
	}
	
	public void stop(BundleContext bundleContext) throws Exception {
		// TODO
	}
};


public class GetNameAction extends AbstractUPnPAction {

	TemperatureSensorModel model;
	
	// HERE specific action members
	
	public GetNameAction(
	    UPnPService upnpService,
		TemperatureSensorModel model
	){
		super(
			upnpService,
			"GetName",
			""
		);
		this.model=model;
		
			addOutArg(
				"CurrentName",
				upnpService.getStateVariable("Name")
			);
		
	}

	/**
	 * @see org.osgi.service.upnp.UPnPAction#invoke(java.util.Dictionary)
	 */
	public Dictionary invoke(Dictionary _args) throws Exception {

	StringHolder currentName = new StringHolder();
		

		// invoke model
		
		 model.getName(
			currentName
		 );
		
		// build returned Dictionary		
		Dictionary _result = new Hashtable();
		
			_result.put("CurrentName",currentName.getObject());
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


public class SetNameAction extends AbstractUPnPAction {

	TemperatureSensorModel model;
	
	// HERE specific action members
	
	public SetNameAction(
	    UPnPService upnpService,
		TemperatureSensorModel model
	){
		super(
			upnpService,
			"SetName",
			""
		);
		this.model=model;
		
			addInArg(
				"NewName",
				upnpService.getStateVariable("Name")
			);
		
	}

	/**
	 * @see org.osgi.service.upnp.UPnPAction#invoke(java.util.Dictionary)
	 */
	public Dictionary invoke(Dictionary _args) throws Exception {

	java.lang.String newName = (java.lang.String) _args.get("NewName");
		

		// invoke model
		
		 model.setName(
			StringHolder.toValue(
			newName)
		
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
	
}
