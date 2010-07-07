/*
   Copyright 2005-2010, OW2 Aspire RFID project 
   
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
package fr.imag.adele.aspire.ibuddy;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.felix.shell.Command;

/**
 * @author Gabriel Pedraza Ferreira
 * 
 */
public class ApplicationExecutor implements Command {

   public void exec(String dir, String[] args) {
      exec(dir, args, null);
   }

   public void exec(String dir, String[] args, Map<String, String> environment) {
      ProcessBuilder pb = new ProcessBuilder(args);
      if (environment != null) {
         Map<String, String> env = pb.environment();
         env.clear();
         for (Map.Entry<String, String> e : environment.entrySet()) {
            env.put(e.getKey(), e.getValue());
         }
      }
      if (dir != null)
         pb.directory(new File(dir));
      try {
         Process p = pb.start();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.apache.felix.shell.Command#execute(java.lang.String, java.io.PrintStream, java.io.PrintStream)
    */
   public void execute(String cmdline, PrintStream out, PrintStream err) {
      StringTokenizer st = new StringTokenizer(cmdline, " ");
      int n = st.countTokens();
      if (n < 2) {
         err.println(getUsage());
         return;
      }

      String[] args = parseCmdLineOrder(cmdline);
      for (int i = 0; i < args.length; i++) {
         System.out.println("Args --------------> " + args[i]);
      }
      exec(null, args);

   }

   private String[] parseCmdLineOrder(String cmdline) {
      StringTokenizer st = new StringTokenizer(cmdline, " ");
      String[] temp = new String[st.countTokens() - 1];
      st.nextToken(); // Ignores the command name      
      int i = 0;
      while (st.hasMoreTokens()) {
         temp[i] = st.nextToken();
         i++;
      }
      return temp;
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.apache.felix.shell.Command#getName()
    */
   public String getName() {
      return "exe-app";
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.apache.felix.shell.Command#getShortDescription()
    */
   public String getShortDescription() {
      return "Execute a external OS process";
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.apache.felix.shell.Command#getUsage()
    */
   public String getUsage() {
      return getName() + " application_path args";
   }

}
