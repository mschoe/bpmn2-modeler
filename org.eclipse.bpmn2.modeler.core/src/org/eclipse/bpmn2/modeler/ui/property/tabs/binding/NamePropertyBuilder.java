package org.eclipse.bpmn2.modeler.ui.property.tabs.binding;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.CallableElement;
import org.eclipse.bpmn2.Collaboration;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.Lane;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.modeler.ui.property.tabs.builder.AbstractPropertiesBuilder;
import org.eclipse.bpmn2.modeler.ui.property.tabs.util.PropertyUtil;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.widgets.Composite;

/**
 * Builder for the name property
 * 
 * @author nico.rehwaldt
 */
public class NamePropertyBuilder extends AbstractPropertiesBuilder<BaseElement> {

	private final EStructuralFeature NAME_FEATURE;
	
	private String label;

	public NamePropertyBuilder(Composite parent, GFPropertySection section, BaseElement bo, String label) {
		super(parent, section, bo);
		
		this.label = label;

		if (bo instanceof FlowElement) {
			NAME_FEATURE = Bpmn2Package.eINSTANCE.getFlowElement_Name();
		} else 
		if (bo instanceof Collaboration) {
			NAME_FEATURE = Bpmn2Package.eINSTANCE.getCollaboration_Name();
		} else
		if (bo instanceof CallableElement) {
			NAME_FEATURE = Bpmn2Package.eINSTANCE.getCallableElement_Name();
		} else
		if (bo instanceof Participant) {
			NAME_FEATURE = Bpmn2Package.eINSTANCE.getParticipant_Name();
		} else 
		if (bo instanceof Lane) {
			NAME_FEATURE = Bpmn2Package.eINSTANCE.getLane_Name();
		} else {
			throw new IllegalArgumentException("Unsupported base element: " + bo);
		}
	}
	
	public NamePropertyBuilder(Composite parent, GFPropertySection section, BaseElement bo) {
		this(parent, section, bo, "Name");
	}
	
	@Override
	public void create() {
		PropertyUtil.createText(section, parent, label, NAME_FEATURE, bo);
	}
}
