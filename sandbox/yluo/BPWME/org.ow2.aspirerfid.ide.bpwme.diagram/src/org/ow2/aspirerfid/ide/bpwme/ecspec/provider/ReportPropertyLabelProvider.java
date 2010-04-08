package org.ow2.aspirerfid.ide.bpwme.ecspec.provider;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.ow2.aspirerfid.ide.bpwme.ecspec.model.ReportSpec;

public class ReportPropertyLabelProvider extends LabelProvider {
	@Override
	public String getText(Object element) {
		// TODO Auto-generated method stub
		return (String)element;
	}
//implements ILabelProvider {
//	List<ILabelProviderListener> listeners = new ArrayList<ILabelProviderListener>();
//	@Override
//	public Image getImage(Object element) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public String getText(Object element) {
//		return (String)element;
////		if(element instanceof String) {
////			return element.toString();
////		} else if(element instanceof ReportSpec) {
////			return ((ReportSpec)element).getName();
////		}
////		return null;
//	}
//
//	@Override
//	public void addListener(ILabelProviderListener listener) {
//		listeners.add(listener);
//	}
//
//	@Override
//	public void dispose() {
//		// TODO Auto-generated method stub
//	}
//
//	@Override
//	public boolean isLabelProperty(Object element, String property) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public void removeListener(ILabelProviderListener listener) {
//		listeners.remove(listener);
//	}
}
