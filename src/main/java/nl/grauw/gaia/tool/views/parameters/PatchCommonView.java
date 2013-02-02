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
package nl.grauw.gaia.tool.views.parameters;

import java.awt.FlowLayout;
import java.nio.charset.Charset;

import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.PlainDocument;

import nl.grauw.gaia.Parameters.ParameterChange;
import nl.grauw.gaia.parameters.PatchCommon;
import nl.grauw.gaia.tool.mvc.AWTObserver;
import nl.grauw.gaia.tool.mvc.Observable;
import nl.grauw.gaia.tool.views.ValueSpinner;

public class PatchCommonView extends JPanel implements AWTObserver {
	private static final long serialVersionUID = 1L;
	
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
		StringBuffer text = new StringBuffer();
		text.append(String.format("Patch level: %s\n", parameters.getPatchLevel()));
		text.append(String.format("Arpeggio switch: %s\n", parameters.getArpeggioSwitch()));
		text.append(String.format("Portamento switch: %s\n", parameters.getPortamentoSwitch()));
		text.append(String.format("Portamento time: %s\n", parameters.getPortamentoTime()));
		text.append(String.format("Mono switch: %s\n", parameters.getMonoSwitch()));
		text.append(String.format("Octave shift: %s\n", parameters.getOctaveShift()));
		text.append(String.format("Pitch bend range up: %s\n", parameters.getPitchBendRangeUp()));
		text.append(String.format("Pitch bend range down: %s\n", parameters.getPitchBendRangeDown()));
		text.append(String.format("Tone 1 switch: %s\n", parameters.getTone1Switch()));
		text.append(String.format("Tone 1 select: %s\n", parameters.getTone1Select()));
		text.append(String.format("Tone 2 switch: %s\n", parameters.getTone2Switch()));
		text.append(String.format("Tone 2 select: %s\n", parameters.getTone2Select()));
		text.append(String.format("Tone 3 switch: %s\n", parameters.getTone3Switch()));
		text.append(String.format("Tone 3 select: %s\n", parameters.getTone3Select()));
		text.append(String.format("SYNC/RING select: %s\n", parameters.getSyncRingSelect()));
		text.append(String.format("Effects master switch: %s\n", parameters.getEffectsMasterSwitch()));
		text.append(String.format("Delay tempo sync switch: %s\n", parameters.getDelayTempoSyncSwitch()));
		text.append(String.format("Low boost switch: %s\n", parameters.getLowBoostSwitch()));
		text.append(String.format("D-Beam assign: %s\n", parameters.getDBeamAssign()));
		text.append(String.format("D-Beam polarity: %s\n", parameters.getDBeamPolarity()));
		text.append(String.format("Effects distortion select: %s\n", parameters.getEffectsDistortionSelect()));
		text.append(String.format("Effects flanger select: %s\n", parameters.getEffectsFlangerSelect()));
		text.append(String.format("Effects delay select: %s\n", parameters.getEffectsDelaySelect()));
		text.append(String.format("Effects reverb select: %s\n", parameters.getEffectsReverbSelect()));
		
//		text.append(String.format("\nReserved 1: %s\n", parameters.getReserved1()));
//		text.append(String.format("Reserved 2: %s\n", parameters.getReserved2()));
//		text.append(String.format("Reserved 3: %s\n", parameters.getReserved3()));
//		text.append(String.format("Reserved 4: %s\n", parameters.getReserved4()));
//		text.append(String.format("Reserved 5: %s\n", parameters.getReserved5()));
//		text.append(String.format("Reserved 6: %s\n", parameters.getReserved6()));
//		text.append(String.format("Reserved 7: %s\n", parameters.getReserved7()));
//		text.append(String.format("Reserved 8: %s\n", parameters.getReserved8()));
//		text.append(String.format("Reserved 9: %s\n", parameters.getReserved9()));
//		text.append(String.format("Reserved 10: %s\n", parameters.getReserved10()));
//		text.append(String.format("Reserved 11: %s\n", parameters.getReserved11()));
//		text.append(String.format("Reserved 12: %s\n", parameters.getReserved12()));
//		text.append(String.format("Reserved 13: %s\n", parameters.getReserved13()));
//		text.append(String.format("Reserved 14: %s\n", parameters.getReserved14()));
//		text.append(String.format("Reserved 15: %s\n", parameters.getReserved15()));
//		text.append(String.format("Reserved 16: %s\n", parameters.getReserved16()));
//		text.append(String.format("Reserved 17: %s\n", parameters.getReserved17()));
//		text.append(String.format("Reserved 18: %s\n", parameters.getReserved18()));
//		text.append(String.format("Reserved 19: %s\n", parameters.getReserved19()));
//		text.append(String.format("Reserved 20: %s\n", parameters.getReserved20()));
//		text.append(String.format("Reserved 21: %s\n", parameters.getReserved21()));
//		text.append(String.format("Reserved 22: %s\n", parameters.getReserved22()));
		
		return text.toString();
	}
	
	@Override
	public void update(Observable source, Object detail) {
		parameterArea.setText(getParametersText());
	}
	
	/**
	 * A custom field for the patch name which synchronises with the parameters.
	 */
	private class NameField extends JTextField implements AWTObserver, DocumentListener {
		private static final long serialVersionUID = 1L;
		
		public NameField() {
			super(new NameDocument(), parameters.getPatchName().trim(), 12);
			parameters.addObserver(this);
			getDocument().addDocumentListener(this);
		}
		
		private String getPaddedText() {
			return (getText() + "            ").substring(0, 12);
		}
		
		@Override
		public void update(Observable source, Object detail) {
			if (detail instanceof ParameterChange && ((ParameterChange)detail).getOffset() < 12) {
				if (!getPaddedText().equals(parameters.getPatchName())) {
					setText(parameters.getPatchName().trim());
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
			//Plain text components do not fire these events
		}
		
		private void setPatchName() {
			if (!getPaddedText().equals(parameters.getPatchName())) {
				parameters.setPatchName(getPaddedText());
			}
		}
		
	}
	
	/**
	 * A plain document which is limited to 12 ASCII characters.
	 * Any additional characters inserted are ignored.
	 * Any non-ASCII characters entered are replaced by a question mark.
	 */
	private static class NameDocument extends PlainDocument {
		private static final long serialVersionUID = 1L;

		private Charset US_ASCII = Charset.forName("US-ASCII");
		
		@Override
		public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
			if (str == null)
				return;
			
			if (getLength() < 12) {
				byte[] asciiBytes = str.getBytes(US_ASCII);
				String asciiStr = new String(asciiBytes, 0, Math.min(asciiBytes.length, 12 - getLength()), US_ASCII);
				super.insertString(offs, asciiStr, a);
			}
		}
	}
	
}
