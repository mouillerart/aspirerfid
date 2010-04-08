package org.ow2.aspirerfid.ide.bpwme.actions;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import bpwme.diagram.simpleditor.SimpleEditor;

public class RefreshXMLEditor extends AbstractHandler{
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		SimpleEditor.refresh();
		return null;
	}
}
