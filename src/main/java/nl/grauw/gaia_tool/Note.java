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
package nl.grauw.gaia_tool;

public class Note {
	
	public enum NoteName {
		C("C"), C_SHARP("C#"), D("D"), D_SHARP("D#"), E("E"), F("F"), F_SHARP("F#"),
		G("G"), G_SHARP("G#"), A("A"), A_SHARP("A#"), B("B");
		
		private String label;
		
		NoteName(String label) {
			this.label = label;
		}
		
		public String toString() {
			return label;
		}
		
		public static NoteName getNoteByName(String name) {
			for (NoteName note : values()) {
				if (note.label.equals(name)) {
					return note;
				}
			}
			return null;
		}
	}
	
	private NoteName note;
	private int octave;
	
	public Note(NoteName note, int octave) {
		if (note == null || octave < -1 || octave > 9 ||
				(octave == 9 && (note == NoteName.A || note == NoteName.A_SHARP || note == NoteName.B))) {
			// XXX: should also include NoteName.G but this is used for OFF sometimes...
			throw new IllegalArgumentException("Specified note is out of range.");
		}
		this.note = note;
		this.octave = octave;
	}
	
	/**
	 * 
	 * @param noteNumber The note number as used in the MIDI standard.
	 */
	public Note(int noteNumber) {
		this(NoteName.values()[noteNumber % 12], noteNumber / 12 - 1);
	}
	
	/**
	 * 
	 * @param noteString A string in the form "C 4" or "G#5". Some variations in case and spacing are accepted.
	 */
	public Note(String noteString) {
		this(parseNoteName(noteString), parseOctave(noteString));
	}
	
	private static NoteName parseNoteName(String noteString) {
		String trimmedString = noteString.trim().toUpperCase();
		return NoteName.getNoteByName(trimmedString.substring(0, trimmedString.charAt(1) == '#' ? 2 : 1));
	}
	
	private static int parseOctave(String noteString) {
		String trimmedString = noteString.trim().toUpperCase();
		return new Integer(trimmedString.substring(trimmedString.charAt(1) == '#' ? 2 : 1).trim());
	}
	
	public NoteName getNote() {
		return note;
	}
	
	public int getOctave() {
		return octave;
	}
	
	public int getNoteNumber() {
		return (octave + 1) * 12 + note.ordinal();
	}
	
	public String toString() {
		return note + (note.toString().length() == 1 ? " " : "") + octave;
	}

}
