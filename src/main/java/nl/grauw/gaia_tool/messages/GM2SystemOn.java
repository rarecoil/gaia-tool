package nl.grauw.gaia_tool.messages;

import javax.sound.midi.InvalidMidiDataException;

public class GM2SystemOn extends UniversalSysex {
	
	final static byte GENERAL_MIDI_MESSAGE = 0x09;
	final static byte GENERAL_MIDI_OFF = 0x03;
	
	public GM2SystemOn() throws InvalidMidiDataException {
		super(GENERAL_MIDI_MESSAGE, GENERAL_MIDI_OFF);
	}
	
}
