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

import java.io.PrintStream;

import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;

public class JNDIUtil {
	public static void listContext(PrintStream out, PrintStream err, DirContext dirCtx) {
		try {
			// listing the context
			out.println("dircontext: " + dirCtx.getNameInNamespace());

			NamingEnumeration list = dirCtx.list("");

			// Go through each item in list
			while (list.hasMore()) {
				NameClassPair nc = (NameClassPair) list.next();
				out.println("nc=" + nc);
			}
			out.println();

		} catch (NamingException e) {
			err.println("Problem getting dn: " + e);
		}
	}

	public static void listAttributes(PrintStream out, PrintStream err, Attributes attrs) {
		try {
			for (NamingEnumeration ae = attrs.getAll(); ae.hasMore();) {
				Attribute attr = (Attribute) ae.next();
				out.println("attribute: " + attr.getID());
				/* Print each value */
				for (NamingEnumeration e = attr.getAll(); e.hasMore();) {
					out.println("value: " + e.next());
				}
				out.println();
			}
		} catch (NamingException e) {
			err.println("Problem getting attribute: " + e);
		}
	}
}
