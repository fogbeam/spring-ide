/*******************************************************************************
 * Copyright (c) 2015 Pivotal, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Pivotal, Inc. - initial API and implementation
 *******************************************************************************/
package org.springframework.ide.eclipse.boot.dash.model;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.ide.eclipse.boot.dash.cloudfoundry.DevtoolsUtil;
import org.springframework.ide.eclipse.boot.dash.model.BootDashModel.ElementStateListener;
import org.springframework.ide.eclipse.boot.dash.model.runtargettypes.RunTargetType;
import org.springframework.ide.eclipse.boot.util.ProcessTracker;
import org.springsource.ide.eclipse.commons.livexp.core.LiveExpression;
import org.springsource.ide.eclipse.commons.livexp.core.LiveSet;
import org.springsource.ide.eclipse.commons.livexp.ui.Disposable;

/**
 * @author Kris De Volder
 */
public class BootDashViewModel implements Disposable {

	private LiveSet<RunTarget> runTargets;
	private BootDashModelManager models;
	private Set<RunTargetType> runTargetTypes;
	private RunTargetPropertiesManager manager;
	private ToggleFiltersModel toggleFiltersModel;
	private TagFilterBoxModel filterBox;
	private LiveExpression<Filter<BootDashElement>> filter;
	private ProcessTracker devtoolsProcessTracker;

	/**
	 * Create an 'empty' BootDashViewModel with no run targets. Targets can be
	 * added by adding them to the runTarget's LiveSet.
	 */
	public BootDashViewModel(BootDashModelContext context, RunTargetType... runTargetTypes) {
		runTargets = new LiveSet<RunTarget>(new LinkedHashSet<RunTarget>());
		models = new BootDashModelManager(context, runTargets);

		manager = new RunTargetPropertiesManager(context, runTargetTypes);
		List<RunTarget> existingtargets = manager.getStoredTargets();
		runTargets.addAll(existingtargets);
		runTargets.addListener(manager);

		this.runTargetTypes = new LinkedHashSet<RunTargetType>(Arrays.asList(runTargetTypes));
		filterBox = new TagFilterBoxModel();
		toggleFiltersModel = new ToggleFiltersModel();
		filter = Filters.compose(filterBox.getFilter(), toggleFiltersModel.getFilter());
		devtoolsProcessTracker = DevtoolsUtil.createProcessTracker(this);
	}

	public LiveSet<RunTarget> getRunTargets() {
		return runTargets;
	}

	@Override
	public void dispose() {
		models.dispose();
		devtoolsProcessTracker.dispose();
	}

	public void addElementStateListener(ElementStateListener l) {
		models.addElementStateListener(l);
	}

	public void removeElementStateListener(ElementStateListener l) {
		models.removeElementStateListener(l);
	}

	public LiveExpression<Set<BootDashModel>> getSectionModels() {
		return models.getModels();
	}

	public Set<RunTargetType> getRunTargetTypes() {
		return runTargetTypes;
	}

	public void removeTarget(RunTarget toRemove, UserInteractions userInteraction) {

		if (toRemove != null) {
			RunTarget found = null;
			for (RunTarget existingTarget : runTargets.getValues()) {
				if (existingTarget.getId().equals(toRemove.getId())) {
					found = existingTarget;
					break;
				}
			}
			if (found != null && userInteraction.confirmOperation("Deleting run target: " + found.getName(),
					"Are you sure that you want to delete " + found.getName()
							+ "? All information regarding this target will be permanently removed.")) {
				runTargets.remove(found);
			}
		}
	}

	public void updatePropertiesInStore(RunTargetWithProperties target) {
		// For now, only properties for secure storage can be updated (e.g. credentials for the run target)
		manager.secureStorage(target.getTargetProperties());
	}

	public ToggleFiltersModel getToggleFilters() {
		return toggleFiltersModel;
	}

	public TagFilterBoxModel getFilterBox() {
		return filterBox;
	}

	public LiveExpression<Filter<BootDashElement>> getFilter() {
		return filter;
	}

	public BootDashModel getSectionByTargetId(String targetId) {
		for (BootDashModel m : getSectionModels().getValue()) {
			if (m.getRunTarget().getId().equals(targetId)) {
				return m;
			}
		};
		return null;
	}
}
