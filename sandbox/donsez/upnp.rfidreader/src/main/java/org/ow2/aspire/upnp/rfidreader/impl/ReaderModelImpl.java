package org.ow2.aspire.upnp.rfidreader.impl;

import java.beans.PropertyChangeEvent;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.felix.upnp.devicegen.holder.LongHolder;
import org.apache.felix.upnp.devicegen.holder.StringHolder;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.upnp.UPnPException;
import org.ow2.aspire.upnp.rfidreader.device.RFIDReader;
import org.ow2.aspire.upnp.rfidreader.model.ReaderModel;
import org.ow2.aspire.upnp.rfidreader.service.Reader.DurationStateVariable;
import org.ow2.aspire.upnp.rfidreader.service.Reader.PropertiesStateVariable;
import org.ow2.aspire.upnp.rfidreader.service.Reader.ReportMembersStateVariable;

import com.tikitag.client.tagservice.ReaderMonitor;
import com.tikitag.client.tagservice.TagMonitor;
import com.tikitag.client.tagservice.TagService;
import com.tikitag.client.tagservice.TagServiceConfiguration;
import com.tikitag.client.tagservice.TagType;
import com.tikitag.client.tagservice.impl.TagServiceImpl;
import com.tikitag.ons.model.util.TagEvent;
import com.tikitag.ons.model.util.TagEvent.TagEventType;

public class ReaderModelImpl implements ReaderModel, BundleActivator, Runnable  {

    private static final Logger log = Logger.getLogger("org.ow2.aspirerfid.reader.tikitag");

	private long duration = 1000; // in millisec
	private String properties = "readerModel=acr122u";

	private Set/*<String>*/ touchputTagIdSet=new HashSet/*<String>*/();
	private Set/*<String>*/ removedTagIdSet=new HashSet/*<String>*/();
	
    private TagService tagService;
    private TagServiceConfiguration tagServiceConfiguration;

    private RFIDReader reader;
    private Thread thread;
    
    public ReaderModelImpl(RFIDReader reader) {
    	this.reader=reader;
    }
    
	/**
	 * Set the duration newDuration in parameter
	 */
	public void setDuration(long newDuration) throws UPnPException {
		if (newDuration > 0) {
			long oldDuration=duration;
			duration = newDuration;
			PropertyChangeEvent event=new PropertyChangeEvent(this,DurationStateVariable.NAME,Long.toString(oldDuration),Long.toString(newDuration));
			reader.propertyChange(event);			
		} else {
			throw new UPnPException(UPnPException.INVALID_ARGS,"Duration must be positive");
		}
	}

	/**
	 * Get the duration currentDuration out parameter
	 */
	public void getDuration(LongHolder currentDuration) throws UPnPException {
		currentDuration.setValue(duration);
	}

	/**
	 * Get the reader properties currentProperties out parameter
	 */
	public void getProperties(StringHolder currentProperties)
			throws UPnPException {
		currentProperties.setValue(properties);
	}

	// Those getters are used for the first notification just after a
	// subscription

	public java.lang.Long getDurationStateVariableValue() {
		return new Long(duration);
	}

	public java.lang.String getReportMembersStateVariableValue(boolean resetTagidSet) {
		StringBuffer sb=new StringBuffer();
		synchronized (this) {
			int cpt=0;
			for (Iterator it=touchputTagIdSet.iterator(); it.hasNext();) {
				String id = (String) it.next();					
				if(cpt!=0) sb.append(";");
				cpt++;
				sb.append(id);
			}
//				for (Iterator it=removedTagIdSet.iterator(); it.hasNext();) {
//					String id = (String) it.next();
//					touchputTagIdSet.remove(id);
//				}
			if(resetTagidSet) touchputTagIdSet.clear();
		}
		return sb.toString();
	}

	
	public java.lang.String getReportMembersStateVariableValue() {
		return getReportMembersStateVariableValue(false);
	}

	public void start(BundleContext bundleContext) throws Exception {
        tagServiceConfiguration = new TagServiceConfiguration();
    	// TODO should be changed via configuration admin and MBean
        // pollInterval is the time for ???
        tagServiceConfiguration.setPollInterval(100);
        // putThreshold is the time for considering a PUT action 
        tagServiceConfiguration.setPutThresholdTime(500);
        
        
        // for the diagnostic
        StringBuffer sb=new StringBuffer();
        sb.append("Detected tag type:");
        TagType[] tagTypes=tagServiceConfiguration.getDetectTagTypes();
        for (int i = 0; i < tagTypes.length; i++) {
        	if(i!=0) sb.append(',');
			TagType type = tagTypes[i];
	        sb.append(' ').append(type);		
		}
        String msg=sb.toString();
        log.log(Level.INFO,msg);
        System.out.println(msg);

        
        tagService = new TagServiceImpl(tagServiceConfiguration);

        ReaderMonitor readerMonitor = new SimpleReaderMonitorImpl();
        tagService.addReaderMonitor(readerMonitor);
        tagService.addTagMonitor(new TagMonitor() {
	            public void onTagEvent(TagEvent tagEvent)
	            {
	            	TagEventType tagEventType=tagEvent.getTagEventType();
	            	String id;
					try {
						id = getHexString(tagEvent.getActionTag().getTagId().asByteArray());
		                if(TagEventType.REMOVE.equals(tagEventType)) {
		                	synchronized(this){
				                removedTagIdSet.add(id);
		                	}
			            } else { // PUT or TOUCH
		                	synchronized(this){
				                removedTagIdSet.remove(id);		                		
				                touchputTagIdSet.add(id);
		                	}
			            }
					} catch (Exception e) {
						
					}
	            }
        	}
        );
        tagService.start();
        
        
		thread=new Thread(this);
		thread.setName(this.getClass().getName());
		thread.start();        
	}

	public void stop(BundleContext bundleContext) throws Exception {
		end=true;
		Thread.interrupted();
		
        tagService.shutdown();
	}
	
    private String printTagEvent(TagEvent tagEvent){
	    StringBuffer sb = new StringBuffer();
	    sb.append("\nDetected tag: ");
	    sb.append("\n  Type        = ").append(tagEvent.getTagEventType()).toString();                
	    sb.append("\n  Client      = ").append(tagEvent.getClientId()).toString();                
	    sb.append("\n  ReaderId    = ").append(tagEvent.getReaderId()).toString();                
	    sb.append("\n  Action  Tag = ").append(tagEvent.getActionTag()).toString();
	    sb.append("\n  Context Tag = ").append(tagEvent.getContextTag()).toString();
    	return sb.toString();
    }

    private String dumpTagEvent(TagEvent tagEvent){
	    StringBuffer sb = new StringBuffer();
	    sb.append("EventTag[");
	    sb.append("type=").append(tagEvent.getTagEventType()).toString();                
	    sb.append(";client=").append(tagEvent.getClientId()).toString();                
	    sb.append(";readerId=").append(tagEvent.getReaderId()).toString();                
	    sb.append(";actionTag=").append(tagEvent.getActionTag()).toString();
	    sb.append("contextTag=").append(tagEvent.getContextTag()).toString();
	    sb.append("]");
    	return sb.toString();
    }	
    
    
    private static String getHexString(byte[] b) throws Exception {
    	  String result = "";
    	  for (int i=0; i < b.length; i++) {
    	    result +=
    	          Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
    	  }
    	  return result;
    	}

    private volatile boolean end=false;
	
	private String previousReportMembers="";
	
	public void run() {
		while(!end) {
			try {
				Thread.sleep(getDurationStateVariableValue().longValue());
				if(end) return;
				String reportMembers=getReportMembersStateVariableValue(true);
				//if(reportMembers==null || reportMembers.length()==0) continue;
				if(reportMembers.equals(previousReportMembers)) continue;
				PropertyChangeEvent event=new PropertyChangeEvent(this,ReportMembersStateVariable.NAME,previousReportMembers,reportMembers);
				previousReportMembers=reportMembers;
				reader.propertyChange(event);				
			} catch (InterruptedException e) {
				return;
			}
		}
	}
    
}
