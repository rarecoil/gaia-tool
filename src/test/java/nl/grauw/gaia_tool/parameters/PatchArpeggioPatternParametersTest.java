package nl.grauw.gaia_tool.parameters;

import static org.junit.Assert.*;

import nl.grauw.gaia_tool.Note.NoteName;

import org.junit.Test;

public class PatchArpeggioPatternParametersTest {

	static byte[] testAddressMap = {
		0x03, 0x0C, // 0x00
		0x00, 0x00, 0x07, 0x0F, 0x08, 0x00, 0x00, 0x00, // 0x02
		0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // 0x0A
		0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // 0x12
		0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // 0x1A
		0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // 0x22
		0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // 0x2A
		0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // 0x32
		0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x04, 0x00  // 0x3A
	};

	@Test (expected = RuntimeException.class)
	public void testPatchArpeggioPatternParameters() {
		new PatchArpeggioPatternParameters(new byte[65]);
	}

	@Test
	public void testGetOriginalNote() {
		PatchArpeggioPatternParameters papp = new PatchArpeggioPatternParameters(testAddressMap);
		assertEquals(NoteName.C, papp.getOriginalNote().getNote());
		assertEquals(4, papp.getOriginalNote().getOctave());
	}

	@Test
	public void testGetStepData() {
		PatchArpeggioPatternParameters papp = new PatchArpeggioPatternParameters(testAddressMap);
		assertEquals(0, papp.getStepData(1).getValue());
		assertEquals(127, papp.getStepData(2).getValue());
		assertEquals(128, papp.getStepData(3).getValue());
		assertEquals(64, papp.getStepData(32).getValue());
	}

	@Test (expected = RuntimeException.class)
	public void testGetStepDataInvalidLow() {
		PatchArpeggioPatternParameters papp = new PatchArpeggioPatternParameters(testAddressMap);
		papp.getStepData(0);
	}

	@Test (expected = RuntimeException.class)
	public void testGetStepDataInvalidHigh() {
		PatchArpeggioPatternParameters papp = new PatchArpeggioPatternParameters(testAddressMap);
		papp.getStepData(33);
	}

}
