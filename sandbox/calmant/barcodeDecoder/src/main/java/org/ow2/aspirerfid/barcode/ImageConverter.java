package org.ow2.aspirerfid.barcode;

import com.google.zxing.BinaryBitmap;

/**
 * Interface to be implemented by all converters 
 * @author Thomas Calmant
 *
 */
public interface ImageConverter {
	/**
	 * Converts the source image into a Zxing readable one.
	 * Source type depends on the implementation.
	 * 
	 * @param source The source image
	 * @return A Zxing readable representation of the source image.
	 */
	public BinaryBitmap convert(Object source);
}
