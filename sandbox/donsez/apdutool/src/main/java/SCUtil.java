import java.util.List;
import java.util.ListIterator;

import javax.smartcardio.ATR;
import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CardTerminals;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import javax.smartcardio.TerminalFactory;

public class SCUtil {
	private static byte[] SELECT = { (byte) 0x00, (byte) 0xA4, (byte) 0x04,
			(byte) 0x00, (byte) 0x09 };

	private static byte[] INS_INC = { (byte) 0x00, (byte) 0x01, (byte) 0x00,
			(byte) 0x00 };

	private static byte[] INS_DEC = { (byte) 0x00, (byte) 0x02, (byte) 0x00,
			(byte) 0x00 };

	private static byte[] INS_READ = { (byte) 0x00, (byte) 0x03, (byte) 0x00,
			(byte) 0x00, (byte) 0x00 };

	private static CommandAPDU SELECT_APDU = new CommandAPDU(SELECT);

	private static CommandAPDU INC_APDU = new CommandAPDU(INS_INC);

	private static CommandAPDU DEC_APDU = new CommandAPDU(INS_DEC);

	private static CommandAPDU READ_APDU = new CommandAPDU(INS_READ);

	
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
	
	
	/**
	 * @param args
	 *            the command line arguments
	 * @throws CardException 
	 */
	public static void main(String[] args) throws CardException {
		TerminalFactory terminalFactory = TerminalFactory.getDefault();
		CardTerminals cardTerminals = terminalFactory.terminals();
		List<CardTerminal> list = cardTerminals.list();

		System.out.println("List of PC/SC Readers connected:");
		ListIterator i = list.listIterator();
		while (i.hasNext()) {
			System.out.println(getCardTerminalInfo((CardTerminal) i.next()));
		}
		
		CardTerminal cardTerminal = list.get(0);
		//CardTerminal cardTerminal = cardTerminals.getTerminal("O2 O2Micro CCID SC Reader 0");
		if(cardTerminal==null) {
			System.out.println("No Terminal with this name");			
			return;
		} else {
			System.out.println("Using "+cardTerminal.getName());			
		}
		
		try {
			if(!cardTerminal.isCardPresent()) {
				System.out.println("Insert the card before 10 seconds");			
				cardTerminal.waitForCardPresent(10000);				
			}
			if(!cardTerminal.isCardPresent()) {
				System.out.println("No card. Exiting ...");			
				return;
			}
			
			Card card = null;
			try {
				card = cardTerminal.connect("T=1");
				System.out.println("Terminal connected");
			} catch (Exception e) {
				System.out.println("Terminal NOT connected: " + e.toString());
			}
			
			System.out.println("ATR: " + arrayToHex(((ATR) card.getATR()).getBytes()));

			CardChannel ch = card.getBasicChannel();
			byte[] x = null;
			if (check9000(ch.transmit(SELECT_APDU))) {
				System.out.println("SELECT OKAY");
			} else {
				System.out.println("SELECT NOT OKAY");
				return;
			}
			
			ResponseAPDU ra = ch.transmit(READ_APDU);
			if (check9000(ra)) {
				System.out.println("Value: " + ra.getBytes()[0]);
			} else {
				System.out.println("Error Reading Value");
			}
			return;
		} catch (CardException e) {
			System.out.println("Error isCardPresent()" + e.toString());
		}
	}

	public static boolean check9000(ResponseAPDU ra) {
		//byte[] response = ra.getBytes();
		//return (response[response.length - 2] == (byte) 0x90 && response[response.length - 1] == 0);
		return (ra.getSW()==0x9000); 
	}

	public static String arrayToHex(byte[] data) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < data.length; i++) {
			String bs = Integer.toHexString(data[i] & 0xFF);
			if (bs.length() == 1) {
				sb.append(0);
			}
			sb.append(bs);
		}
		return sb.toString();
	}
}

/*
 * 
 *  // show the list of available terminals TerminalFactory factory =
 * TerminalFactory.getDefault(); List<CardTerminal> terminals =
 * factory.terminals().list(); System.out.println("Terminals: " + terminals); //
 * get the first terminal CardTerminal terminal = terminals.get(0); // establish
 * a connection with the card Card card = terminal.connect("T=0");
 * System.out.println("card: " + card);
 * CardChannel channel =
 * card.getBasicChannel(); ResponseAPDU r = channel.transmit(new
 * CommandAPDU(c1)); System.out.println("response: " + toString(r.getBytes())); //
 * disconnect card.disconnect(false);
 * 
 * 
 */

/*
card_status = card0.status
pp card_status
puts "ATR Length: #{card_status[:atr].length}\n"

begin
  puts "IFD vendor: #{card0.get_attribute Smartcard::PCSC::ATTR_VENDOR_IFD_VERSION}\n"
rescue RuntimeError => e
  puts "Card.get_attribute threw exception #{e}\n"
end

puts "Selecting applet\n"
aid = [0x19, 0x83, 0x12, 0x29, 0x10, 0xba, 0xbe]
select_apdu = [0x00, 0xA4, 0x04, 0x00, aid.length, aid].flatten
send_ioreq = {Smartcard::PCSC::PROTOCOL_T0 => Smartcard::PCSC::IOREQUEST_T0,
              Smartcard::PCSC::PROTOCOL_T1 => Smartcard::PCSC::IOREQUEST_T1}[card_status[:protocol]]
recv_ioreq = Smartcard::PCSC::IoRequest.new
select_response = card0.transmit(select_apdu.map {|byte| byte.chr}.join(''), send_ioreq, recv_ioreq)
select_response_str = (0...select_response.length).map { |i| ' %02x' % select_response[i].to_i }.join('')
puts "Response:#{select_response_str}\n"

begin
  # This only works with GemPlus readers... any other suggestions?
  puts "Testing low-level control\n"
  ctl_string = [0x82, 0x01, 0x07, 0x00].map {|byte| byte.chr}.join('')
  ctl_response = card0.control 2049, ctl_string, 4
  pp ctl_response
rescue RuntimeError => e
  puts "Card.control threw exception #{e}\n"
end

puts "Disconnecting and cleaning up\n"
card0.disconnect Smartcard::PCSC::DISPOSITION_LEAVE
context.release

*/