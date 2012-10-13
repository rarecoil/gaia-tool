/*
 * Copyright 2010 Laurens Holst
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
			throw new IllegalArgumentException("Invalid device ID.");
	}
	
	public String toString() {
		String device = getDeviceID() == UniversalSysex.BROADCAST_DEVICE ? "broadcast" : String.format("%02XH", getDeviceID());
		return "Identity request message. Device ID: " + device + ".";
	}
	
}
