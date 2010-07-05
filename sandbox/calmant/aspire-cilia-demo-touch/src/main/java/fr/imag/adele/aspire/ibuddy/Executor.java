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

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

import liglab.adele.aspire.common.tag.Tag;

import org.apache.felix.shell.ShellService;

import fr.liglab.adele.cilia.Data;
import fr.liglab.adele.cilia.framework.IProcessor;

/**
 * @author Gabriel Pedraza Ferreira
 *
 */
public class Executor implements IProcessor {
   
   private Dictionary<String, String> myCommands;
   
   private ShellService shell;
   
   public void executeCommand(String id) {
      System.err.println("Command to execute... ");
      String aCommand = myCommands.get(id);
      if (aCommand!=null) {
         try {
            shell.executeCommand(aCommand, System.out, System.err);
         } catch (Exception e) {
            e.printStackTrace();
         }
      } else {
         System.out.println("------------------  Command for " + id + " not found");
      }
   }
   
   
   
   
   public void addCommand(String id, String command) {
      myCommands.put(id, command);
   }

   public List<Data> process(final List dataSet) {
      assert dataSet != null;
      List<Data> temp = new ArrayList<Data>(1);
      Data data = (Data) dataSet.get(0);
      if (data != null) {
         List<Tag> original = (List<Tag>) data.getContent();
         for (Tag tag : original) {
            executeCommand(tag.getId());
         }
      } else {
         temp.add(new Data(new ArrayList<Tag>(0)));
      }
      return temp;
   }
   
}
