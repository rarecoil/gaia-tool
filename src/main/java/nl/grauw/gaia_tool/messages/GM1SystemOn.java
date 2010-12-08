package nl.grauw.gaia_tool.messages;

import javax.sound.midi.InvalidMidiDataException;

public class GM1SystemOn extends UniversalSysex {
	
	final static byte GENERAL_MIDI_MESSAGE = 0x09;
	final static byte GENERAL_MIDI_ON = 0x01;
	
	public GM1SystemOn() throws InvalidMidiDataException {
		super(GENERAL_MIDI_MESSAGE, GENERAL_MIDI_ON);
	}
	
}
