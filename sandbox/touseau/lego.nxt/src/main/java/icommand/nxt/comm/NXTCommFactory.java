package icommand.nxt.comm;

import java.util.Properties;
import java.io.*;

class NXTCommFactory {

	public static final String COM_PORT = "nxtcomm";
	public static final String COM_TYPE = "nxtcomm.type";
	public static final String BT_ADDRESS = "nxt.btaddress";
	
    public static final int USB = 1;
    public static final int BLUETOOTH = 2;

    private static final String PROPSFNAME = "icommand.properties";
	private static final String UDIR = System.getProperty("user.dir");
	private static final String UHOME = System.getProperty("user.home");
	private static final String FSEP = System.getProperty("file.separator");
	    
	public static NXTComm createInstance() {
		
		/* Create properties with defaults for appropriate OS */
		Properties props = getDefaults();
		
		/* Load settings from icommand.properties file */
		File file = new File(UDIR + FSEP + PROPSFNAME);
		if(!file.exists())
			file = new File(UHOME + FSEP + PROPSFNAME);
		System.out.println("Properties location: " + file.getAbsolutePath());
		try {
			if(file.exists()) 
				props.load(new FileInputStream(file));
			else {
				System.out.println(PROPSFNAME + " does NOT exist. Creating.");
				
				file.createNewFile();
				// NOTE: Default settings might not be stored?
				props.store(new FileOutputStream(file), "Auto-generated iCommand Properties file");
			}
			
		} catch(IOException e) {
			System.out.println(e);
		}
			
		/* Return appropriate NXTComm Object. */
		if (props.getProperty(NXTCommFactory.COM_TYPE).equals(NXTComm.TYPE_BLUECOVE)) {
			String address = props.getProperty(BT_ADDRESS);
			if(address == null) {
				System.out.println("No NXT address found in " + PROPSFNAME + ". Searching... (this may take up to a minute)"); 
				NXTCommBlueCove bc = new NXTCommBlueCove(); 
				NXTInfo[] nxts = bc.search(null, BLUETOOTH);
				if(nxts[0] != null) {
					address = nxts[0].btDeviceAddress;
					props.setProperty(BT_ADDRESS, address);
					// Now save to icommand.properties:
					try {
						props.store(new FileOutputStream(file), "Auto-detected BT address. Feel free to change.");
					} catch(IOException e) {System.out.println(e);}
				} else {
					System.out.println("No NXT found via Bluetooth. Perhaps it is not turned on?");
					System.exit(0);
				}
				bc.setAddress(address);
				return bc;
			} else 
				return new NXTCommBlueCove(props);
		} else
		if (props.getProperty(NXTCommFactory.COM_TYPE).equals(NXTComm.TYPE_BLUEZ)) {
			String address = props.getProperty(BT_ADDRESS);
			if(address == null) {
				System.out.println("No NXT address found in " + PROPSFNAME + ". Searching... (this may take up to a minute)");
				NXTCommBluez bluez = new NXTCommBluez();				
				NXTInfo[] nxts = bluez.search(null, BLUETOOTH);
				if(nxts[0] != null) {
					address = nxts[0].btDeviceAddress;
					props.setProperty(BT_ADDRESS, address);
					// Now save to icommand.properties:
					try {
						props.store(new FileOutputStream(file), "Auto-detected BT address. Feel free to change.");
					} catch(IOException e) {System.out.println(e);}
				} else {
					System.out.println("No NXT found via Bluetooth. Perhaps it is not turned on?");
					System.exit(0);
				}
				bluez.setAddress(address);
				return bluez;
			} else 
				return new NXTCommBluez(props);
		} /*else
		if (props.getProperty(NXTCommFactory.COM_TYPE).equals(NXTComm.TYPE_SUN)) {
			return new NXTCommSun(props);
		} else 
		{
			return new NXTCommRXTX(props);
		}*/
		else return null; // replacing code
	}
	
	
	/**
	 * This creates a Properties object with some default settings.
	 * The Properties class will resort to using default settings
	 * if a setting is blank.
	 * @return Properties object with some default settings
	 */
	private static Properties getDefaults() {
		Properties defaults = new Properties();
		
		String os = System.getProperty("os.name");
    	boolean windows = false;
    	boolean mac = false;
    	
    	// Search for different OS's
    	if (os.length() >= 7 && os.substring(0,7).equals("Windows"))
    		windows = true;
    	if (os.length() >= 3 && os.substring(0,3).equals("Mac"))
    		mac = true;
    	
    	// Set appropriate default solution for OS
		if (windows) {
			defaults.setProperty(COM_TYPE, NXTComm.TYPE_BLUECOVE);
		} else if (mac){
			defaults.setProperty(COM_TYPE, NXTComm.TYPE_RXTX);
		} else {
			defaults.setProperty(COM_TYPE, NXTComm.TYPE_BLUEZ);
		}
		
		return defaults;
	}
}
