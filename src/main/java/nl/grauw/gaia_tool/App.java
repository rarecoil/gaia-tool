package nl.grauw.gaia_tool;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

public class App {
	
	public static void main(String[] args) throws MidiUnavailableException, InvalidMidiDataException
	{
		Gaia gaia = new Gaia();
		gaia.open();
		
		System.out.println("Well, um...");

		gaia.requestIdentity();
		gaia.playTestNote();
		
		System.out.println("Ya.");
		gaia.close();
	}
	
}
