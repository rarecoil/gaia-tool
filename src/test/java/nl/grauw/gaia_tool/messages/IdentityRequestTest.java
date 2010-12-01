package nl.grauw.gaia_tool.messages;

import static org.junit.Assert.*;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;

import nl.grauw.gaia_tool.messages.IdentityRequest;

import org.junit.Test;

public class IdentityRequestTest {

	@Test
	public void testIdentityRequest() throws InvalidMidiDataException {
		MidiMessage mm = new IdentityRequest();
		byte[] message = mm.getMessage();
		byte[] expected = {(byte)0xF0, 0x7E, 0x7F, 0x06, 0x01, (byte)0xF7};
		assertArrayEquals(expected, message);
	}

	@Test
	public void testIdentityRequestInt() throws InvalidMidiDataException {
		MidiMessage mm = new IdentityRequest(0x10);
		byte[] message = mm.getMessage();
		byte[] expected = {(byte)0xF0, 0x7E, 0x10, 0x06, 0x01, (byte)0xF7};
		assertArrayEquals(expected, message);
	}

	@Test (expected=RuntimeException.class)
	public void testIdentityRequestInt_InvalidDevice() throws InvalidMidiDataException {
		new IdentityRequest(0x0F);
	}

	@Test (expected=RuntimeException.class)
	public void testIdentityRequestInt_InvalidDevice2() throws InvalidMidiDataException {
		new IdentityRequest(0x20);
	}

	@Test (expected=RuntimeException.class)
	public void testIdentityRequestInt_InvalidDevice3() throws InvalidMidiDataException {
		new IdentityRequest(0x7F);
	}

}
