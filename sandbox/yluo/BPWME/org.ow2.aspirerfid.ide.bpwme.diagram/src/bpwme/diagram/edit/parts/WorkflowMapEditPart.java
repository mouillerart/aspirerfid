package bpwme.diagram.edit.parts;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartListener;
import org.eclipse.gef.NodeListener;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.notation.View;
import org.ow2.aspirerfid.ide.bpwme.test.FakeListener;
import org.ow2.aspirerfid.ide.bpwme.utils.MainControl;
import org.ow2.aspirerfid.ide.bpwme.utils.MainControl.FileAction;

import bpwme.diagram.edit.policies.WorkflowMapCanonicalEditPolicy;
import bpwme.diagram.edit.policies.WorkflowMapItemSemanticEditPolicy;
import bpwme.impl.OLCBProcImpl;
import bpwme.impl.WorkflowMapImpl;

/**
 * @generated
 */
public class WorkflowMapEditPart extends DiagramEditPart {

	/**
	 * @generated
	 */
	public final static String MODEL_ID = "Bpwme"; //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final int VISUAL_ID = 1000;

	/**
	 * @generated
	 */
	public WorkflowMapEditPart(View view) {
		super(view);
		//MainControl mc = MainControl.getMainControl();
		//mc.wme = this;
		//mc.fl = new FakeListener();
		//this.addEditPartListener(mc.fl);
	}

	/**
	 * @generated
	 */
	protected void createDefaultEditPolicies() {
		super.createDefaultEditPolicies();
		installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE,
				new WorkflowMapItemSemanticEditPolicy());
		installEditPolicy(EditPolicyRoles.CANONICAL_ROLE,
				new WorkflowMapCanonicalEditPolicy());
		// removeEditPolicy(org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles.POPUPBAR_ROLE);
	}
	
	@Override
	protected void addChild(EditPart child, int index) {
		//robert add
		MainControl mc = MainControl.getMainControl();
		if(mc.fa == FileAction.OpenAction) {
			//System.out.println("Add Child:" + child);
			mc.olcbep = (OLCBProcEditPart) child;
		}
		super.addChild(child, index);
	}
	
	
	@Override
	protected void handleNotificationEvent(Notification event) {
		super.handleNotificationEvent(event);
		//robert add
		//System.out.println(event);
		if((event.getEventType() == Notification.ADD) 
				&& (event.getNewValue() instanceof OLCBProcImpl)) {		
			OLCBProcImpl opi = (OLCBProcImpl)event.getNewValue();
//			
			//no parent node
//			if(APDLFileHandler.olcbProc == null) {
//				APDLFileHandler.olcbProc = new OLCBProc();
//			}
//			//add to index
//			APDLFileHandler.addObject(opi.hashCode(), APDLFileHandler.olcbProc);
//			System.out.println("ADD OLCB DONE");
			
			try {
				MainControl mc = MainControl.getMainControl();
				org.ow2.aspirerfid.commons.apdl.model.OLCBProc olcbp = mc.createOLCBProc();
				olcbp.setId(opi.getId());
				mc.addMap(opi.hashCode(), olcbp);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else if((event.getEventType() == Notification.REMOVE) 
				&& (event.getOldValue() instanceof OLCBProcImpl)) {
			OLCBProcImpl opi = (OLCBProcImpl)event.getOldValue();
			MainControl mc = MainControl.getMainControl();
			mc.delMap(opi.hashCode());			
		}
	}
}
