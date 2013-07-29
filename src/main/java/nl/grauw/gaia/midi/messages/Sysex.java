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

public class Sysex extends Message {
	
	public static final int SYSTEM_EXCLUSIVE = 0xF0;
	public static final int SPECIAL_SYSTEM_EXCLUSIVE = 0xF7;
	public static final int END_OF_EXCLUSIVE = 0xF7;
	
	public Sysex(byte[] message) {
		super(message);
	}
	
	public Sysex(int status, byte[] data) {
		super(createMessage(status, data));
		
		if (status != SYSTEM_EXCLUSIVE && status != SPECIAL_SYSTEM_EXCLUSIVE)
			throw new IllegalArgumentException("Invalid status code.");
	}
	
	private static byte[] createMessage(int status, byte[] data) {
		byte[] message = new byte[data.length + 1];
		message[0] = (byte)status;
		System.arraycopy(data, 0, message, 1, data.length);
		return message;
	}
	
}
