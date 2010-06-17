/*
 *  Copyright (C) Aspire
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.ow2.aspirerfid.barcode.image.j2se;

import java.awt.image.BufferedImage;

import org.ow2.aspirerfid.barcode.ImageConverter;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.common.HybridBinarizer;

/**
 * Converts a BufferedImage into a Zxing readable BinaryBitmap. Code based on
 * J2SE module of Zxing
 * 
 * @author Thomas Calmant
 * 
 */
public class BufferedImageConverter implements ImageConverter {
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.aspirerfid.sandbox.calmant.barcode.ImageConverter#convert(java
	 * .lang.Object)
	 */
	public BinaryBitmap convert(Object source) {
		if (source instanceof BufferedImage)
			return convertFromImage((BufferedImage) source);

		// If the input format is unknown, throw an exception
		throw new IllegalArgumentException(
				"The source image must be a java.awt.image.BufferedImage");
	}

	private BinaryBitmap convertFromImage(BufferedImage image) {
		// Can throw IllegalArgumentException, if source data is invalid
		BufferedImageLuminanceSource luminanceSource = new BufferedImageLuminanceSource(
				image);

		return new BinaryBitmap(new HybridBinarizer(luminanceSource));
	}
}
