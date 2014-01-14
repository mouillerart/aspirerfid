package org.ow2.aspirerfid.xmpp.xmppclient.impl;

/**
 *
 */

import java.util.ArrayList;
import java.util.Iterator;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.ow2.aspirerfid.xmpp.xmppclient.XMPPClientService;

public class XMPPClientServiceImpl implements XMPPClientService {

	/**
	 * Noms du serveur, de l'utilisateur, plus le port
	 */

	private String server = "talk.google.com";
	private int port = 5222;
	private String userName = "my.xmpp.server";
	private String password = "12011986";
	private String receiver = "benothmane.amel@gmail.com";

	/**
	 * Connexion XMPP
	 */
	private XMPPConnection conn;

	/**
	 * Stockage des messages
	 */
	private ArrayList<String> log;
	// private Packet packet;
	private boolean isConnected = false;

	public XMPPClientServiceImpl() {

		this.log = new ArrayList<String>(20);

	}

	public void start() {

		try {
			// connect to gtalk server

			ConnectionConfiguration config = new ConnectionConfiguration(
					this.server, this.port, "gmail.com");

			this.conn = new XMPPConnection(config);

			this.conn.connect();

			// login with username and password
			this.conn.login(this.userName, this.password);

			// this.conn.addPacketListener(this, null );
			conn.addConnectionListener(null);

			// set presence status info
			Presence presence = new Presence(Presence.Type.available);

			conn.sendPacket(presence);

			this.isConnected = true;

		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			System.out.println("Impossible de se connecter au serveur XMPP");
			this.isConnected = false;
		}

	}

	public void stop() {
		this.conn.disconnect();
		// this.conn.removePacketListener(this);
		// this.conn.removeConnectionListener(this);

	}

	public void addPacketListener(PacketListener pl, PacketFilter pf) {
		this.conn.addPacketListener(pl, pf);

	}

	public void addConnectionListener(ConnectionListener cl) {
		this.conn.addConnectionListener(cl);
	}

	public void removePacketListener(PacketListener pl) {
		this.conn.removePacketListener(pl);

	}

	public void removeConnectionListener(ConnectionListener cl) {
		this.conn.removeConnectionListener(cl);

	}

	// send a message to somebody

	public void sendMessage(String message) throws XMPPException {
		Iterator<String> it = this.log.iterator();
		String result = "";
		Message msg = null;

		// On verifie l'etat de la connexion
		if (!this.conn.isConnected()) {
			this.conn.connect();
		}

		// On envoie chaque valeur au serveur
		while (it.hasNext()) {
			result = it.next();
			msg = new Message(this.receiver);
			msg.setBody(result);
			this.conn.sendPacket(msg);
		}

		this.log.removeAll(this.log);
	}

	public void addMessage(final String message) {
		this.log.add(message);
	}

	/*
	 * public void sendMessage(String message) throws XMPPException {
	 * conn.sendPacket(new Message(message)); }
	 *
	 * public void sendMessage(String message,String to) throws XMPPException {
	 * Chat chat=conn.getChatManager().createChat(this.receiver,this);
	 * chat.sendMessage(message); }
	 *
	 *
	 *
	 * public void processMessage(Chat chat, Message message) { if
	 * (message.getType() == Message.Type.chat)
	 * System.out.println(chat.getParticipant() + " says: " +
	 * message.getBody());
	 *
	 * }
	 */

	public void processPacket(Packet packet) {
		Message m = (Message) packet;
		String current = m.getBody();

		System.out.println("recu : " + current);
		// System.out.println("Server:"+packet.getFrom()+":"+packet.toXML());
		/*
		 * if (packet instanceof Message){ Message msg=(Message) packet;
		 * System.out.println(msg.getFrom()+":"+msg.getBody()); }
		 */

	}

	public void connectionClosed() {
		System.out.println("CONNECTION: close");

	}

	public void connectionClosedOnError(Exception e) {
		System.out.println("RECONNECTION: exception " + e.getMessage());

	}

	public void reconnectingIn(int port) {
		System.out.println("RECONNECTION: " + port);

	}

	public void reconnectionFailed(Exception arg0) {
		// TODO Auto-generated method stub

	}

	public void reconnectionSuccessful() {
		System.out.println("RECONNECTION: successful");

	}

}
