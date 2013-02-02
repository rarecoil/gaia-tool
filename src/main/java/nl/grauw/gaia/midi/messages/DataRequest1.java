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
package nl.grauw.gaia.midi.messages;

import nl.grauw.gaia.Address;

/**
 * Universal non-realtime system exclusive MIDI message
 */
public class DataRequest1 extends Sysex {
	
	final static int BROADCAST_DEVICE = 0x7F;
	final static int ROLAND_ID = 0x41;
	final static int MODEL_SH01 = 0x41;
	final static int COMMAND_RQ1 = 0x11;
	
	public DataRequest1(Address address, int size) {
		this(BROADCAST_DEVICE, address, size);
	}
	
	public DataRequest1(int device_id, Address address, int size) {
		super(createMessage(device_id, address, size));
		
		if (device_id != 0x7F && (device_id < 0x10 || device_id > 0x1F))
			throw new IllegalArgumentException("Invalid device ID.");
	}
	
	private static byte[] createMessage(int device_id, Address address, int size) {
		byte[] message = new byte[17];
		message[0] = (byte)SYSTEM_EXCLUSIVE;
		message[1] = ROLAND_ID;
		message[2] = (byte)device_id;
		message[3] = 0;
		message[4] = 0;
		message[5] = MODEL_SH01;
		message[6] = COMMAND_RQ1;
		message[7] = address.getByte1();
		message[8] = address.getByte2();
		message[9] = address.getByte3();
		message[10] = address.getByte4();
		message[11] = (byte) (size >> 21 & 0x7F);
		message[12] = (byte) (size >> 14 & 0x7F);
		message[13] = (byte) (size >> 7 & 0x7F);
		message[14] = (byte) (size & 0x7F);
		message[15] = calculateChecksum(message);
		message[16] = (byte)END_OF_EXCLUSIVE;
		return message;
	}
	
	private static byte calculateChecksum(byte[] message) {
		byte sum = 0;
		for (int i = 7; i < message.length - 2; i++) {
			sum += message[i];
		}
		return (byte) (0x80 - sum & 0x7F);
	}
	
	public Address getAddress() {
		return new Address(getData(6), getData(7), getData(8), getData(9));
	}
	
	public int getSize() {
		return getData(10) << 21 | getData(11) << 14 | getData(12) << 7 | getData(13);
	}
	
	public String toString() {
		return String.format("Data request 1. Address: %s. Size: %XH.", this.getAddress(), this.getSize());
	}
	
}
