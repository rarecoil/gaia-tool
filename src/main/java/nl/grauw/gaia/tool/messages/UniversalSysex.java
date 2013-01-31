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
package nl.grauw.gaia.tool.messages;

import javax.sound.midi.InvalidMidiDataException;

/**
 * Universal non-realtime system exclusive MIDI message
 */
public class UniversalSysex extends Sysex {
	
	final static int UNIVERSAL_NONREALTIME_SYSEX = 0x7E;
	final static int BROADCAST_DEVICE = 0x7F;
	
	public UniversalSysex(byte[] message) {
		super(message);
	}
	
	public UniversalSysex(int sub_id1, int sub_id2) throws InvalidMidiDataException {
		this(BROADCAST_DEVICE, sub_id1, sub_id2);
	}
	
	public UniversalSysex(int device_id, int sub_id1, int sub_id2) throws InvalidMidiDataException {
		this(device_id, sub_id1, sub_id2, new byte[0]);
	}
	
	public UniversalSysex(int device_id, int sub_id1, int sub_id2, byte[] data) throws InvalidMidiDataException {
		super(createMessage(device_id, sub_id1, sub_id2, data));
		
		if (device_id != 0x7F && (device_id < 0x10 || device_id > 0x1F))
			throw new IllegalArgumentException("Invalid device ID.");
	}
	
	private static byte[] createMessage(int device_id, int sub_id1, int sub_id2, byte[] data) {
		byte[] message = new byte[data.length + 6];
		message[0] = (byte)SYSTEM_EXCLUSIVE;
		message[1] = UNIVERSAL_NONREALTIME_SYSEX;
		message[2] = (byte)device_id;
		message[3] = (byte)sub_id1;
		message[4] = (byte)sub_id2;
		System.arraycopy(data, 0, message, 5, data.length);
		message[5 + data.length] = (byte)END_OF_EXCLUSIVE;
		return message;
	}
	
	public int getDeviceId() {
		return getData()[1];
	}
	
}
