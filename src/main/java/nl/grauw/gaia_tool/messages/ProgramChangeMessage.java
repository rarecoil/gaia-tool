package nl.grauw.gaia_tool.messages;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;

public class ProgramChangeMessage extends ShortMessage {
	
	public ProgramChangeMessage(ShortMessage sm) {
		super(sm.getMessage());
	}
	
	public ProgramChangeMessage(int channel, int program) throws InvalidMidiDataException {
		super();
		setMessage(PROGRAM_CHANGE, channel, program, 0);
	}
	
	public int getProgram() {
		return getData1();
	}
	
	public String toString() {
		return "Program change message. Channel: " + (getChannel() + 1) +
				". Program: " + (getProgram() + 1) + ".";
	}

}
