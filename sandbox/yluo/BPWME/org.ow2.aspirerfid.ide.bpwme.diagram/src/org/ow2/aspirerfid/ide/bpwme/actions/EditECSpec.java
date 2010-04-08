package org.ow2.aspirerfid.ide.bpwme.actions;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gmf.runtime.common.ui.action.AbstractActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.gmf.runtime.notation.View;
import org.ow2.aspirerfid.bpwme.diagram.edit.parts.EBProcEditPart;
import org.ow2.aspirerfid.bpwme.impl.EBProcImpl;
import org.ow2.aspirerfid.commons.ale.model.ale.ECSpec;
import org.ow2.aspirerfid.commons.apdl.model.ApdlDataField;
import org.ow2.aspirerfid.commons.apdl.model.EBProc;
import org.ow2.aspirerfid.ide.bpwme.ecspec.utils.ECSpecBuilder;
import org.ow2.aspirerfid.ide.bpwme.ecspec.utils.LRSpecBuilder;
import org.ow2.aspirerfid.ide.bpwme.utils.MainControl;
import org.ow2.aspirerfid.ide.bpwme.ecspec.views.*;


public class EditECSpec extends AbstractActionDelegate 
implements IObjectActionDelegate {

	@Override
	protected void doRun(IProgressMonitor progressMonitor) {
		//get the selected EBProc part
		EBProcEditPart epe = (EBProcEditPart)getStructuredSelection().getFirstElement();		
		EBProcImpl ebi = (EBProcImpl)((View)epe.getModel()).getElement();
		
		//get the relevant EBProc in Apdl
		MainControl mc = MainControl.getMainControl();
		EBProc ebp = (EBProc)mc.objectMap.get(ebi.hashCode());
		
		mc.lrsb = new LRSpecBuilder(ebp);
		boolean newECSB = true;
		for(ApdlDataField adf:ebp.getApdlDataFields().getApdlDataField()) {
			//if there exists one ECSpec definition
			if(adf.getECSpec() != null) {
				mc.ecsb = new ECSpecBuilder(ebp, adf.getECSpec());
				newECSB = false;
				break;
			}
		}
		if(newECSB) {
			mc.ecsb = new ECSpecBuilder(ebp);
		}

		ECSpecEditorInput eei = new ECSpecEditorInput(mc.lrsb, mc.ecsb);
		
		IWorkbenchPage page = PlatformUI.getWorkbench().
		getActiveWorkbenchWindow().getActivePage();
		try {
			ECSpecEditor ese = (ECSpecEditor)page.openEditor(eei, ECSpecEditor.ID);
			ese.setDirty(true);
			mc.ecEditor = ese;
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}
}
