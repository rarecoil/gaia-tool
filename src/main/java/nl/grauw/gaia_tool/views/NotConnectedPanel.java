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

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.sound.midi.MidiUnavailableException;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;

import nl.grauw.gaia_tool.Gaia;

public class NotConnectedPanel extends JPanel implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	
	private Gaia gaia;
	
	private JLabel title;
	private JLabel problem;
	private JLabel firstHint;
	private JLabel secondHint;
	private JButton connect;
	private JButton configure;
	
	public NotConnectedPanel(Gaia gaia) {
		this.gaia = gaia;
		initComponents();
	}
	
	private void initComponents() {
		title = new JLabel("<html>GAIA not connected</html>");
		problem = new JLabel("<html>The Roland GAIA is currently not connected.</html>");
		firstHint = new JLabel("<html><b>Is your GAIA turned on?</b> If not, then turn it on and " +
				"press the connect button below.</html>");
		secondHint = new JLabel("<html>Otherwise, the MIDI port auto-detection might have been " +
				"unsuccessful. Press the “Configure MIDI” button to manually select the MIDI " +
				"ports.</html>");
		connect = new JButton("Connect");
		connect.addActionListener(this);
		configure = new JButton("Configure MIDI");
		configure.addActionListener(this);
		
		title.setFont(title.getFont().deriveFont(24f).deriveFont(Font.BOLD));
		
		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setHorizontalGroup(
				layout.createParallelGroup()
					.addComponent(title)
					.addComponent(problem)
					.addComponent(firstHint)
					.addComponent(secondHint)
					.addComponent(connect)
					.addComponent(configure)
			);
		layout.setVerticalGroup(
				layout.createSequentialGroup()
					.addComponent(title)
					.addComponent(problem)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(firstHint)
					.addComponent(secondHint)
					.addComponent(connect)
					.addComponent(configure)
			);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == connect) {
			reconnectGaia();
		} else if (e.getSource() == configure) {
			new MIDIDeviceSelector(gaia, this).show();
		}
	}
	
	private void reconnectGaia() {
		if (!gaia.canOpen()) {
			gaia.autoDetectMIDIDevices();
		}
		if (gaia.canOpen()) {
			try {
				gaia.open();
			} catch (MidiUnavailableException ex) {
				JOptionPane.showMessageDialog(this, "MIDI port unavailable",
						"Problem connecting to Roland GAIA", JOptionPane.ERROR_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(this, "The GAIA MIDI ports could not be found. Is your GAIA turned on?",
					"Problem connecting to Roland GAIA", JOptionPane.ERROR_MESSAGE);
		}
	}
	
}
