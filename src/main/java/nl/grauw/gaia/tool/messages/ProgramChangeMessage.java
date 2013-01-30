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

public class ProgramChangeMessage extends ShortMessage {
	
	public ProgramChangeMessage(byte[] message) {
		super(message);
	}
	
	public ProgramChangeMessage(int channel, int program) throws InvalidMidiDataException {
		super();
		setMessage(PROGRAM_CHANGE, channel, program, 0);
	}
	
	public int getProgram() {
		return getData1();
	}
	
	public String toString() {
		return "Program change message. Channel: " + (getChannel() + 1) +
				". Program: " + (getProgram() + 1) + ".";
	}

}
