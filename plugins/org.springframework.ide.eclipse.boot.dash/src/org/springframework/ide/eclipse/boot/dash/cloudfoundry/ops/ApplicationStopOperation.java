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
package org.springframework.ide.eclipse.boot.dash.cloudfoundry.ops;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.springframework.ide.eclipse.boot.dash.cloudfoundry.ApplicationRunningStateTracker;
import org.springframework.ide.eclipse.boot.dash.cloudfoundry.CloudAppInstances;
import org.springframework.ide.eclipse.boot.dash.cloudfoundry.CloudDashElement;
import org.springframework.ide.eclipse.boot.dash.cloudfoundry.CloudFoundryBootDashModel;

public class ApplicationStopOperation extends CloudApplicationOperation {

	private CloudDashElement element;

	public ApplicationStopOperation(CloudDashElement element, CloudFoundryBootDashModel model) {
		super("Stopping application", model, element.getName());
		this.element = element;
	}

	@Override
	protected void doCloudOp(IProgressMonitor monitor) throws Exception, OperationCanceledException {
		requests.stopApplication(element.getName());
		model.getElementConsoleManager().terminateConsole(element.getName());
		CloudAppInstances updatedInstances = requests.getExistingAppInstances(element.getName());

		boolean checkTermination = false;
		this.eventHandler.fireEvent(eventFactory.updateRunState(updatedInstances, getDashElement(),
				ApplicationRunningStateTracker.getRunState(updatedInstances)), checkTermination);
	}

	public ISchedulingRule getSchedulingRule() {
		return new StopApplicationSchedulingRule(model.getRunTarget(), appName);
	}
}
