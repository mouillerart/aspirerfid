package org.ow2.aspirerfid.example.myxmpp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.ow2.aspirerfid.xmpp.xmppclient.XMPPClientService;


public class XmppClient implements PacketListener,ConnectionListener{

	XMPPClientService m_xmpp;

	public void begin() {
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String cmdLine = null;

		while (true) {
			try {
				System.out.print(">xmpp ");
				cmdLine = br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (cmdLine == null || cmdLine.length() == 0) {
				System.out.println("Please enter your message");
			} else if (cmdLine.equalsIgnoreCase("bye")
					|| cmdLine.equalsIgnoreCase("exit")) {
				break;
			} else {
				try {
					m_xmpp.addMessage(cmdLine);
					m_xmpp.sendMessage(cmdLine);
					m_xmpp.addPacketListener(this, null);

				} catch (XMPPException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}
		this.end();


	}

	public void end() {

		m_xmpp.removePacketListener(this);
		m_xmpp.addConnectionListener(this);

	}

	public void processPacket(Packet packet) {
		Message m = (Message) packet;
		String current = m.getBody();

		System.out.println("recu : " + current);
		// TODO Auto-generated method stub
		//System.out.println("Client:"+packet.getFrom()+":"+packet.toString());

	}

	public void connectionClosed() {
		// TODO Auto-generated method stub

	}

	public void connectionClosedOnError(Exception arg0) {
		// TODO Auto-generated method stub

	}

	public void reconnectingIn(int arg0) {
		// TODO Auto-generated method stub

	}

	public void reconnectionFailed(Exception arg0) {
		// TODO Auto-generated method stub

	}

	public void reconnectionSuccessful() {
		// TODO Auto-generated method stub

	}

}
