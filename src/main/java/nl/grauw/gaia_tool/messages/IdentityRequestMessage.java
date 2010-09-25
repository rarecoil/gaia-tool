package nl.grauw.gaia_tool.messages;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.SysexMessage;

public class IdentityRequestMessage extends SysexMessage {

	/**
	 * Construct an identity request message with device ID 0x7F.
	 */
	public IdentityRequestMessage() throws InvalidMidiDataException {
		this(0x7F);
	}
	
	/**
	 * Construct an identity request message with nonstandard device ID.
	 * @param device_id The device ID. Must be between 0x10-0x1F (inclusive), or 0x7F.
	 */
	public IdentityRequestMessage(int device_id) throws InvalidMidiDataException {
		super();
		if ((device_id < 0x10 || device_id > 0x1F) && device_id != 0x7F)
			throw new InvalidMidiDataException("Device ID out of range.");
		
		byte[] message = {0x7E, (byte)device_id, 0x06, 0x01, (byte)0xF7};
		setMessage(SYSTEM_EXCLUSIVE, message, message.length);
	}
	
}
