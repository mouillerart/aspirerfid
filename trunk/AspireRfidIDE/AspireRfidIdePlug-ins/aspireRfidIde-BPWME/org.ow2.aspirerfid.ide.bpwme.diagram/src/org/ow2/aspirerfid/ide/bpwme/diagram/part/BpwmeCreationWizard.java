package org.ow2.aspirerfid.ide.bpwme.diagram.part;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.gmf.runtime.emf.core.resources.GMFResource;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PartInitException;
import org.ow2.aspirerfid.ide.bpwme.BpwmeFactory;
import org.ow2.aspirerfid.ide.bpwme.OLCBProc;
import org.ow2.aspirerfid.ide.bpwme.WorkflowMap;
import org.ow2.aspirerfid.ide.bpwme.diagram.application.WizardNewProjectCreationPage;
import org.ow2.aspirerfid.ide.bpwme.diagram.edit.parts.WorkflowMapEditPart;
import org.ow2.aspirerfid.ide.bpwme.impl.WorkflowMapImpl;
import org.ow2.aspirerfid.ide.bpwme.utils.MainUtil;

/**
 * @generated
 */
public class BpwmeCreationWizard extends Wizard implements INewWizard {

	/**
	 * @generated
	 */
	private IWorkbench workbench;

	/**
	 * @generated
	 */
	protected IStructuredSelection selection;

	/**
	 * @generated
	 */
//	protected BpwmeCreationWizardPage diagramModelFilePage;

	protected WizardNewProjectCreationPage newProjectPage;
	
	/**
	 * @generated
	 */
	//protected BpwmeCreationWizardPage domainModelFilePage;

	/**
	 * @generated
	 */
	protected Resource diagram;

	/**
	 * @generated
	 */
	private boolean openNewlyCreatedDiagramEditor = true;

	/**
	 * @generated
	 */
	public IWorkbench getWorkbench() {
		return workbench;
	}

	/**
	 * @generated
	 */
	public IStructuredSelection getSelection() {
		return selection;
	}

	/**
	 * @generated
	 */
	public final Resource getDiagram() {
		return diagram;
	}

	/**
	 * @generated
	 */
	public final boolean isOpenNewlyCreatedDiagramEditor() {
		return openNewlyCreatedDiagramEditor;
	}

	/**
	 * @generated
	 */
	public void setOpenNewlyCreatedDiagramEditor(
			boolean openNewlyCreatedDiagramEditor) {
		this.openNewlyCreatedDiagramEditor = openNewlyCreatedDiagramEditor;
	}

	/**
	 * @generated
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.workbench = workbench;
		this.selection = selection;
		setWindowTitle(Messages.BpwmeCreationWizardTitle);
		setDefaultPageImageDescriptor(BpwmeDiagramEditorPlugin
				.getBundledImageDescriptor("icons/wizban/NewBpwmeWizard.gif")); //$NON-NLS-1$
		setNeedsProgressMonitor(true);
	}

	/**
	 * @generated
	 */
	public void addPages() {
		newProjectPage = new WizardNewProjectCreationPage("DiagramModelFile");
		newProjectPage.setTitle("Create new project");
		newProjectPage.setDescription("Create a new bpwme project");
		addPage(newProjectPage);		
		
//		diagramModelFilePage = new BpwmeCreationWizardPage(
//				"DiagramModelFile", getSelection(), "bpwme_diagram"); //$NON-NLS-1$ //$NON-NLS-2$
//		diagramModelFilePage
//				.setTitle(Messages.BpwmeCreationWizard_DiagramModelFilePageTitle);
//		diagramModelFilePage
//				.setDescription(Messages.BpwmeCreationWizard_DiagramModelFilePageDescription);
//		addPage(diagramModelFilePage);
	}

	public boolean performFinish() {
		IRunnableWithProgress op = new IRunnableWithProgress() {

			public void run(IProgressMonitor monitor)
					throws InvocationTargetException, InterruptedException {
				diagram = BpwmeDiagramEditorUtil.createDiagram(newProjectPage.getURI(),monitor);
				
//				diagram = BpwmeDiagramEditorUtil.createDiagram(
//						diagramModelFilePage.getURI(),monitor);
				
				if (isOpenNewlyCreatedDiagramEditor() && diagram != null) {
					try {
						BpwmeDiagramEditorUtil.openDiagram(diagram);
						
						IEditorPart editor = MainUtil.getEditor(BpwmeDiagramEditor.ID);
						if(editor != null) {
							BpwmeDiagramEditor beditor = (BpwmeDiagramEditor)editor;
							WorkflowMapEditPart wPart = (WorkflowMapEditPart)beditor.getDiagramEditPart();
							WorkflowMapImpl wmp = (WorkflowMapImpl)((View)wPart.getModel()).getElement();
							OLCBProc olcb = BpwmeFactory.eINSTANCE.createOLCBProc();
							//OLCBProcEditPart olcbp = BpwmeEditPartFactory
						}
						
//						GMFResource gr = (GMFResource)diagram;
//						EList<EObject> objects = gr.getContents();
//						
//						for(EObject object : objects) {
//							//if(object instanceof WorkflowMapEditPart) {
//								System.out.println(object);								
//							//}
//						}

						
					} catch (PartInitException e) {
						ErrorDialog.openError(getContainer().getShell(),
								Messages.BpwmeCreationWizardOpenEditorError,
								null, e.getStatus());
					}
				}
			}
		};
		try {
			getContainer().run(false, true, op);
		} catch (InterruptedException e) {
			return false;
		} catch (InvocationTargetException e) {
			if (e.getTargetException() instanceof CoreException) {
				ErrorDialog.openError(getContainer().getShell(),
						Messages.BpwmeCreationWizardCreationError, null,
						((CoreException) e.getTargetException()).getStatus());
			} else {
				BpwmeDiagramEditorPlugin.getInstance().logError(
						"Error creating diagram", e.getTargetException()); //$NON-NLS-1$
			}
			return false;
		}
		return diagram != null;
	}
}
