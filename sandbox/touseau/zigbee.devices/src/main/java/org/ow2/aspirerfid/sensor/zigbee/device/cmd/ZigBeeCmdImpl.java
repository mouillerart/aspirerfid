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
package org.ow2.aspirerfid.sensor.zigbee.device.cmd;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.felix.shell.Command;
import org.ow2.aspirerfid.sensor.zigbee.device.NodeDiscoveryService;
import org.ow2.aspirerfid.sensor.zigbee.device.NodePacketService;

/**
 * This class provides a shell command to interact with the ZigBee devices
 * 
 * @author Didier Donsez
 */

public class ZigBeeCmdImpl implements Command {

    private PrintStream m_out = System.out;

    private PrintStream m_err = System.err;

    private NodeDiscoveryService nodeDiscoveryService;
    
    private NodePacketService nodePacketService;
    
	/**
	 * @param nodeDiscoveryService the nodeDiscoveryService to bind
	 */
	public void bindNodeDiscoveryService(NodeDiscoveryService nodeDiscoveryService) {
		this.nodeDiscoveryService = nodeDiscoveryService;
	}

	/**
	 * @param nodeDiscoveryService the nodeDiscoveryService to unbind
	 */
	public void unbindNodeDiscoveryService(NodeDiscoveryService nodeDiscoveryService) {
		this.nodeDiscoveryService = null;
	}

	/**
	 * @param nodePacketService the nodePacketService to bind
	 */
	public void unbindNodePacketService(NodePacketService nodePacketService) {
		this.nodePacketService = null;
	}

	/**
	 * @param nodePacketService the nodePacketService to set
	 */
	public void bindNodePacketService(NodePacketService nodePacketService) {
		this.nodePacketService = nodePacketService;
	}

	public String getName() {
        return "zigbee";
    }

    public String getUsage() {
        return "zigbee help";
    }

    public String getShortDescription() {
        return "discover ZigBee nodes and send/receive raw packets to/from them";
    }

    public void execute(String s, PrintStream out, PrintStream err) {
        try {
            StringTokenizer st = new StringTokenizer(s, " ");

            // Ignore the command name.
            st.nextToken();

            if (st.hasMoreTokens()) {
                List tokenList = new ArrayList();
                String option = st.nextToken();

                while (st.hasMoreTokens()) {
                    tokenList.add(st.nextToken());
                }

                String[] args = null;

                if (tokenList.size() > 0) {
                    args = new String[tokenList.size()];
                    args = (String[]) tokenList.toArray(args);
                }

                if (option.equals("nodes")) {
                	printNotImplemented(out, err);
                     return;
                } else if (option.equals("node")) {
                	printNotImplemented(out, err);
                    return;
                } else if (option.equals("broadcast")) {
                	printNotImplemented(out, err);
                    return;
                } else if (option.equals("send")) {
                	printNotImplemented(out, err);
                    return;
                } else if (option.equals("addReceiver")) {
                	printNotImplemented(out, err);
                    return;
                } else if (option.equals("removeReceiver")) {
                	printNotImplemented(out, err);
                    return;
                } else if (option.equals("listAll")) {
                	printNotImplemented(out, err);
                    return;
                } else if (option.equals("removeAll")) {
                	printNotImplemented(out, err);
                    return;
               } else {
                    err.println("Unknown sub-command");
                    printHelp(out, err);
                    return;
                }
            }
            printHelp(out, err);
        } catch (Exception e) {
            e.printStackTrace(err);
        }
    }

    /**
     * @param out
     * @param err
     */
    private void printNotImplemented(PrintStream out, PrintStream err) {
        err.println("this option is not implemented");
    }

    /**
     * @param out
     * @param err
     */
    private void printHelp(PrintStream out, PrintStream err) {
        err.println("zigbee help\tdisplay this help");
        err.println("zigbee nodes\tlist known ZigBee nodes");
        err.println("zigbee node <nodeAddress>\tgive infos on the node");
        err.println("zigbee send <nodeAddress>:<port> <hexa byte array>\tsend a raw packet to a node");
        err.println("zigbee broadcast <port> <hexa byte array>\broadcast a raw packet to a node");
        err.println("zigbee addReceiver <nodeAddress>:<port> [outputFormat]\tadd a receiver of packets from a node");
        err.println("zigbee removeReceiver <nodeAddress>\tremove a receiver of packets from a node");
        err.println("zigbee listReceiver\tlist all active receivers");
        err.println("zigbee removeAll\tremove all active receivers");
     }

}