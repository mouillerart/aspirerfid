/*
 * Copyright 2005-2008, Aspire
 * 
 * This library is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by 
 * the Free Software Foundation (the "LGPL"); either version 2.1 of the 
 * License, or (at your option) any later version. If you do not alter this 
 * notice, a recipient may use your version of this file under either the 
 * LGPL version 2.1, or (at his option) any later version.
 * 
 * You should have received a copy of the GNU Lesser General Public License 
 * along with this library; if not, write to the Free Software Foundation, 
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * 
 * This software is distributed on an "AS IS" basis, WITHOUT WARRANTY OF ANY 
 * KIND, either express or implied. See the GNU Lesser General Public 
 * License for the specific language governing rights and limitations.
 */
package org.ow2.aspirerfid.reader.remote.http;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.osgi.service.event.EventConstants;
import org.ow2.aspirerfid.util.RFIDConstants;
import org.ow2.aspirerfid.wires.RFIDTagRead;

public class RfidPropertyAdapter {
	private static final String ID = "id";
	private static final String TIMESTAMP = "timestamp";
	private static final String READER_ID = "readerid";
	private static Map<String, String> equivalenceMap = new HashMap<String,String>();
	static final String[] MANDATORY_FIELDS = new String[]{ID,TIMESTAMP, READER_ID};
	private boolean valid;
	private TagAdapter tagAdapter = new TagAdapter();
//	private RFIDTagRead tagProp;
	
	//Added to use event instead of RFIDTagRead WireAdmin
	private String id, timestamp, readerId;
	 
//	public RfidPropertyAdapter(Map valuesToAdapt) {
//		if (validate(valuesToAdapt)) {
//			adaptValues(valuesToAdapt);
//		}
//	}

	public RfidPropertyAdapter(Map values) {
		if (validate(values)) {
			setTagInfo(values);
		}
	}
	
//	private void adaptValues(Map values) {
//		tagProp = new RFIDTagRead();
//		tagProp.put(RFIDConstants.READERGUID_KEY, values.get(READER_ID));
//		tagProp.put(EventConstants.TIMESTAMP, values.get(TIMESTAMP));
//		tagProp.put(RFIDConstants.TAGGUID_KEY, values.get(ID));
//	}
	
	private boolean validate(Map valuesToAdapt) {
		this.valid = true;
		for (String mandatoryField : RfidPropertyAdapter.MANDATORY_FIELDS) {
			String param = (String)valuesToAdapt.get(mandatoryField);
			if (param == null || param.trim().equals("")) {
				this.valid = false;
				break;
			}
		}
		
		return valid;
	}
	
	boolean isValid() {
		return valid;
	}
	
//	RFIDTagRead getTagRead() {
//		return tagProp;
//	}
	
	/**
	 * @return a dictionary containing a tagId, a timestamp and a readerId 
	 */
	public Dictionary getTagInfo(){
		Properties tagInfo = new Properties();
		tagInfo.put(RFIDConstants.TAGGUID_KEY, tagAdapter.adaptIDToEPC(id));
		tagInfo.put(EventConstants.TIMESTAMP, timestamp);
		tagInfo.put(RFIDConstants.READERGUID_KEY, readerId);
		return tagInfo;
	}	
	
	/**
	 * @param values, a tagID, a timestamp and the readerID
	 */
	public void setTagInfo(Map values){
		id = (String)values.get(ID);
		timestamp = (String)values.get(TIMESTAMP);
		readerId = (String)values.get(READER_ID);
	}
}
//Temp solution for adapting non EPC tag ids (e.g. NFC) to a format compliant with rfidsuite's tag factory
//Current workaround forces a GID prefix
//FIXME Migrate this to a TDT compliant solution (if any for NFC tags...)
class TagAdapter {
    private static final int TAG_LENGTH = 24;
    private static final String GID_TAG_PREFIX = "35";
    
    public String adaptIDToEPC(String tagID) {
        StringBuffer sb = new StringBuffer(tagID);
        sb.insert(0,GID_TAG_PREFIX);
        while (sb.length() < TAG_LENGTH) {
            sb.insert(2, "0");
        }
        return sb.toString();
    }
}