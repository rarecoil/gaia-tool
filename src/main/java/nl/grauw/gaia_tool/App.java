package nl.grauw.gaia_tool;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Transmitter;

/**
 * Hello world!
 *
 */
public class App {
	
	int channel = 0;
	ResponseReceiver responseReceiver = new ResponseReceiver();
	
	public static void main(String[] args) throws MidiUnavailableException, InvalidMidiDataException
	{
		App app = new App();
		app.blaat();
	}
	
	public void blaat() throws MidiUnavailableException, InvalidMidiDataException {
		Transmitter t = MidiSystem.getTransmitter();
		t.setReceiver(responseReceiver);
		
		Receiver r = MidiSystem.getReceiver();
		System.out.println("Well, um...");
		
		ShortMessage sm = new ShortMessage();
		sm.setMessage(ShortMessage.NOTE_ON, channel, 60, 127);
		r.send(sm, -1);
		
		System.out.println("Ya.");
//		t.close();
	}
}
