/*
 __BANNER__
 */
// this file was generated at 11-July-2010 04:42 PM by ${author}
package org.ow2.aspire.upnp.rfidreader.service;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;

import org.apache.felix.upnp.devicegen.holder.IntegerHolder;
import org.apache.felix.upnp.devicegen.holder.StringHolder;
import org.apache.felix.upnp.devicegen.util.AbstractUPnPAction;
import org.apache.felix.upnp.devicegen.util.AbstractUPnPService;
import org.apache.felix.upnp.devicegen.util.AbstractUPnPStateVariable;
import org.apache.felix.upnp.devicegen.util.UPnPStateVariableDescriptor;
import org.osgi.framework.BundleContext;
import org.osgi.service.upnp.UPnPDevice;
import org.osgi.service.upnp.UPnPService;
import org.ow2.aspire.upnp.rfidreader.model.LowPowerDeviceModel;

public class LowPowerDevice extends AbstractUPnPService {

	private LowPowerDeviceModel model;

	/**
	 * constructor
	 */
	public LowPowerDevice(UPnPDevice upnpDevice, String serviceId,
			String serviceType, String version, LowPowerDeviceModel model) {
		super(upnpDevice, serviceId, serviceType, version);
		this.model = model;

		stateVariables = new HashMap();

		stateVariables.put("ExternalPowerSupplySource",
				new ExternalPowerSupplySourceStateVariable(this, model));

		stateVariables.put("BatteryLow", new BatteryLowStateVariable(this,
				model));

		stateVariables.put("PowerSupplyStatus",
				new PowerSupplyStatusStateVariable(this, model));

		stateVariables.put("SleepPeriod", new SleepPeriodStateVariable(this,
				model));

		stateVariables.put("PowerState", new PowerStateStateVariable(this,
				model));

		stateVariables.put("WakeupMethod", new WakeupMethodStateVariable(this,
				model));

		actions = new HashMap();

		actions.put("GetPowerManagementInfo", new GetPowerManagementInfoAction(
				this, model));

		actions.put("GoToSleep", new GoToSleepAction(this, model));

		actions.put("Wakeup", new WakeupAction(this, model));

	}

	// UPnPStateVariable classes

	// class ExternalPowerSupplySourceStateVariable
	public class ExternalPowerSupplySourceStateVariable extends
			AbstractUPnPStateVariable

	implements UPnPStateVariableDescriptor {

		public final static byte EXTERNALPOWERSUPPLYSOURCE_AC = 1;
		public final static byte EXTERNALPOWERSUPPLYSOURCE_INTERNAL = 0;

		public final static String NAME = "ExternalPowerSupplySource";
		public final static String DATATYPE = "i4";

		// HERE specific state variable members
		private LowPowerDeviceModel model;

		public ExternalPowerSupplySourceStateVariable(UPnPService upnpService,
				LowPowerDeviceModel model) {
			super(upnpService, NAME, DATATYPE, null, // TODO defaultValue
														// changed for
					null, // TODO step changed for
					null, // TODO step changed for
					null, // TODO minimum changed for
					null, // TODO maximum changed for
					// sendEventsAttribute
					true, true // TODO required changed for OR
			);

			this.model = model;

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
		 * 
		 * @see it.cnr.isti.niche.osgi.upnp.util.UPnPStateVariableDescriptor#getValue()
		 */
		public Object getValue() {

			return model.getExternalPowerSupplySourceStateVariableValue();

		}
	};

	// class BatteryLowStateVariable
	public class BatteryLowStateVariable extends AbstractUPnPStateVariable

	implements UPnPStateVariableDescriptor {

		public final static String NAME = "BatteryLow";
		public final static String DATATYPE = "boolean";

		// HERE specific state variable members
		private LowPowerDeviceModel model;

		public BatteryLowStateVariable(UPnPService upnpService,
				LowPowerDeviceModel model) {
			super(upnpService, NAME, DATATYPE, null, // TODO defaultValue
														// changed for
					null, // TODO step changed for
					null, // TODO step changed for
					null, // TODO minimum changed for
					null, // TODO maximum changed for
					// sendEventsAttribute
					true, true // TODO required changed for OR
			);

			this.model = model;

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
		 * 
		 * @see it.cnr.isti.niche.osgi.upnp.util.UPnPStateVariableDescriptor#getValue()
		 */
		public Object getValue() {

			return model.getBatteryLowStateVariableValue();

		}
	};

	// class PowerSupplyStatusStateVariable
	public class PowerSupplyStatusStateVariable extends
			AbstractUPnPStateVariable

	{

		public final static String NAME = "PowerSupplyStatus";
		public final static String DATATYPE = "string";

		// HERE specific state variable members
		private LowPowerDeviceModel model;

		public PowerSupplyStatusStateVariable(UPnPService upnpService,
				LowPowerDeviceModel model) {
			super(upnpService, NAME, DATATYPE, null, // TODO defaultValue
														// changed for
					null, // TODO step changed for
					null, // TODO step changed for
					null, // TODO minimum changed for
					null, // TODO maximum changed for
					// sendEventsAttribute
					false, true // TODO required changed for OR
			);

			this.model = model;

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
		 * 
		 * @see it.cnr.isti.niche.osgi.upnp.util.UPnPStateVariableDescriptor#getValue()
		 */
		public Object getValue() {
			return null;
		}
	};

	// class SleepPeriodStateVariable
	public class SleepPeriodStateVariable extends AbstractUPnPStateVariable

	{

		public final static int SLEEPPERIOD_INFINITE = -1;

		
		public final static String NAME = "SleepPeriod";
		public final static String DATATYPE = "i4";

		// HERE specific state variable members
		private LowPowerDeviceModel model;

		public SleepPeriodStateVariable(UPnPService upnpService,
				LowPowerDeviceModel model) {
			super(upnpService, NAME, DATATYPE, null, // TODO defaultValue
														// changed for
					null, // TODO step changed for
					null, // TODO step changed for
					null, // TODO minimum changed for
					null, // TODO maximum changed for
					// sendEventsAttribute
					false, true // TODO required changed for OR
			);

			this.model = model;

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
		 * 
		 * @see it.cnr.isti.niche.osgi.upnp.util.UPnPStateVariableDescriptor#getValue()
		 */
		public Object getValue() {
			return null;
		}
	};

	// class PowerStateStateVariable
	public class PowerStateStateVariable extends AbstractUPnPStateVariable

	{
		
		// PowerState value
		public final static int POWERSTATE_ACTIVE=0;
		public final static int POWERSTATE_TRANSPARENT=1;
		public final static int POWERSTATE_DEEPSLEEP=2;
		public final static int POWERSTATE_OFFLINE=4;
		
		
		


		public final static String NAME = "PowerState";
		public final static String DATATYPE = "string";

		// HERE specific state variable members
		private LowPowerDeviceModel model;

		public PowerStateStateVariable(UPnPService upnpService,
				LowPowerDeviceModel model) {
			super(upnpService, NAME, DATATYPE, null, // TODO defaultValue
														// changed for
					null, // TODO step changed for
					null, // TODO step changed for
					null, // TODO minimum changed for
					null, // TODO maximum changed for
					// sendEventsAttribute
					false, true // TODO required changed for OR
			);

			this.model = model;

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
		 * 
		 * @see it.cnr.isti.niche.osgi.upnp.util.UPnPStateVariableDescriptor#getValue()
		 */
		public Object getValue() {
			return null;
		}
	};

	// class WakeupMethodStateVariable
	public class WakeupMethodStateVariable extends AbstractUPnPStateVariable

	{

		public final static String NAME = "WakeupMethod";
		public final static String DATATYPE = "string";

		// HERE specific state variable members
		private LowPowerDeviceModel model;

		public WakeupMethodStateVariable(UPnPService upnpService,
				LowPowerDeviceModel model) {
			super(upnpService, NAME, DATATYPE, null, // TODO defaultValue
														// changed for
					null, // TODO step changed for
					null, // TODO step changed for
					null, // TODO minimum changed for
					null, // TODO maximum changed for
					// sendEventsAttribute
					false, true // TODO required changed for OR
			);

			this.model = model;

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
		 * 
		 * @see it.cnr.isti.niche.osgi.upnp.util.UPnPStateVariableDescriptor#getValue()
		 */
		public Object getValue() {
			return null;
		}
	};

	// UPnPAction classes

	public class GetPowerManagementInfoAction extends AbstractUPnPAction {

		LowPowerDeviceModel model;

		// HERE specific action members

		public GetPowerManagementInfoAction(UPnPService upnpService,
				LowPowerDeviceModel model) {
			super(upnpService, "GetPowerManagementInfo", "");
			this.model = model;

			addOutArg("WakeupMethod", upnpService
					.getStateVariable("WakeupMethod"));

			addOutArg("PowerSupplyStatus", upnpService
					.getStateVariable("PowerSupplyStatus"));

		}

		/**
		 * @see org.osgi.service.upnp.UPnPAction#invoke(java.util.Dictionary)
		 */
		public Dictionary invoke(Dictionary _args) throws Exception {

			StringHolder wakeupMethod = new StringHolder();
			StringHolder powerSupplyStatus = new StringHolder();

			// invoke model

			model.getPowerManagementInfo(wakeupMethod,

			powerSupplyStatus);

			// build returned Dictionary
			Dictionary _result = new Hashtable();

			_result.put("WakeupMethod", wakeupMethod.getObject());
			// java.lang.String
			_result.put("PowerSupplyStatus", powerSupplyStatus.getObject());
			// java.lang.String
			return _result.isEmpty() ? null : _result;
		}

		public void start(BundleContext bundleContext) throws Exception {
			// TODO
		}

		public void stop(BundleContext bundleContext) throws Exception {
			// TODO
		}
	};

	public class GoToSleepAction extends AbstractUPnPAction {

		LowPowerDeviceModel model;

		// HERE specific action members

		public GoToSleepAction(UPnPService upnpService,
				LowPowerDeviceModel model) {
			super(upnpService, "GoToSleep", "");
			this.model = model;

			addInArg("RecommendedSleepPeriod", upnpService
					.getStateVariable("SleepPeriod"));

			addInArg("RecommendedPowerState", upnpService
					.getStateVariable("PowerState"));

			addOutArg("SleepPeriod", upnpService
					.getStateVariable("SleepPeriod"));

			addOutArg("PowerState", upnpService.getStateVariable("PowerState"));

		}

		/**
		 * @see org.osgi.service.upnp.UPnPAction#invoke(java.util.Dictionary)
		 */
		public Dictionary invoke(Dictionary _args) throws Exception {

			java.lang.Integer recommendedSleepPeriod = (java.lang.Integer) _args
					.get("RecommendedSleepPeriod");
			java.lang.String recommendedPowerState = (java.lang.String) _args
					.get("RecommendedPowerState");
			IntegerHolder sleepPeriod = new IntegerHolder();
			StringHolder powerState = new StringHolder();

			// invoke model

			model.goToSleep(IntegerHolder.toValue(recommendedSleepPeriod),

			StringHolder.toValue(recommendedPowerState),

			sleepPeriod,

			powerState);

			// build returned Dictionary
			Dictionary _result = new Hashtable();

			_result.put("SleepPeriod", sleepPeriod.getObject());
			// java.lang.Integer
			_result.put("PowerState", powerState.getObject());
			// java.lang.String
			return _result.isEmpty() ? null : _result;
		}

		public void start(BundleContext bundleContext) throws Exception {
			// TODO
		}

		public void stop(BundleContext bundleContext) throws Exception {
			// TODO
		}
	};

	public class WakeupAction extends AbstractUPnPAction {

		LowPowerDeviceModel model;

		// HERE specific action members

		public WakeupAction(UPnPService upnpService, LowPowerDeviceModel model) {
			super(upnpService, "Wakeup", "");
			this.model = model;

		}

		/**
		 * @see org.osgi.service.upnp.UPnPAction#invoke(java.util.Dictionary)
		 */
		public Dictionary invoke(Dictionary _args) throws Exception {

			// invoke model

			model.wakeup(

			);

			// build returned Dictionary
			Dictionary _result = new Hashtable();

			return _result.isEmpty() ? null : _result;
		}

		public void start(BundleContext bundleContext) throws Exception {
			// TODO
		}

		public void stop(BundleContext bundleContext) throws Exception {
			// TODO
		}
	};

}
