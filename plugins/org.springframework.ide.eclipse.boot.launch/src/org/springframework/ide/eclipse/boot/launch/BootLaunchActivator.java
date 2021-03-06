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
package org.springframework.ide.eclipse.boot.launch;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.springframework.ide.eclipse.boot.launch.util.BootLaunchConfDeleter;

/**
 * @author Kris De Volder
 */
public class BootLaunchActivator extends AbstractUIPlugin {

	private BootLaunchConfDeleter workspaceListener;

	public BootLaunchActivator() {
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		System.out.println("Activating: "+context.getBundle().getSymbolicName());
		workspaceListener = new BootLaunchConfDeleter(ResourcesPlugin.getWorkspace(), DebugPlugin.getDefault().getLaunchManager());
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		if (workspaceListener!=null) {
			workspaceListener.dispose();
		}
		super.stop(context);
	}

}
