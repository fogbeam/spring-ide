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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunchManager;
import org.springframework.ide.eclipse.boot.dash.BootDashActivator;
import org.springframework.ide.eclipse.boot.dash.cloudfoundry.CloudDashElement;
import org.springframework.ide.eclipse.boot.dash.cloudfoundry.CloudFoundryBootDashModel;
import org.springframework.ide.eclipse.boot.dash.cloudfoundry.DevtoolsUtil;
import org.springframework.ide.eclipse.boot.dash.model.RunState;

/**
 * Operation for (re)starting Remote DevTools Client
 *
 * @author Alex Boyko
 *
 */
public class RemoteDevClientStartOperation extends CloudApplicationOperation {

	private final RunState startMode;

	public RemoteDevClientStartOperation(CloudFoundryBootDashModel model, String appName, RunState startMode) {
		super("Starting Remote DevTools Client for application '" + appName + "'", model, appName);
		this.startMode = startMode;
	}

	@Override
	protected void doCloudOp(IProgressMonitor monitor) throws Exception, OperationCanceledException {
		CloudDashElement cde = model.getElement(appName);
		if (cde == null || cde.getProject() == null) {
			throw new CoreException(new Status(IStatus.ERROR, BootDashActivator.PLUGIN_ID, "Local project not associated to CF app '" + appName + "'"));
		}
		DevtoolsUtil.disconnectDevtoolsClientsFor(cde);
		DevtoolsUtil.launchDevtools(cde, DevtoolsUtil.getSecret(cde.getProject()), startMode == RunState.DEBUGGING ? ILaunchManager.DEBUG_MODE : ILaunchManager.RUN_MODE , monitor);
	}

}
