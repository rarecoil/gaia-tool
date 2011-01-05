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

import javax.swing.GroupLayout;
import javax.swing.JPanel;

import nl.grauw.gaia_tool.mvc.AWTObserver;
import nl.grauw.gaia_tool.mvc.Observable;
import nl.grauw.gaia_tool.parameters.ArpeggioCommon;
import nl.grauw.gaia_tool.views.EnumComboBox;
import nl.grauw.gaia_tool.views.ValueSpinner;

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
			gridComboBox = new EnumComboBox(parameters.getArpeggioGrid(), "Grid");
			gridComboBox.setToolTipText("<html>Sets the note division and resolution in a " +
					"grid, and how much of a “shuffle” syncopation is applied to it.</html>");
		}
		return gridComboBox;
	}
	
	private EnumComboBox getArpeggioDurationComboBox() {
		if (durationComboBox == null) {
			durationComboBox = new EnumComboBox(parameters.getArpeggioDuration(), "Duration");
			durationComboBox.setToolTipText("<html>Determines whether the sounds are played staccato " +
					"(short and clipped), or tenuto (fully drawn out).<br><br> " +
					"<b>30–120%:</b> The percentage of the length of the note relative to the " +
					"full length of the note set in the grid.<br> When a series of grids is connected with " +
					"ties, it is relative to the final grid.<br> " +
					"<b>Full:</b> Even if the linked grid is not connected with a tie, the same note " +
					"continues to sound until<br> the point at which the next new sound is specified.</html>");
		}
		return durationComboBox;
	}
	
	private EnumComboBox getArpeggioMotifComboBox() {
		if (motifComboBox == null) {
			motifComboBox = new EnumComboBox(parameters.getArpeggioMotif(), "Motif");
			motifComboBox.setToolTipText("<html>The method used to play sounds (motif) when you have a greater " +
					"number of notes than programmed for the arpeggio style.<br>" +
					"When the number of keys played is less than the number of notes in the arpeggio style, " +
					"the highest pitched of the pressed keys is played by default.<br><br> " +
					"Base motif patterns:<br> " +
					"<b>Up:</b> The notes play in order from the lowest of the pressed keys.<br> " +
					"<b>Down:</b> The notes play in order from the highest of the pressed keys. " +
					"No note is played every time.<br> " +
					"<b>Up & down:</b> The notes play in order from the lowest of the pressed keys and then back " +
					"again in the reverse order.<br> " +
					"<b>Random:</b> The notes play randomly.<br> " +
					"<b>Phrase:</b> Pressing just one key plays a phrase based on the pitch of that key. " +
					"If you press more than one key, the key you press last is used.<br><br> " +
					"Motif annotations:<br> " +
					"<b>(L)</b> Notes from the lowest of the keys pressed are sounded every time.<br> " +
					"<b>(L&H)</b> Notes from the lowest and highest of the keys pressed are sounded every time.<br> " +
					"</html>");
		}
		return motifComboBox;
	}
	
	private ValueSpinner getArpeggioOctaveRangeSpinner() {
		if (octaveRangeSpinner == null) {
			octaveRangeSpinner = new ValueSpinner(parameters.getArpeggioOctaveRange(), "Octave range");
			octaveRangeSpinner.setToolTipText("<html>The range of the arpeggio.<br> " +
					"Adds an effect that shifts arpeggios one cycle at a time in octave units.<br> " +
					"You can set the shift range upwards or downwards (up to three octaves up or down).</html>");
		}
		return octaveRangeSpinner;
	}
	
	private ValueSpinner getArpeggioAccentRateSpinner() {
		if (accentRateSpinner == null) {
			accentRateSpinner = new ValueSpinner(parameters.getArpeggioAccentRate(), "Accent rate");
			accentRateSpinner.setToolTipText("<html>The accent strength of the arpeggio.<br> " +
					"With a setting of “100”, the arpeggiated notes will have the velocities that are " +
					"programmed by the arpeggio style.<br> " +
					"With a setting of “0”, all arpeggiated notes will be sounded at a fixed velocity.</html>");
		}
		return accentRateSpinner;
	}
	
	private ValueSpinner getArpeggioVelocitySpinner() {
		if (velocitySpinner == null) {
			velocitySpinner = new ValueSpinner(parameters.getArpeggioVelocity(), "Velocity");
			velocitySpinner.setToolTipText("<html>The loudness of the notes that you play.<br><br> " +
					"<b>REAL (0):</b> The velocity value of each note will depend on how strongly you play the keyboard.<br> " +
					"<b>1–127:</b> Each note will have a fixed velocity regardless of how strongly you play the keyboard.</html>");
		}
		return velocitySpinner;
	}
	
	private ValueSpinner getEndStepSpinner() {
		if (endStepSpinner == null) {
			endStepSpinner = new ValueSpinner(parameters.getEndStep(), "End step");
			endStepSpinner.setToolTipText("The number of steps for the arpeggio style.");
		}
		return endStepSpinner;
	}
	
}
