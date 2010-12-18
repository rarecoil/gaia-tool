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

import java.awt.Dimension;

import javax.swing.AbstractSpinnerModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import nl.grauw.gaia_tool.Value;
import nl.grauw.gaia_tool.mvc.Observable;
import nl.grauw.gaia_tool.mvc.Observer;

public class ValueSpinner extends JPanel {
	
	public class ValueSpinnerModel extends AbstractSpinnerModel implements Observer {
		
		private Value value;

		public ValueSpinnerModel(Value value) {
			this.value = value;
			value.addObserver(this);
		}
		
		@Override
		public Object getValue() {
			return value.getValue();
		}

		@Override
		public void setValue(Object val) {
			if (val instanceof Integer) {
				Integer iVal = (Integer) val;
				value.setValue(iVal);
			} else {
				throw new IllegalArgumentException("");
			}
		}

		@Override
		public Object getNextValue() {
			int v = value.getValue();
			if (v >= value.getMaximum())
				return null;
			return v + 1;
		}

		@Override
		public Object getPreviousValue() {
			int v = value.getValue();
			if (v <= value.getMinimum())
				return null;
			return v - 1;
		}

		@Override
		public void update(Observable o, Object arg) {
			if (o == value) {
				fireStateChanged();
			}
		}
		
	}
	
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
			valueSpinner = new JSpinner(new ValueSpinnerModel(value));
			Dimension preferredSize = new Dimension(60, valueSpinner.getPreferredSize().height);
			valueSpinner.setPreferredSize(preferredSize);
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
