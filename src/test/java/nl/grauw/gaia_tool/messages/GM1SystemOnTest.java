/**
 * 
 */
package nl.grauw.gaia_tool.messages;

import static org.junit.Assert.*;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;

import nl.grauw.gaia_tool.messages.GMSystemOn;

import org.junit.Test;

/**
 * @author Grauw
 *
 */
public class GM1SystemOnTest {

	/**
	 * Test method for {@link nl.grauw.gaia_tool.messages.GMSystemOff#GMSystemOff()}.
	 */
	@Test
	public void testGM1SystemOn() throws InvalidMidiDataException {
		MidiMessage mm = new GMSystemOn();
		byte[] message = mm.getMessage();
		byte[] expected = {(byte)0xF0, 0x7E, 0x7F, 0x09, 0x01, (byte)0xF7};
		assertArrayEquals(expected, message);
	}

}
