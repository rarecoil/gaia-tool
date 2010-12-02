package nl.grauw.gaia_tool;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import nl.grauw.gaia_tool.views.GaiaView;

public class App {
	
	public static void main(String[] args) throws MidiUnavailableException, InvalidMidiDataException {
		// instantiate model
		Gaia gaia = new Gaia();
		gaia.open();
		gaia.requestIdentity();
		
		// render view
		GaiaView view = new GaiaView(gaia);
		view.setVisible(true);
	}
	
}
