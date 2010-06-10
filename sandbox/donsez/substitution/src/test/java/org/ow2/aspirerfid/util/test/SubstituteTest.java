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
package org.ow2.aspirerfid.util.test;



import java.util.HashMap;
import java.util.Map;

import org.ow2.aspirerfid.util.SubstituteUtility;

import junit.framework.TestCase;

/**
 * @author Didier Donsez
 *
 */
public class SubstituteTest extends TestCase {

	
	String str="the little fox is running ${place} . the little dog is barking ${otherPlace: in the house}. ${foo:bar}";
	String rep1="the little fox is running in the forest . the little dog is barking in the street. BAR";
	String rep2="the little fox is running  . the little dog is barking . ";
	String rep3="the little fox is running in the forest . the little dog is barking in the street. bar";
	String rep4="the little fox is running  . the little dog is barking  in the house. bar";

	protected  void setUp(){		
	}
	
	protected  void tearDown() {
	}
	
	public void testGetTokens() {
		Map keysvalues=SubstituteUtility.getKeyValueMap(str);
		assertEquals("",keysvalues.get("place"));
		assertEquals(" in the house",keysvalues.get("otherPlace"));
    }

	public void testReplaceWithDefaults1() {
		Map keysvalues=new HashMap();
		keysvalues.put("place", "in the forest");
		keysvalues.put("otherPlace", "in the street");
		keysvalues.put("foo", "BAR");
		String rep=SubstituteUtility.substitute(str,keysvalues, true);
		System.out.println(rep);
		assertEquals(rep1,rep);		
    }

	public void testReplaceWithoutDefaults2() {
		Map keysvalues=new HashMap();
		String rep=SubstituteUtility.substitute(str,keysvalues, false);
		System.out.println(rep);
		assertEquals(str,rep);		
    }

	public void testReplaceWithDefaults3() {
		Map keysvalues=new HashMap();
		keysvalues.put("place", "in the forest");
		keysvalues.put("otherPlace", "in the street");
		String rep=SubstituteUtility.substitute(str,keysvalues, true);
		System.out.println(rep);
		assertEquals(rep3,rep);		
    }

	public void testReplaceWithDefaults4() {
		Map keysvalues=new HashMap();
		String rep=SubstituteUtility.substitute(str,keysvalues, true);
		System.out.println(rep);
		assertEquals(rep4,rep);		
    }

	
}
