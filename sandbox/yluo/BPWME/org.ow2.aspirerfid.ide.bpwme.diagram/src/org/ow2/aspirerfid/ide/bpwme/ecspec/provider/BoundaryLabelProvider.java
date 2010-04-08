package org.ow2.aspirerfid.ide.bpwme.ecspec.provider;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.ow2.aspirerfid.ide.bpwme.ecspec.model.BoundaryContent;

public class BoundaryLabelProvider implements ITableLabelProvider {
	List listeners = new ArrayList();

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		BoundaryContent bc = (BoundaryContent)element;
		switch(columnIndex) {
		case 0:
			return bc.getName();
		case 1:
			return bc.getValue();
		default:
			return null;
		}
	}

	@Override
	public void addListener(ILabelProviderListener listener) {
		listeners.add(listener);
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
		listeners.remove(listener);
	}

}
