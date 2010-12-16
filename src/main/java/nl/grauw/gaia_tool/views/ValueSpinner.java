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

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import nl.grauw.gaia_tool.Value;

public class ValueSpinner extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private Value value;
	private String label;
	
	private JLabel labelLabel;
	private JSpinner valueSpinner;
	
	public ValueSpinner(Value value, String label) {
		this.value = value;
		this.label = label;
		initComponents();
	}
	
	private void initComponents() {
		add(getLabel());
		add(getSpinner());
	}
	
	private JSpinner getSpinner() {
		if (valueSpinner == null) {
			valueSpinner = new JSpinner(new SpinnerNumberModel(value.getValue(), value.getMinimum(), value.getMaximum(), 1));
		}
		return valueSpinner;
	}
	
	private JLabel getLabel() {
		if (labelLabel == null) {
			labelLabel = new JLabel(label);
		}
		return labelLabel;
	}
	
}
