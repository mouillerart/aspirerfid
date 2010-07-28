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
package org.ow2.aspirerfid.patrolman.questionnaire;

/**
 * Questions common interface. Questions can be identified, loaded/saved from an
 * Object (question implementation dependent), validated, and their answer can
 * be converted into an XML string.
 * 
 * @author Thomas Calmant
 * 
 */
public interface Question {
	/**
	 * Retrieve question's identifier
	 * 
	 * @return question's identifier
	 */
	public String getId();

	/**
	 * Test if the answer is correct. If not verification can be done, return
	 * true
	 * 
	 * @return True if the answer is correct, or no control can be made
	 */
	public boolean isCorrect();

	/**
	 * Reset the question state (no entry/no selection/...)
	 */
	public void clear();

	/**
	 * Returns the XML representation of the current state of the answer
	 * 
	 * @return A ECReport-ready XML representation
	 */
	public String toXML();

	/**
	 * Returns the XML representation of the given state of the answer
	 * 
	 * @return A ECReport-ready XML representation
	 */
	public String toXML(Object data);

	/**
	 * @return Question internal data for a future reload
	 */
	public Object getData();

	/**
	 * @param value
	 *            Previously saved data (see {@link #getData()})
	 */
	public void setData(Object value);
}
