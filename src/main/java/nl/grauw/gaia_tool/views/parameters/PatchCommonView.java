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
package nl.grauw.gaia_tool.views.parameters;

import java.awt.FlowLayout;

import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DefaultCaret;

import nl.grauw.gaia_tool.Parameters.ParameterChange;
import nl.grauw.gaia_tool.mvc.AWTObserver;
import nl.grauw.gaia_tool.mvc.Observable;
import nl.grauw.gaia_tool.parameters.PatchCommon;
import nl.grauw.gaia_tool.views.ValueSpinner;

public class PatchCommonView extends JPanel implements AWTObserver {
	private static final long serialVersionUID = 1L;
	
	private class NameField extends JTextField implements AWTObserver, DocumentListener {
		private static final long serialVersionUID = 1L;
		
		public NameField() {
			super(parameters.getPatchName().trim(), 12);
			parameters.addObserver(this);
			getDocument().addDocumentListener(this);
		}
		
		@Override
		public void update(Observable source, Object detail) {
			if (detail instanceof ParameterChange && ((ParameterChange)detail).getOffset() < 12) {
				String name = parameters.getPatchName().trim();
				if (!name.equals(getText())) {
					setText(name);
				}
			}
		}
		
		@Override
		public void insertUpdate(DocumentEvent e) {
			setPatchName();
		}
		
		@Override
		public void removeUpdate(DocumentEvent e) {
			setPatchName();
		}
		
		@Override
		public void changedUpdate(DocumentEvent e) {
			setPatchName();
		}
		
		private void setPatchName() {
			String name = parameters.getPatchName().trim();
			if (!name.equals(getText())) {
				parameters.setPatchName(getText());
			}
		}
		
	}
	
	private PatchCommon parameters;

	private JTextArea parameterArea;
	private JScrollPane parameterScrollPane;
	private JPanel editPanel;
	private JLabel patchNameLabel;
	private JTextField patchNameField;
	private ValueSpinner tempoSpinner;
	
	public PatchCommonView(PatchCommon pc) {
		parameters = pc;
		initComponents();
		parameters.addObserver(this);
		update(parameters, null);
	}
	
	public PatchCommon getParameters() {
		return parameters;
	}
	
	protected void initComponents() {
		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setHorizontalGroup(
				layout.createParallelGroup()
					.addGroup(layout.createSequentialGroup()
						.addComponent(getEditPanel())
						.addComponent(getTempoSpinner(), GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					)
					.addComponent(getParameterScrollPane())
			);
		layout.setVerticalGroup(
				layout.createSequentialGroup()
					.addGroup(layout.createParallelGroup()
						.addComponent(getEditPanel(), GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(getTempoSpinner(), GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					)
					.addComponent(getParameterScrollPane())
			);
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
			parameterArea.setText(getParametersText());
		}
		return parameterArea;
	}
	
	private JPanel getEditPanel() {
		if (editPanel == null) {
			editPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			editPanel.add(getPatchNameLabel());
			editPanel.add(getPatchNameField());
		}
		return editPanel;
	}
	
	private JLabel getPatchNameLabel() {
		if (patchNameLabel == null) {
			patchNameLabel = new JLabel("Patch name:");
		}
		return patchNameLabel;
	}
	
	private JTextField getPatchNameField() {
		if (patchNameField == null) {
			patchNameField = new NameField();
		}
		return patchNameField;
	}
	
	private ValueSpinner getTempoSpinner() {
		if (tempoSpinner == null) {
			tempoSpinner = new ValueSpinner(parameters.getPatchTempo(), "Tempo");
			tempoSpinner.setToolTipText("Tempo in BPM, range 5-300.");
		}
		return tempoSpinner;
	}
	
	private String getParametersText() {
		return String.format("Patch level: %s\n", parameters.getPatchLevel()) +
				String.format("Arpeggio switch: %s\n", parameters.getArpeggioSwitch()) +
				String.format("Portamento switch: %s\n", parameters.getPortamentoSwitch()) +
				String.format("Portamento time: %s\n", parameters.getPortamentoTime()) +
				String.format("Mono switch: %s\n", parameters.getMonoSwitch()) +
				String.format("Octave shift: %s\n", parameters.getOctaveShift()) +
				String.format("Pitch bend range up: %s\n", parameters.getPitchBendRangeUp()) +
				String.format("Pitch bend range down: %s\n", parameters.getPitchBendRangeDown()) +
				String.format("Tone 1 switch: %s\n", parameters.getTone1Switch()) +
				String.format("Tone 1 select: %s\n", parameters.getTone1Select()) +
				String.format("Tone 2 switch: %s\n", parameters.getTone2Switch()) +
				String.format("Tone 2 select: %s\n", parameters.getTone2Select()) +
				String.format("Tone 3 switch: %s\n", parameters.getTone3Switch()) +
				String.format("Tone 3 select: %s\n", parameters.getTone3Select()) +
				String.format("SYNC/RING select: %s\n", parameters.getSyncRingSelect()) +
				String.format("Effects master switch: %s\n", parameters.getEffectsMasterSwitch()) +
				String.format("Delay tempo sync switch: %s\n", parameters.getDelayTempoSyncSwitch()) +
				String.format("Low boost switch: %s\n", parameters.getLowBoostSwitch()) +
				String.format("D-Beam assign: %s\n", parameters.getDBeamAssign()) +
				String.format("D-Beam polarity: %s\n", parameters.getDBeamPolarity()) +
				String.format("Effects distortion select: %s\n", parameters.getEffectsDistortionSelect()) +
				String.format("Effects flanger select: %s\n", parameters.getEffectsFlangerSelect()) +
				String.format("Effects delay select: %s\n", parameters.getEffectsDelaySelect()) +
				String.format("Effects reverb select: %s\n", parameters.getEffectsReverbSelect());
	}
	
	@Override
	public void update(Observable source, Object detail) {
		parameterArea.setText(getParametersText());
	}
	
}
