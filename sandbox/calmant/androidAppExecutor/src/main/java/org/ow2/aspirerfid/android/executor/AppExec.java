package org.ow2.aspirerfid.android.executor;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.apache.felix.shell.Command;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import android.net.LocalSocket;
import android.net.LocalSocketAddress;

public class AppExec implements BundleActivator, Command {
	/** Felix starter */
	public static final String APP_LAUNCHER = "felix-browser";

	/** Command registration */
	private ServiceRegistration m_cmdReg;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) {
		m_cmdReg = context.registerService(Command.class.getName(), this, null);
		
		if(m_cmdReg != null)
			System.out.println("*** Command registered");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) {
		if (m_cmdReg != null)
			m_cmdReg.unregister();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.felix.shell.Command#execute(java.lang.String,
	 * java.io.PrintStream, java.io.PrintStream)
	 */
	public void execute(String cmdLine, PrintStream out, PrintStream err) {
		String uri = cmdLine.substring(getName().length() + 1);
		uri.trim();

		launchUri(uri, err);
	}

	private void launchUri(String uri, PrintStream err) {
		LocalSocket socket = new LocalSocket();

		try {
			socket.connect(new LocalSocketAddress(APP_LAUNCHER));
		} catch (IOException e) {
			err.println("** Error connecting the launcher");
			e.printStackTrace(err);
			return;
		}

		DataOutputStream dos = null;

		try {
			dos = new DataOutputStream(socket.getOutputStream());
			dos.writeUTF(uri);
		} catch (IOException e) {
			err.println("** Error sending data to launcher");
			e.printStackTrace(err);
		}

		if (dos != null)
			try {
				dos.close();
			} catch (IOException e) {
				// do nothing
			}

		try {
			socket.close();
		} catch (IOException e) {
			// do nothing
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.felix.shell.Command#getName()
	 */
	public String getName() {
		return "android-app";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.felix.shell.Command#getShortDescription()
	 */
	public String getShortDescription() {
		return "Execute Android Intent with ACTION_VIEW";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.felix.shell.Command#getUsage()
	 */
	public String getUsage() {
		return getName() + " intent_URI";
	}
}
