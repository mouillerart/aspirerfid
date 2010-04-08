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
 * A representation of the model object '<em><b>Extended Attributes</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link bpwme.ExtendedAttributes#getExtendedAttribute <em>Extended Attribute</em>}</li>
 * </ul>
 * </p>
 *
 * @see bpwme.BpwmePackage#getExtendedAttributes()
 * @model
 * @generated
 */
public interface ExtendedAttributes extends EObject {
	/**
	 * Returns the value of the '<em><b>Extended Attribute</b></em>' containment reference list.
	 * The list contents are of type {@link bpwme.ExtendedAttribute}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Extended Attribute</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Extended Attribute</em>' containment reference list.
	 * @see bpwme.BpwmePackage#getExtendedAttributes_ExtendedAttribute()
	 * @model containment="true"
	 * @generated
	 */
	EList<ExtendedAttribute> getExtendedAttribute();

} // ExtendedAttributes