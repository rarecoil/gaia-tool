package nl.grauw.gaia_tool.messages;

import javax.sound.midi.InvalidMidiDataException;

public class IdentityRequest extends UniversalSysex {
	
	final static int GENERAL_INFORMATION = 0x06;
	final static int IDENTITY_REQUEST = 0x01;
	
	/**
	 * Construct a broadcast identity request message.
	 */
	public IdentityRequest() throws InvalidMidiDataException {
		super(GENERAL_INFORMATION, IDENTITY_REQUEST);
	}
	
	/**
	 * Construct an identity request message for a specific device.
	 * @param device_id The device ID. Must be between 0x10-0x1F (inclusive).
	 */
	public IdentityRequest(int device_id) throws InvalidMidiDataException {
		super(device_id, GENERAL_INFORMATION, IDENTITY_REQUEST);
		
		// stupid super call restriction prevents me from making this assertion earlier
		if (device_id < 0x10 || device_id > 0x1F)
			throw new RuntimeException("Invalid device ID.");
	}
	
}
