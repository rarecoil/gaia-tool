/**
 * 
 */
package nl.grauw.gaia_tool.messages;

import static org.junit.Assert.*;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;

import nl.grauw.gaia_tool.messages.GMSystemOff;

import org.junit.Test;

/**
 * @author Grauw
 *
 */
public class GMSystemOffTest {

	/**
	 * Test method for {@link nl.grauw.gaia_tool.messages.GMSystemOff#GMSystemOff()}.
	 */
	@Test
	public void testGMSystemOff() throws InvalidMidiDataException {
		MidiMessage mm = new GMSystemOff();
		byte[] message = mm.getMessage();
		assertEquals(6, message.length);
		assertEquals((byte) 0xF0, message[0]);
		assertEquals((byte) 0x7E, message[1]);
		assertEquals((byte) 0x7F, message[2]);
		assertEquals((byte) 0x09, message[3]);
		assertEquals((byte) 0x02, message[4]);
		assertEquals((byte) 0xF7, message[5]);
	}

}
