package org.eclipse.bpmn2.modeler.core.test.assertions;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.mm.pictograms.PictogramLink;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.fest.assertions.api.AbstractAssert;
import org.fest.assertions.api.Assertions;
import org.fest.assertions.core.Condition;

public abstract class AbstractShapeAssert<S extends AbstractShapeAssert<S, A>, A extends Shape> extends AbstractAssert<S, A> {

	public AbstractShapeAssert(A actual, Class<S> assertCls) {
		super(actual, assertCls);
	}
	
	public abstract AbstractShapeAssert<S, A> hasChildren();
	
	public abstract AbstractShapeAssert<S, A> hasChildLinkedTo(Condition<EObject> condition);
	
	public AbstractShapeAssert<S, A> isLinkedTo(Condition<EObject> condition) {
		PictogramLink link = actual.getLink();
		Assertions
			.assertThat(link)
			.isNotNull();
		
		EList<EObject> businessObjects = link.getBusinessObjects();
		
		
		Assertions
			.assertThat(businessObjects)
			.isNotNull()
			.areAtLeast(1, condition);
		
		return myself;
	}

	public abstract AbstractShapeAssert<S, A> isContainerShape();
	
	public abstract AbstractShapeAssert<S, A> hasNoChildren();
}
