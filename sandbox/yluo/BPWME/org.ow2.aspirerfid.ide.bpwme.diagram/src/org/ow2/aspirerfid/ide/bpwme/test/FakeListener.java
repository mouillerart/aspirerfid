package org.ow2.aspirerfid.ide.bpwme.test;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartListener;

public class FakeListener implements EditPartListener{

	@Override
	public void childAdded(EditPart child, int index) {
		// TODO Auto-generated method stub
		System.out.println("Fake Add:" + child);
	}

	@Override
	public void partActivated(EditPart editpart) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void partDeactivated(EditPart editpart) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removingChild(EditPart child, int index) {
		// TODO Auto-generated method stub
		System.out.println("Fake Remove:" + child);
	}

	@Override
	public void selectedStateChanged(EditPart editpart) {
		// TODO Auto-generated method stub
		
	}

}
