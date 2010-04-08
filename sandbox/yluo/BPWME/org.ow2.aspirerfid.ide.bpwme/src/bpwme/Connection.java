/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package bpwme;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Connection</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link bpwme.Connection#getSourceNode <em>Source Node</em>}</li>
 *   <li>{@link bpwme.Connection#getTargetNode <em>Target Node</em>}</li>
 * </ul>
 * </p>
 *
 * @see bpwme.BpwmePackage#getConnection()
 * @model
 * @generated
 */
public interface Connection extends EObject {
	/**
	 * Returns the value of the '<em><b>Source Node</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link bpwme.Node#getOutgoingConnections <em>Outgoing Connections</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Source Node</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Source Node</em>' reference.
	 * @see #setSourceNode(Node)
	 * @see bpwme.BpwmePackage#getConnection_SourceNode()
	 * @see bpwme.Node#getOutgoingConnections
	 * @model opposite="outgoingConnections" required="true"
	 * @generated
	 */
	Node getSourceNode();

	/**
	 * Sets the value of the '{@link bpwme.Connection#getSourceNode <em>Source Node</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Source Node</em>' reference.
	 * @see #getSourceNode()
	 * @generated
	 */
	void setSourceNode(Node value);

	/**
	 * Returns the value of the '<em><b>Target Node</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link bpwme.Node#getIngoingConnections <em>Ingoing Connections</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Target Node</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Target Node</em>' reference.
	 * @see #setTargetNode(Node)
	 * @see bpwme.BpwmePackage#getConnection_TargetNode()
	 * @see bpwme.Node#getIngoingConnections
	 * @model opposite="ingoingConnections" required="true"
	 * @generated
	 */
	Node getTargetNode();

	/**
	 * Sets the value of the '{@link bpwme.Connection#getTargetNode <em>Target Node</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Target Node</em>' reference.
	 * @see #getTargetNode()
	 * @generated
	 */
	void setTargetNode(Node value);

} // Connection
