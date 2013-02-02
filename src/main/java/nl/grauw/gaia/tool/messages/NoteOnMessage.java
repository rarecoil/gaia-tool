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

import nl.grauw.gaia.tool.Note;

public class NoteOnMessage extends ChannelMessage {
	
	public NoteOnMessage(byte[] message) {
		super(message);
	}
	
	public NoteOnMessage(int channel, Note note, int velocity) {
		super(NOTE_ON, channel, note.getNoteNumber(), velocity);
	}
	
	public Note getNote() {
		return new Note(getData(0));
	}
	
	public int getVelocity() {
		return getData(1);
	}
	
	public String toString() {
		return "Note on message. Channel: " + (getChannel() + 1) + ". Note: " + getNote() +
				". Velocity: " + getVelocity() + ".";
	}

}
