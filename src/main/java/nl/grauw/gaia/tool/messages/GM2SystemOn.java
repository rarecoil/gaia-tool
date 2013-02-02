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

public class GM2SystemOn extends UniversalSysex {
	
	final static byte GENERAL_MIDI_MESSAGE = 0x09;
	final static byte GENERAL_MIDI_2_ON = 0x03;
	
	public GM2SystemOn() {
		super(GENERAL_MIDI_MESSAGE, GENERAL_MIDI_2_ON);
	}
	
	public GM2SystemOn(int device_id) {
		super(device_id, GENERAL_MIDI_MESSAGE, GENERAL_MIDI_2_ON);
	}
	
}
