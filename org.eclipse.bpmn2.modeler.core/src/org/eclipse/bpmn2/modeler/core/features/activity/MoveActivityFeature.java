/******************************************************************************* 
 * Copyright (c) 2011 Red Hat, Inc. 
 *  All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 *
 * @author Ivar Meikas
 ******************************************************************************/
package org.eclipse.bpmn2.modeler.core.features.activity;

import static org.eclipse.bpmn2.modeler.core.utils.ContextUtil.copyProperties;

import org.eclipse.bpmn2.SubProcess;
import org.eclipse.bpmn2.modeler.core.features.MoveFlowNodeFeature;
import org.eclipse.bpmn2.modeler.core.features.PropertyNames;
import org.eclipse.bpmn2.modeler.core.features.event.AbstractBoundaryEventOperation;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.core.utils.ContextUtil;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IMoveShapeFeature;
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.eclipse.graphiti.features.context.impl.MoveShapeContext;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;

public class MoveActivityFeature extends MoveFlowNodeFeature {

	public static final String ACTIVITY_MOVE_PROPERTY = "activity.move";
	public static final String SELECTION_MOVE_PROPERTY = "selection.move";

	public MoveActivityFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	protected void postMoveShape(final IMoveShapeContext context) {
		
		PictogramElement containerShape = context.getPictogramElement();
		
		repositionBoundaryEvents(context);
		
		if (containerShape.eContainer() instanceof ContainerShape) {
			PictogramElement pe = (PictogramElement) containerShape.eContainer();
			if (BusinessObjectUtil.containsElementOfType(pe, SubProcess.class)) {
				layoutPictogramElement(pe);
			}
		}
		
		// perform post move and layout
		super.postMoveShape(context);
	}
	
	private void repositionBoundaryEvent(ContainerShape container, IMoveShapeContext context) {

		GraphicsAlgorithm ga = container.getGraphicsAlgorithm();
		
		MoveShapeContext newContext = new MoveShapeContext(container);
		newContext.setDeltaX(context.getDeltaX());
		newContext.setDeltaY(context.getDeltaY());
		newContext.setSourceContainer(context.getSourceContainer());
		newContext.setTargetContainer(context.getTargetContainer());
		newContext.setTargetConnection(context.getTargetConnection());
		newContext.setLocation(ga.getX(), ga.getY());
		newContext.putProperty(ACTIVITY_MOVE_PROPERTY, true);

		copyProperties(context, newContext, MOVE_PROPERTIES);
		
		IMoveShapeFeature moveFeature = getFeatureProvider().getMoveShapeFeature(newContext);
		if (moveFeature.canMoveShape(newContext)) {
			moveFeature.moveShape(newContext);
		}
	}
	
	private void repositionBoundaryEvents(final IMoveShapeContext context) {
		Shape activityShape = (Shape) context.getPictogramElement();
		
		new AbstractBoundaryEventOperation() {
			@Override
			protected void applyTo(ContainerShape boundaryEventShape) {
				
				// move only if it is not moved by itself
				if (!isEditorSelection(boundaryEventShape)) {
					repositionBoundaryEvent(boundaryEventShape, context);
				}
			}
		}.execute(activityShape, context.getSourceContainer());
	}
}