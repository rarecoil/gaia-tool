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

public class Message {
	
	protected final byte[] message;
	
	public Message(byte[] message) {
		this.message = message;
		
		if (message.length < 1)
			throw new RuntimeException("Message too short.");
		if ((message[0] & 0x80) == 0)
			throw new RuntimeException("Invalid status value.");
		for (int i = 1; i < message.length; i++)
			if ((message[i] & 0x80) == 1)
				throw new RuntimeException("Data byte out of range.");
	}
	
	public byte[] getMessage() {
		return message.clone();
	}
	
	public int getStatus() {
		return message[0];
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
			throw new RuntimeException("Data position out of range.");
		return message[i + 1];
	}
	
	protected static String toHex(int number) {
		return String.format("%1$02XH", number);
	}
	
	public String toString() {
		String s = "MIDI message. Status code: " + toHex(getStatus()) + ". Body: ";
		byte[] message_bytes = getMessage();
		for (byte message_byte : message_bytes) {
			s += toHex(message_byte & 0xFF) + " ";
		}
		return s.trim() + ".";
	}

}
