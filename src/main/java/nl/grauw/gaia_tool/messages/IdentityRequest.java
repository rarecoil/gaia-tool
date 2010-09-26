package nl.grauw.gaia_tool.messages;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.SysexMessage;

public class IdentityRequest extends SysexMessage {
	
	final static int BROADCAST_DEVICE = 0x7F;
	
	/**
	 * Construct a broadcast identity request message.
	 */
	public IdentityRequest() throws InvalidMidiDataException {
		super();
		byte[] message = {0x7E, BROADCAST_DEVICE, 0x06, 0x01, (byte)0xF7};
		setMessage(SYSTEM_EXCLUSIVE, message, message.length);
	}
	
	/**
	 * Construct an identity request message for a specific device.
	 * @param device_id The device ID. Must be between 0x10-0x1F (inclusive).
	 */
	public IdentityRequest(int device_id) throws InvalidMidiDataException {
		super();
		if (device_id < 0x10 || device_id > 0x1F)
			throw new InvalidMidiDataException("Device ID out of range.");
		
		byte[] message = {0x7E, (byte)device_id, 0x06, 0x01, (byte)0xF7};
		setMessage(SYSTEM_EXCLUSIVE, message, message.length);
	}
	
}
