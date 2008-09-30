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
package org.ow2.aspirerfid.ale.server;

import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.HashMap;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.accada.tdt.TDTEngine;
import org.accada.tdt.TDTException;
import org.accada.tdt.types.LevelTypeList;


/**
 * @author Nikos Kefalakis
 * 
 */
public class EpcUriFactory {

	private static final String TDT_RESOURCE = "C:\\ASPIRE_APPLICATION_FILES\\props\\";    // EpcUriFactory.class.getResource("/props/").getPath();
	
	//EpcUriFactory.class.getResource("WEB-INF/classes/props").
	//this.getClass().getClassLoader().getResource("myConfig.xml");
	//EpcUriFactory.class.getResource("WEB-INF/classes/props").getAuthority();
	//EpcUriFactory.class.getResource("classes/props").getPath();

	
	public String rawDecimal(byte[] tag) {

		// HexUtil.byteArrayToHexString(tag);
		// new BigInteger(HexUtil.byteArrayToHexString(tag),
		// 16).toByteArray().toString();
		// new BigInteger(tag).toString(2);
		// new BigInteger(tag).toString(2).toUpperCase();
		// Create a BigInteger using the byte array
		BigInteger bi = new BigInteger(tag);
		// Format to binary
		String tagInBinary = bi.toString(2);
		Integer tagLength = (tag.length) * 8;

		if (tagInBinary.length() < tagLength) {
			// zero padding until filling the length
			int zeros = tagLength - tagInBinary.length();
			for (int i = 0; i < zeros; i++) {
				tagInBinary = "0".concat(tagInBinary);
			}
		}

		String rawDecimal = null;
		TDTEngine tdtEngine = null;
		// String tagInHex = HexUtil.byteArrayToHexString(tag);
		rawDecimal = "urn:epc:raw:";
		try {
			tdtEngine = new TDTEngine(TDT_RESOURCE);
			System.out.println("TDT_RESOURCE file URI is at:"+EpcUriFactory.class.getResource("/props").toString());

			HashMap<String, String> param = new HashMap<String, String>();
			LevelTypeList levelType = LevelTypeList.BINARY;
			param.put("taglength", tagLength.toString());
			param.put("filter", "1");
			param.put("companyprefixlength", "7");
			rawDecimal = rawDecimal + tagLength.toString() + ".";
			rawDecimal = rawDecimal
					+ new BigInteger(new BigInteger(tdtEngine.convert(
							tagInBinary, param, levelType), 2).toByteArray())
							.toString(10).toUpperCase();
			// String[] uriArray =
			// tdtEngine.convert(tagInBinary,param,levelType).split(":");
			// rawDecimal = rawDecimal + uriArray[4];

		} catch (MarshalException e) {
			e.printStackTrace();
		} catch (ValidationException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (TDTException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return rawDecimal;
	}

	public String rawHex(byte[] tag) {
		String rawHex = null;
		// String tagInHex = HexUtil.byteArrayToHexString(tag);
		TDTEngine tdtEngine = null;

		// Create a BigInteger using the byte array
		BigInteger bi = new BigInteger(tag);

		// Format to binary
		String tagInBinary = bi.toString(2);
		Integer tagLength = (tag.length) * 8;
		if (tagInBinary.length() < tagLength) {
			// zero padding until 4
			int zeros = tagLength - tagInBinary.length();
			for (int i = 0; i < zeros; i++) {
				tagInBinary = "0".concat(tagInBinary);
			}
		}

		rawHex = "urn:epc:raw:";
		try {
			tdtEngine = new TDTEngine(TDT_RESOURCE);

			HashMap<String, String> param = new HashMap<String, String>();
			LevelTypeList levelType = LevelTypeList.BINARY;
			param.put("taglength", tagLength.toString());
			param.put("filter", "1");
			param.put("companyprefixlength", "7");
			rawHex = rawHex + tagLength.toString() + ".x";
			rawHex = rawHex
					+ new BigInteger(new BigInteger(tdtEngine.convert(
							tagInBinary, param, levelType), 2).toByteArray())
							.toString(16).toUpperCase();

		} catch (MarshalException e) {
			e.printStackTrace();
		} catch (ValidationException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (TDTException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return rawHex;
	}

	public String getTagIDAsTagURI(byte[] tag) {
		TDTEngine engine = null;
		HashMap<String, String> extraparams = null;
		Integer tagLength = (tag.length) * 8;

		// Create a BigInteger using the byte array
		BigInteger bi = new BigInteger(tag);

		// Format to binary
		String tagInBinary = bi.toString(2);

		if (tagInBinary.length() < tagLength) {
			// zero padding until 4
			int zeros = tagLength - tagInBinary.length();
			for (int i = 0; i < zeros; i++) {
				tagInBinary = "0".concat(tagInBinary);
			}
		}

		try {
			engine = new TDTEngine(TDT_RESOURCE);
			extraparams = new HashMap<String, String>();
			extraparams.put("taglength", tagLength.toString());
			extraparams.put("filter", "1");
			extraparams.put("companyprefixlength", "7");

		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (org.exolab.castor.xml.MarshalException ex) {
			ex.printStackTrace();
		} catch (org.exolab.castor.xml.ValidationException ex) {
			ex.printStackTrace();
		}
		return engine.convert(tagInBinary, extraparams,
				LevelTypeList.TAG_ENCODING);
	}

}
