package org.eclipse.bpmn2.modeler.ui.property.tabs;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.modeler.core.property.AbstractTabSection;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.widgets.Composite;

/**
 * General tabs for all elements
 * 
 * @author nico.rehwaldt
 */
public class GeneralTabSection extends AbstractTabSection {
	
	@Override
	protected Composite createCompositeForObject(Composite parent, EObject businessObject) {
		if (businessObject instanceof BaseElement) {
			return new GeneralTabCompositeFactory(this, parent).createCompositeForBusinessObject((BaseElement) businessObject);
		}
		
		return parent;
	}
}
