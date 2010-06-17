package org.ow2.aspirerfid.barcode.test;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

import javax.imageio.ImageIO;

import org.ow2.aspirerfid.barcode.BarcodeDecoderService;
import org.ow2.aspirerfid.barcode.BarcodeListener;
import org.ow2.aspirerfid.barcode.image.j2se.BufferedImageConverter;
import org.ow2.aspirerfid.barcode.impl.BarcodeDecoderImpl;

import com.google.zxing.BinaryBitmap;

import junit.framework.*;

/**
 * Test class for a complete convert-decode sequence of the bar code reader
 * 
 * @author Thomas Calmant
 */
public class DecoderTest extends TestCase {
	private int nb_listeners;
	private int nb_listeners_calls;

	protected class DecoderListener implements BarcodeListener {
		public void barcodeDecoded(String decodedText) {
			System.out.println("Decoded text : " + decodedText);
			nb_listeners_calls++;
		}
	}

	/**
	 * Test the barcode decoding from an image file
	 */
	public void testBufferedImage() {
		// URLs to be tested. Use small easy pictures in order to have a fast
		// and valid test
		Vector<URL> test_files = new Vector<URL>();

		try {
			// Image from Wikipedia QR Code french page, image in Public Domain.
			test_files.add(new URL("http", "upload.wikimedia.org",
					"/wikipedia/commons/5/5b/Qrcode_wikipedia.jpg"));
		} catch (MalformedURLException e1) {
			System.err.println("Error adding test URL");
		}

		String result = null;
		BarcodeDecoderService decoder = new BarcodeDecoderImpl();
		BufferedImageConverter converter = new BufferedImageConverter();

		// Tests x bar code listener
		nb_listeners = 2;
		for (int i = 0; i < nb_listeners; i++)
			decoder.addBarcodeListener(new DecoderListener());

		for (URL test_file : test_files) {
			try {
				BufferedImage image = ImageIO.read(test_file);
				BinaryBitmap zxing_bmp = converter.convert(image);

				// Silent test (tests the decode engine)
				result = decoder.silentDecode(zxing_bmp);
				assertNotNull("Unable to decode the test file : "
						+ test_file.toString(), result);

				System.out.println("You should see this text " + nb_listeners
						+ " times : '" + result + "'");
				nb_listeners_calls = 0;

				// Tests the notification to subscriber
				assertTrue("Listeners notification", decoder.decode(zxing_bmp));
				
				assertEquals("All listeners must be called", nb_listeners,
						nb_listeners_calls);
			} catch (IOException e) {
				System.err.println("Error reading the test file");
			}
		}
	}

	/**
	 * Force an exception by using an invalid argument for the converter
	 */
	public void testNotBufferedImage() {
		BufferedImageConverter converter = new BufferedImageConverter();
		boolean except = false;
		try {
			converter.convert("test");
		} catch (IllegalArgumentException e) {
			except = true;
		}

		assertTrue(
				"This call must fail : BufferedImageConverter may not handle Strings",
				except);
	}
}
