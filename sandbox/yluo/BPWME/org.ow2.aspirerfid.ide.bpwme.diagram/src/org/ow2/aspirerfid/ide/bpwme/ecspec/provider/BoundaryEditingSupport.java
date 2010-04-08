package org.ow2.aspirerfid.ide.bpwme.ecspec.provider;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.ow2.aspirerfid.ide.bpwme.ecspec.model.BoundaryContent;


public class BoundaryEditingSupport extends EditingSupport{
	private CellEditor editor;

	
	public BoundaryEditingSupport(ColumnViewer viewer) {
		super(viewer);
		editor = new TextCellEditor(((TableViewer) viewer).getTable());
		// TODO Auto-generated constructor stub
	}

	@Override
	protected boolean canEdit(Object element) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		// TODO Auto-generated method stub
		return editor;
	}

	@Override
	protected Object getValue(Object element) {
		// TODO Auto-generated method stub
		BoundaryContent bc = (BoundaryContent)element;
		return bc.getValue();
		//return null;
	}

	@Override
	protected void setValue(Object element, Object value) {
		// TODO Auto-generated method stub
		BoundaryContent bc = (BoundaryContent)element;
		bc.setValue((String)value);
		//getViewer().update(element, null);
	}
}
