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
package org.ow2.aspirerfid.patrolman.nfc;

import java.io.IOException;

import javax.microedition.contactless.TargetProperties;
import javax.microedition.contactless.TargetType;
import javax.microedition.contactless.ndef.NDEFMessage;
import javax.microedition.contactless.ndef.NDEFRecord;
import javax.microedition.contactless.ndef.NDEFTagConnection;
import javax.microedition.io.Connector;

import org.ow2.aspirerfid.nfc.midlet.generic.ReaderThread;
import org.ow2.aspirerfid.nfc.midlet.generic.RequestMessage;
import org.ow2.aspirerfid.nfc.midlet.reader.TagDetector;

/**
 * Processes the information tag and creates a message with all the information.
 * 
 * @author Andres Gomez
 * @author Thomas Calmant
 */
public class TagReaderThread extends ReaderThread {

	/**
	 * Instance the class.
	 * 
	 * @param properties
	 *            Set of properties of the tag.
	 * @param detector
	 *            Object called when a tag has been read.
	 */
	public TagReaderThread(TargetProperties[] properties, TagDetector detector) {
		super(properties, detector);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.aspirerfid.nfc.midlet.generic.ReaderThread#buildMessage(javax
	 * .microedition.contactless.TargetProperties)
	 */
	protected RequestMessage buildMessage(TargetProperties targetProp) {
		TagReaderMessage reader_msg = new TagReaderMessage(m_midlet);

		if (targetProp == null)
			return reader_msg;

		// store UID
		reader_msg.setTagUID(targetProp.getUid());

		// Try to read all record, with their type
		if (targetProp.hasTargetType(TargetType.NDEF_TAG)) {
			NDEFTagConnection conn = null;

			try {
				conn = (NDEFTagConnection) Connector.open(targetProp.getUrl());
				NDEFMessage msg = conn.readNDEF();

				if (msg != null) {
					NDEFRecord[] records = msg.getRecords();
					int nb_records = records.length;

					for (int i = 0; i < nb_records; i++) {
						NDEFRecord rec = records[i];
						if (rec != null) {
							// Get the record type
							reader_msg.addRecordType(rec.getRecordType()
									.toString());
						}
					}
				}
			} catch (Exception error) {
				reader_msg.setThrownException(error);
			} finally {
				if (conn != null) {
					try {
						conn.close();
					} catch (IOException e) {
						// do nothing
					}
				}
			}
		}

		return reader_msg;
	}
}
