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

/**
 * 
 */
package org.ow2.aspirerfid.rp.proxy.msg.stubs.serializers.xml;

import org.ow2.aspirerfid.rp.proxy.msg.stubs.serializers.TriggerSerializer;
import org.ow2.aspirerfid.reader.rp.impl.core.msg.command.TriggerCommand;

/**
 * @author Andreas
 * 
 */
public class TriggerSerializerImpl extends CommandSerializerImpl implements
		TriggerSerializer {

	private TriggerCommand trgCommand = null;

	/**
	 * @param targetName
	 */
	public TriggerSerializerImpl(String targetName) {
		super(targetName);
		init();
	}

	/**
	 * @param id
	 */
	public TriggerSerializerImpl(int id) {
		super(id);
		init();
	}

	/**
	 * @param id
	 * @param targetName
	 */
	public TriggerSerializerImpl(int id, String targetName) {
		super(id, targetName);
		init();
	}

	private void init() {
		trgCommand = cmdFactory.createTriggerCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.accada.reader.testclient.command.TriggerSerializer#create(java.lang.String)
	 */
	public String create(final String name, final String type,
			final String value) {
		resetCommand();
		TriggerCommand.Create c = cmdFactory
				.createTriggerCommandCreate();
		c.setName(name);
		c.setTriggerType(type);
		c.setTriggerValue(value);
		trgCommand.setCreate(c);
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.accada.reader.testclient.command.TriggerSerializer#getMaxNumberSupported()
	 */
	public String getMaxNumberSupported() {
		resetCommand();
		trgCommand.setGetMaxNumberSupported(cmdFactory.createNoParamType());
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.accada.reader.testclient.command.TriggerSerializer#getName()
	 */
	public String getName() {
		resetCommand();
		trgCommand.setGetName(cmdFactory.createNoParamType());
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.accada.reader.testclient.command.TriggerSerializer#getType()
	 */
	public String getType() {
		resetCommand();
		trgCommand.setGetType(cmdFactory.createNoParamType());
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.accada.reader.testclient.command.TriggerSerializer#getValue()
	 */
	public String getValue() {
		resetCommand();
		trgCommand.setGetValue(cmdFactory.createNoParamType());
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.accada.reader.testclient.command.TriggerSerializer#fire()
	 */
	public String fire() {
		resetCommand();
		trgCommand.setFire(cmdFactory.createNoParamType());
		return serializeCommand();
	}

	/**
	 * Serializes an Trigger command.
	 */
	public String serializeCommand() {
		command.setTrigger(trgCommand);
		return super.serializeCommand();
	}
}
