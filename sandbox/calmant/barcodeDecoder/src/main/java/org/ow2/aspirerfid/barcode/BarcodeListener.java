package org.ow2.aspirerfid.barcode;

/**
 * Interface to be implemented by all classes aiming to receive information
 * about bar codes read by a BarcodeDecoderService
 * 
 * @author Thomas Calmant
 */
public interface BarcodeListener {
	/**
	 * Called when a bar code has been decoded
	 * 
	 * @param decoded
	 *            The result of a succeeded decoding process
	 */
	public void barcodeDecoded(String decodedText);
}
