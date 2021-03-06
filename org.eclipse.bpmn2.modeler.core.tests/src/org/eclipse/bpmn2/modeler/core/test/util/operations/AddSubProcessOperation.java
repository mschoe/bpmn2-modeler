package org.eclipse.bpmn2.modeler.core.test.util.operations;

import org.eclipse.bpmn2.SubProcess;
import org.eclipse.bpmn2.modeler.ui.diagram.BPMN2FeatureProvider;
import org.eclipse.bpmn2.modeler.ui.features.activity.subprocess.SubProcessFeatureContainer.CreateSubProcessFeature;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IFeature;
import org.eclipse.graphiti.features.context.impl.CreateContext;

/**
 * 
 * @author nico.rehwaldt
 *
 */
public class AddSubProcessOperation extends AbstractAddOperation<CreateSubProcessFeature, AddSubProcessOperation> {

	public AddSubProcessOperation(IDiagramTypeProvider diagramTypeProvider) {
		super(diagramTypeProvider, AddSubProcessOperation.class);
	}
	
	@Override
	protected CreateSubProcessFeature createFeature(CreateContext context) {
		
		BPMN2FeatureProvider featureProvider = (BPMN2FeatureProvider) diagramTypeProvider.getFeatureProvider();
		IFeature createFeature = featureProvider.getCreateFeatureForBusinessObject(SubProcess.class);
		
		return (CreateSubProcessFeature) createFeature;
	}

	public static AddSubProcessOperation addTask(IDiagramTypeProvider diagramTypeProvider) {
		return new AddSubProcessOperation(diagramTypeProvider);
	}
}
