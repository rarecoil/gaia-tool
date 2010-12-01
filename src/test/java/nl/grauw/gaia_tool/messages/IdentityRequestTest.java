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
		assertEquals(6, message.length);
		assertEquals((byte) 0xF0, message[0]);
		assertEquals((byte) 0x7E, message[1]);
		assertEquals((byte) 0x7F, message[2]);
		assertEquals((byte) 0x06, message[3]);
		assertEquals((byte) 0x01, message[4]);
		assertEquals((byte) 0xF7, message[5]);
	}

	@Test
	public void testIdentityRequestInt() throws InvalidMidiDataException {
		MidiMessage mm = new IdentityRequest(0x10);
		byte[] message = mm.getMessage();
		assertEquals(6, message.length);
		assertEquals((byte) 0xF0, message[0]);
		assertEquals((byte) 0x7E, message[1]);
		assertEquals((byte) 0x10, message[2]);
		assertEquals((byte) 0x06, message[3]);
		assertEquals((byte) 0x01, message[4]);
		assertEquals((byte) 0xF7, message[5]);
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
