package org.eclipse.bpmn2.modeler.ui.features.label;

import static org.eclipse.bpmn2.modeler.core.layout.util.ConversionUtil.point;

import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.bpmn2.modeler.core.di.DIUtils;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.core.utils.LabelUtil;
import org.eclipse.bpmn2.modeler.ui.features.context.IRepositionContext;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;

public class RepositionShapeLabelFeature extends AbstractRepositionFeature {

	public RepositionShapeLabelFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	protected Point getAdjustedPosition(Shape labelShape, IRepositionContext context) {
		Shape labeledShape = (Shape) context.getReferenceElement();
		
		Point labelAdjustment = LabelUtil.getAdjustedLabelPosition(labelShape, labeledShape);
		Point labelPosition = LabelUtil.getLabelPosition(labelShape);
		
		return point(
			labelAdjustment.getX() + labelPosition.getX(), 
			labelAdjustment.getY() + labelPosition.getY());
	}

	@Override
	protected void updateDi(Shape labelShape) {
		BPMNShape bpmnShape = BusinessObjectUtil.getFirstElementOfType(labelShape, BPMNShape.class);
		DIUtils.updateDILabel((ContainerShape) labelShape, bpmnShape);
	}
}
