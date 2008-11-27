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

package org.ow2.aspirerfid.deploy.shutdownhooktask;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.BuildListener;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Target;
import org.apache.tools.ant.Task;

/**
 * Creates a JVM shutdown hook executing a target (and dependencies) if shutdown (ie Ctrl-C).
 * @author Didier Donsez
 */
public class ShutdownHookTask extends Task implements Runnable, BuildListener {

	private static int hookCounter=0;
	
	/**
	 * the name of target to execute if shutdown
	 */
    private String targetName;

    /**
     * the message to log if shutdown 
     */
    private String message;
    
    private Target target;
    private Thread hook;

	private boolean isHookRunning=false;
    
    
    /**
     * finished targets (do not included the target finished before the task declaration)
     * <p>the targets set is shared between several shutdownhook task declaration  
     */
    private static Set finishedTargets=new HashSet(); // TODO should be shared in the project scope
    /**
     * started targets (do not included the target started before the task declaration).
     * <p>the targets set is shared between several shutdownhook task declaration  
     */
    private static Set startedTargets=new HashSet(); // TODO should be shared in the project scope

	/**
     * Execute the task.
     * @throws BuildException on error
     */
    public void execute() throws BuildException {
        target=(Target)getProject().getTargets().get(targetName);
        if(target==null) {	        	
        	throw new BuildException("no target "+targetName+" in this project");
        }
        hook=new Thread(this);
        hook.setName("ShutdownHookTask-"+(++hookCounter));
		Runtime.getRuntime().addShutdownHook(hook);
		getProject().addBuildListener(this);
    }    

	/**
	 * @param target the target to set
	 */
	public void setTarget(String target) {
		this.targetName = target;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @param name the name of the task
	 */
	public void setTaskName(String name) {
		super.setTaskName(name);
	}

	
	/**
	 * executes the target and its dependencies (previously) 
	 * @param target the target to execute
	 */
	private void recursiveExecute(Target target){
	    String unlessProperty=target.getUnless();
	    String ifProperty=target.getIf();
	    if(unlessProperty!=null && getProject().getProperty(unlessProperty)!=null) {
	    	return;
	    }
	    if(ifProperty!=null && getProject().getProperty(ifProperty)==null) {
	    	return;
	    }
	    Enumeration enumeration=target.getDependencies();
	    while (enumeration.hasMoreElements()) {
			String tname = (String) enumeration.nextElement();
			Target t=(Target)getProject().getTargets().get(tname);
			if(t==null) {
	        	throw new BuildException("no target "+tname+" in this project");
			}
			if(finishedTargets.contains(t)) {
				// do nothing since already executed
			} else if(startedTargets.contains(t)) {
				log("target" + t.getName() + "was started but not finished : discard this target",Project.MSG_WARN);
			} else {
				recursiveExecute(t);
			}
		}
	    target.execute();
	}
	
	public void run() {
		isHookRunning=true;
	    if(message!=null) log(message);
	    recursiveExecute(target);
	}
	
	public void buildFinished(BuildEvent buildEvent) {
		if(!isHookRunning){
			Runtime.getRuntime().removeShutdownHook(hook);
		}
	}

	public void buildStarted(BuildEvent buildEvent) {
	}

	public void messageLogged(BuildEvent buildEvent) {
	}

	public void targetFinished(BuildEvent buildEvent) {
		synchronized (finishedTargets) {
			finishedTargets.add((Target)buildEvent.getTarget());
		}
	}

	public void targetStarted(BuildEvent buildEvent) {
		synchronized (startedTargets) {
			startedTargets.add((Target)buildEvent.getTarget());
		}
	}

	public void taskFinished(BuildEvent buildEvent) {
	}

	public void taskStarted(BuildEvent buildEvent) {
	}
}
