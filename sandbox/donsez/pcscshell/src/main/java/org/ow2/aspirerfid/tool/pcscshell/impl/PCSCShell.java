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
package org.ow2.aspirerfid.tool.pcscshell.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.ListIterator;
import java.util.StringTokenizer;

import javax.smartcardio.ATR;
import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CardTerminals;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import javax.smartcardio.TerminalFactory;

import com.sun.rmi.rmid.ExecOptionPermission;

/**
 * This class provides a simple utility to send/receive APDU with a smartcard
 * connected to a PC/SC smartcard reader.
 * 
 * @author Didier Donsez (didier.donsez@imag.fr)
 *         <p>
 *         This version uses the Java Platform 6 javax.smartcardio API.
 *         <p>
 *         The previous version uses the non-maintained OpenCard Framework (OCF)
 *         API.
 */

public class PCSCShell {

	private static final String NAME="pcsctool";
	private static final char ENDL='\n';
	private static final String USAGEFILE="usage.txt";

	private static final String ATRBANKRESOURCE="/atrbank.txt";
	private static final String ATRBANKLOCALFILE="atrbank.txt";
	private static final String ATRBANKURL="http://ludovic.rousseau.free.fr/softwares/pcsc-tools/smartcard_list.txt";
	private static final Object PROMPT = "\n>";

	private TerminalFactory terminalFactory = null;
	private CardTerminals cardTerminals=null;
	private CardTerminal cardTerminal=null;
	private String protocol="*"; // "T=0", "T=1", or "T=CL"
	private Card card=null;
	private CardChannel cardChannel=null;
	private ATR cardATR=null;
	
	private byte[] lastResponse=null;

	private ATRBank atrBank=new ATRBank();
	
	private static PrintStream out=System.out;
	private static PrintStream err=System.err;

	private static boolean mustPrintCommandLine=false;

	
	private static void printBanner(){
		out.println ("---------------------------------------------------------------");
		out.println ("PCSCShell");
		out.println ("Author: Didier DONSEZ <X.Y@imag.fr> where X=Didier and Y=Donsez");
		out.println ("Licence: LGPL v2.1");
		out.println ("---------------------------------------------------------------");
	}
	
	public static void main (String [] args) {
		
		printBanner();
		BufferedReader in=null;

		if(args.length==1) {
			try {
				in=new BufferedReader(new FileReader(args[0]));
				mustPrintCommandLine=true;
			} catch (java.io.FileNotFoundException f) {
				out.println("File \""+args[0]+"\"not exists");
				printFile(USAGEFILE);
				return;
			}
		} else {
			in=new BufferedReader(new InputStreamReader(System.in));
		}

		loop(in);
		
	}
	
	public PCSCShell(){
		terminalFactory = TerminalFactory.getDefault();
		cardTerminals = terminalFactory.terminals();		
	}
	
	/**
	 * load ATR bank from a URL (common repository)
	 */
	public void loadATRBank(URL url){
		try {
			atrBank.load(new InputStreamReader(url.openStream()));
		} catch (IOException e) {
			err.println("Can't load the ATR Bank:"+ e.getLocalizedMessage());
		}
	}
	
	/**
	 * load ATR bank with the local file
	 */
	public void loadATRBank(File file){
		try {
			atrBank.load(new FileReader(file));
		} catch (IOException e) {
			err.println("Can't load the ATR Bank:"+ e.getLocalizedMessage());
		}
	}

	/**
	 * load ATR bank with the ressource
	 */
	public void loadATRBank(String path){
		try {
			 InputStream is=PCSCShell.class.getClassLoader().getResourceAsStream(path);
			 if(is!=null) atrBank.load(new InputStreamReader(is));
			 else err.println("Can't load the ATR Bank from the ressource " + path);
		} catch (IOException e) {
			 err.println("Can't load the ATR Bank:"+ e.getLocalizedMessage());
		}
	}
		
	
	public void execute(String line, PrintStream out, PrintStream err) throws CardException {
		// split in command and arguments
		StringTokenizer st=new StringTokenizer(line);
		String command=st.nextToken();
		if(command==null) return;
		String argument=line.substring(command.length());

		// command execution
		if(command.equals("send") || command.equals(">") || command.equals("-->")) {
			byte[] cmdapdu=null;
			try {
				cmdapdu=HexString.parse(argument," \t");
			} catch(java.lang.NumberFormatException e){
				err.println ("Incorrect APDU format");
				return;
			}
			ResponseAPDU responseAPDU =cardChannel.transmit(new CommandAPDU(cmdapdu));
			if (check9000(responseAPDU)) {
				out.println("Response OK: "+HexString.hexify(responseAPDU.getBytes()," "));
			} else {
				out.println("Response KO: "+HexString.hexify(responseAPDU.getBytes()," "));
			}
			
			lastResponse=responseAPDU.getBytes();
		}
		else if(command.equals("card")) {
			// getCardID();
			// TODO : match ATR in the atrbank
		}
		else if(command.equals("exec") || command.equals("run") || command.equals("batch")) {
			// exec <url>;
			// TODO : match ATR in the atrbank
		}
		else if(command.equals("atr")) {
			printATR(out);
		}
		else if(command.equals("atrbankload")) {
			// TODO;
			try {
				atrBank.load(new InputStreamReader((new URL(line.substring(line.lastIndexOf(" "))).openStream())));
			} catch (IOException e) {
				err.println("Can't load the ATR Bank:"+ e.getLocalizedMessage());
			}
		}
		else if(command.equals("protocol")) {
			int b=line.indexOf("\"");
			int e=line.lastIndexOf("\"");
			if(!(b>0 && e>0 && e>b)) {
				err.println("Incorrect format for the protocol");
				if(protocol!=null) err.println("Current protocol is " + protocol);
				return;							
			}
			protocol=line.substring(b+1, e);											
		}
		else if(command.equals("terminal")) {
			int b=line.indexOf("\"");
			int e=line.lastIndexOf("\"");
			if(!(b>0 && e>0 && e>b)) {
				err.println("Incorrect format for the terminal name");
				if(cardTerminal!=null) err.println("Current terminal is " + cardTerminal.getName());
				return;							
			}
			String name=line.substring(b+1, e);						
			
			CardTerminal _cardTerminal = cardTerminals.getTerminal(name);
			if(_cardTerminal==null) {
				err.println("No Terminal with this name");			
				return;
			} else {
				out.println("Using "+_cardTerminal.getName());			
				cardTerminal=_cardTerminal;
			}
		}
		else if(command.equals("terminals")) {
			cardTerminals = terminalFactory.terminals();
			List<CardTerminal> list = cardTerminals.list();
			ListIterator i = list.listIterator();
			while (i.hasNext()) {
				out.println(getCardTerminalInfo((CardTerminal) i.next()));
			}
		}
		else if(command.equals("last")) {
			if(lastResponse==null){
				out.println();
			} else {
				if(argument.indexOf("HEX")!=-1){
					out.println(HexString.hexify(lastResponse," ",16));
				} else if(argument.indexOf("CHAR")!=-1){
						out.println(HexString.hexifyWithAscii(lastResponse," ",16));
				} else {
					out.println("Format not implemented");
				}
			}
		} else if(command.equals("close")) {
			close();
		}
		else if(command.equals("echo")) {
			out.println(argument);
		}
		else if( command.equals("#") || "#".equals(line.substring(0,1))) {
			// do nothing
			// out.println(argument);
		}
		else if(command.equals("waitinsertion")) {
			byte[] requiredATR=null;
			try {
				requiredATR=HexString.parse(argument, " \t");
				if(requiredATR!=null && requiredATR.length==0) requiredATR=null;
				waitInsertion(requiredATR);
			} catch(java.lang.NumberFormatException e) {
				err.println ("Incorrect ATR format");
			}
		}
		else if(command.equals("atrbank")) {
				out.println("Local ATR bank");
			printFile(ATRBANKLOCALFILE);
			out.println("Remote common ATR bank");
			printUrl(ATRBANKURL);
		}
		else if(command.equals("help")||command.equals("?")) {
			printFile(USAGEFILE);
		}
// else if(command.equals("applets")) {
// //getApplets();
// }
// else if(command.equals("select")) {
// byte[] x = null;
// if (check9000(ch.transmit(SELECT_APDU))) {
// out.println("SELECT OKAY");
// } else {
// out.println("SELECT NOT OKAY");
// return;
// }
// }
		else {
			err.println("unknown command");
		}
	}
	
	private static void loop(BufferedReader in){
		
		PCSCShell pcscShell=new PCSCShell();
		
		try {
			pcscShell.loadATRBank(ATRBANKRESOURCE);
		} catch (Exception e) {
			err.println("ATRBank loading error for "+ATRBANKRESOURCE + " : "+e.getLocalizedMessage());
		}
		try {
			pcscShell.loadATRBank(new URL(ATRBANKURL));
		} catch (Exception e) {
			err.println("ATRBank loading error for "+ATRBANKURL + " : "+e.getLocalizedMessage());
		}
		try {
			pcscShell.loadATRBank(new File(ATRBANKLOCALFILE));
		} catch (Exception e) {
			err.println("ATRBank loading error for "+ATRBANKLOCALFILE + " : "+e.getLocalizedMessage());
		}
		
		while(true){

			try {
				out.print(PROMPT);

				// read a command line
				String line=in.readLine();
				if(line==null || line.length()==0) continue;

				// print the line
				if(mustPrintCommandLine) out.println(line);

				if(line.startsWith("quit") || line.startsWith("exit")) {
					pcscShell.close();
					break;
				}
				pcscShell.execute(line,out,err);
				
			} catch (Exception e) {
				e.printStackTrace(err);
			}
		}
	}
	
	
	public void close() throws CardException {
		// if(cardChannel!=null) {cardChannel.close(); cardChannel=null; }
		if(card!=null) {card.disconnect(false); card=null; }
		if(cardTerminal!=null) {cardTerminal=null; }
	}

	public static boolean check9000(ResponseAPDU responseAPDU) {
		return (responseAPDU.getSW()==0x9000); 
	}

	static void printUrl(String url){
		BufferedReader in;
		String line;
		try {
			in=new BufferedReader(new InputStreamReader((new java.net.URL(url)).openStream()));
		} catch (Exception e) {
			System.err.println("Problem to load \""+url+"\" !");
			return;
		}
		try {
			while((line=in.readLine())!=null) out.println(line);
		} catch (java.io.IOException f) {
			System.err.println("IOException while reading file \""+url+"\"");
			return;
		}
	}

	static void printFile(String filename){
		BufferedReader in;
		String line;
		try {
			in=new BufferedReader(new FileReader(filename));
		} catch (java.io.FileNotFoundException f) {
			err.println("File \""+filename+"\"not found");
			return;
		}
		try {
			while((line=in.readLine())!=null) out.println(line);
		} catch (java.io.IOException f) {
			err.println("IOException while reading file \""+filename+"\"");
			return;
		}
	}

	static void printRessource(String ressource){
		BufferedReader in;
		String line;
		try {
			
			in=new BufferedReader(new InputStreamReader(PCSCShell.class.getClassLoader().getResource(ressource).openStream()));
		} catch (java.io.IOException ioe) {
			err.println("Ressource \""+ressource+"\" not found");
			return;
		}
		try {
			while((line=in.readLine())!=null) out.println(line);
		} catch (java.io.IOException e) {
			err.println("IOException while reading file \""+ressource+"\"");
			return;
		}

	}	
	
	public void waitInsertion(byte[] requiredATR) throws CardException {
		waitInsertion(requiredATR, 10000);
	}

	/**
	 * process the command waitinsertion
	 * 
	 * @throws CardException
	 */
	public void waitInsertion(byte[] requiredATR, long timeout) throws CardException {
		
		if(!cardTerminal.isCardPresent()) {
			out.println("Insert the card before "+timeout/1000+" seconds");			
			cardTerminal.waitForCardPresent(timeout);				
		}
		if(!cardTerminal.isCardPresent()) {
			out.println("No card inserted.");			
			return;
		}
		
		try {
			card = cardTerminal.connect(protocol);
			out.println("Terminal connected");
		} catch (Exception e) {
			err.println("Terminal NOT connected: " + e.toString());
			return;
		}
				
		cardATR=card.getATR();
		lastResponse=card.getATR().getBytes();
		printATR(out);
		
		// if an ATR is required, test if the inserted card matches this ATR
		if(requiredATR!=null && cardATR!=null) {
			if(!java.util.Arrays.equals(cardATR.getBytes(),requiredATR)) {
				card=null;
				return;
			}
		}		
		cardChannel = card.getBasicChannel();
	}
	
	private void printATR(PrintStream out) {
		if(cardATR!=null){
			out.println("ATR: " + HexString.hexify(cardATR.getBytes(), " "));
			String description=atrBank.getDescription(cardATR.getBytes());
			if(description!=null) out.println(description);
			
		} else {
			out.println("ATR: none");			
		}
	}

	private String getCardTerminalInfo(CardTerminal cardTerminal) {
		StringBuffer sb=new StringBuffer();
		sb.append("CardTerminal[name=");
		sb.append(cardTerminal.getName());
		sb.append(";isCardPresent=");
		try {
			sb.append(cardTerminal.isCardPresent());
		} catch (CardException e) {
			e.printStackTrace(err);
		}
		sb.append("]");
		return sb.toString();
	}	
}