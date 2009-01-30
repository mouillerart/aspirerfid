/*
 * Copyright � 2008-2010, Aspire
 * 
 * Aspire is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License version 2.1 as published by
 * the Free Software Foundation (the "LGPL").
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library in the file COPYING-LGPL-2.1; if not, write to the
 * Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301 USA.
 * 
 * This software is distributed on an "AS IS" basis, WITHOUT WARRANTY OF ANY
 * KIND, either express or implied. See the GNU Lesser General Public License
 * for the specific language governing rights and limitations.
 */


package org.ow2.aspirerfid.ide.tcpmc.preferences;

import java.util.ArrayList;
import java.util.HashMap;

import org.ow2.aspirerfid.ide.tcpmc.epcmc.CaptureApp;
import org.ow2.aspirerfid.ide.tcpmc.rawmc.RawTcpMessageCapturer;


/**
 * Constant definitions for plug-in preferences
 *
 * @author Nikos Kefalakis (nkef) e-mail: nkef@ait.edu.gr
 *
 */
public class PreferenceConstants {

	public static final String P_RawTcpListeningPort = "7777";
	public static final String P_RawEpcListeningPort = "7777";
	public static HashMap<String, RawTcpMessageCapturer> P_RawTcpMessageCaptureThreads = null;
	public static HashMap<String,CaptureApp> P_EpcTcpMessageCaptureThreads = null;
	
}