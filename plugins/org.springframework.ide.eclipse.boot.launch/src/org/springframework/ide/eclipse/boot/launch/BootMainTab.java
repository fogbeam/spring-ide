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

import static org.springframework.ide.eclipse.boot.ui.BootUIImages.BOOT_ICON;

import java.util.Arrays;
import java.util.List;

import org.eclipse.swt.graphics.Image;
import org.springframework.ide.eclipse.boot.launch.livebean.EnableJmxSection;
import org.springframework.ide.eclipse.boot.launch.profiles.ProfileHistory;
import org.springframework.ide.eclipse.boot.launch.profiles.ProfileLaunchTabSection;
import org.springframework.ide.eclipse.boot.launch.properties.PropertiesTableSection;
import org.springframework.ide.eclipse.boot.launch.util.LaunchConfigurationTabWithSections;
import org.springframework.ide.eclipse.boot.ui.BootUIImages;
import org.springsource.ide.eclipse.commons.livexp.ui.HLineSection;
import org.springsource.ide.eclipse.commons.livexp.ui.IPageSection;
import org.springsource.ide.eclipse.commons.livexp.ui.IPageWithSections;

/**
 * @author Kris De Volder
 */
public class BootMainTab extends LaunchConfigurationTabWithSections implements IPageWithSections {

	@Override
	public String getName() {
		return "Spring Boot";
	}

	@Override
	public Image getImage() {
		return BootUIImages.getImage(BOOT_ICON);
	}

	@Override
	protected List<IPageSection> createSections() {
		BootLaunchUIModel model = new BootLaunchUIModel(new ProfileHistory());
		return Arrays.asList(new IPageSection[] {
				SelectProjectLaunchTabSection.create(this, model.project),
				new MainTypeLaunchTabSection(this, model.project.selection, model.mainTypeName),
				new ProfileLaunchTabSection(this, model.profile),
				new HLineSection(this),
				new EnableDebugSection(this, model.enableDebug),
				new EnableJmxSection(this, model.enableJmx),
				new HLineSection(this),
				new PropertiesTableSection(this, model.project.selection)
		});
	}

}
