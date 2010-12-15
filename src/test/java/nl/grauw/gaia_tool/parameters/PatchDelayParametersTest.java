package nl.grauw.gaia_tool.parameters;

import static org.junit.Assert.*;

import nl.grauw.gaia_tool.parameters.PatchDelayParameters.DelayType;

import org.junit.Test;

public class PatchDelayParametersTest {

	static byte[] testAddressMap = {
		0x02, // 0x00
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
	public void testPatchDelayParameters() {
		new PatchDelayParameters(new byte[80]);
	}

	@Test
	public void testGetDelayType() {
		PatchDelayParameters pdp = new PatchDelayParameters(testAddressMap);
		assertEquals(DelayType.PANNING_DELAY, pdp.getDelayType());
	}

	@Test
	public void testGetDelayParameter() {
		PatchDelayParameters pdp = new PatchDelayParameters(testAddressMap);
		assertEquals(127, pdp.getDelayParameter(1).getValue());
		assertEquals(126, pdp.getDelayParameter(2).getValue());
		assertEquals(125, pdp.getDelayParameter(3).getValue());
		assertEquals(124, pdp.getDelayParameter(4).getValue());
		assertEquals(-20000, pdp.getDelayParameter(5).getValue());
		assertEquals(20000, pdp.getDelayParameter(6).getValue());
		assertEquals(19198, pdp.getDelayParameter(19).getValue());
		assertEquals(15038, pdp.getDelayParameter(20).getValue());
	}

	@Test (expected = RuntimeException.class)
	public void testGetDelayParameterInvalidLow() {
		PatchDelayParameters pdp = new PatchDelayParameters(testAddressMap);
		pdp.getDelayParameter(0);
	}

	@Test (expected = RuntimeException.class)
	public void testGetDelayParameterInvalidHigh() {
		PatchDelayParameters pdp = new PatchDelayParameters(testAddressMap);
		pdp.getDelayParameter(21);
	}

}
