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

import javax.swing.GroupLayout;
import javax.swing.JPanel;

import nl.grauw.gaia_tool.mvc.AWTObserver;
import nl.grauw.gaia_tool.mvc.Observable;
import nl.grauw.gaia_tool.parameters.ArpeggioCommon;
import nl.grauw.gaia_tool.parameters.ArpeggioCommon.ArpeggioDuration;
import nl.grauw.gaia_tool.parameters.ArpeggioCommon.ArpeggioGrid;
import nl.grauw.gaia_tool.parameters.ArpeggioCommon.ArpeggioMotif;

public class ArpeggioCommonView extends JPanel implements AWTObserver {
	
	private ArpeggioCommon parameters;
	
	private static final long serialVersionUID = 123L;
	private EnumComboBox gridComboBox;
	private EnumComboBox durationComboBox;
	private EnumComboBox motifComboBox;
	private ValueSpinner octaveRangeSpinner;
	private ValueSpinner accentRateSpinner;
	private ValueSpinner velocitySpinner;
	private ValueSpinner endStepSpinner;
	
	public ArpeggioCommonView(ArpeggioCommon pacp) {
		parameters = pacp;
		initComponents();
		update(parameters, null);
	}
	
	public ArpeggioCommon getModel() {
		return parameters;
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if (o == parameters) {
			//
		}
	}
	
	private void initComponents() {
		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setHorizontalGroup(
				layout.createParallelGroup()
					.addGroup(
						layout.createSequentialGroup()
							.addComponent(getArpeggioGridComboBox())
							.addComponent(getArpeggioDurationComboBox())
							.addComponent(getArpeggioMotifComboBox())
						)
					.addGroup(
						layout.createSequentialGroup()
							.addComponent(getArpeggioOctaveRangeSpinner())
							.addComponent(getArpeggioAccentRateSpinner())
							.addComponent(getArpeggioVelocitySpinner())
							.addComponent(getEndStepSpinner())
						)
			);
		layout.setVerticalGroup(
				layout.createSequentialGroup()
					.addGroup(
						layout.createParallelGroup()
							.addComponent(getArpeggioGridComboBox())
							.addComponent(getArpeggioDurationComboBox())
							.addComponent(getArpeggioMotifComboBox())
						)
					.addGroup(
						layout.createParallelGroup()
							.addComponent(getArpeggioOctaveRangeSpinner())
							.addComponent(getArpeggioAccentRateSpinner())
							.addComponent(getArpeggioVelocitySpinner())
							.addComponent(getEndStepSpinner())
						)
			);
	}
	
	private EnumComboBox getArpeggioGridComboBox() {
		if (gridComboBox == null) {
			gridComboBox = new EnumComboBox(parameters.getArpeggioGrid(), ArpeggioGrid.values(), "Grid");
		}
		return gridComboBox;
	}
	
	private EnumComboBox getArpeggioDurationComboBox() {
		if (durationComboBox == null) {
			durationComboBox = new EnumComboBox(parameters.getArpeggioDuration(), ArpeggioDuration.values(), "Duration");
		}
		return durationComboBox;
	}
	
	private EnumComboBox getArpeggioMotifComboBox() {
		if (motifComboBox == null) {
			motifComboBox = new EnumComboBox(parameters.getArpeggioMotif(), ArpeggioMotif.values(), "Motif");
		}
		return motifComboBox;
	}
	
	private ValueSpinner getArpeggioOctaveRangeSpinner() {
		if (octaveRangeSpinner == null) {
			octaveRangeSpinner = new ValueSpinner(parameters.getArpeggioOctaveRange(), "Octave range");
		}
		return octaveRangeSpinner;
	}
	
	private ValueSpinner getArpeggioAccentRateSpinner() {
		if (accentRateSpinner == null) {
			accentRateSpinner = new ValueSpinner(parameters.getArpeggioAccentRate(), "Accent rate");
		}
		return accentRateSpinner;
	}
	
	private ValueSpinner getArpeggioVelocitySpinner() {
		if (velocitySpinner == null) {
			velocitySpinner = new ValueSpinner(parameters.getArpeggioVelocity(), "Velocity");
		}
		return velocitySpinner;
	}
	
	private ValueSpinner getEndStepSpinner() {
		if (endStepSpinner == null) {
			endStepSpinner = new ValueSpinner(parameters.getEndStep(), "End step");
		}
		return endStepSpinner;
	}
	
}
