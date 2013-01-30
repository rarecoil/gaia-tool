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
import javax.sound.midi.ShortMessage;
import javax.sound.midi.SysexMessage;

import nl.grauw.gaia.tool.Address;

/**
 * Universal non-realtime system exclusive MIDI message
 */
public class DataSet1 extends SysexMessage {
	
	final static int BROADCAST_DEVICE = 0x7F;
	final static int ROLAND_ID = 0x41;
	final static int MODEL_SH01 = 0x41;
	final static int COMMAND_DT1 = 0x12;
	
	public DataSet1(SysexMessage sem) {
		super(sem.getMessage());
		
		byte[] data = getData();
		if (data[0] != ROLAND_ID || data[2] != 0 || data[3] != 0 || data[4] != MODEL_SH01 || data[5] != COMMAND_DT1)
			throw new IllegalArgumentException("Not a SH-01 DT1 MIDI message.");
		if (calculateChecksum(data) != data[data.length - 2])
			throw new IllegalArgumentException("Checksum mismatch.");
	}
	
	public DataSet1(Address address, byte[] data) throws InvalidMidiDataException {
		this(BROADCAST_DEVICE, address, data);
	}
	
	public DataSet1(int device_id, Address address, byte[] data) throws InvalidMidiDataException {
		super();
		if (device_id != 0x7F && (device_id < 0x10 || device_id > 0x1F))
			throw new IllegalArgumentException("Invalid device ID.");
		
		byte[] message = new byte[data.length + 12];
		message[0] = ROLAND_ID;
		message[1] = (byte)device_id;
		message[2] = 0;
		message[3] = 0;
		message[4] = MODEL_SH01;
		message[5] = COMMAND_DT1;
		message[6] = address.getByte1();
		message[7] = address.getByte2();
		message[8] = address.getByte3();
		message[9] = address.getByte4();
		for (int i = 0; i < data.length; i++) {
			message[10 + i] = data[i];
		}
		message[data.length + 10] = calculateChecksum(message);
		message[data.length + 11] = (byte)ShortMessage.END_OF_EXCLUSIVE;
		setMessage(SYSTEM_EXCLUSIVE, message, message.length);
	}
	
	private byte calculateChecksum(byte[] message) {
		byte sum = 0;
		for (int i = 6; i < message.length - 2; i++) {
			sum += message[i];
		}
		return (byte) (0x80 - sum & 0x7F);
	}
	
	public Address getAddress() {
		byte[] data = getData();
		return new Address(data[6], data[7], data[8], data[9]);
	}
	
	public int getSize() {
		return getData().length - 12;
	}
	
	public byte[] getDataSet() {
		byte[] data = getData();
		byte[] dataSet = new byte[data.length - 12];
		for (int i = 0; i < dataSet.length; i++) {
			dataSet[i] = data[i + 10];
		}
		return dataSet;
	}
	
	public String toString() {
		return String.format("Data set 1. Address: %s. Size: %XH.", this.getAddress(), this.getSize());
	}
	
}
