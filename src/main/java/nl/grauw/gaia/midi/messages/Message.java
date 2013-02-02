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

public class Message {
	
	protected final byte[] message;
	
	public Message(byte[] message) {
		this.message = message;
		
		if (message.length < 1)
			throw new IllegalArgumentException("Message too short.");
		if ((message[0] & 0x80) == 0)
			throw new IllegalArgumentException("Invalid status value.");
		for (int i = 1; i < message.length; i++)
			if ((message[i] & 0x80) != 0 && !((message[i] & 0xFF) == Sysex.END_OF_EXCLUSIVE && (message[0] & 0xFF) == Sysex.SYSTEM_EXCLUSIVE && i == message.length - 1))
				throw new IllegalArgumentException("Data byte out of range.");
	}
	
	public byte[] getMessage() {
		return message.clone();
	}
	
	public int getStatus() {
		return message[0] & 0xFF;
	}
	
	public int getSize() {
		return message.length;
	}
	
	public int getDataSize() {
		return message.length - 1;
	}
	
	public byte[] getData() {
		byte[] copy = new byte[message.length - 1];
		System.arraycopy(message, 1, copy, 0, copy.length);
		return copy;
	}
	
	public int getData(int i) {
		if (i < 0 || i >= (message.length - 1))
			throw new IllegalArgumentException("Data position out of range.");
		return message[i + 1] & 0xFF;
	}
	
	protected static String toHex(int number) {
		return String.format("%1$02XH", number);
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		byte[] message_bytes = getMessage();
		for (byte message_byte : message_bytes) {
			builder.append(toHex(message_byte & 0xFF));
			builder.append(" ");
		}
		return String.format("MIDI message: %s.", builder.toString().trim());
	}

}
