package nl.grauw.gaia_tool;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
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
		gaia.open();
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
