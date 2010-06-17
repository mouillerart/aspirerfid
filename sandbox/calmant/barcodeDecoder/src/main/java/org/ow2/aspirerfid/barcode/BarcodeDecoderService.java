package org.ow2.aspirerfid.barcode;

import com.google.zxing.BinaryBitmap;

/**
 * Description of the bar code reader
 * 
 * @author Thomas Calmant
 */
public interface BarcodeDecoderService {
	/**
	 * Tries to decode a bar code, and notifies all listener if the operation
	 * succeeded. Calls {@link #silentDecode(BarcodeBitmap)} for the decoding
	 * process
	 * 
	 * @param bitmap
	 *            Bitmap representation of the bar code
	 * @return True if the bar code has been decoded
	 */
	public boolean decode(BinaryBitmap bitmap);

	/**
	 * Try to decode a bar code, without notifying listeners
	 * 
	 * @param bitmap
	 *            Bitmap representation of the bar code
	 * 
	 * @return The RAW text decoded by Zxing
	 */
	public String silentDecode(BinaryBitmap bitmap);

	/**
	 * Subscribe a listener to the notification process
	 * 
	 * @param listener
	 *            The listener to be added
	 */
	public void addBarcodeListener(BarcodeListener listener);

	/**
	 * Un-subscribe a listener from the notification process
	 * 
	 * @param listener
	 *            The listener to be removed
	 */
	public void removeBarcodeListener(BarcodeListener listener);
}
