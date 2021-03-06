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
package org.springframework.ide.eclipse.boot.dash.cloudfoundry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cloudfoundry.client.lib.domain.CloudApplication;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.springframework.ide.eclipse.boot.dash.BootDashActivator;

public class CloudApplicationDeploymentProperties extends BaseDeploymentProperties {

	public static final int DEFAULT_MEMORY = 1024;
	public static final int DEFAULT_INSTANCES = 1;
	/*
	 * These should never be null
	 */
	private final List<String> boundServices = new ArrayList<String>();
	private final Map<String, String> environmentVariables = new HashMap<String, String>();

	private int memory = DEFAULT_MEMORY;
	private int instances = DEFAULT_INSTANCES;
	
	
	/*
	 * Additional properties
	 */
	private String buildpackUrl;

	private boolean shouldRestart = true;

	public CloudApplicationDeploymentProperties(IProject project) {
		super(project);
	}

	public void setBuildpackUrl(String buildpackUrl) {
		this.buildpackUrl = buildpackUrl;
	}

	public void setServices(List<String> services) {
		this.boundServices.clear();
		if (services != null) {
			this.boundServices.addAll(services);
		}
	}

	public void setMemory(int memory) {
		this.memory = memory;
	}

	public void setInstances(int instances) {
		this.instances = instances;
	}

	public void setEnvironmentVariables(Map<String, String> environmentVariables) {
		this.environmentVariables.clear();
		if (environmentVariables != null) {
			this.environmentVariables.putAll(environmentVariables);
		}
	}

	public void setShouldRestart(boolean shouldRestart) {
		this.shouldRestart = shouldRestart;
	}

	public boolean shouldRestart() {
		return this.shouldRestart;
	}

	public String getBuildpackUrl() {
		return buildpackUrl;
	}

	public int getMemory() {
		return memory;
	}

	public int getInstances() {
		return instances;
	}

	/**
	 *
	 * @return never null
	 */
	public Map<String, String> getEnvironmentVariables() {
		return environmentVariables;
	}

	/**
	 *
	 * @return never null
	 */
	public List<String> getServices() {
		return boundServices;
	}

	public IStatus validate() {
		String errorMessage = null;

		if (getAppName() == null || getAppName().trim().length() == 0) {
			errorMessage = "Missing application name.";
		} else if (getMemory() <= 0) {
			errorMessage = "Invalid memory. Memory must be greater than 0.";
		} else if (getInstances() < 1) {
			errorMessage = "Invalid instances. There must be at least one instance for the application.";
		}

		if (errorMessage == null) {
			return Status.OK_STATUS;
		} else {
			return BootDashActivator.createErrorStatus(null, errorMessage);
		}
	}

	public static CloudApplicationDeploymentProperties getFor(CloudApplication app, IProject project) {
		CloudApplicationDeploymentProperties properties = new CloudApplicationDeploymentProperties(project);
		properties.setAppName(app.getName());
		properties.setBuildpackUrl(app.getStaging() != null ? app.getStaging().getBuildpackUrl() : null);
		properties.setEnvironmentVariables(app.getEnvAsMap());
		properties.setInstances(app.getInstances());
		properties.setMemory(app.getMemory());
		properties.setServices(app.getServices());
		properties.setUrls(app.getUris());
		return properties;
	}

}
