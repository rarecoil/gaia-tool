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

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class EnumComboBox<T extends Enum<T>> extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private T value;
	private T[] enums;
	private String label;
	
	private JLabel labelLabel;
	private JComboBox valueComboBox;
	
	public EnumComboBox(T value, String label) {
		this.value = value;
		this.enums = value.getDeclaringClass().getEnumConstants();
		this.label = label;
		initComponents();
	}
	
	private void initComponents() {
		add(getLabel());
		add(getSpinner());
	}
	
	private JComboBox getSpinner() {
		if (valueComboBox == null) {
			valueComboBox = new JComboBox(enums);
			valueComboBox.setSelectedItem(value);
		}
		return valueComboBox;
	}
	
	private JLabel getLabel() {
		if (labelLabel == null) {
			labelLabel = new JLabel(label);
		}
		return labelLabel;
	}
	
}
