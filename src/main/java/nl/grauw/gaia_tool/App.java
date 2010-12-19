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
package nl.grauw.gaia_tool;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import nl.grauw.gaia_tool.views.GaiaView;

public class App implements Runnable {
	
	Gaia gaia;
	
	public static void main(String[] args) throws MidiUnavailableException, InvalidMidiDataException {
		new App();
	}
	
	public App() throws MidiUnavailableException, InvalidMidiDataException {
		// set up the Nimbus look and feel
		GaiaView.installLookAndFeel();
		
		// instantiate model
		gaia = new Gaia();
		try {
			gaia.open();
		} catch(MidiUnavailableException e) {
			JOptionPane.showMessageDialog(null, e + "\n\nVerify that the GAIA is connected by USB, and turned on.",
					"Error connecting to Roland GAIA SH-01.", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		gaia.requestIdentity();
		
		// render view
		SwingUtilities.invokeLater(this);
	}
	
	@Override
	public void run() {
		GaiaView view2 = new GaiaView(gaia);
		view2.setVisible(true);
	}
	
}
