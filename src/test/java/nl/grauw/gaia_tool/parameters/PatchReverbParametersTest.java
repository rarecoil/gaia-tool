package nl.grauw.gaia_tool.parameters;

import static org.junit.Assert.*;

import nl.grauw.gaia_tool.parameters.PatchReverbParameters.ReverbType;

import org.junit.Test;

public class PatchReverbParametersTest {

	static byte[] testAddressMap = {
		0x01, // 0x00
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
	public void testPatchReverbParameters() {
		new PatchReverbParameters(new byte[80]);
	}

	@Test
	public void testGetReverbType() {
		PatchReverbParameters prp = new PatchReverbParameters(testAddressMap);
		assertEquals(ReverbType.REVERB, prp.getReverbType());
	}

	@Test
	public void testGetReverbParameter() {
		PatchReverbParameters prp = new PatchReverbParameters(testAddressMap);
		assertEquals(127, prp.getReverbParameter(1).getValue());
		assertEquals(126, prp.getReverbParameter(2).getValue());
		assertEquals(125, prp.getReverbParameter(3).getValue());
		assertEquals(124, prp.getReverbParameter(4).getValue());
		assertEquals(-20000, prp.getReverbParameter(5).getValue());
		assertEquals(20000, prp.getReverbParameter(6).getValue());
		assertEquals(19198, prp.getReverbParameter(19).getValue());
		assertEquals(15038, prp.getReverbParameter(20).getValue());
	}

	@Test (expected = RuntimeException.class)
	public void testGetReverbParameterInvalidLow() {
		PatchReverbParameters prp = new PatchReverbParameters(testAddressMap);
		prp.getReverbParameter(0);
	}

	@Test (expected = RuntimeException.class)
	public void testGetReverbParameterInvalidHigh() {
		PatchReverbParameters prp = new PatchReverbParameters(testAddressMap);
		prp.getReverbParameter(21);
	}

}
