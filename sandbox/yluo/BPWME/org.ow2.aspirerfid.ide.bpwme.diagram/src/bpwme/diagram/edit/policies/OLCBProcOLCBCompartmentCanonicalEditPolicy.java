package bpwme.diagram.edit.policies;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.gmf.runtime.diagram.ui.editpolicies.CanonicalEditPolicy;
import org.eclipse.gmf.runtime.notation.View;

import bpwme.BpwmePackage;
import bpwme.diagram.edit.parts.CLCBProcEditPart;
import bpwme.diagram.part.BpwmeDiagramUpdater;
import bpwme.diagram.part.BpwmeNodeDescriptor;
import bpwme.diagram.part.BpwmeVisualIDRegistry;

/**
 * @generated
 */
public class OLCBProcOLCBCompartmentCanonicalEditPolicy extends
		CanonicalEditPolicy {

	/**
	 * @generated
	 */
	Set myFeaturesToSynchronize;

	/**
	 * @generated
	 */
	protected List getSemanticChildrenList() {
		View viewObject = (View) getHost().getModel();
		List result = new LinkedList();
		for (Iterator it = BpwmeDiagramUpdater
				.getOLCBProcOLCBCompartment_7001SemanticChildren(viewObject)
				.iterator(); it.hasNext();) {
			result.add(((BpwmeNodeDescriptor) it.next()).getModelElement());
		}
		return result;
	}

	/**
	 * @generated
	 */
	protected boolean isOrphaned(Collection semanticChildren, final View view) {
		int visualID = BpwmeVisualIDRegistry.getVisualID(view);
		switch (visualID) {
		case CLCBProcEditPart.VISUAL_ID:
			if (!semanticChildren.contains(view.getElement())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @generated
	 */
	protected String getDefaultFactoryHint() {
		return null;
	}

	/**
	 * @generated
	 */
	protected Set getFeaturesToSynchronize() {
		if (myFeaturesToSynchronize == null) {
			myFeaturesToSynchronize = new HashSet();
			myFeaturesToSynchronize.add(BpwmePackage.eINSTANCE
					.getOLCBProc_CLCBProc());
		}
		return myFeaturesToSynchronize;
	}

}
