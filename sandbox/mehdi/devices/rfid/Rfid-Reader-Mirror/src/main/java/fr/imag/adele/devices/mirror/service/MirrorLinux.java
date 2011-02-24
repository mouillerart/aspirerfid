package fr.imag.adele.devices.mirror.service;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import org.apache.log4j.Logger;
import org.sembysem.sam.demo.rfid.service.RfidTag;

public class MirrorLinux implements Runnable{

	private Logger logger = Logger.getLogger(MirrorLinux.class);
	
	private MirrorDescriptor mirror;
	private boolean stopped = false;
	private File f ;
	private FileInputStream fstream;
	
	public MirrorLinux(MirrorDescriptor mirrorDescriptor) throws FileNotFoundException {
		mirror = mirrorDescriptor;
		f = new File("/dev/mirror");
		fstream = new FileInputStream(f);
	}

	


	public void run() {
		byte[] b = new byte[16];
		while (!stopped ) {
			try {
				int t = fstream.read(b);
				if (t == 16) {
					if (b[0] != 0) {
						/*
						 * 14 start
						 * 15 stop (upsideDown)
						 * 21 tag detected
						 * 22 tag removed
						 */
						
						if (b[0]==1){
							if (b[1]==4){
								//Mirror (re)started
								mirror.setIsStarted(true);
								mirror.setIsUpSideDown(false);
								logger.info("Mirror (re)started");
							} else if (b[1]==5){
								//Mirror stopped (Mirror is upside down)
								mirror.setIsUpSideDown(true);
								mirror.setIsStarted(false);
								logger.info("Mirror stopped : Mirror is upside down");
							}	
						} else if(b[0]==2){
							
							/*
							 * The Mirror device seems to miss the 3 last bytes of
							 * the tag id, the value is truncated and appears as XXXX000
							 */
							
							if (b[1]==1){
								//Tag detected
								String currentTag = String.format("%02X%02X%02X%02X%02X%02X%02X",
										b[5], b[6], b[7], b[8], b[9], b[10], b[11]);
								mirror.setCurrentTag(new RfidTag(currentTag, new Date()));
								logger.info("Mirror Add Tag : " + currentTag);
							} else if (b[1]==2){
								//Tag removed
								String removedTag = String.format("%02X%02X%02X%02X%02X%02X%02X",
										b[5], b[6], b[7], b[8], b[9], b[10], b[11]);
								mirror.setRemovedTag(new RfidTag(removedTag, new Date()));
								logger.info("Mirror Remove Tag : " + removedTag);
								mirror.setCurrentTag(null);
							}
						}
					} 
				}
			} catch (IOException e) {
			}
		}
	}


	public void setStop(boolean stopped) {
		this.stopped = stopped;
	}
}
