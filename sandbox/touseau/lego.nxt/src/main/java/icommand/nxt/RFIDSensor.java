package icommand.nxt;

import icommand.nxt.I2CSensor;
import icommand.nxt.comm.ErrorMessages;
import icommand.nxt.comm.NXTCommand;
import icommand.nxt.comm.NXTProtocol;

public class RFIDSensor extends I2CSensor {

	private static final byte RFID_ADDRESS = 0x04;	
	
	private static final byte RFID_STATUS = 0x32;
	
	/* RFID sensor registers */
	private static final byte COMMAND_REGISTER = 0x41;
	private static final byte READ_DATA_REGISTER = 0x42; // 5 bytes
	
	private static final byte RFID_SERIAL = (byte) 0xA0;  // 4 bytes
	

	/* RFID sensor commands */
	public static final byte STOP = 0x00;
	public static final byte SINGLE_READ = 0x01;
	public static final byte CONTINUOUS_READ = 0x02;
	private static final byte CMD_BOOT = (byte)0x81;
	private static final byte CMD_START = (byte)0x83;
	
	private byte m_currentMode;
	
	
	
	public RFIDSensor(SensorPort sp) {
		super(sp, NXTProtocol.LOWSPEED_9V);
	}
	
	/**
	 * Dummy method to wake up the RFID sensor
	 * Writes 0x00 in the register 0x00
	 */
	public void dummyRFID (){
		this.sendData((byte)0x00, (byte)0x00);
	}
	
	/**
	 * blocking method. Useful or to delete?
	 */
	public void waitI2C(){
		System.out.println("waiting for the I2C bus");
		while (getSingleByte(RFID_STATUS) == 0){
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("status != 0 => end of wait loop");
	}
	
	public void setReadMode (byte mode){
		this.sendData(COMMAND_REGISTER, mode);
		m_currentMode = mode;
	}
	
	public byte getReadMode(){
		return m_currentMode;
	}
	
	public boolean isActive(){
		if (m_currentMode == STOP)
			return false;
		else
			return true;
	}
	
	/**
	 * @return a string representing the tag ID
	 */
	public String readTag() {
		
		switch (m_currentMode) {
		case STOP:
			setReadMode(CONTINUOUS_READ);
			break;
		case SINGLE_READ:
			// do nothing
			break;
		case CONTINUOUS_READ:
			// do nothing
			break;
		default:
			setReadMode(CONTINUOUS_READ);
			break;
		}
		
		// Read the TAG ID in the sensor register
		byte[] id = getData(READ_DATA_REGISTER, 5);
		System.out.println("nbBytes: "+id.length);
		
		// convert it to an hexadecimal string with unsigned bytes
		String tagID;
		StringBuffer buff = new StringBuffer();
		for (int i = 0; i < id.length; i++){
			if ((id[i] & 0xFF) == 0){
				buff.append("00");
			} else
				buff.append(Integer.toHexString(id[i] & 0xFF));
		}
		tagID = buff.toString().toUpperCase();

		return tagID;
	}
	
	/**
	 * Sends a BOOT then a START command to the RFID sensor
	 */
	public void bootStart(){
		this.sendData(COMMAND_REGISTER, CMD_BOOT);
		this.sendData(COMMAND_REGISTER, CMD_START);
	}
	
	/* (non-Javadoc)
	 * Override
	 * @see icommand.nxt.I2CSensor#sendData(byte, byte)
	 */
	protected void sendData(byte register, byte value) {
		byte [] txData = {RFID_ADDRESS, register, value};
		NXTCommand.getSingleton().LSWrite(super.port, txData, (byte)0);
	}	
	
	/**
	 * Override method because of unreliability retrieving more than a single
	 * byte at a time with some I2C Sensors (bug in Lego firmware).
	 * Note: This is slower because it takes more Bluetooth calls.
	 * @param register e.g. FACTORY_SCALE_DIVISOR, BYTE0, etc....
	 * @param length Length of data to read (minimum 1, maximum 16) 
	 * @return
	 */
	protected byte [] getData(byte register, int length) {
		
		NXTCommand nxtCommand = NXTCommand.getSingleton();
		
		byte [] txData = {RFID_ADDRESS, register};
		nxtCommand.LSWrite(super.port, txData, (byte)length);
		
		byte [] status;
		do {
			status = nxtCommand.LSGetStatus(port);
		} while(status[0] == ErrorMessages.PENDING_COMMUNICATION_TRANSACTION_IN_PROGRESS|status[0] == ErrorMessages.SPECIFIED_CHANNEL_CONNECTION_NOT_CONFIGURED_OR_BUSY);
		//System.out.println("Error is " + status[0] + "  Data is now ready? " + status[1] + " bytes available.");
		if(status[1] == 0) {
			System.out.println("No bytes to be read in I2CSensor.getData(). Returning 0.");
			return new byte[1];
		}
				
		byte [] result = nxtCommand.LSRead(port);
		for(int i=0;i<length;i++){
			System.out.println("Byte "+i+" : "+Integer.toHexString(result[i]));
		}
		return result;
	}

	/**
	 * This method is a decomposition of the I2CSensor.getData() method
	 * @param register
	 * @return a single byte at a time
	 */
	private byte getSingleByte(byte register) {
		byte [] txData = {RFID_ADDRESS, register};
		NXTCommand nxtCommand = NXTCommand.getSingleton();
		byte length = 0x01;
		nxtCommand.LSWrite(super.port, txData, length);
		byte [] status;
		do {
			status = nxtCommand.LSGetStatus(port);
		} while(status[0] == ErrorMessages.PENDING_COMMUNICATION_TRANSACTION_IN_PROGRESS|status[0] == ErrorMessages.SPECIFIED_CHANNEL_CONNECTION_NOT_CONFIGURED_OR_BUSY);
		//System.out.println("Error is " + status[0] + "  Data is now ready? " + status[1] + " bytes available.");
		if(status[1] == 0) {
			System.out.println("No bytes to be read in I2CSensor.getData(). Returning 0.");
			return 0;
		}
		byte result = nxtCommand.LSRead(port)[0];
		return result;
	}

	public void stop() {
		this.sendData(COMMAND_REGISTER, STOP);
	}	
	
}
