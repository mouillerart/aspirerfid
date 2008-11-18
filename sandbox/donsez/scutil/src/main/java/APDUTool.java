import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.List;
import java.util.ListIterator;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.smartcardio.Card;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CardTerminals;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import javax.smartcardio.TerminalFactory;


/**
 * A small tool to send APDU command to a card.
 *
 * @author  Didier Donsez (didier.donsez@imag.fr)
 **/

public class APDUTool {

	private static final String NAME="apdutool";
	private static final char ENDL='\n';
	private static final String USAGEFILE="usage.txt";
	private static final String ATRBANKFILE="atrbank.txt";
	private static final String ATRBANKURL="http://www-adele.imag.fr/~donsez/pub/apdutool/atrbank.txt";

	//private static boolean CARD_INSERTED=false;
	private static Card card=null;
	private static byte[] lastResponseAPDU=null;
	private static byte[][] atrBank=null; // for future purposes


	public static void main (String [] args) {
		System.out.println ("---------------------------------------------------");
		System.out.println ("APDUTool");
		System.out.println ("Author: Didier DONSEZ <Didier.Donsez@imag.fr>");
		System.out.println ("Licence: LGPL v2.1");
		System.out.println ("---------------------------------------------------");

		BufferedReader in=null;

		boolean mustPrintCommandLine   = false;

		if(args.length==1) {
			try {
				in=new BufferedReader(new FileReader(args[0]));
				mustPrintCommandLine=true;
			} catch (java.io.FileNotFoundException f) {
				System.out.println("File \""+args[0]+"\"not exists");
				printfile(USAGEFILE);
				return;
			}
		} else {
			in=new BufferedReader(new InputStreamReader(System.in));
		}

		// load ATR bank
		// from the local file and from a URL (common repository)

		
		TerminalFactory terminalFactory = TerminalFactory.getDefault();
		CardTerminals cardTerminals = terminalFactory.terminals();
		List<CardTerminal> list = cardTerminals.list();

		System.out.println("List of PC/SC Readers connected:");
		ListIterator i = list.listIterator();
		while (i.hasNext()) {
			System.out.println(getCardTerminalInfo((CardTerminal) i.next()));
		}

		
		try {

			while(true){

				try {
					// read a command line
					System.out.print("\n>"); // prompt
					String line=in.readLine();
					if(line==null) continue;

					// print the line
					if(mustPrintCommandLine) System.out.println(line);

					// split in command and arguments
					StringTokenizer st=new StringTokenizer(line);
					String command=st.nextToken();
					if(command==null) continue;
					String argument=line.substring(command.length());

					// command execution
					if(command.equals("send")) {
						try {
							byte[] cmdapdu=parseHexString(argument);
							sendAPDU(cmdapdu);
						} catch(java.lang.NumberFormatException e){
							System.out.println ("Incorrect APDU format");
						}
					}
					else if(command.equals("card")) {
						getCardID();
						// TODO : match ATR in the atrbank
					}
					else if(command.equals("terminal")) {
						getTerminalInfos();
					}
					else if(command.equals("applets")) {
						getApplets();
					}
					else if(command.equals("select")) {
						try {
							byte[] aid=parseHexString(argument);
							selectApplet(aid);
						} catch(java.lang.NumberFormatException e) {
							System.out.println ("Incorrect AID format");
						}
					}
					else if(command.equals("properties")||command.equals("prop")) {
						getProperties();
					}
					else if(command.equals("quit") || command.equals("exit")) {
						break;
					}
					else if(command.equals("echo")) {
						System.out.println(argument);
					}
					else if(command.equals("last")) {
						if(lastResponseAPDU==null){
							System.out.println();
						} else {
							if(argument.indexOf("HEX")!=-1){
								System.out.println(HexString.hexify(lastResponseAPDU));
							} else {
								System.out.println("Format not implemented");
							}
						}
					}
					else if( command.equals("#") || "#".equals(line.substring(0,1))) {
						// do nothing
						// System.out.println(argument);
					}
					else if(command.equals("waitinsertion")) {
						byte[] atr;
						// parse a ATR
						try {
							atr=parseHexString(argument);
						} catch(java.lang.NumberFormatException e) {
							System.out.println ("Incorrect ATR format");
							break;
						}
						if(atr==null || atr.length==0) atr=null;
						waitInsertion(atr);
						// setOutWait() done after the exception catches
					}
					else if(command.equals("atrbank")) {
	  					System.out.println("Local ATR bank");
						printfile(ATRBANKFILE);
						System.out.println("Remote common ATR bank");
						printurl(ATRBANKURL);
					}
					else if(command.equals("help")||command.equals("?")) {
						printfile(USAGEFILE);
					}
					else {
						System.out.println("unknown command");
					}
				}
				catch (CardTerminalException cte) {
					System.out.println ("CardTerminalException: ");
					System.out.println (cte.getMessage () );
				} catch (CardServiceException cse) {
					System.out.println ("CardServiceException: ");
					System.out.println (cse.getMessage () );
				} catch (Exception e) {
					System.out.println (e.getMessage () );
					e.printStackTrace();
				}
				setOutWait();
			}

		}
		catch (OpenCardPropertyLoadingException plfe) {
			System.out.println ("OpenCardPropertyLoadingException: ");
			System.out.println (plfe.getMessage () );
		}
		catch (ClassNotFoundException cnfe) {
			System.out.println ("ClassNotFoundException: ");
			System.out.println (cnfe.getMessage () );
		}
		catch (CardServiceException cse) {
			System.out.println ("CardServiceException: ");
			System.out.println (cse.getMessage () );
		}
		catch (CardTerminalException cte) {
			System.out.println ("CardTerminalException: ");
			System.out.println (cte.getMessage () );
		}
		catch (Exception e) {
			System.out.println (e.getMessage () );
			e.printStackTrace();
		}

		try{
			SmartCard.shutdown ();
		} catch (CardTerminalException cte) {
			System.out.println ("CardTerminalException: ");
			System.out.println (cte.getMessage () );
		}
	} // main

	static void printurl(String url){
		BufferedReader in;
		String line;
		try {
			in=new BufferedReader(new InputStreamReader((new java.net.URL(url)).openStream()));
		} catch (Exception e) {
			System.err.println("Problem to load \""+url+"\" !");
			return;
		}
		try {
			while((line=in.readLine())!=null) System.out.println(line);
		} catch (java.io.IOException f) {
			System.err.println("IOException while reading file \""+url+"\"");
			return;
		}
	}


	static void printfile(String filename){
		BufferedReader in;
		String line;
		try {
			in=new BufferedReader(new FileReader(filename));
		} catch (java.io.FileNotFoundException f) {
			System.err.println("File \""+filename+"\"not found");
			return;
		}
		try {
			while((line=in.readLine())!=null) System.out.println(line);
		} catch (java.io.IOException f) {
			System.err.println("IOException while reading file \""+filename+"\"");
			return;
		}
	}

	//----------------------------------------------------------------------------
	//

	/**
	 * Parse a Hex string
	 * @param c a symbol
	 * @note : java.lang.Integer.parseInt(String s, int radix) do not verify if symbol is correct !!
	 * @note : opencard.core.util.HexString.parseHexString(String s) requires fixed space in string
	 */
	public static byte[] parseHexString(String hexString)
	throws java.lang.NumberFormatException {

		StringTokenizer st = new StringTokenizer(hexString);
		byte[] result = new byte[st.countTokens() ];
		for(int i=0; st.hasMoreTokens(); i++) {
			//result[i]=(byte)Integer.parseInt(st.nextToken(),16);
			char[] ca=(st.nextToken()).toCharArray();
			if(ca.length!=2) throw new java.lang.NumberFormatException();
			result[i]=(byte)(parseHexChar(ca[0])*16+parseHexChar(ca[1]));
		}
		return result;
	}

	/**
	 * Parse a radix 16 symbol
	 * @param c a symbol
	 * @note : java.lang.Integer.parseInt(String s, int radix) do not verify if symbol is correct !!
	 */
	public static byte parseHexChar(char c)
	throws java.lang.NumberFormatException {
		if((c>='0' && c<='9') || (c>='a' && c<='f') || (c>='A' && c<='F'))
			return (byte)(Character.digit(c,16));
		throw new java.lang.NumberFormatException();

	}

	/**
	 * Print utility for APDU command
	 * @param apdu a APDU
	 */
	public static void printAPDU(APDU apdu) {
		System.out.println("response:\n"+HexString.hexify(apdu.getBytes()));
	}

	//----------------------------------------------------------------------------
	/**
	 * Get Applets installed in the card
	 */
	public static void getApplets()
	throws opencard.core.terminal.CardTerminalException {

		AppletInfo [] templates = null;
		/*
		try {
		AppletAccessCardService aacs
		= (AppletAccessCardService) card.getCardService(
		 opencard.opt.emv.mgmt.BasicEMVAppletAccess,
		 true);
		templates = aacs.list();
		for(int i=0;i<templates.length;i++){
		AppletID aid = templates[0].getAppletID();
		// CardFilePath filePath = new CardFilePath(aid.toString());
		System.out.println(i+":"+HexString.hexify(aid.getBytes()));
	}
	} catch (Exception e) {
		System.out.println("Problem in applets command");
		e.printStackTrace();
	}
		*/
	}


	//----------------------------------------------------------------------------
	/**
	 * Get Applets installed in the card
	 */
	public static void selectApplet(byte[] _aid)
	throws opencard.core.terminal.CardTerminalException {

		AppletInfo [] templates = null;
		try {
			AppletID aid = new AppletID(_aid);
			/*
			To complete

			 Class opencard.opt.applet.mgmt.AbstractAppletAccessor
			 AppletInfo selectApplet(CardChannel channel, AppletID appletID)
			   Selects the card applet with the given application ID using the given channel for communication with the card.

			*/
			System.out.println("Applet "+HexString.hexify(aid.getBytes())+" selected");
		} catch (Exception e) {
			System.out.println("Problem in select command");
			e.printStackTrace();
		}
	}

	//----------------------------------------------------------------------------
	/**
	 * Send an APDU command to the card and print the response APDU
	 */
	public static void sendAPDU(byte[] apdu)
	throws opencard.core.terminal.CardTerminalException {
		if (card!=null && cardserv != null) {
			CommandAPDU cmd = new CommandAPDU(apdu);
			ResponseAPDU resp = cardserv.sendCommandAPDU(cmd);
			printAPDU(resp);
			lastResponseAPDU=resp.getBytes();
		} else {
			System.out.println("Card must be inserted");
		}
	}

	//----------------------------------------------------------------------------


	/**
	 * Get the properties relitive to opencard
	 */
	public static void getProperties(){
		Properties props = System.getProperties ();
		for (Enumeration e = props.keys() ; e.hasMoreElements() ;) {
			String k=(String)(e.nextElement());
			if(k.startsWith("opencard.")) {
				System.out.println(k+"="+props.getProperty(k));
			}
		}
	}


	//----------------------------------------------------------------------------

	/**
	 * Get the information on the inserted card
	 * Source code from GetCardID in the OCF demos
	 */

	public static void getCardID(){
		if (card != null) {
			CardID cardID = card.getCardID ();
			printCardID (cardID);
		} else {
			System.out.println("Card must be inserted");
		}
	}
	/**
	* Prints out the information of the <TT>CardID</TT> object passed.
	*/
	public static void printCardID (CardID cardID) {
		StringBuffer sb = new StringBuffer("Obtained the following CardID:\n\n");

		byte [] atr = cardID.getATR ();
		sb.append (HexString.hexify (atr) ).append ('\n');

		appendHistoricals (sb, cardID);

		/* This should be completed someday...
		 * the output seems to have problems with default values,
		 * that's the main reason why it's disabled
		appendTS (sb, atr[0]);
		appendT0 (sb, atr[1]);

		int i = 2;

		if ((atr[0]&0x10) != 0) {
		  appendClockrate(sb, atr[i]);
		  appendBitAdjust(sb, atr[i]);
		  i++;
	}

		if ((atr[0]&0x20) != 0) {
		  appendProgCurr(sb, atr[i]);
	}
		* end of disabled output
		*/

		System.out.println(sb);
	} // printCardID


	private static void appendHistoricals(StringBuffer sb, CardID cardID) {
		byte[] hist = cardID.getHistoricals();

		sb.append("Historicals: ");
		if (hist == null) {
			sb.append("none\n");
		}
		else {
			sb.append(HexString.hexify(hist)).append('\n');
			sb.append("as a string: ");
			for(int i=0; i<hist.length; i++)
				sb.append((hist[i]<32)? // signed byte extension!
				          ' ' : (char)hist[i]);
			sb.append('\n');
		}
	}


	private static void appendTS(StringBuffer sb, byte ts) {
		sb.append("TS = ").append(HexString.hexify(ts)).append("    ");
		sb.append((ts==(byte)0x3b) ? "direct" : "inverse").append(" convention\n");
	}

	private static void appendT0(StringBuffer sb, byte t0) {
		sb.append("TS = ").append(HexString.hexify(t0)).append("    ");
		binify(sb, t0);
		sb.append('\n');
	}

	private static void appendClockrate(StringBuffer sb, byte cr) {
		// why is the output always "???" ?
		double[] mhz  = { -1.0, 5.0, 6.0, 8.0, 12.0, 16.0, 20.0, -1.0,
		                  5.0, 7.5, 10.0, 15.0, 20.0, -1.0, -1.0, -1.0 };
		int[] factors = { -2, 372, 558, 744, 1116, 1488, 1860, -1,
		                  512, 768, 1024, 1536, 2048, -1, -1, -1 };

		int fi = (cr >> 4) & 0xf;

		double speed =   mhz  [fi];
		int   factor = factors[fi];

		sb.append("Clock speed ");
		if (speed < 0)
			sb.append("???");
		else
			sb.append(speed);

		sb.append(" MHz, divided by ");
		if (factor < 0)
			sb.append("???");
		else
			sb.append(factor);
		sb.append('\n');
	}

	private static void appendBitAdjust(StringBuffer sb, byte b) {
		// why is the output always "???" ?
		double[] bra = { -1.0, 1.0, 2.0, 4.0, 8.0, 16.0, 32.0, -1.0,
		                 12.0, 20.0, 1.0/2, 1.0/4, 1.0/8, 1.0/16, 1.0/32, 1.0/64 };
		int di = b & 0xf;

		sb.append("bit rate adjustment ");
		if (bra[di] < 0)
			sb.append("???");
		else
			sb.append(bra[di]);
		sb.append('\n');
	}

	private static void appendProgCurr(StringBuffer sb, byte b) {
		// why is the output always "???" ?
		int[] mpg = { 25, 50, 100, -1 };
		int ii = (b >> 5) & 3;

		sb.append("max prog current ");
		if (b < 0)
			sb.append("???");
		else
			sb.append(mpg[ii]).append(" mA");
		sb.append('\n');
	}

	private static void binify(StringBuffer sb, byte b) {
		for(int i=0; i<8; i++) {
			sb.append((b<0) ? '1' : '0');
			b <<= 1;
		}
	}

	//----------------------------------------------------------------------------

	/**
	 * use to indicate to cardRemoved() if the main thread process the waitinsertion command
	 */

	private static boolean inWait=false;

	private static void setInWait() {
		inWait=true;
	}

	private static void setOutWait() {
		inWait=false;
	}

	private static boolean isInWait() {
		return inWait;
	}


	/**
	 * wait for a card insertion
	 */

	public static boolean cardInsertion(SmartCard _card, byte[] ATRtoWait)
	throws opencard.core.terminal.CardTerminalException   {
		if (_card != null){
			byte[] atr=_card.getCardID().getATR();
			System.out.println("ATR:"+HexString.hexify(atr));

			if(ATRtoWait!=null && !java.util.Arrays.equals(ATRtoWait,atr))
				return false;

			card=_card;
			//CARD_INSERTED=true;
			try {
				cardserv = (PassThruCardService)card.getCardService(PassThruCardService.class, true);
				if(cardserv!=null) return true;
			} catch (java.lang.ClassNotFoundException cnfe) {
				card=null;
				System.out.println ("did not get PassThruCardService");
			} catch (opencard.core.service.CardServiceException cse) {
				card=null;
				System.out.println ("did not get PassThruCardService");
			}
		} else // hm, we have a problem
			System.out.println ("did NOT get a SmartCard");
		return false;
	}

	/**
	 * process the command waitinsertion
	 */
	public static void waitInsertion(byte[] atr)
	throws opencard.core.terminal.CardTerminalException   {

		// if an ATR is required, test if the inserted card matches this ATR
		if(atr!=null && card!=null) {
			if(!java.util.Arrays.equals(card.getCardID().getATR(),atr)) {
				card=null;
				cardserv=null;
			}
		}

		// wait for a card that matches the ATR
		while(card==null) {
			if(atr==null) {
				System.out.println("Insert a card ...");
			} else {
				System.out.println("Insert a card with ATR="+HexString.hexify(atr)+" ...");
			}
			setInWait();
			cardInsertion(
			        SmartCard.waitForCard (
			                new CardRequest (CardRequest.ANYCARD,null,null)
			        ), atr
			);
			if(card==null) {
				try{
					Thread.sleep(1000);
				} catch(java.lang.InterruptedException e) {
				}
			}
		}
	}
	
	static String getCardTerminalInfo(CardTerminal cardTerminal) {
		StringBuffer sb=new StringBuffer();
		sb.append("CardTerminal[name=");
		sb.append(cardTerminal.getName());
		sb.append(";isCardPresent=");
		try {
			sb.append(cardTerminal.isCardPresent());
		} catch (CardException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sb.append("]");
		return sb.toString();
	}

	
}