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
package org.ow2.aspirerfid.reader.tikitag.impl;

import com.tikitag.client.tagservice.ReaderMonitor;
import com.tikitag.client.tagservice.TagMonitor;
import com.tikitag.client.tagservice.TagService;
import com.tikitag.client.tagservice.TagServiceConfiguration;
import com.tikitag.client.tagservice.TagType;
import com.tikitag.client.tagservice.impl.TagServiceImpl;
import com.tikitag.ons.model.util.TagEvent;
import com.tikitag.util.HexFormatter;


/**
 * 
 * @author Didier Donsez
 *
 */
public class SimpleTikitagClient
{

    private TagService tagService;
    private TagServiceConfiguration tagServiceConfiguration;

    public static void main(String args[])
    {
        //Parameters params = Parameters.parse(args);
        SimpleTikitagClient client = new SimpleTikitagClient();
        client.start();
    }

    public void start()
    {
        tagServiceConfiguration = new TagServiceConfiguration();

        // pollInterval is the time for ???
        tagServiceConfiguration.setPollInterval(100);
        // putThreshold is the time for considering a PUT action 
        tagServiceConfiguration.setPutThresholdTime(1000);
        
        TagType[] tagTypes=tagServiceConfiguration.getDetectTagTypes();
        for (int i = 0; i < tagTypes.length; i++) {
			TagType type = tagTypes[i];
	        System.out.println("Detected tag type:"+type.toString());		
		}

        tagService = new TagServiceImpl(tagServiceConfiguration);

//        ClientLifecycleHandler clientLifeCycleHandler = new ClientLifecycleHandler(tagServiceConfiguration);
//        tagService.addReaderMonitor(clientLifeCycleHandler);
        ReaderMonitor readerMonitor = new SimpleReaderMonitorImpl();
        tagService.addReaderMonitor(readerMonitor);
        tagService.addTagMonitor(new TagMonitor() {

            public void onTagEvent(TagEvent tagEvent)
            {
                StringBuilder sb = new StringBuilder();
                sb.append((new StringBuilder()).append("\n  Type        = ").append(tagEvent.getTagEventType()).toString());                
                sb.append((new StringBuilder()).append("\n  Client      = ").append(tagEvent.getClientId()).toString());                
                sb.append((new StringBuilder()).append("\n  ReaderId    = ").append(tagEvent.getReaderId()).toString());                
                sb.append((new StringBuilder()).append("\n  Action  Tag = ").append(tagEvent.getActionTag()).toString());
                sb.append((new StringBuilder()).append("\n  Context Tag = ").append(tagEvent.getContextTag()).toString());
                sb.append((new StringBuilder()).append(HexFormatter.pageView(HexFormatter.toHexString(tagEvent.getActionTag().getTagData()))));
                System.out.println((new StringBuilder()).append("Detected tags: ").append(sb.toString()).toString());
            }

        	}
        );
        tagService.start();
    }

    public void stop()
    {
        tagService.shutdown();
    }
}
