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

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import nl.grauw.gaia_tool.IntValue;
import nl.grauw.gaia_tool.mvc.AWTObserver;
import nl.grauw.gaia_tool.mvc.Observable;

public class ValueSpinner extends JPanel {
	
	public class ValueSpinnerModel extends SpinnerNumberModel implements AWTObserver {
		private static final long serialVersionUID = 1L;
		
		public ValueSpinnerModel() {
			value.getParameters().addObserver(this);
		}
		
		@Override
		public Object getValue() {
			return value.getValue();
		}

		@Override
		public void setValue(Object val) {
			if (val instanceof Integer) {
				Integer iVal = (Integer) val;
				if (value.getValue() != iVal)
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
		@SuppressWarnings("rawtypes")
		public Comparable getMaximum() {
			return value.getMaximum();
		}
		
		@Override
		@SuppressWarnings("rawtypes")
		public Comparable getMinimum() {
			return value.getMinimum();
		}
		
		@Override
		public Number getStepSize() {
			return 1;
		}
		
		@Override
		public Number getNumber() {
			return value.getValue();
		}
		
		@Override
		@SuppressWarnings("rawtypes")
		public void setMaximum(Comparable maximum) {
			throw new RuntimeException("Changing maximum is not allowed.");
		}
		
		@Override
		@SuppressWarnings("rawtypes")
		public void setMinimum(Comparable minimum) {
			throw new RuntimeException("Changing minimum is not allowed.");
		}
		
		@Override
		public void setStepSize(Number stepSize) {
			throw new RuntimeException("Setting step size is not allowed.");
		}

		@Override
		public void update(Observable source, Object arg) {
			if (value.testChanged(source, arg)) {
				fireStateChanged();
			}
		}
		
	}
	
	private static final long serialVersionUID = 1L;
	
	private IntValue value;
	private String label;
	
	private JLabel labelLabel;
	private JSpinner valueSpinner;
	
	public ValueSpinner(IntValue value, String label) {
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
			valueSpinner = new JSpinner(new ValueSpinnerModel());
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
	
	@Override
	public void setToolTipText(String text) {
		getLabel().setToolTipText(text);
	}
	
}
