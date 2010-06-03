/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.ow2.aspirerfid.ide.bpwme.util;


import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;
import org.ow2.aspirerfid.ide.bpwme.*;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see org.ow2.aspirerfid.ide.bpwme.BpwmePackage
 * @generated
 */
public class BpwmeAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static BpwmePackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BpwmeAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = BpwmePackage.eINSTANCE;
		}
	}

	/**
	 * Returns whether this factory is applicable for the type of the object.
	 * <!-- begin-user-doc -->
	 * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
	 * <!-- end-user-doc -->
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object object) {
		if (object == modelPackage) {
			return true;
		}
		if (object instanceof EObject) {
			return ((EObject)object).eClass().getEPackage() == modelPackage;
		}
		return false;
	}

	/**
	 * The switch that delegates to the <code>createXXX</code> methods.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected BpwmeSwitch<Adapter> modelSwitch =
		new BpwmeSwitch<Adapter>() {
			@Override
			public Adapter caseWorkflowMap(WorkflowMap object) {
				return createWorkflowMapAdapter();
			}
			@Override
			public Adapter caseNode(Node object) {
				return createNodeAdapter();
			}
			@Override
			public Adapter caseConnection(Connection object) {
				return createConnectionAdapter();
			}
			@Override
			public Adapter caseOLCBProc(OLCBProc object) {
				return createOLCBProcAdapter();
			}
			@Override
			public Adapter caseCLCBProc(CLCBProc object) {
				return createCLCBProcAdapter();
			}
			@Override
			public Adapter caseEBProc(EBProc object) {
				return createEBProcAdapter();
			}
			@Override
			public Adapter caseTransitions(Transitions object) {
				return createTransitionsAdapter();
			}
			@Override
			public Adapter caseTransition(Transition object) {
				return createTransitionAdapter();
			}
			@Override
			public Adapter caseExtendedAttribute(ExtendedAttribute object) {
				return createExtendedAttributeAdapter();
			}
			@Override
			public Adapter caseExtendedAttributes(ExtendedAttributes object) {
				return createExtendedAttributesAdapter();
			}
			@Override
			public Adapter caseCondition(Condition object) {
				return createConditionAdapter();
			}
			@Override
			public Adapter defaultCase(EObject object) {
				return createEObjectAdapter();
			}
		};

	/**
	 * Creates an adapter for the <code>target</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param target the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	@Override
	public Adapter createAdapter(Notifier target) {
		return modelSwitch.doSwitch((EObject)target);
	}


	/**
	 * Creates a new adapter for an object of class '{@link org.ow2.aspirerfid.ide.bpwme.WorkflowMap <em>Workflow Map</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.ow2.aspirerfid.ide.bpwme.WorkflowMap
	 * @generated
	 */
	public Adapter createWorkflowMapAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.ow2.aspirerfid.ide.bpwme.Node <em>Node</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.ow2.aspirerfid.ide.bpwme.Node
	 * @generated
	 */
	public Adapter createNodeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.ow2.aspirerfid.ide.bpwme.Connection <em>Connection</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.ow2.aspirerfid.ide.bpwme.Connection
	 * @generated
	 */
	public Adapter createConnectionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.ow2.aspirerfid.ide.bpwme.OLCBProc <em>OLCB Proc</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.ow2.aspirerfid.ide.bpwme.OLCBProc
	 * @generated
	 */
	public Adapter createOLCBProcAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.ow2.aspirerfid.ide.bpwme.CLCBProc <em>CLCB Proc</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.ow2.aspirerfid.ide.bpwme.CLCBProc
	 * @generated
	 */
	public Adapter createCLCBProcAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.ow2.aspirerfid.ide.bpwme.EBProc <em>EB Proc</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.ow2.aspirerfid.ide.bpwme.EBProc
	 * @generated
	 */
	public Adapter createEBProcAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.ow2.aspirerfid.ide.bpwme.Transitions <em>Transitions</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.ow2.aspirerfid.ide.bpwme.Transitions
	 * @generated
	 */
	public Adapter createTransitionsAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.ow2.aspirerfid.ide.bpwme.Transition <em>Transition</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.ow2.aspirerfid.ide.bpwme.Transition
	 * @generated
	 */
	public Adapter createTransitionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.ow2.aspirerfid.ide.bpwme.ExtendedAttribute <em>Extended Attribute</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.ow2.aspirerfid.ide.bpwme.ExtendedAttribute
	 * @generated
	 */
	public Adapter createExtendedAttributeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.ow2.aspirerfid.ide.bpwme.ExtendedAttributes <em>Extended Attributes</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.ow2.aspirerfid.ide.bpwme.ExtendedAttributes
	 * @generated
	 */
	public Adapter createExtendedAttributesAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.ow2.aspirerfid.ide.bpwme.Condition <em>Condition</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.ow2.aspirerfid.ide.bpwme.Condition
	 * @generated
	 */
	public Adapter createConditionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for the default case.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter() {
		return null;
	}

} //BpwmeAdapterFactory
