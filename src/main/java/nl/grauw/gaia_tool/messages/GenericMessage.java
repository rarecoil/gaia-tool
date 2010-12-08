package nl.grauw.gaia_tool.messages;

import javax.sound.midi.MidiMessage;

public class GenericMessage extends MidiMessage {
	
	public GenericMessage(MidiMessage mm) {
		super(mm.getMessage());
	}

	@Override
	public Object clone() {
		return new GenericMessage(this);
	}
	
	protected static String toHex(int number) {
		return String.format("%1$02XH", number);
	}
	
	public String toString() {
		String s = "Generic MIDI message. Status code: " + toHex(getStatus()) + ". Body: ";
		byte[] message_bytes = getMessage();
		for (byte message_byte : message_bytes) {
			s += toHex(message_byte & 0xFF) + " ";
		}
		return s.trim() + ".";
	}

}
