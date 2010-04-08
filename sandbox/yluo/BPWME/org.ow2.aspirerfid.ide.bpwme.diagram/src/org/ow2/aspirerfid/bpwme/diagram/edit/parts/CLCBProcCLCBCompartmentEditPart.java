package org.ow2.aspirerfid.bpwme.diagram.edit.parts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ListCompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeCompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.CreationEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.DragDropEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ResizableCompartmentEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.figures.ResizableCompartmentFigure;
import org.eclipse.gmf.runtime.draw2d.ui.figures.ConstrainedToolbarLayout;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.ui.PlatformUI;
import org.ow2.aspirerfid.bpwme.diagram.edit.policies.CLCBProcCLCBCompartmentCanonicalEditPolicy;
import org.ow2.aspirerfid.bpwme.diagram.edit.policies.CLCBProcCLCBCompartmentItemSemanticEditPolicy;
import org.ow2.aspirerfid.bpwme.diagram.part.Messages;
import org.ow2.aspirerfid.bpwme.impl.CLCBProcImpl;
import org.ow2.aspirerfid.bpwme.impl.EBProcImpl;
import org.ow2.aspirerfid.ide.bpwme.dialog.ReportTypeDialog;
import org.ow2.aspirerfid.ide.bpwme.utils.MainControl;
import org.ow2.aspirerfid.ide.bpwme.utils.MainControl.EventType;



/**
 * @generated
 */
public class CLCBProcCLCBCompartmentEditPart extends ShapeCompartmentEditPart {

	/**
	 * @generated
	 */
	public static final int VISUAL_ID = 7002;

	/**
	 * @generated
	 */
	public CLCBProcCLCBCompartmentEditPart(View view) {
		super(view);
	}

	/**
	 * @generated
	 */
	public String getCompartmentName() {
		return Messages.CLCBProcCLCBCompartmentEditPart_title;
	}

	/**
	 * @generated
	 */
	public IFigure createFigure() {
		ResizableCompartmentFigure result = (ResizableCompartmentFigure) super
				.createFigure();
		result.setTitleVisibility(false);
		return result;
	}

	/**
	 * @generated
	 */
	protected void createDefaultEditPolicies() {
		super.createDefaultEditPolicies();
		installEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE,
				new ResizableCompartmentEditPolicy());
		installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE,
				new CLCBProcCLCBCompartmentItemSemanticEditPolicy());
		installEditPolicy(EditPolicyRoles.CREATION_ROLE,
				new CreationEditPolicy());
		installEditPolicy(EditPolicyRoles.DRAG_DROP_ROLE,
				new DragDropEditPolicy());
		installEditPolicy(EditPolicyRoles.CANONICAL_ROLE,
				new CLCBProcCLCBCompartmentCanonicalEditPolicy());
	}

	/**
	 * @generated
	 */
	protected void setRatio(Double ratio) {
		if (getFigure().getParent().getLayoutManager() instanceof ConstrainedToolbarLayout) {
			super.setRatio(ratio);
		}
	}
	/*
	 * To synchronize the in-editor model and diagram with the APDL file
	 * Handling add, remove action to the included node (EBProc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeCompartmentEditPart#handleNotificationEvent(org.eclipse.emf.common.notify.Notification)
	 */
	protected void handleNotificationEvent(Notification event) {
		super.handleNotificationEvent(event);
		
		if((event.getOldValue() instanceof EBProcImpl) 
				&& (event.getEventType() == Notification.REMOVE)) {
			
			CLCBProcImpl cpi = (CLCBProcImpl)event.getNotifier();
			EBProcImpl epi = (EBProcImpl)event.getOldValue();
			
			MainControl mc = MainControl.getMainControl();
			org.ow2.aspirerfid.commons.apdl.model.CLCBProc clcb =
				(org.ow2.aspirerfid.commons.apdl.model.CLCBProc)mc.getMapObject(cpi.hashCode());
			org.ow2.aspirerfid.commons.apdl.model.EBProc ebproc = 
				(org.ow2.aspirerfid.commons.apdl.model.EBProc)mc.getMapObject(epi.hashCode());
			clcb.getEBProc().remove(ebproc);
			mc.saveObject();
			mc.delMap(epi.hashCode());
			
			mc.ebprocMap.remove(ebproc);
//			CLCBProc cp = (CLCBProc)APDLFileHandler.getObject(cpi.hashCode());
//			EBProc ep = (EBProc)APDLFileHandler.getObject(epi.hashCode());
//			
//			if(cp == null) {
//				System.out.println("Find no CLCB Node\n" + cpi + "\n in APDL file");
//				return;
//			}
//			cp.getEBProc().remove(ep);
//			APDLFileHandler.delObject(epi.hashCode());
			//System.out.println("DEL EB DONE");
			//APDLFileHandler.printStructure();
			//System.out.println(event);
		}else if ((event.getNewValue() instanceof EBProcImpl) 
				&&(event.getEventType() == Notification.ADD)) {
			CLCBProcImpl cpi = (CLCBProcImpl)event.getNotifier();
			EBProcImpl epi = (EBProcImpl)event.getNewValue();
			
			ReportTypeDialog rtd = new ReportTypeDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell());
			rtd.setText("Select Event Type");
			EventType result = rtd.open();
			
			
			MainControl mc = MainControl.getMainControl();
			org.ow2.aspirerfid.commons.apdl.model.CLCBProc clcb = 
				(org.ow2.aspirerfid.commons.apdl.model.CLCBProc) mc.getMapObject(cpi.hashCode());
			org.ow2.aspirerfid.commons.apdl.model.EBProc ebproc = mc.createEBProc(clcb);
			
			ebproc.setId(epi.getId());
			mc.addMap(epi.hashCode(), ebproc);
			
			mc.ebprocMap.put(ebproc.getId(), result);
			mc.saveAssistantFile();
			
			//1. get the parent node
//			CLCBProc cp = (CLCBProc)APDLFileHandler.getObject(cpi.hashCode());
//			if(cp == null) {
//				System.out.println("Find no CLCB Node\n" + cpi + "\n in APDL file");
//				return;
//			}
//			//2. add the node to the parent node
//			EBProc ep = new EBProc();
//			cp.getEBProc().add(ep);
//			//3. add to index
//			APDLFileHandler.addObject(epi.hashCode(), ep);
			//System.out.println("ADD EB DONE");
			//APDLFileHandler.printStructure();
		}	
	}
}
