package icommand.nxt.comm;

interface NXTComm {
	
	public static final String TYPE_RXTX = "rxtx";
	
	public static final String TYPE_BLUEZ = "bluez";
	
	public static final String TYPE_BLUECOVE = "bluecove";
	
	public static final String TYPE_SUN = "sun";
	
	public void open() throws Exception;

	public void sendData(byte[] request);

	public byte[] readData();
	
	public void close();

}
