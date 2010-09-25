package nl.grauw.gaia_tool;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;

import nl.grauw.gaia_tool.messages.GenericMessage;

public class ResponseReceiver implements Receiver {

	@Override
	public void send(MidiMessage message, long timeStamp) {
		System.out.println("MIDI message received at " + timeStamp + ":");
		System.out.println("* " + processMidiMessage(message));
	}

	@Override
	public void close() {
	}
	
	public MidiMessage processMidiMessage(MidiMessage message) {
		return new GenericMessage(message);
	}

}
