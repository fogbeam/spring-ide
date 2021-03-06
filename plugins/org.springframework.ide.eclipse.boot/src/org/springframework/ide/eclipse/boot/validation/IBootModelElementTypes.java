/*******************************************************************************
 * Copyright (c) 2015 GoPivotal, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Pivotal, Inc. - initial API and implementation
 *******************************************************************************/
package org.springframework.ide.eclipse.boot.validation;

public interface IBootModelElementTypes {
	/**
	 * Constant representing a compilation unit. A model element with this type
	 * can be safely cast to {@link SpringCompilationUnit}.
	 */
	int COMPILATION_UNIT_TYPE = 2;
	// starts with 2 because 1 is reserved for the model
}
