package nl.grauw.gaia_tool.messages;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;

public class PitchBendChangeMessage extends ShortMessage {
	
	public PitchBendChangeMessage(ShortMessage sm) {
		super(sm.getMessage());
	}
	
	public PitchBendChangeMessage(int channel, int value) throws InvalidMidiDataException {
		super();
		setMessage(PITCH_BEND, channel, value + 8192 & 0x7F, value + 8192 >> 7);
	}
	
	public int getValue() {
		return (getData2() << 7 | getData1()) - 8192;
	}
	
	public String toString() {
		return "Pitch bend change message. Channel: " + (getChannel() + 1) +
				". Value: " + getValue() + ".";
	}

}
