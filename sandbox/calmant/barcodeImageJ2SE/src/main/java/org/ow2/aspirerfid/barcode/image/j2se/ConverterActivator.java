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
import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.ow2.aspirerfid.barcode.ImageConverter;

/**
 * Bundle activator for the J2SE bar code image converter.
 * 
 * @author Thomas Calmant
 * 
 */
public class ConverterActivator implements BundleActivator {
	/** The image converter */
	private BufferedImageConverter m_converter;
	
	/** The registered service */
	private ServiceRegistration m_service;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {
		m_converter = new BufferedImageConverter();

		// Properties are defined to be able to filter converter services
		Hashtable<String, String> properties = new Hashtable<String, String>(2);
		properties.put("platform", "j2se");
		properties.put("source", BufferedImage.class.getName());
		
		m_service = context.registerService(ImageConverter.class.getName(),
				m_converter, properties);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		if(m_service != null)
			m_service.unregister();
	}

}
