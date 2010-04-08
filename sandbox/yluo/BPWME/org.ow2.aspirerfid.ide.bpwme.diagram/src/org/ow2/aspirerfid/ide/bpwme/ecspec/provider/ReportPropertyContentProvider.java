package org.ow2.aspirerfid.ide.bpwme.ecspec.provider;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.ow2.aspirerfid.ide.bpwme.ecspec.model.*;

public class ReportPropertyContentProvider implements IStructuredContentProvider{


	@Override
	public Object[] getElements(Object inputElement) {
		//System.out.println("Input" + inputElement);
		return ((List)inputElement).toArray();
		//String[] kk= new String[]{"111","222"};
		//return kk;
		//return null;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub
		
	}
}
