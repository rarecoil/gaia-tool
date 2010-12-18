package nl.grauw.gaia_tool.parameters;

import static org.junit.Assert.*;

import nl.grauw.gaia_tool.Address;
import nl.grauw.gaia_tool.ParameterData;
import nl.grauw.gaia_tool.parameters.Reverb.ReverbType;

import org.junit.Test;

public class ReverbTest {

	static Address testAddress = new Address(0x10, 0x00, 0x0A, 0x00);
	static byte[] testParameterData = {
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
	
	public static Reverb getTestParameters() {
		ParameterData data = new ParameterData(testAddress, testParameterData);
		return new Reverb(data);
	}

	@Test (expected = RuntimeException.class)
	public void testPatchReverbParameters() {
		new Reverb(new ParameterData(testAddress, new byte[80]));
	}

	@Test
	public void testGetReverbType() {
		Reverb prp = getTestParameters();
		assertEquals(ReverbType.REVERB, prp.getReverbType());
	}

	@Test
	public void testGetReverbParameter() {
		Reverb prp = getTestParameters();
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
		Reverb prp = getTestParameters();
		prp.getReverbParameter(0);
	}

	@Test (expected = RuntimeException.class)
	public void testGetReverbParameterInvalidHigh() {
		Reverb prp = getTestParameters();
		prp.getReverbParameter(21);
	}

}
