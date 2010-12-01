package nl.grauw.gaia_tool.messages;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.SysexMessage;

import nl.grauw.gaia_tool.Address;

/**
 * Universal non-realtime system exclusive MIDI message
 */
public class DataRequest1 extends SysexMessage {
	
	final static int BROADCAST_DEVICE = 0x7F;
	final static int ROLAND_ID = 0x41;
	final static int MODEL_SH01 = 0x41;
	final static int COMMAND_RQ1 = 0x11;
	
	public DataRequest1(Address address, int size) throws InvalidMidiDataException {
		this(BROADCAST_DEVICE, address, size);
	}
	
	public DataRequest1(int device_id, Address address, int size) throws InvalidMidiDataException {
		super();
		if (device_id != 0x7F && (device_id < 0x10 || device_id > 0x1F))
			throw new RuntimeException("Invalid device ID.");
		
		byte[] message = new byte[16];
		message[0] = ROLAND_ID;
		message[1] = (byte)device_id;
		message[2] = 0;
		message[3] = 0;
		message[4] = MODEL_SH01;
		message[5] = COMMAND_RQ1;
		message[6] = address.getByte1();
		message[7] = address.getByte2();
		message[8] = address.getByte3();
		message[9] = address.getByte4();
		message[10] = (byte) (size >> 21 & 0x7F);
		message[11] = (byte) (size >> 14 & 0x7F);
		message[12] = (byte) (size >> 7 & 0x7F);
		message[13] = (byte) (size & 0x7F);
		message[14] = calculateChecksum(message);
		message[15] = (byte)ShortMessage.END_OF_EXCLUSIVE;
		setMessage(SYSTEM_EXCLUSIVE, message, message.length);
	}
	
	private byte calculateChecksum(byte[] message) {
		byte sum = 0;
		for (int i = 6; i < message.length - 2; i++) {
			sum += message[i];
		}
		return (byte) (0x80 - sum & 0x7F);
	}
	
}
