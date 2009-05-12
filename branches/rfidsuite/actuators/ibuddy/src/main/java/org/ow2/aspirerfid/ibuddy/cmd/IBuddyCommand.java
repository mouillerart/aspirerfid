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

package org.ow2.aspirerfid.ibuddy.cmd;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.felix.shell.Command;
import org.ow2.aspirerfid.ibuddy.service.Action;
import org.ow2.aspirerfid.ibuddy.service.IBuddyDescriptor;
import org.ow2.aspirerfid.ibuddy.service.IIBuddy;
import org.ow2.aspirerfid.ibuddy.service.MyBuddy;

/**
 * provides a shell command to control connected iBuddies.
 * @author El Mehdi Damon
 */
public class IBuddyCommand implements Command {

	private IIBuddy buddy;
	
	/**
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start()  {
	}

	/**
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(){
	}

	/**
	 * @see org.apache.felix.shell.Command#execute(java.lang.String, java.io.PrintStream, java.io.PrintStream)
	 */
	public void execute(String cmdline, PrintStream out, PrintStream err) {
		StringTokenizer st=new StringTokenizer(cmdline, " ");
		int n=st.countTokens();

		
		if(n<2) {
			err.println(getFullUsage());
			return;
		}
		
		st.nextToken(); // skip command name
		
		
		String subcommand=st.nextToken();
		n-=2;
		Map<Long, IBuddyDescriptor> buddies =  buddy.getListIbuddy();
		if(subcommand.equalsIgnoreCase("*")){
			
//			System.out.println(buddies.keySet().size()+ " ibuddy(s) found!");
//			for (Iterator<?> iterator = buddies.keySet().iterator(); iterator.hasNext();) {
//				Long id = (Long) iterator.next();
//				System.out.println("buddy: " + id );
//				
//			}
		}else{
			
			
			try{
				Long id = Long.parseLong(subcommand);
				Map <Long, List> listBuddy =  new HashMap<Long, List>();
				String cmd =null;
				String listcmd = new String();
				if (st.hasMoreTokens()) cmd = st.nextToken();
				while (st.hasMoreTokens()) {
					if (cmd.equalsIgnoreCase("ibuddy")){
						List<Map<Action,List<String>>> buddycmd = parseCmdLineOrder(listcmd);
						if (listBuddy.keySet().contains(id)){
							listBuddy.get(id).addAll(buddycmd);
						}else{
							listBuddy.put(id, buddycmd);
						}
						id = Long.parseLong(st.nextToken());
						listcmd = new String();
					}
					else{
						listcmd = listcmd + " " + cmd;
					}
					if (st.hasMoreTokens()) cmd = st.nextToken();
				}
				
				List<Map<Action,List<String>>> buddycmd = parseCmdLineOrder(listcmd);
				if (listBuddy.keySet().contains(id)){
					listBuddy.get(id).addAll(buddycmd);
				}else{
					listBuddy.put(id, buddycmd);
				}
				
				IBuddyDescriptor abudy = null;
				for (Iterator<Long> iterator = listBuddy.keySet().iterator(); iterator
						.hasNext();) {
					Long idbuddy = (Long) iterator.next();
					out.println(">>iBuddy " + idbuddy +  ":" );
					abudy = buddies.get(idbuddy);
					List<Map<Action,List<String>>> buddyDo = listBuddy.get(idbuddy);
					for (Iterator<Map<Action, List<String>>> iterator2 = buddyDo.iterator(); iterator2
							.hasNext();) {
						Map<Action, List<String>> map = (Map<Action, List<String>>) iterator2.next();
						for (Iterator iterator3 = map.keySet().iterator(); iterator3
								.hasNext();) {
							Action action = (Action) iterator3.next();
							out.println("	"+action +  " : "  + map.get(action));
						}
						new MyBuddy(abudy,map);
					}
					
				}
				
				
				/////////////////////////////////////////////////////////////////////////////////////////////////
//				while (st.hasMoreTokens()) {
//					if (cmd.equalsIgnoreCase("ibuddy")){
//						Map<Action,List<String>> buddycmd = parseCmdLine(listcmd);
//						if (listBuddy.keySet().contains(id)){
//							for (Iterator iterator = buddycmd.keySet().iterator(); iterator.hasNext();) {
//								Action type = (Action) iterator.next();
//								if (listBuddy.get(id).keySet().contains(type)){
//									((List<String>)listBuddy.get(id).get(type)).addAll(buddycmd.get(type));
//								}else {
//									listBuddy.get(id).put(type,buddycmd.get(type));
//								}
//						
//							}
////						listBuddy.get(id).putAll(buddycmd);
//						}else {
//							listBuddy.put(id,buddycmd);
//						}
//						id = Long.parseLong(st.nextToken());
//						listcmd = new String();
//					}else{
//						listcmd = listcmd + " " + cmd;
//					}
//					if (st.hasMoreTokens()) cmd = st.nextToken();
//				}
//				
//				Map<Action,List<String>> list = parseCmdLine(listcmd);
//				if (listBuddy.keySet().contains(id)){
//					for (Iterator iterator = list.keySet().iterator(); iterator
//							.hasNext();) {
//						Action type = (Action) iterator.next();
//						if (listBuddy.get(id).keySet().contains(type)){
//							((List<String>)listBuddy.get(id).get(type)).addAll(list.get(type));
//						}else {
//							listBuddy.get(id).put(type,list.get(type));
//						}
//						
//					}
//					
////					listBuddy.get(id).putAll(list);
//				}else {
//					listBuddy.put(id,list);
//				}
//				IBuddyDescriptor abudy = null;
//				for (Iterator<Long> iterator = listBuddy.keySet().iterator(); iterator
//						.hasNext();) {
//					Long idbuddy = (Long) iterator.next();
//					out.println(">>iBuddy " + idbuddy +  ":" );
//					abudy = buddies.get(idbuddy);
//					Map<Action,List<String>> buddyDo = listBuddy.get(idbuddy);
//					for (Iterator<Action> iterator2 = buddyDo.keySet().iterator(); iterator2
//							.hasNext();) {
//						Action action = (Action) iterator2.next();
//						out.println("	"+action +  " : "  + buddyDo.get(action));
//						
//					}
//					new MyBuddy(abudy,buddyDo);
//				}
				///////////////////////////////////////////////////////////////////////////////////////////////////
				
				
//				if ((id >= 0) && (id <buddies.size())){
//					buddies.get(id).open();
//					buddies.get(id).sendReset();
//					while (st.hasMoreTokens()) {
//						String cmd = st.nextToken();
//				        System.out.println(cmd);
//				        if (cmd.equalsIgnoreCase("rotate")){
//				        	cmd = st.nextToken();
//				        	cmd = cmd.toUpperCase();
//				        	while (cmd.equalsIgnoreCase("left") || cmd.equalsIgnoreCase("right")){
//					        	cmd = cmd.toUpperCase();
//					        	buddies.get(id).sendOrientation(Orientation.valueOf(cmd));
//					        	Thread.sleep(500);
//					        	cmd = st.nextToken();	
//				        	}
//						}
//				        if (cmd.equalsIgnoreCase("FLAP")){
//				        	cmd = st.nextToken();
//				        	try{
//				        	Integer times = Integer.parseInt(cmd);
//				        	flapwings(buddies.get(id),times);
//				        	}catch (NumberFormatException e) {
//								System.out.println(cmd + " is incorrect parameter! \n Flap command requiere a number parameter, ex : flap 10");
//							}
//				        }
//				     }
//					buddies.get(id).close();
//				}else{
//					System.out.println(subcommand + " is an incorrect parameter!! \n  choose buddy id from 0 to " + buddies.size());
//				}
				
			}catch (CmdException e){
				err.println( "\n" +  e.messageError);
			}
		}

	}

	private Map<Action,List<String>> parseCmdLine(String cmdline) throws CmdException{
		
		Map<Action,List<String>> commandToDo = new HashMap<Action, List<String>>();
		StringTokenizer st=new StringTokenizer(cmdline, " ");
		List<String> actions = null;
		String cmd=null;
		if (st.hasMoreTokens()) cmd = st.nextToken();
		while (st.hasMoreTokens()) {
//	        System.out.println(cmd);
	        if (cmd.equalsIgnoreCase("rotate")){
	        	actions = new ArrayList<String>();
	        	if (st.hasMoreTokens()) cmd = st.nextToken();
	        	else throw (new CmdException(cmd + " need paramter! \n tape help to get list of command "));
	        	cmd = cmd.toUpperCase();
	        	while (cmd.equalsIgnoreCase("left") || cmd.equalsIgnoreCase("right")){
		        	cmd = cmd.toUpperCase();
		        	actions.add(cmd);
		        	if (st.hasMoreTokens()) cmd = st.nextToken();
		        	else break;	
	        	}
	        	if (commandToDo.keySet().contains(Action.ROTATE)){
	        		commandToDo.get(Action.ROTATE).addAll(actions);
	        	}else{
	        		commandToDo.put(Action.ROTATE, actions);
	        	}
			}
	        else if (cmd.equalsIgnoreCase("flap")){
	        	actions = new ArrayList<String>();
	        	if (st.hasMoreTokens()) cmd = st.nextToken();
	        	else throw (new CmdException(cmd + " need paramter! \n tape help to get list of command "));
	        	try{
	        		Integer times = Integer.parseInt(cmd);
	        		actions.add(times.toString());
	        	}catch (NumberFormatException e) {
					throw (new CmdException(cmd + " is incorrect parameter! \n Flap command requiere a number parameter, ex : flap 10"));	
				}	        	
	        	if (commandToDo.keySet().contains(Action.FLAP)){
	        		commandToDo.get(Action.FLAP).addAll(actions);
	        	}else{
	        		commandToDo.put(Action.FLAP, actions);
	        	}
	        	if (st.hasMoreTokens()) cmd = st.nextToken();
	        }
	        else if (cmd.equalsIgnoreCase("heart")){
	        	actions = new ArrayList<String>();
	        	if (st.hasMoreTokens()) cmd = st.nextToken();
	        	else throw (new CmdException(cmd + " need paramter! \n tape help to get list of command "));
	        	cmd = cmd.toUpperCase();
	        	while (cmd.equalsIgnoreCase("on") || cmd.equalsIgnoreCase("off")){
		        	cmd = cmd.toUpperCase();
		        	actions.add(cmd);
		        	if (st.hasMoreTokens()) cmd = st.nextToken();
		        	else break;		
	        	}
	        	
	        	if (commandToDo.keySet().contains(Action.HEART)){
	        		commandToDo.get(Action.HEART).addAll(actions);
	        	}else{
	        		commandToDo.put(Action.HEART, actions);
	        	}
	        }
	        
	        else if(cmd.equalsIgnoreCase("head")){
	        	actions = new ArrayList<String>();
	        	if (st.hasMoreTokens()) cmd = st.nextToken();
	        	else throw (new CmdException(cmd + " need paramter! \n tape help to get list of command "));
	        	cmd = cmd.toUpperCase();
	        	while (cmd.equalsIgnoreCase("NONE") || cmd.equalsIgnoreCase("YELLOW") || 
	        			cmd.equalsIgnoreCase("BLUE")|| cmd.equalsIgnoreCase("WHITE") ||
	        			cmd.equalsIgnoreCase("VIOLET")|| cmd.equalsIgnoreCase("CYAN")|| 
	        			cmd.equalsIgnoreCase("GREEN")|| cmd.equalsIgnoreCase("RED")){
	        		
		        	cmd = cmd.toUpperCase();
		        	actions.add(cmd);
		        	if (st.hasMoreTokens()) cmd = st.nextToken();
		        	else break;	
	        	}
	        	
	        	if (commandToDo.keySet().contains(Action.HEAD)){
	        		commandToDo.get(Action.HEAD).addAll(actions);
	        	}else{
	        		commandToDo.put(Action.HEAD, actions);
	        	}
	        }
	        
	        else throw (new CmdException(cmd  + "is an unknown command"));    
	     }
		
		return commandToDo;
		
	}
	
	private List<Map<Action,List<String>>> parseCmdLineOrder(String cmdline) throws CmdException{
		int order = 0;
		List<Map<Action,List<String>>> orderList= new ArrayList<Map<Action,List<String>>>(); 
		Map<Action,List<String>> commandToDo = null;
		
		StringTokenizer st=new StringTokenizer(cmdline, " ");
		List<String> actions = null;
		String cmd=null;
		if (st.hasMoreTokens()) cmd = st.nextToken();
		while (st.hasMoreTokens()) {
			commandToDo = new HashMap<Action, List<String>>();
			orderList.add(commandToDo);
//	        System.out.println(cmd);
	        if (cmd.equalsIgnoreCase("rotate")){
	        	actions = new ArrayList<String>();
	        	if (st.hasMoreTokens()) cmd = st.nextToken();
	        	else throw (new CmdException(cmd + " need paramter! \n tape help to get list of command "));
	        	Integer times = Integer.parseInt(cmd);
        		actions.add(times.toString());
        		if (st.hasMoreTokens()) cmd = st.nextToken();
	        	else throw (new CmdException(cmd + " need paramter! \n tape help to get list of command "));
	        	cmd = cmd.toUpperCase();
	        	while (cmd.equalsIgnoreCase("left") || cmd.equalsIgnoreCase("right")){
		        	cmd = cmd.toUpperCase();
		        	actions.add(cmd);
		        	if (st.hasMoreTokens()) cmd = st.nextToken();
		        	else break;	
	        	}
	        	if (commandToDo.keySet().contains(Action.ROTATE)){
	        		commandToDo.get(Action.ROTATE).addAll(actions);
	        	}else{
	        		commandToDo.put(Action.ROTATE, actions);
	        	}
			}
	        else if (cmd.equalsIgnoreCase("flap")){
	        	actions = new ArrayList<String>();
	        	if (st.hasMoreTokens()) cmd = st.nextToken();
	        	else throw (new CmdException(cmd + " need paramter! \n tape help to get list of command "));
	        	try{
	        		Integer times = Integer.parseInt(cmd);
	        		actions.add(times.toString());
	        		if (st.hasMoreTokens()) cmd = st.nextToken();
		        	else throw (new CmdException(cmd + " need paramter! \n tape help to get list of command "));
	        		times = Integer.parseInt(cmd);
	        		actions.add(times.toString());
	        	}catch (NumberFormatException e) {
					throw (new CmdException(cmd + " is incorrect parameter! \n Flap command requiere a number parameter, ex : flap 10"));	
				}	        	
	        	if (commandToDo.keySet().contains(Action.FLAP)){
	        		commandToDo.get(Action.FLAP).addAll(actions);
	        	}else{
	        		commandToDo.put(Action.FLAP, actions);
	        	}
	        	if (st.hasMoreTokens()) cmd = st.nextToken();
	        }
	        else if (cmd.equalsIgnoreCase("heart")){
	        	actions = new ArrayList<String>();
	        	if (st.hasMoreTokens()) cmd = st.nextToken();
	        	else throw (new CmdException(cmd + " need paramter! \n tape help to get list of command "));
	        	Integer times = Integer.parseInt(cmd);
        		actions.add(times.toString());
        		if (st.hasMoreTokens()) cmd = st.nextToken();
	        	else throw (new CmdException(cmd + " need paramter! \n tape help to get list of command "));
	        	cmd = cmd.toUpperCase();
	        	while (cmd.equalsIgnoreCase("on") || cmd.equalsIgnoreCase("off")){
		        	cmd = cmd.toUpperCase();
		        	actions.add(cmd);
		        	if (st.hasMoreTokens()) cmd = st.nextToken();
		        	else break;		
	        	}
	        	
	        	if (commandToDo.keySet().contains(Action.HEART)){
	        		commandToDo.get(Action.HEART).addAll(actions);
	        	}else{
	        		commandToDo.put(Action.HEART, actions);
	        	}
	        }
	        
	        else if(cmd.equalsIgnoreCase("head")){
	        	actions = new ArrayList<String>();
	        	if (st.hasMoreTokens()) cmd = st.nextToken();
	        	else throw (new CmdException(cmd + " need paramter! \n tape help to get list of command "));
	        	Integer times = Integer.parseInt(cmd);
        		actions.add(times.toString());
        		if (st.hasMoreTokens()) cmd = st.nextToken();
	        	else throw (new CmdException(cmd + " need paramter! \n tape help to get list of command "));
	        	cmd = cmd.toUpperCase();
	        	while (cmd.equalsIgnoreCase("NONE") || cmd.equalsIgnoreCase("YELLOW") || 
	        			cmd.equalsIgnoreCase("BLUE")|| cmd.equalsIgnoreCase("WHITE") ||
	        			cmd.equalsIgnoreCase("VIOLET")|| cmd.equalsIgnoreCase("CYAN")|| 
	        			cmd.equalsIgnoreCase("GREEN")|| cmd.equalsIgnoreCase("RED")){
	        		
		        	cmd = cmd.toUpperCase();
		        	actions.add(cmd);
		        	if (st.hasMoreTokens()) cmd = st.nextToken();
		        	else break;	
	        	}
	        	
	        	if (commandToDo.keySet().contains(Action.HEAD)){
	        		commandToDo.get(Action.HEAD).addAll(actions);
	        	}else{
	        		commandToDo.put(Action.HEAD, actions);
	        	}
	        }
	        
	        else throw (new CmdException(cmd  + "is an unknown command"));    
	     }
		
		return orderList;
		
	}

	/**
	 * @see org.apache.felix.shell.Command#getName()
	 */
	public String getName() {
		return "ibuddy";
	}
	
	/**
	 * @see org.apache.felix.shell.Command#getShortDescription()
	 */
	public String getShortDescription() {
		return "command to control ibuddy";
	}

	/**
	 * @see org.apache.felix.shell.Command#getUsage()
	 */
	public String getUsage() {
		return	getName() + " [help]";
	}	

	/**
	 * @see org.apache.felix.shell.Command#getUsage()
	 */
	public String getFullUsage() {
		return			getName() + " config <serial number> <token> : configure the bunny' identifiers"
		+"\n"+	getName() + " play <left ear pos> <right ear pos> <text to speech> [<choregraphy>]: speech a text and play a chroregraphy"
		+"\n"+	getName() + " voice <voice>: set the default voice (eg julie22k,claire22s,caroline22k,bruno22k,graham22s,lucy22s,heather22k,ryan22k,aaron22s,laura22s)"
		+"\n"+	getName() + " voices : list the available voices"
		+"\n"+	getName() + " baseurl <baseurl>: set the default base url of the Nabaztag server or proxy"
		+"\n"+	getName() + " ears : get ears positions"
		+"\n"+	getName() + " raw <url encoded params>: send the URL with raw encoded parameters"
		+"\n"+	getName() + " sleep : sleep the bunny"
		+"\n"+	getName() + " wakeup : wakeup the bunny"
		+"\n"+	getName() + " issleeping : get the status of the bunny"
		+"\n"+	getName() + " whoiam : get the name of the bunny"
		+"\n"+	getName() + " trace <on|off> : set on|off the trace"
		;
	}	
}
