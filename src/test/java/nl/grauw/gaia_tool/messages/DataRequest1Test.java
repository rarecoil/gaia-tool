/**
 * 
 */
package nl.grauw.gaia_tool.messages;

import static org.junit.Assert.*;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;

import nl.grauw.gaia_tool.Address;

import org.junit.Test;

/**
 * @author Grauw
 *
 */
public class DataRequest1Test {

	/**
	 * Test method for {@link nl.grauw.gaia_tool.messages.DataRequest1#DataRequest1(int, int)}.
	 */
	@Test
	public void testDataRequest1AddressInt() throws InvalidMidiDataException {
		MidiMessage mm = new DataRequest1(new Address(0x01, 0x23, 0x45, 0x67), 0x17F);
		byte[] message = mm.getMessage();
		byte[] expected = {(byte)0xF0, 0x41, 0x7F, 0x00, 0x00, 0x41, 0x11,
				0x01, 0x23, 0x45, 0x67, 0x00, 0x00, 0x02, 0x7F, 0x2F, (byte)0xF7};
		assertArrayEquals(expected, message);
	}

	/**
	 * Test method for {@link nl.grauw.gaia_tool.messages.DataRequest1#DataRequest1(int, int, int)}.
	 */
	@Test
	public void testDataRequest1IntAddressInt() throws InvalidMidiDataException {
		MidiMessage mm = new DataRequest1(0x10, new Address(0x01, 0x23, 0x45, 0x67), 0x17F);
		byte[] message = mm.getMessage();
		byte[] expected = {(byte)0xF0, 0x41, 0x10, 0x00, 0x00, 0x41, 0x11,
				0x01, 0x23, 0x45, 0x67, 0x00, 0x00, 0x02, 0x7F, 0x2F, (byte)0xF7};
		assertArrayEquals(expected, message);
	}

}
