package org.ow2.aspirerfid.ide.bpwme.ecspec.views;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;
import org.ow2.aspirerfid.ide.bpwme.ecspec.utils.ECSpecBuilder;
import org.ow2.aspirerfid.ide.bpwme.ecspec.utils.LRSpecBuilder;

public class ECSpecEditorInput implements IEditorInput{
	
	private LRSpecBuilder lrsb;
	private ECSpecBuilder ecsb;
	
	public ECSpecEditorInput(LRSpecBuilder lrsb, ECSpecBuilder ecsb) {
		this.lrsb = lrsb;
		this.ecsb = ecsb;
	}
	
	@Override
	public boolean exists() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "LR Spec Editor";
	}

	@Override
	public IPersistableElement getPersistable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getToolTipText() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public Object getAdapter(Class adapter) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public LRSpecBuilder getLRSpecBuilder() {
		return lrsb;
	}
	
	public ECSpecBuilder getECSpecBuilder() {
		return ecsb;
	} 

}

