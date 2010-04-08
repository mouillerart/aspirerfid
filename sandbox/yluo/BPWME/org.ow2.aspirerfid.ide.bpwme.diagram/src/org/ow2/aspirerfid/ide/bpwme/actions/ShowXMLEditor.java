package org.ow2.aspirerfid.ide.bpwme.actions;

import java.io.File;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.ow2.aspirerfid.ide.bpwme.utils.MainControl;

import bpwme.diagram.simpleditor.PathEditorInput;
import bpwme.diagram.simpleditor.SimpleEditor;


public class ShowXMLEditor extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// TODO Auto-generated method stub
		//ECSpecView.getECSpecView();
//		TestUtil tu = TestUtil.getUtil();
//		LRSpecEditorInput lei = new LRSpecEditorInput(tu.getLrsb(),tu.getEcsb());
//		IWorkbenchPage page = PlatformUI.getWorkbench().
//		getActiveWorkbenchWindow().getActivePage();
//		
//		File f = new File("lgylym.xml");
//		
//		IPath location= new Path(f.getAbsolutePath());
//		PathEditorInput input= new PathEditorInput(location);
//		SimpleEditor.setEditorInput(input);
//		SimpleEditor.getEditor();
		MainControl mc = MainControl.getMainControl();
		mc.getSimpleEditor();
		return null;
		
//		System.out.println(input);
//		try {
//			SimpleEditor edt = (SimpleEditor)page.openEditor(input, "bpwme.diagram.simpleditor.SimpleEditor");
//			System.out.println(edt);
//
//			//lrse.setDirty(true);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
	}
}
