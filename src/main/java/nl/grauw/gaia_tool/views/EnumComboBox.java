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

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import nl.grauw.gaia_tool.EnumValue;
import nl.grauw.gaia_tool.mvc.AWTObserver;
import nl.grauw.gaia_tool.mvc.Observable;

public class EnumComboBox extends JPanel {
	private static final long serialVersionUID = 1L;
	
	public class EnumComboBoxModel extends AbstractListModel implements ComboBoxModel, AWTObserver {
		private static final long serialVersionUID = 1L;
		
		public EnumComboBoxModel() {
			value.getParameters().addObserver(this);
		}
		
		@Override
		public int getSize() {
			return value.getChoices().length;
		}
		
		@Override
		public Object getElementAt(int index) {
			return value.getChoices()[index];
		}
		
		@Override
		public void setSelectedItem(Object anItem) {
			value.setValue(anItem);
		}
		
		@Override
		public Object getSelectedItem() {
			return value.getValue();
		}
		
		@Override
		public void update(Observable source, Object arg) {
			if (value.testChanged(source, arg)) {
				int ordinal = value.getValue().ordinal();
				fireContentsChanged(this, ordinal, ordinal);
			}
		}
		
	}
	
	private EnumValue<?> value;
	private String label;
	
	private JLabel labelLabel;
	private JComboBox valueComboBox;
	
	public EnumComboBox(EnumValue<?> value, String label) {
		this.value = value;
		this.label = label;
		initComponents();
	}
	
	private void initComponents() {
		add(getLabel());
		add(getComboBox());
	}
	
	private JComboBox getComboBox() {
		if (valueComboBox == null) {
			valueComboBox = new JComboBox(new EnumComboBoxModel());
		}
		return valueComboBox;
	}
	
	private JLabel getLabel() {
		if (labelLabel == null) {
			labelLabel = new JLabel(label);
		}
		return labelLabel;
	}
	
	@Override
	public void setToolTipText(String text) {
		getLabel().setToolTipText(text);
	}
	
}
