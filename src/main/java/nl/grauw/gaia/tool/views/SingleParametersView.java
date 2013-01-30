/*
 * Copyright 2010 Laurens Holst
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.grauw.gaia.tool.views;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

import nl.grauw.gaia.tool.Parameters;
import nl.grauw.gaia.tool.mvc.AWTObserver;
import nl.grauw.gaia.tool.mvc.Observable;

public abstract class SingleParametersView extends ParametersView implements AWTObserver {
	private static final long serialVersionUID = 1L;
	
	private JPanel parametersContainer;
	private JTextArea parameterArea;
	private JScrollPane parameterScrollPane;
	
	private boolean loading = false;
	
	public abstract Parameters getParameters();
	
	protected abstract String getParametersText();
	
	@Override
	protected void initComponents() {
		if (getParameters() == null) {
			loadParameters();
		} else if (!getParameters().hasObserver(this)) {
			getParameters().addObserver(this);
		}
		super.initComponents();
	}
	
	@Override
	public void update(Observable source, Object detail) {
		if (source == getParameters()) {
			parameterArea.setText(getParametersText());
		} else {
			// register self as observer on parameters
			if (getParameters() != null) {
				loading = false;
				if (!getParameters().hasObserver(this))
					getParameters().addObserver(this);
				update(getParameters(), null);
			} else {
				parameterArea.setText("");
				if (!loading && isShowing()) {
					loadParameters();
					loading = true;
				}
			}
		}
	}
	
	protected JComponent getParametersContainer() {
		if (parametersContainer == null) {
			parametersContainer = new JPanel();
			parametersContainer.setLayout(new BorderLayout());
			parametersContainer.add(getParameterScrollPane());
		}
		return parametersContainer;
	}
	
	private JScrollPane getParameterScrollPane() {
		if (parameterScrollPane == null) {
			parameterScrollPane = new JScrollPane();
			parameterScrollPane.setViewportView(getParameterArea());
		}
		return parameterScrollPane;
	}
	
	private JTextArea getParameterArea() {
		if (parameterArea == null) {
			parameterArea = new JTextArea();
			parameterArea.setEditable(false);
			parameterArea.setLineWrap(true);
			parameterArea.setWrapStyleWord(true);
			DefaultCaret caret = (DefaultCaret)parameterArea.getCaret();
			caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
			if (getParameters() != null) {
				parameterArea.setText(getParametersText());
			}
		}
		return parameterArea;
	}
	
}
