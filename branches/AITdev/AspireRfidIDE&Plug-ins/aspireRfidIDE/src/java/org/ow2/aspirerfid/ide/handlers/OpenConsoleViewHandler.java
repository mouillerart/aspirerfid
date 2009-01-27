/*
 * Copyright © 2008-2010, Aspire
 * 
 * Aspire is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License version 2.1 as published by
 * the Free Software Foundation (the "LGPL").
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library in the file COPYING-LGPL-2.1; if not, write to the
 * Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301 USA.
 * 
 * This software is distributed on an "AS IS" basis, WITHOUT WARRANTY OF ANY
 * KIND, either express or implied. See the GNU Lesser General Public License
 * for the specific language governing rights and limitations.
 */

package org.ow2.aspirerfid.ide.handlers;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.IOConsole;
import org.eclipse.ui.console.IOConsoleOutputStream;
import org.eclipse.ui.handlers.HandlerUtil;
import org.ow2.aspirerfid.ide.views.*;


/**
 * @author nkef
 *
 */
public class OpenConsoleViewHandler extends AbstractHandler implements IHandler {

//	IOConsoleOutputStream standarDebugOutputConsole;
//	IOConsole standarDebugConsole;
//	IConsoleManager manager;
//	IConsole[] consolesx;
	
	
	
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
//		initiateConsole();
		try {
			HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().showView("org.eclipse.ui.console.ConsoleView");//ConsoleView.ID
		} catch (PartInitException e) {
			throw new ExecutionException("Error while opening view", e);
		}
		
//		OutputStream out = new OutputStream() {
//			private StringBuffer buffer = new StringBuffer();
//			private final Object obj = new Object();
//
//			@Override
//			public void write(final int b) throws IOException {
//				synchronized (obj) {
//					if (debugStyledText.isDisposed())
//						return;
//
//					buffer.append((char) b);
//				}
//			}
//			@Override
//			public void write(byte[] b, int off, int len) throws IOException {
//				super.write(b, off, len);
//				flush();
//			}
//			@Override
//			public void flush() throws IOException {
//				synchronized (obj) {
//					final String newText = buffer.toString();
//					writeToConsole(newText);
//					buffer = new StringBuffer();
//				}
//			}
//
//		};
//		final PrintStream oldOut = System.out;
//		System.setOut(new PrintStream(out));
//		debugStyledText.addDisposeListener(new DisposeListener() {
//			public void widgetDisposed(DisposeEvent e) {
//				System.setOut(oldOut);
//			}
//		});

		return null;
	}
	
//	/**
//	 * Initiate the Ale Server Configurator Console
//	 */
//	private void initiateConsole() {
//		manager = ConsolePlugin.getDefault().getConsoleManager();
//		consolesx = manager.getConsoles();
//		boolean exist = false;
//		for (int i = 0; i < consolesx.length; i++) {
//			if (consolesx[i].getName().equals("Standar Debug Out"))
//				standarDebugConsole = (IOConsole) consolesx[i];
//			exist = true;
//		}
//		if (!exist) {
//			standarDebugConsole = new IOConsole(
//					"Standar Debug Out", null);
//			manager
//					.addConsoles(new IConsole[] { standarDebugConsole });
//		}
//		manager.showConsoleView(standarDebugConsole);
//		standarDebugConsole.clearConsole();
//		standarDebugOutputConsole = standarDebugConsole
//				.newOutputStream();
//	}
//	
//	/**
//	 * Write to the IDE Console
//	 * 
//	 * @param message
//	 */
//	private void writeToConsole(String message) {
//
//		try {
//			standarDebugOutputConsole.write(message + "\n");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	

}
