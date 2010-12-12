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
		GaiaView.installLnF();
		SwingUtilities.invokeLater(this);
	}
	
	@Override
	public void run() {
		GaiaView view2 = new GaiaView(gaia);
		view2.setVisible(true);
	}
	
}
