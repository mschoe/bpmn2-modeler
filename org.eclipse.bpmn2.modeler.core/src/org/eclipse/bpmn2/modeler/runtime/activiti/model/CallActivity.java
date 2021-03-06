/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.eclipse.bpmn2.modeler.runtime.activiti.model;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Call Activity</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.bpmn2.modeler.runtime.activiti.model.CallActivity#getCalledElement <em>Called Element</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.bpmn2.modeler.runtime.activiti.model.ModelPackage#getCallActivity()
 * @model extendedMetaData="name='tCallActivity' kind='elementOnly'"
 * @generated
 */
public interface CallActivity extends org.eclipse.bpmn2.CallActivity {
	/**
   * Returns the value of the '<em><b>Called Element</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Called Element</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Called Element</em>' attribute.
   * @see #setCalledElement(String)
   * @see org.eclipse.bpmn2.modeler.runtime.activiti.model.ModelPackage#getCallActivity_CalledElement()
   * @model extendedMetaData="kind='attribute' name='calledElement'"
   * @generated
   */
	String getCalledElement();

	/**
   * Sets the value of the '{@link org.eclipse.bpmn2.modeler.runtime.activiti.model.CallActivity#getCalledElement <em>Called Element</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Called Element</em>' attribute.
   * @see #getCalledElement()
   * @generated
   */
	void setCalledElement(String value);

} // CallActivity
