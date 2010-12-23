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
package nl.grauw.gaia_tool.views;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

import nl.grauw.gaia_tool.Parameters;
import nl.grauw.gaia_tool.mvc.Observable;

public abstract class SingleParametersView extends ParametersView {
	private static final long serialVersionUID = 1L;
	
	private JPanel parametersContainer;
	private JTextArea parameterArea;
	private JScrollPane parameterScrollPane;
	
	public abstract Parameters getParameters();
	
	@Override
	public void update(Observable source, Object arg) {
		if (source == getParameters()) {
			parameterArea.setText(getParameters().toString());
		} else {
			// register self as observer on parameters
			if (getParameters() != null) {
				if (!getParameters().hasObserver(this))
					getParameters().addObserver(this);
				update(getParameters(), null);
			}
		}
		
	}
	
	protected JPanel getParametersContainer() {
		if (parametersContainer == null) {
			parametersContainer = new JPanel();
			parametersContainer.setLayout(new BoxLayout(parametersContainer, BoxLayout.X_AXIS));
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
			if (getParameters() == null) {
				loadParameters();
			} else {
				parameterArea.setText(getParameters().toString());
			}
		}
		return parameterArea;
	}
	
}
