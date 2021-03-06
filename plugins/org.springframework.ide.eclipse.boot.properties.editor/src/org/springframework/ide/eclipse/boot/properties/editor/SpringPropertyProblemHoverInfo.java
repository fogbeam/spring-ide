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
package org.springframework.ide.eclipse.boot.properties.editor;

import java.util.List;

import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.springframework.ide.eclipse.boot.properties.editor.quickfix.QuickfixContext;
import org.springframework.ide.eclipse.boot.properties.editor.reconciling.SpringPropertyProblem;
import org.springframework.ide.eclipse.boot.properties.editor.util.HtmlBuffer;

/**
 * @author Kris De Volder
 */
public class SpringPropertyProblemHoverInfo extends HoverInfo {

	private SpringPropertyProblem problem;
	private QuickfixContext context;

	public SpringPropertyProblemHoverInfo(SpringPropertyProblem problem, QuickfixContext context) {
		this.problem = problem;
		this.context = context;
	}

	@Override
	protected String renderAsHtml() {
		HtmlBuffer html = new HtmlBuffer();

		html.text(problem.getMessage());
		problem.getQuickfixes(context);

		renderQuickfixes(html, problem.getQuickfixes(context));

		return html.toString();
	}

	private void renderQuickfixes(HtmlBuffer html, List<ICompletionProposal> quickfixes) {
		if (!quickfixes.isEmpty()) {
			html.hline();

			if (quickfixes.size()==1) {
				html.p("1 quickfix available:");
			} else {
				html.text(quickfixes.size()+" quickfixes available:");
			}

			html.raw("<ul>");
			for (ICompletionProposal fix : quickfixes) {
				html.raw("<li>");
				actionLink(html, fix.getDisplayString(), applyQuickfix(fix));
			}
			html.raw("</ul>");
			//Yuck... we need to add some extra empty pars at end or the browser information control is
			// sized too small and doesn't show the quickfixes.
			html.p("");
			html.p("");
			html.p("");
			html.p("");
		}
	}

	private Runnable applyQuickfix(final ICompletionProposal fix) {
		return new Runnable() {
			public void run() {
				//TODO: need document in general, but currently implemented quickfixes don't care so
				// we can get away with this for now.
				fix.apply(null);
			}
		};
	}

	@Override
	public String toString() {
		return "SpringPropertyProblemHoverInfo("+problem.getMessage()+")";
	}
}
