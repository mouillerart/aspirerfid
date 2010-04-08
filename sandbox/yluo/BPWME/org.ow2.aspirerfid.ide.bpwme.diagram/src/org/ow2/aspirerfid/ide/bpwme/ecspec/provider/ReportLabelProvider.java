package org.ow2.aspirerfid.ide.bpwme.ecspec.provider;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.ow2.aspirerfid.commons.ale.model.ale.ECFilterSpec;
import org.ow2.aspirerfid.commons.ale.model.ale.ECGroupSpec;
import org.ow2.aspirerfid.commons.ale.model.ale.ECReportOutputSpec;
import org.ow2.aspirerfid.commons.ale.model.ale.ECReportSpec;
import org.ow2.aspirerfid.ide.bpwme.ecspec.model.ReportSpec;

public class ReportLabelProvider implements ILabelProvider {

	List<ILabelProviderListener> listeners = new ArrayList();

	@Override
	public Image getImage(Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getText(Object element) {
		if(element instanceof ReportSpec) {
			return ((ReportSpec)element).getName();
		} else if(element instanceof String) {
			return (String) element;
		}
		return null;
		
		/*if(element instanceof List<?>) {
			return "Report List";
		}else if(element instanceof ECReportSpec) {
			return ((ECReportSpec)element).getReportName();
		}else if(element instanceof ECReportOutputSpec ||
				element instanceof ECFilterSpec ||
				element instanceof ECGroupSpec) {
			return "Something";
		}
		return null;*/
	}

	@Override
	public void addListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		listeners.remove(listener);
	}
}
