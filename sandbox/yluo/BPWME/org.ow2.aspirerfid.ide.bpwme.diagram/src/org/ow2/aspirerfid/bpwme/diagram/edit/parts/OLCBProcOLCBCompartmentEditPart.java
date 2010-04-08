package org.ow2.aspirerfid.bpwme.diagram.edit.parts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeCompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.CreationEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.DragDropEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ResizableCompartmentEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.figures.ResizableCompartmentFigure;
import org.eclipse.gmf.runtime.draw2d.ui.figures.ConstrainedToolbarLayout;
import org.eclipse.gmf.runtime.notation.View;
import org.ow2.aspirerfid.bpwme.diagram.edit.policies.OLCBProcOLCBCompartmentCanonicalEditPolicy;
import org.ow2.aspirerfid.bpwme.diagram.edit.policies.OLCBProcOLCBCompartmentItemSemanticEditPolicy;
import org.ow2.aspirerfid.bpwme.diagram.part.Messages;
import org.ow2.aspirerfid.bpwme.impl.CLCBProcImpl;
import org.ow2.aspirerfid.bpwme.impl.OLCBProcImpl;
import org.ow2.aspirerfid.bpwme.impl.WorkflowMapImpl;
import org.ow2.aspirerfid.ide.bpwme.utils.MainControl;



/**
 * @generated
 */
public class OLCBProcOLCBCompartmentEditPart extends ShapeCompartmentEditPart {

	/**
	 * @generated
	 */
	public static final int VISUAL_ID = 7001;

	/**
	 * @generated
	 */
	public OLCBProcOLCBCompartmentEditPart(View view) {
		super(view);
	}

	/**
	 * @generated
	 */
	public String getCompartmentName() {
		return Messages.OLCBProcOLCBCompartmentEditPart_title;
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
				new OLCBProcOLCBCompartmentItemSemanticEditPolicy());
		installEditPolicy(EditPolicyRoles.CREATION_ROLE,
				new CreationEditPolicy());
		installEditPolicy(EditPolicyRoles.DRAG_DROP_ROLE,
				new DragDropEditPolicy());
		installEditPolicy(EditPolicyRoles.CANONICAL_ROLE,
				new OLCBProcOLCBCompartmentCanonicalEditPolicy());
	}

	/**
	 * @generated
	 */
	protected void setRatio(Double ratio) {
		if (getFigure().getParent().getLayoutManager() instanceof ConstrainedToolbarLayout) {
			super.setRatio(ratio);
		}
	}
	
	@Override
	protected void handleNotificationEvent(Notification event) {
		super.handleNotificationEvent(event);
		if((event.getEventType() == event.ADD) 
				&& (event.getNewValue() instanceof CLCBProcImpl)) {
			OLCBProcImpl opi = (OLCBProcImpl)event.getNotifier();
			CLCBProcImpl cpi = (CLCBProcImpl)event.getNewValue();
			//get parent node
			MainControl mc = MainControl.getMainControl();
			org.ow2.aspirerfid.commons.apdl.model.CLCBProc clcb = mc.createCLCBProc();
			clcb.setId(cpi.getId());
			mc.addMap(cpi.hashCode(), clcb);
			//create child node
//			CLCBProc cp = new CLCBProc();
//			APDLFileHandler.olcbProc.getCLCBProc().add(cp);
//			//add to index
//			APDLFileHandler.addObject(cpi.hashCode(), cp);
//			System.out.println("ADD CLCB DONE");
		}else if((event.getEventType() == event.REMOVE) 
				&& (event.getOldValue() instanceof CLCBProcImpl)) {
			
			OLCBProcImpl opi = (OLCBProcImpl)event.getNotifier();
			CLCBProcImpl cpi = (CLCBProcImpl)event.getOldValue();
			MainControl mc = MainControl.getMainControl();
			org.ow2.aspirerfid.commons.apdl.model.CLCBProc clcb = 
				(org.ow2.aspirerfid.commons.apdl.model.CLCBProc) mc.getMapObject(cpi.hashCode());
			mc.removeCLCBProc(clcb);
			mc.delMap(cpi.hashCode());
			
//			//remove from apdl
//			CLCBProc cp = (CLCBProc) APDLFileHandler.getObject(cpi.hashCode());
//			APDLFileHandler.olcbProc.getCLCBProc().remove(cp);
//			APDLFileHandler.delObject(cpi.hashCode());
//			System.out.println("DEL CLCB DONE");
		}
		
	}

}
