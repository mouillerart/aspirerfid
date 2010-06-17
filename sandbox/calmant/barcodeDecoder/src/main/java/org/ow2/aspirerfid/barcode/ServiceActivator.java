package org.ow2.aspirerfid.barcode;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.ow2.aspirerfid.barcode.impl.BarcodeDecoderImpl;

public class ServiceActivator implements BundleActivator {
	/** The bar code decoder */
	private BarcodeDecoderService m_decoder;

	/** Service registration */
	private ServiceRegistration m_service;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {
		// Create the decoder instance
		m_decoder = new BarcodeDecoderImpl();

		// Register the corresponding service
		m_service = context.registerService(
				BarcodeDecoderService.class.getName(), m_decoder, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		// Unregister the service
		m_service.unregister();
	}

}
