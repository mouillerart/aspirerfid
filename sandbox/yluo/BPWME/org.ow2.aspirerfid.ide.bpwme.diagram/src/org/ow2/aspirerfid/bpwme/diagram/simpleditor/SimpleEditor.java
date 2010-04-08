package org.ow2.aspirerfid.bpwme.diagram.simpleditor;

import java.io.File;
import java.util.HashMap;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.AbstractTextEditor;


/**
 * A simple text editor.
 * 
 * @see org.eclipse.ui.examples.rcp.texteditor.editors.SimpleDocumentProvider
 * @since 3.0
 */
public class SimpleEditor extends AbstractTextEditor {

	public static String ID = "bpwme.diagram.simpleditor.SimpleEditor";
	protected static SimpleEditor singleEditor;
	protected static IEditorInput input;
	
	public SimpleEditor() {
		super();
		// make sure we inherit all the text editing commands (delete line etc).
		setKeyBindingScopes(new String[] { "org.eclipse.ui.textEditorScope" });  //$NON-NLS-1$
		internal_init();
		singleEditor = this;
	}
	
	public static void setEditorInput(IEditorInput input) {
		SimpleEditor.input = input;
	}
	
	public static SimpleEditor getEditor() {
		//if(singleEditor == null) {
//		if(singleEditor != null) {
//			IWorkbenchPage page = singleEditor.getEditorSite().getPage();
//			page.closeEditor(singleEditor, singleEditor.isDirty());
//		}
		IWorkbenchPage page = PlatformUI.getWorkbench().
		getActiveWorkbenchWindow().getActivePage();

		try {
			singleEditor = (SimpleEditor)page.openEditor(input, "bpwme.diagram.simpleditor.SimpleEditor");
			//lrse.setDirty(true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//}
		//System.out.println(singleEditor);
		return singleEditor;
	}
	
	
	
	@Override
	public void close(boolean save) {
		// TODO Auto-generated method stub
		super.close(save);
		singleEditor = null;
	}

	/**
	 * Initializes the document provider and source viewer configuration.
	 * Called by the constructor. Subclasses may replace this method.
	 */
	protected void internal_init() {
		configureInsertMode(SMART_INSERT, false);
		setDocumentProvider(new SimpleDocumentProvider());
	}
	
	public static void refresh() {
		if(singleEditor == null) {
			return;
		} else {
			IWorkbenchPage page = singleEditor.getEditorSite().getPage();
			page.closeEditor(singleEditor, singleEditor.isDirty());
			try {
				singleEditor = (SimpleEditor)page.openEditor(singleEditor.getEditorInput(), "bpwme.diagram.simpleditor.SimpleEditor");
			} catch (PartInitException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
