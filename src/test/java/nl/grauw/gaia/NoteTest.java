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
package nl.grauw.gaia;

import static org.junit.Assert.*;

import nl.grauw.gaia.Note;
import nl.grauw.gaia.Note.NoteName;

import org.junit.Test;

public class NoteTest {

	@Test
	public void testGetNote() {
		Note note = new Note(60);
		assertEquals(NoteName.C, note.getNote());
		assertEquals(4, note.getOctave());
	}

	@Test
	public void testGetOctave() {
		Note note = new Note(60);
		assertEquals(4, note.getOctave());
	}

	@Test
	public void testGetNoteNumber() {
		Note note = new Note(NoteName.C, 4);
		assertEquals(60, note.getNoteNumber());
	}

}
