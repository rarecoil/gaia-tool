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

import javax.sound.midi.MidiMessage;

public class GenericMessage extends MidiMessage {
	
	public GenericMessage(MidiMessage mm) {
		super(mm.getMessage());
	}

	@Override
	public Object clone() {
		return new GenericMessage(this);
	}
	
	protected static String toHex(int number) {
		return String.format("%1$02XH", number);
	}
	
	public String toString() {
		String s = "Generic MIDI message. Status code: " + toHex(getStatus()) + ". Body: ";
		byte[] message_bytes = getMessage();
		for (byte message_byte : message_bytes) {
			s += toHex(message_byte & 0xFF) + " ";
		}
		return s.trim() + ".";
	}

}
