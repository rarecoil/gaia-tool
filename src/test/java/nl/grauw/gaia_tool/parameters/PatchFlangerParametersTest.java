package nl.grauw.gaia_tool.parameters;

import static org.junit.Assert.*;

import nl.grauw.gaia_tool.parameters.PatchFlangerParameters.FlangerType;

import org.junit.Test;

public class PatchFlangerParametersTest {

	static byte[] testAddressMap = {
		0x03, // 0x00
		0x08, 0x00, 0x07, 0x0F, 0x08, 0x00, 0x07, 0x0E, // 0x01
		0x08, 0x00, 0x07, 0x0D, 0x08, 0x00, 0x07, 0x0C, // 0x09
		0x03, 0x01, 0x0E, 0x00, 0x0C, 0x0E, 0x02, 0x00, // 0x11
		0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // 0x19
		0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // 0x21
		0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // 0x29
		0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // 0x31
		0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // 0x39
		0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // 0x41
		0x0C, 0x0A, 0x0F, 0x0E, 0x0B, 0x0A, 0x0B, 0x0E  // 0x49
	};

	@Test (expected = RuntimeException.class)
	public void testPatchFlangerParameters() {
		new PatchFlangerParameters(new byte[80]);
	}

	@Test
	public void testGetFlangerType() {
		PatchFlangerParameters pfp = new PatchFlangerParameters(testAddressMap);
		assertEquals(FlangerType.PITCH_SHIFTER, pfp.getFlangerType());
	}

	@Test
	public void testGetFlangerParameter() {
		PatchFlangerParameters pfp = new PatchFlangerParameters(testAddressMap);
		assertEquals(127, pfp.getFlangerParameter(1).getValue());
		assertEquals(126, pfp.getFlangerParameter(2).getValue());
		assertEquals(125, pfp.getFlangerParameter(3).getValue());
		assertEquals(124, pfp.getFlangerParameter(4).getValue());
		assertEquals(-20000, pfp.getFlangerParameter(5).getValue());
		assertEquals(20000, pfp.getFlangerParameter(6).getValue());
		assertEquals(19198, pfp.getFlangerParameter(19).getValue());
		assertEquals(15038, pfp.getFlangerParameter(20).getValue());
	}

	@Test (expected = RuntimeException.class)
	public void testGetFlangerParameterInvalidLow() {
		PatchFlangerParameters pfp = new PatchFlangerParameters(testAddressMap);
		pfp.getFlangerParameter(0);
	}

	@Test (expected = RuntimeException.class)
	public void testGetFlangerParameterInvalidHigh() {
		PatchFlangerParameters pfp = new PatchFlangerParameters(testAddressMap);
		pfp.getFlangerParameter(21);
	}

}
