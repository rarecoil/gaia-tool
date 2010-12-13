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

import nl.grauw.gaia_tool.mvc.Observable;
import nl.grauw.gaia_tool.mvc.Observer;
import nl.grauw.gaia_tool.parameters.Parameters;

public class ParametersView extends JPanel implements Observer {
	
	private Parameters parameters;
	
	private static final long serialVersionUID = 123L;
	private JTextArea parameterArea;
	private JScrollPane parameterScrollPane;
	
	public ParametersView() {
		initComponents();
	}
	
	public ParametersView(Parameters p) {
		this();
		setModel(p);
	}
	
	public void setModel(Parameters p) {
		if (parameters != null) {
			parameters.removeObserver(this);
		}
		if (p != null) {
			parameters = p;
		} else {
			parameters = new Parameters(new byte[0]) {
				public String toString() {
					return "";
				}
			};
		}
		parameters.addObserver(this);
		update(parameters, null);
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if (o == parameters) {
			parameterArea.setText(parameters.toString());
		}
	}
	
	private void initComponents() {
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		add(getParameterScrollPane());
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
		}
		return parameterArea;
	}
	
}
