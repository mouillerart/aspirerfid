package org.ow2.aspirerfid.ide.bpwme.actions;

import java.io.File;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.common.ui.URIEditorInput;
import org.eclipse.emf.common.util.URI;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.ow2.aspirerfid.commons.apdl.model.*;
import org.ow2.aspirerfid.ide.bpwme.utils.MainControl;
import org.ow2.aspirerfid.ide.bpwme.utils.OLCBProcAssistant;
import org.ow2.aspirerfid.ide.bpwme.utils.MainControl.FileAction;

import bpwme.CLCBProc;
import bpwme.EBProc;
import bpwme.diagram.part.BpwmeDiagramEditor;
import bpwme.impl.CLCBProcImpl;
import bpwme.impl.OLCBProcImpl;

public class OpenDiagram extends AbstractHandler{

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		MainControl mc = MainControl.getMainControl();
		mc.fa = FileAction.OpenAction;
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		IWorkbenchPage page = window.getActivePage();
		FileDialog fileDialog = new FileDialog(window.getShell(),
				SWT.OPEN);
		fileDialog.open();
		if (fileDialog.getFileName() != null
				&& fileDialog.getFileName().length() > 0) {
			URI fileURI = URI.createFileURI(fileDialog.getFilterPath()
					+ File.separator + fileDialog.getFileName());
			try {
				page.openEditor(new URIEditorInput(fileURI), BpwmeDiagramEditor.ID);
			} catch (PartInitException e) {
				e.printStackTrace();
			}
			mc.setAPDLFileName(fileURI);
			//System.out.println("1.1.1");
			mc.rebuild();
			//System.out.println(mc.olcbProc);
			mc.mapModels();
			mc.loadAssistantFile();
			//mapModels(mc);
			//System.out.println(mc.objectMap);
		}
		return null;
	}
	
	//map diagram and apdl model together
	//diagram in mc.olcbep
	//apdl model in mc.olcbProc
//	private void mapModels(MainControl mc) {
//		//1. from diagram get the logical model
//		OLCBProcImpl opi = (OLCBProcImpl)((View)mc.olcbep.getModel()).getElement();
//		//2. from apdl file get the real model
//		OLCBProc op = mc.olcbProc;
//		OLCBProcAssistant oa = new OLCBProcAssistant(op);
//		//3. do the model mapping by id
//		mc.objectMap.clear();
//		if(!opi.getId().equals(op.getId())) {
//			System.err.println("Wrong Mapping");
//			return;
//		}
//		mc.addMap(opi.hashCode(), op);
//		for(CLCBProc cpi : opi.getCLCBProc()) {
//			org.ow2.aspirerfid.commons.apdl.model.CLCBProc cp;
//			if((cp = oa.getCLCB(cpi.getId())) != null) {
//				mc.addMap(cpi.hashCode(), cp);
//				for(EBProc ebi : cpi.getEBProc()) {
//					org.ow2.aspirerfid.commons.apdl.model.EBProc ep;
//					if((ep = oa.getEBProc(cp, ebi.getId())) != null) {
//						mc.addMap(ebi.hashCode(), ep);
//					}else {
//						System.err.println("Wrong Mapping");
//						return;
//					}
//				}
//			}else {
//				System.err.println("Wrong Mapping");
//				return;
//			}
//		}
//	}
}
