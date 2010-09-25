package nl.grauw.gaia_tool.messages;

import javax.sound.midi.MidiMessage;

public class GenericMessage extends MidiMessage {
	
	public GenericMessage(MidiMessage mm) {
		super(mm.getMessage());
	}

	@Override
	public Object clone() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String toString() {
		String s = "Generic MIDI message. Status code: " + String.format("0x%1$02X", this.getStatus()) + ". Body: ";
		byte[] message_bytes = this.getMessage();
		for (byte message_byte : message_bytes) {
			s += String.format("0x%1$02X", message_byte & 0xFF) + " ";
		}
		return s.trim() + ".";
	}

}
