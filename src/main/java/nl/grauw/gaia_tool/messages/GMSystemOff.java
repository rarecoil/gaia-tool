package nl.grauw.gaia_tool.messages;

import javax.sound.midi.InvalidMidiDataException;

public class GMSystemOff extends UniversalSysex {
	
	final static byte GENERAL_MIDI_MESSAGE = 0x09;
	final static byte GENERAL_MIDI_OFF = 0x02;
	
	public GMSystemOff() throws InvalidMidiDataException {
		super(GENERAL_MIDI_MESSAGE, GENERAL_MIDI_OFF);
	}
	
}
