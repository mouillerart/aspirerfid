package org.sembysem.sam.demo.rfid.service;

import java.util.Date;

public class RfidTag {

	
	private Date timeStamp;
	private String value;

	public RfidTag(String value, Date timeStamp){
		this.value = value;
		this.timeStamp = timeStamp;
	}
	
	public String getTagValue(){
		return value;
	}
	
	public Date getTime(){
		return timeStamp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((timeStamp == null) ? 0 : timeStamp.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RfidTag other = (RfidTag) obj;
//		if (timeStamp == null) {
//			if (other.timeStamp != null)
//				return false;
//		} else if (!timeStamp.equals(other.timeStamp))
//			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
}
