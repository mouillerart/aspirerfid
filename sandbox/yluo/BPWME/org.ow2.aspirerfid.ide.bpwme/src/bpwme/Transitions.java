/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package bpwme;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Transitions</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link bpwme.Transitions#getTransition <em>Transition</em>}</li>
 * </ul>
 * </p>
 *
 * @see bpwme.BpwmePackage#getTransitions()
 * @model
 * @generated
 */
public interface Transitions extends EObject {
	/**
	 * Returns the value of the '<em><b>Transition</b></em>' reference list.
	 * The list contents are of type {@link bpwme.Transition}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Transition</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Transition</em>' reference list.
	 * @see bpwme.BpwmePackage#getTransitions_Transition()
	 * @model
	 * @generated
	 */
	EList<Transition> getTransition();

} // Transitions