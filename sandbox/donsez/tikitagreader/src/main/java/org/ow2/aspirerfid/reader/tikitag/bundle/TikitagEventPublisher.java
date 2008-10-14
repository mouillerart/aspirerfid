/*
   Copyright 2005-2008, OW2 Aspire RFID project 
   
   This library is free software; you can redistribute it and/or modify it 
   under the terms of the GNU Lesser General Public License as published by 
   the Free Software Foundation (the "LGPL"); either version 2.1 of the 
   License, or (at your option) any later version. If you do not alter this 
   notice, a recipient may use your version of this file under either the 
   LGPL version 2.1, or (at his option) any later version.
   
   You should have received a copy of the GNU Lesser General Public License 
   along with this library; if not, write to the Free Software Foundation, 
   Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
   
   This software is distributed on an "AS IS" basis, WITHOUT WARRANTY OF ANY 
   KIND, either express or implied. See the GNU Lesser General Public 
   License for the specific language governing rights and limitations.

   Contact: OW2 Aspire RFID project <X AT Y DOT org> (with X=aspirerfid and Y=ow2)

   LGPL version 2.1 full text http://www.gnu.org/licenses/lgpl-2.1.txt    
*/
package org.ow2.aspirerfid.reader.tikitag.bundle;

import java.util.Dictionary;
import java.util.Hashtable;

import org.apache.log4j.Logger;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.osgi.util.tracker.ServiceTracker;
import org.ow2.aspirerfid.reader.tikitag.impl.SimpleReaderMonitorImpl;

import com.tikitag.client.tagservice.ReaderMonitor;
import com.tikitag.client.tagservice.TagMonitor;
import com.tikitag.client.tagservice.TagService;
import com.tikitag.client.tagservice.TagServiceConfiguration;
import com.tikitag.client.tagservice.TagType;
import com.tikitag.client.tagservice.impl.TagServiceImpl;
import com.tikitag.ons.model.util.TagEvent;

/**
 * This class publishs events related to tags scanned on the Tikitag readers connected to the host. 
 * @author Didier Donsez
 */
public class TikitagEventPublisher implements BundleActivator
{
	public static final String TOPIC = "tikitag";
	
	private ServiceTracker serviceTracker;
    private TagService tagService;
    private TagServiceConfiguration tagServiceConfiguration;
    private static final Logger log = Logger.getLogger("org.ow2.aspirerfid.reader.tikitag");
    
    public void start(BundleContext bundleContext) throws Exception
    {
    	
    	serviceTracker=new ServiceTracker(bundleContext,EventAdmin.class.getName(),null);
    	serviceTracker.open();
    	
        tagServiceConfiguration = new TagServiceConfiguration();
    	// TODO should be changed via configuration admin and MBean
        // pollInterval is the time for ???
        tagServiceConfiguration.setPollInterval(100);
        // putThreshold is the time for considering a PUT action 
        tagServiceConfiguration.setPutThresholdTime(1000);
        
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
        log.debug(msg);
        System.out.println(msg);
        
        tagService = new TagServiceImpl(tagServiceConfiguration);

        ReaderMonitor readerMonitor = new SimpleReaderMonitorImpl();
        tagService.addReaderMonitor(readerMonitor);
        tagService.addTagMonitor(new TagMonitor() {

	            public void onTagEvent(TagEvent tagEvent)
	            {
	                if(!"REMOVE".equals(tagEvent.getTagEventType())) {
		                EventAdmin eventAdmin=(EventAdmin)serviceTracker.getService();
		                if(eventAdmin!=null) {
		                	Dictionary dictionary=new Hashtable();
			                dictionary.put("type",tagEvent.getTagEventType());        
			                dictionary.put("readerid",tagEvent.getReaderId().getUid());          
			                dictionary.put("readersn",tagEvent.getReaderId().getSerialNr());     
			                dictionary.put("tagid",tagEvent.getActionTag().getTagId().asByteArray());                
			                dictionary.put("tagdata",tagEvent.getActionTag().getTagData());
			                dictionary.put("timestamp",new Long(System.currentTimeMillis())); // TODO Date or Long ?			                
		                	Event rfidEvent=new Event(TOPIC, dictionary);
		                	eventAdmin.postEvent(rfidEvent);
		                }
		            }
	                StringBuffer sb = new StringBuffer();
	                sb.append("\nDetected tags: ");
	                sb.append("\n  Type        = ").append(tagEvent.getTagEventType()).toString();                
	                sb.append("\n  Client      = ").append(tagEvent.getClientId()).toString();                
	                sb.append("\n  ReaderId    = ").append(tagEvent.getReaderId()).toString();                
	                sb.append("\n  Action  Tag = ").append(tagEvent.getActionTag()).toString();
	                sb.append("\n  Context Tag = ").append(tagEvent.getContextTag()).toString();
	                String msg=sb.toString();
	                log.debug(msg);
	                System.out.println(msg);
	            }
        	}
        );
        tagService.start();
    }

    public void stop(BundleContext bundleContext) throws Exception
    {
        tagService.shutdown();
        serviceTracker.close();
    }
}
