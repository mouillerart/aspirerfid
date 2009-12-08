/*
 * Copyright (C) 2008-2010 Loic Schmidt - INRIA
 * 
 * This file is part of AspireRFID.
 *
 * AspireRFID is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1, as published by the Free Software Foundation.
 *
 * AspireRFID is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with AspireRFID. If not, see
 * <http://www.gnu.org/licenses/>.
 */

package org.ow2.aspirerfid.tdt.iso;

/**
 *
 *	<p>This interface represents a ISO UID
 *      
 * @version Revision: 0.5 Date: 2009/11/23
 */

public interface ISOSerial {

    //-----------/
    //- Methods -/
    //-----------/
    //
    /** 
     *	Convert the isoSerial in other representation
     * 
     *  @param outputFormat format representation for output
     *  @return output ISO tag code in the new representation
     */
    public String convert(String outputFormat);




}
