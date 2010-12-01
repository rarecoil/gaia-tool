package nl.grauw.gaia_tool.messages;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.SysexMessage;

/**
 * Universal non-realtime system exclusive MIDI message
 */
public class UniversalSysex extends SysexMessage {
	
	final static int UNIVERSAL_NONREALTIME_SYSEX = 0x7E;
	final static int BROADCAST_DEVICE = 0x7F;
	
	public UniversalSysex(int sub_id1, int sub_id2) throws InvalidMidiDataException {
		this(BROADCAST_DEVICE, sub_id1, sub_id2);
	}
	
	public UniversalSysex(int device_id, int sub_id1, int sub_id2) throws InvalidMidiDataException {
		this(device_id, sub_id1, sub_id2, new byte[0]);
	}
	
	public UniversalSysex(int device_id, int sub_id1, int sub_id2, byte[] data) throws InvalidMidiDataException {
		super();
		if (device_id != 0x7F && (device_id < 0x10 || device_id > 0x1F))
			throw new RuntimeException("Invalid device ID.");
		
		byte[] message = new byte[data.length + 5];
		message[0] = UNIVERSAL_NONREALTIME_SYSEX;
		message[1] = (byte)device_id;
		message[2] = (byte)sub_id1;
		message[3] = (byte)sub_id2;
		for (int i = 0; i < data.length; i++) {
			message[4 + i] = data[i];
		}
		message[4 + data.length] = (byte)ShortMessage.END_OF_EXCLUSIVE;
		setMessage(SYSTEM_EXCLUSIVE, message, message.length);
	}
	
}
