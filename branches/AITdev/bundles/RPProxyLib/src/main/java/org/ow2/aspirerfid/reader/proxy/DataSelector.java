/*
 * Copyright (C) 2007 ETH Zurich
 *
 * This file is part of Accada (www.accada.org).
 *
 * Accada is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1, as published by the Free Software Foundation.
 *
 * Accada is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Accada; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA  02110-1301  USA
 */

package org.ow2.aspirerfid.rp.proxy;



public interface DataSelector {

	/**
	 * Returns the name of the given DataSelector object.
	 * @return The name of the data selector
	 */
	String getName() throws RPProxyException;

	/**
	 * Adds the specified field names to the list of field names currently
	 * associated with this dataselector.
	 * @param fieldNameList
	 *           A list of field names
	 */
	void addFieldNames(final String[] fieldNameList) throws RPProxyException;

	/**
	 * Removes a fieldname of the list of fieldnames currently associated with
	 * this dataselector.
	 * @param fieldNameList
	 *           The list of fieldnames to remove
	 */
	void removeFieldNames(final String[] fieldNameList) throws RPProxyException;

	/**
	 * Removes all fieldnames of the list of fieldnames currently associated with
	 * this dataselector.
	 */
	void removeAllFieldNames() throws RPProxyException;

	/**
	 * Returns a list with all fieldnames currentliy associated with this
	 * dataselector.
	 * @return A list with all fieldnames currentliy associated with this
	 *         dataselector
	 */
	String[] getAllFieldNames() throws RPProxyException;

	/**
	 * Adds the specified eventtypes to the list of eventtypes currently
	 * associated with this DataSelector. Only events of the types that have been
	 * added shall be reported.
	 * @param eventList
	 *           A list of event types to add
	 */
	void addEventFilters(final String[] eventList) throws RPProxyException;

	/**
	 * Removes the specified eventtypes from the list of eventfilters currently
	 * associated with this dataselector.
	 * @param eventList
	 *           A list of eventtypes to remove
	 */
	void removeEventFilters(final String[] eventList) throws RPProxyException;

	/**
	 * Removes all eventtypes currently associated with this dataselector.
	 */
	void removeAllEventFilters() throws RPProxyException;

	/**
	 * Returns tahe list of all eventtypes currently associated with this
	 * dataselector.
	 * @return Returns the list of event filters
	 */
	String[] getAllEventFilters() throws RPProxyException;

	/**
	 * Adds the specified tagfieldnames to the list of tagfieldnames currently
	 * associated with this dataselector.
	 * @param tagFieldNameList
	 *           A list of tag field names to add (list of strings)
	 */
	void addTagFieldNames(final String[] tagFieldNameList) throws RPProxyException;

	/**
	 * Removes certain tagfieldnames from the list of tagfieldnames currently
	 * associated with this dataselector.
	 * @param tagFieldNameList
	 *           A list of tagfieldnames to remove
	 */
	void removeTagFieldNames(final String[] tagFieldNameList) throws RPProxyException;

	/**
	 * Removes all tagfieldnames currently associated with this dataselector.
	 */
	void removeAllTagFieldNames() throws RPProxyException;

	/**
	 * Returns a list of all tagfieldnames currently associated with this
	 * dataselector. Note, this is a list of Strings.
	 * @return A list of all tagfieldnames
	 */
	String[] getAllTagFieldNames() throws RPProxyException;

	int getMaxNumberSupported() throws RPProxyException;

}