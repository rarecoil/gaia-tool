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
			throw new IllegalArgumentException("Invalid device ID.");
		
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
	
	public int getDeviceID() {
		return getData()[1];
	}
	
}
