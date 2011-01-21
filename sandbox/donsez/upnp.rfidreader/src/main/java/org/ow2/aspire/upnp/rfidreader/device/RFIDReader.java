
/*
__BANNER__
*/
// this file was generated at 21-January-2011 12:56 AM by ${author}
package org.ow2.aspire.upnp.rfidreader.device;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.service.upnp.UPnPDevice;
import org.osgi.service.upnp.UPnPIcon;
import org.osgi.service.upnp.UPnPService;

import org.apache.felix.upnp.devicegen.util.AbstractUPnPDevice;
import org.apache.felix.upnp.devicegen.util.DeviceIcon;

import org.ow2.aspire.upnp.rfidreader.model.*;
import org.ow2.aspire.upnp.rfidreader.service.*;
import org.ow2.aspire.upnp.rfidreader.impl.*;


	

public class RFIDReader extends AbstractUPnPDevice {

	public RFIDReader(BundleContext context, AbstractUPnPDevice parent) {
		super(context,parent);

		DEVICE_ID="uuid:ow2-aspire-rfidreader";

		// ServiceModel Declaration List
		ReaderModel myReaderModel=null; // TODO initialise it
TemperatureSensorModel myTemperatureSensorModel=null; // TODO initialise it
LowPowerDeviceModel myLowPowerDeviceModel=null; // TODO initialise it

		
		// Icon Lists
		
		List iconsList=new LinkedList();
	
		iconsList.add(
				new DeviceIcon(
					"image/png", // MimeType
					48, //Width
					48, // Height
					0, // ??
					16, // Depth
					"/icon/reader.png", // URL
					RFIDReader.class.getClassLoader()
				)
		);

		icons=(UPnPIcon[])iconsList.toArray(new UPnPIcon[]{});


		// Service List
		
		List servicesList=new LinkedList();
	
	
		/*
				SCPDURL=URL to service description
				controlURL=URL for control
				eventSubURL=URL for eventing
		*/
		servicesList.add(
				new Reader(
						this,
						"urn:ow2aspire-org:serviceId:Reader:1",
						"urn:schemas-ow2aspire-org:service:Reader:1",
						"1",
						myReaderModel									
				)
			);				

	
		/*
				SCPDURL=URL to service description
				controlURL=URL for control
				eventSubURL=URL for eventing
		*/
		servicesList.add(
				new TemperatureSensor(
						this,
						"urn:upnp-org:serviceId:TemperatureSensor:1",
						"urn:schemas-upnp-org:service:TemperatureSensor:1",
						"1",
						myTemperatureSensorModel									
				)
			);				

	
		/*
				SCPDURL=URL to service description
				controlURL=URL for control
				eventSubURL=URL for eventing
		*/
		servicesList.add(
				new LowPowerDevice(
						this,
						"urn:upnp-org:serviceId:LowPowerDevice:1",
						"urn:schemas-upnp-org:service:LowPowerDevice:1",
						"1",
						myLowPowerDeviceModel									
				)
			);				

		services=(UPnPService[])servicesList.toArray(new UPnPService[]{});


		// Embedded Device List
			
		//children=new LinkedList();
		
	
	}
	
	protected void setupDeviceProperties(){	
		dictionary.put(UPnPDevice.TYPE,"urn:schemas-upnp-org:device:DimmableLight:1");
		dictionary.put(UPnPDevice.FRIENDLY_NAME,"RFID Reader UPnP Device");
		dictionary.put(UPnPDevice.MANUFACTURER,"Didier Donsez (OW2 Aspire team)");
		dictionary.put(UPnPDevice.MANUFACTURER_URL,"http://wiki.aspire.ow2.org");
		dictionary.put(UPnPDevice.MODEL_DESCRIPTION,"RFID Reader UPnP Device");
		dictionary.put(UPnPDevice.MODEL_NAME,"RFID Reader");
		dictionary.put(UPnPDevice.MODEL_NUMBER,"123");
		dictionary.put(UPnPDevice.MODEL_URL,"http://wiki.aspire.ow2.org");
		dictionary.put(UPnPDevice.SERIAL_NUMBER,"123456789");
		dictionary.put(UPnPDevice.UDN,getUDN());
		dictionary.put(UPnPDevice.ID,dictionary.get(UPnPDevice.UDN));
		dictionary.put(UPnPDevice.UPC,"upc:ow2-aspire-rfidreader");
		dictionary.put(UPnPDevice.PRESENTATION_URL,"http://www.apache.org/~donsez/dev/osgi/upnp.devicegen/readme.html");		

		if(parent!=null) {
			dictionary.put(UPnPDevice.PARENT_UDN,parent.getUDN());
		}
		
		if(children!=null && children.size()!=0){
			String[] childrenUDN=new String[children.size()];
			Iterator iter=children.iterator();
			for(int i=0;iter.hasNext();i++){
				childrenUDN[i]=((AbstractUPnPDevice)iter.next()).getUDN();
			}
			dictionary.put(UPnPDevice.CHILDREN_UDN,childrenUDN);
		}
		
	}
}
