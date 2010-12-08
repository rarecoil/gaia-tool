/**
 * 
 */
package nl.grauw.gaia_tool;

import static org.junit.Assert.*;

import nl.grauw.gaia_tool.Note.NoteName;

import org.junit.Test;

/**
 * @author Grauw
 *
 */
public class NoteTest {

	/**
	 * Test method for {@link nl.grauw.gaia_tool.Note#getNote()}.
	 */
	@Test
	public void testGetNote() {
		Note note = new Note(60);
		assertEquals(NoteName.C, note.getNote());
		assertEquals(4, note.getOctave());
	}

	/**
	 * Test method for {@link nl.grauw.gaia_tool.Note#getOctave()}.
	 */
	@Test
	public void testGetOctave() {
		Note note = new Note(60);
		assertEquals(4, note.getOctave());
	}

	/**
	 * Test method for {@link nl.grauw.gaia_tool.Note#getNoteNumber()}.
	 */
	@Test
	public void testGetNoteNumber() {
		Note note = new Note(NoteName.C, 4);
		assertEquals(60, note.getNoteNumber());
	}

}
