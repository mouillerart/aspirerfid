/**
 * Copyright (c) 2008-2010, AspireRFID
 *
 * This library is free software; you can redistribute it and/or
 * modify it either under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation
 * (the "LGPL"). If you do not alter this
 * notice, a recipient may use your version of this file under the LGPL.
 *
 * You should have received a copy of the LGPL along with this library
 * in the file COPYING-LGPL-2.1; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * This software is distributed on an "AS IS" basis, WITHOUT WARRANTY
 * OF ANY KIND, either express or implied. See the LGPL  for
 * the specific language governing rights and limitations.
 *
 * Contact: AspireRFID mailto:aspirerfid@ow2.org
 */
package org.ow2.aspirerfid.util;

import java.util.HashMap;
import java.util.Map;

/**
 * This class provides methods to replace ${key} and ${key:value} substring by values stored in a map
 * @author Didier Donsez
 */

public class SubstituteUtility {

	/**
	 * get the map of key-value present in the string str. The formats are ${key:defaultValue} or ${key}. In the ${key} format, the default value is "".
	 * @param str the string to parse
	 * @return the map of the key-value
	 */
	public static Map getKeyValueMap(String str) {
		int len = str.length();

		Map pairs=new HashMap();
		
		int prev = 0;
		int start = str.indexOf("${");
		int end = str.indexOf("}", start);
		while (start != -1 && end != -1) {
			String pair = str.substring(start + 2, end);
			String key;
			String value;
			int sep=pair.indexOf(":");
			if(sep>=0) {
				key=pair.substring(0, sep);
				value=pair.substring(sep+1);
			} else {
				key=pair;
				value="";
			}
			pairs.put(key, value);

			prev = end + 1;
			if (prev >= str.length())
				break;

			start = str.indexOf("${", prev);
			if (start != -1)
				end = str.indexOf("}", start);
		}

		return pairs;
	}
    
    /**
	 * This method substitute ${key} and ${key:defaultValue} substring by values stored in a map.
	 * @param str the string to replace
	 * @param values the key-values pairs to replace
	 * @param substituteWithDefaultValues a flag to force the replacement with default values when the key is not in the values map. The default value for ${key} is "".
	 * @return the string with replacement
	 */	
	public static String substitute(String str, Map values, boolean substituteWithDefaultValues) {

		int len = str.length();
		StringBuffer sb = new StringBuffer(len);

		int prev = 0;
		int start = str.indexOf("${");
		int end = str.indexOf("}", start);
		while (start != -1 && end != -1) {
			
			String pair = str.substring(start + 2, end);
			String key;
			String defaultValue;
			int sep=pair.indexOf(":");
			if(sep>=0) {
				key=pair.substring(0, sep);
				defaultValue=pair.substring(sep+1);
			} else {
				key=pair;
				defaultValue="";
			}
			
			Object value = values.get(key);
			if (value != null) {
				sb.append(str.substring(prev, start));
				sb.append(value);
			} else if(substituteWithDefaultValues) {
				sb.append(str.substring(prev, start));
				sb.append(defaultValue);					
			} else {
				sb.append(str.substring(prev, end + 1));
			}
			prev = end + 1;
			if (prev >= str.length())
				break;

			start = str.indexOf("${", prev);
			if (start != -1)
				end = str.indexOf("}", start);
		}

		sb.append(str.substring(prev));

		return sb.toString();
	}
}