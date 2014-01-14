/*
 * Copyright 2005-2009, Aspire
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

package org.ow2.aspirerfid.xmpp.xmppclient;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketFilter;

public interface XMPPClientService {

	public void addMessage(String message);

	public void sendMessage(String message) throws XMPPException;

	public void addPacketListener(PacketListener pl, PacketFilter pf);

	public void addConnectionListener(ConnectionListener cl);

	public void removeConnectionListener(ConnectionListener cl);

	public void removePacketListener(PacketListener pl);

}
