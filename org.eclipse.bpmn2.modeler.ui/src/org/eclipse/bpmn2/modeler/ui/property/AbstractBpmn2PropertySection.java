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
 * @author Bob Brodt
 ******************************************************************************/
package org.eclipse.bpmn2.modeler.ui.property;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.bpmn2.modeler.core.features.AbstractBpmn2CreateConnectionFeature;
import org.eclipse.bpmn2.modeler.core.features.AbstractBpmn2CreateFeature;
import org.eclipse.bpmn2.modeler.core.runtime.IBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.core.runtime.ModelEnablementDescriptor;
import org.eclipse.bpmn2.modeler.core.utils.PropertyUtil;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil.Bpmn2DiagramType;
import org.eclipse.bpmn2.modeler.ui.diagram.BPMNFeatureProvider;
import org.eclipse.bpmn2.modeler.ui.editor.BPMN2Editor;
import org.eclipse.core.resources.IProject;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.features.IFeature;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.platform.IDiagramEditor;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.views.properties.tabbed.ISection;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabContents;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public abstract class AbstractBpmn2PropertySection extends GFPropertySection implements IBpmn2PropertySection {
	
	protected TabbedPropertySheetPage tabbedPropertySheetPage;
	protected Composite parent;
	protected BPMN2Editor editor;
	private IWorkbenchWindow cachedWorkbenchWindow;
	private IPartListener partActivationListener = new IPartListener() {

		public void partActivated(IWorkbenchPart part) {
			if (part instanceof BPMN2Editor) {
				editor = (BPMN2Editor)part;
			}
		}

		public void partBroughtToTop(IWorkbenchPart part) {
		}

		public void partClosed(IWorkbenchPart part) {
		}

		public void partDeactivated(IWorkbenchPart part) {
		}

		public void partOpened(IWorkbenchPart part) {
		}
	};
	
	public AbstractBpmn2PropertySection() {
		cachedWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		cachedWorkbenchWindow.getPartService().addPartListener(
				partActivationListener);
	}

	@Override
	public void dispose() {
		super.dispose();
		if (cachedWorkbenchWindow != null) {
			cachedWorkbenchWindow.getPartService().removePartListener(
				partActivationListener);
			cachedWorkbenchWindow = null;
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.views.properties.tabbed.AbstractPropertySection#createControls(org.eclipse.swt.widgets.Composite, org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage)
	 */
	@Override
	public void createControls(Composite parent, TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);
		parent.addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent e) {
				if (e.widget instanceof Composite) {
					Composite parent = ((Composite)e.widget);
					Control[] kids = parent.getChildren();
					for (Control c : kids)
						c.dispose();
				}
			}
			
		});
		this.tabbedPropertySheetPage = aTabbedPropertySheetPage;
		this.parent = parent;
		parent.setLayout(new GridLayout(1, false));
		editor = BPMN2Editor.getActiveEditor();
	}

	/**
	 * Returns the section's parent composite. This is the composite that was
	 * created by the TabbedPropertySheetPage, not the "root" composite for
	 * this section.
	 * 
	 * @return the TabbedPropertySheetPage composite.
	 */
	public Composite getParent() {
		return parent;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.ui.platform.GFPropertySection#getDiagramEditor()
	 */
	@Override
	protected IDiagramEditor getDiagramEditor() {
		return editor;
	}

	/**
	 * Returns the property section's TabbedPropertySheetPage
	 * 
	 * @return the TabbedPropertySheetPage that owns this section.
	 */
	public TabbedPropertySheetPage getTabbedPropertySheetPage() {
		return tabbedPropertySheetPage;
	}

	/**
	 * Get the section's root composite, which is a subclass of AbstractBpmn2PropertiesComposite.
	 * Create the composite if it has not been created yet.
	 * 
	 * @return the composite
	 */
	public AbstractBpmn2PropertiesComposite getSectionRoot() {
		AbstractBpmn2PropertiesComposite sectionRoot = null;
		if (parent!=null && !parent.isDisposed()) {
			if (parent.getChildren().length==0) {
				sectionRoot = createSectionRoot();
				sectionRoot.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false,1,1));
			}
			sectionRoot = (AbstractBpmn2PropertiesComposite)parent.getChildren()[0];
		}
		return sectionRoot;
	}
	
	/**
	 * The subclass must provide the parent Composite it created in createControls()
	 * so that we can pass the PropertySheetPage down to the Composite. This is
	 * useful for allowing the Composite to resize itself based on the parent's size.
	 * 
	 * @return
	 */
	protected abstract AbstractBpmn2PropertiesComposite createSectionRoot();

	protected EObject getBusinessObjectForPictogramElement(PictogramElement pe) {
		return PropertyUtil.getBusinessObjectForPictogramElement(pe);
	}
	
	protected EObject getBusinessObjectForSelection() {
		PictogramElement pe = getSelectedPictogramElement();
		if (pe != null)
			return getBusinessObjectForPictogramElement(pe);
		return null;
	}
	
	/* (non-Javadoc)
	 * Yet another ugly hack: this restores the current property sheet page parent
	 * composite when a different BPMN2Editor is activated. Apparently TabbedPropertySheetPage
	 * does not do this for us automatically!
	 *  
	 * @see org.eclipse.ui.views.properties.tabbed.AbstractPropertySection#setInput(org.eclipse.ui.IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
	 */
	@Override
	public void setInput(IWorkbenchPart part, ISelection selection) {
		super.setInput(part, selection);
		if (part instanceof BPMN2Editor) {
			editor = (BPMN2Editor)part;
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.views.properties.tabbed.AbstractPropertySection#refresh()
	 */
	@Override
	public void refresh() {
		PictogramElement pe = getSelectedPictogramElement();
		if (pe != null) {
			final EObject be = getBusinessObjectForPictogramElement(pe);
			if (be!=null) {
				AbstractBpmn2PropertiesComposite sectionRoot = getSectionRoot();
				if (sectionRoot!=null) {
					sectionRoot.setEObject((BPMN2Editor) getDiagramEditor(), be);
					PropertyUtil.recursivelayout(sectionRoot);
				}
			}
		}
	}

	@Override
	// make this public!
	public PictogramElement getSelectedPictogramElement() {
		return super.getSelectedPictogramElement();
	}

	/**
	 * Force a layout of the property sheet page.
	 */
	public void layout() {
		Control sectionRoot = (AbstractBpmn2PropertiesComposite)parent.getChildren()[0];
		Composite composite = (Composite)tabbedPropertySheetPage.getControl();
		composite.layout(true);
		tabbedPropertySheetPage.resizeScrolledComposite();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.views.properties.tabbed.AbstractPropertySection#shouldUseExtraSpace()
	 */
	@Override
	public boolean shouldUseExtraSpace() {
		return true;
	}

	/* (non-Javadoc)
	 * Override this to allow the section to decide whether or not it will be rendered.
	 * @see org.eclipse.bpmn2.modeler.core.runtime.IBpmn2PropertySection#appliesTo(org.eclipse.ui.IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
	 */
	@Override
	public boolean appliesTo(IWorkbenchPart part, ISelection selection) {
		BPMN2Editor editor = (BPMN2Editor)part;
		PictogramElement pe = PropertyUtil.getPictogramElementForSelection(selection);
		EObject selectionBO = PropertyUtil.getBusinessObjectForSelection(selection);
		ModelEnablementDescriptor modelEnablement = editor.getTargetRuntime().getModelEnablements(selectionBO);
		
		if (selectionBO!=null && modelEnablement.isEnabled(selectionBO.eClass())) {
			EObject thisBO = getBusinessObjectForPictogramElement(pe);
			return thisBO!=null;			
		}
		return false;
	}
	
	public boolean doReplaceTab(String id, IWorkbenchPart part, ISelection selection) {
		return true;
	}
}
