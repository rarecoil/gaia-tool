package nl.grauw.gaia_tool;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.SysexMessage;

import nl.grauw.gaia_tool.messages.GenericMessage;
import nl.grauw.gaia_tool.messages.IdentityReplyMessage;

public class ResponseReceiver implements Receiver {

	@Override
	public void send(MidiMessage message, long timeStamp) {
		System.out.println("MIDI message received at " + timeStamp + ":");
		try {
			System.out.println("* " + processMidiMessage(message));
		} catch(Exception e) {
			System.out.println("*** Error: " + e);
		}
	}

	@Override
	public void close() {
	}
	
	public MidiMessage processMidiMessage(MidiMessage message) throws InvalidMidiDataException {
		if (message instanceof SysexMessage) {
			SysexMessage sem = (SysexMessage) message;
			if (sem.getData()[2] == 6 && sem.getData()[3] == 2) {
				return new IdentityReplyMessage(sem);
			}
		}
		return new GenericMessage(message);
	}

}
