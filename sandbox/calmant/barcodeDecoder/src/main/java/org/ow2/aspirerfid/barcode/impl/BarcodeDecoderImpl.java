package org.ow2.aspirerfid.barcode.impl;

import java.util.Vector;

import org.ow2.aspirerfid.barcode.BarcodeDecoderService;
import org.ow2.aspirerfid.barcode.BarcodeListener;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;

/**
 * Implementation of a bar code decoder. Based on Zxing
 * 
 * @author Thomas Calmant
 * 
 */
public class BarcodeDecoderImpl implements BarcodeDecoderService {
	/** Zxing bar code decoder */
	private MultiFormatReader m_reader;

	/** List of notification subscribers */
	private Vector<BarcodeListener> m_listeners;

	/**
	 * Allocations...
	 */
	public BarcodeDecoderImpl() {
		m_listeners = new Vector<BarcodeListener>();
		m_reader = new MultiFormatReader();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.aspirerfid.barcode.BarcodeDecoderService#decode(com.google.zxing
	 * .BinaryBitmap)
	 */
	public boolean decode(BinaryBitmap bitmap) {
		// Decode the bar code
		String result = silentDecode(bitmap);
		if (result == null || result.isEmpty())
			return false;

		// Notify subscribers only if a bar code has been found
		for (BarcodeListener listener : m_listeners)
			listener.barcodeDecoded(result);

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.aspirerfid.barcode.BarcodeDecoderService#silentDecode(com.google
	 * .zxing.BinaryBitmap)
	 */
	public String silentDecode(BinaryBitmap bitmap) {
		try {
			// Decode and return the result
			Result result = m_reader.decode(bitmap);
			return result.getText();
		} catch (NotFoundException e) {
			// Bar code not found
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.aspirerfid.barcode.BarcodeDecoderService#addBarcodeListener(org
	 * .ow2.aspirerfid.barcode.BarcodeListener)
	 */
	public void addBarcodeListener(BarcodeListener listener) {
		m_listeners.add(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.aspirerfid.barcode.BarcodeDecoderService#removeBarcodeListener
	 * (org.ow2.aspirerfid.barcode.BarcodeListener)
	 */
	public void removeBarcodeListener(BarcodeListener listener) {
		m_listeners.remove(listener);
	}

}
