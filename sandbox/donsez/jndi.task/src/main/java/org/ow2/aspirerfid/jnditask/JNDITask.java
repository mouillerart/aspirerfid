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
package org.ow2.aspirerfid.jnditask;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/**
 * This Ant task query a directory (LDAP or others) to get attributes
 * @author Didier Donsez
 * @see http://java.sun.com/docs/books/tutorial/jndi/
 */
public class JNDITask extends Task {

	/**
	 * Nested element for filter
	 */
	public class Filter {
		
		public Filter(){}
				
		String value;
		
		public void addText(String text) {
			value=text;
		}
	}
	
	/**
	 * Nested element
	 */
	public class Env {
		
		public Env(){}
		
		String name;
		
		String value;

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @param name the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}

		/**
		 * @return the value
		 */
		public String getValue() {
			return value;
		}

		/**
		 * @param value the value to set
		 */
		public void setValue(String value) {
			this.value = value;
		}
		
		/**
		 * @param value the value to set
		 */
		public void addText(String value) {
			this.value = value;
		}
	}
	
	private String dirContextRef;
	
	/**
	 * properties file for address, principal, credential
	 */
	private File propertiesFile;

	/**
	 * properties file for address, principal, credential
	 */
	private List<Env> envs=new ArrayList<Env>();	
	
	/**
	 * Name for searching
	 */
	private String name;

	/**
	 * Filter for searching
	 */
	private String filter = null;

	private Filter _filter = null; 
	
	/**
	 * Attributes to list
	 */
	private String[] attributes = null;
	
	private boolean verbose = false;

	private boolean closeAtEnd = false;

	private String propertiesPrefix = "";

	private static Object dummyContext=new Object();
	
	public void execute() throws BuildException {

		if(_filter!=null){
			if(filter!=null) {
				throw new BuildException("filter attribute and filter element could be not setted together");
			} else {
				filter=_filter.value;
			}
		}
		
		DirContext dirContext = null;

		if (dirContextRef != null) {
			// Get the current directory context
			Object o = getProject().getReference(dirContextRef);
			if(!dummyContext.equals(o)){
				dirContext = (DirContext) o;
			}
		}

		if (dirContext == null) {
			Properties properties = new Properties();
			
			try {
				if (propertiesFile != null) {
					InputStream is = new FileInputStream(propertiesFile);
					properties = new Properties();
					properties.load(is);
				}
			} catch (IOException e) {
				throw new BuildException(e.getMessage());
			}
			
			Iterator<Env> iter=envs.iterator();
			while (iter.hasNext()) {
				JNDITask.Env env = (JNDITask.Env) iter.next();
				properties.put(env.name, env.value);
			}
			
			// Create the initial directory context
			try {
				dirContext = new InitialDirContext(properties);// TODO InitialLdapContext
			} catch (NamingException e) {
				throw new BuildException(e.getMessage());
			} 

			if (dirContext == null) {
				throw new BuildException("Could not create the dircontext");
			} else {
				if (dirContextRef != null) {
					getProject().addReference(dirContextRef, dirContext);
				}
			}
		}

		if (name != null) {

			SearchControls searchControls = null;

			// Perform search
			NamingEnumeration<SearchResult> searchResults;
			try {
				
				searchResults = dirContext.search(name, filter, searchControls);

				int counter = 0;
				Hashtable properties = getProject().getProperties();

				while ((searchResults.hasMore())) {
					SearchResult searchResult = searchResults.next();
					Attributes attributes = searchResult.getAttributes();
					NamingEnumeration<String> idEnumeration = attributes
							.getIDs();

					while (idEnumeration.hasMore()) {
						String id = idEnumeration.next();
						Attribute attribute = attributes.get(id);
						NamingEnumeration ne = attribute.getAll();
						int cpt = 0;
						while (ne.hasMore()) {
							String propvalue = ne.next().toString();
							String propname = propertiesPrefix + id + (ne.hasMore() && cpt==0 ? '.' + (cpt++) : "") ;
							getProject().setProperty(propname, propvalue);
							if (verbose) {
								log(propname + "=" + propvalue);
							}
							counter++;
						}
					}
				}
				log(counter+" properties prefixed by " + propertiesPrefix);

			} catch (NamingException e) {
				throw new BuildException(e.getMessage());
			}

		}

		if (closeAtEnd) {
			// Close the context when we're done
			try {
				dirContext.close();
			} catch (NamingException e) {
				throw new BuildException(e.getMessage());
			} 
			if (dirContextRef != null) {
				getProject().addReference(dirContextRef, dummyContext);
			}
		}
	}
	
	/**
	 * @param env add a env
	 */
	public Filter createFilter() {
		Filter f=new Filter();
		this._filter=f;
		return f;
	}

	/**
	 * @param env add a env
	 */
	public Env createEnv() {
		Env env=new Env();
		this.envs.add(env);
		return env;
	}
	
	/**
	 * @param dirContextRef the dirContextRef to set
	 */
	public void setDirContextRef(String dirContextRef) {
		this.dirContextRef = dirContextRef;
	}


	/**
	 * @param propertiesFile the propertiesFile to set
	 */
	public void setPropertiesFile(File propertiesFile) {
		this.propertiesFile = propertiesFile;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * @param filter the filter to set
	 */
	public void setFilter(String filter) {
		this.filter = filter;
	}

	/**
	 * @param verbose the verbose to set
	 */
	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}


	/**
	 * @param closeAtEnd the closeAtEnd to set
	 */
	public void setCloseAtEnd(boolean closeAtEnd) {
		this.closeAtEnd = closeAtEnd;
	}


	/**
	 * @param propertiesPrefix the propertiesPrefix to set
	 */
	public void setPropertiesPrefix(String propertiesPrefix) {
		this.propertiesPrefix = propertiesPrefix;
	}
	
	
}
