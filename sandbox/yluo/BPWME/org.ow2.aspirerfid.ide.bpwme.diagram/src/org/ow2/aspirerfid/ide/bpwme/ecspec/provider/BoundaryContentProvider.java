package org.ow2.aspirerfid.ide.bpwme.ecspec.provider;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.ow2.aspirerfid.ide.bpwme.ecspec.model.BoundaryContent;
import org.ow2.aspirerfid.ide.bpwme.ecspec.utils.ECSpecBuilder;

public class BoundaryContentProvider implements IStructuredContentProvider {

	@Override
	public Object[] getElements(Object inputElement) {
		// TODO Auto-generated method stub
		ECSpecBuilder ecsb = (ECSpecBuilder)inputElement;
		BoundaryContent[] bc = new BoundaryContent[3];
		bc[0] = new BoundaryContent(ecsb, "Repeat Period",ecsb.getRepeatPeriod());
		bc[1] = new BoundaryContent(ecsb,"Duration",ecsb.getDuration());
		bc[2] = new BoundaryContent(ecsb,"Stable Set Interval",ecsb.getStableSetInterval());
		return bc;
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


