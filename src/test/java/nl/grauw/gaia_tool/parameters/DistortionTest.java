package nl.grauw.gaia_tool.parameters;

import static org.junit.Assert.*;

import javax.sound.midi.InvalidMidiDataException;

import nl.grauw.gaia_tool.Address;
import nl.grauw.gaia_tool.messages.ControlChangeMessage;
import nl.grauw.gaia_tool.messages.ControlChangeMessage.Controller;
import nl.grauw.gaia_tool.parameters.Distortion.DistortionType;

import org.junit.Test;

public class DistortionTest {

	static Address testAddress = new Address(0x10, 0x00, 0x04, 0x00);
	static byte[] testParameterData = {
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
		0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // 0x49
		0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // 0x51
		0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // 0x59
		0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // 0x61
		0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // 0x69
		0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // 0x71
		0x0C, 0x0A, 0x0F, 0x0E, 0x0B, 0x0A, 0x0B, 0x0E  // 0x79
	};
	
	public static Distortion getTestParameters() {
		return new Distortion(testAddress, testParameterData.clone());
	}

	@Test (expected = RuntimeException.class)
	public void testPatchDistortionParameters() {
		new Distortion(testAddress, new byte[128]);
	}
	
	@Test
	public void testUpdateParameters_Control_1() throws InvalidMidiDataException {
		Distortion parameters = getTestParameters();
		ControlChangeMessage cc = new ControlChangeMessage(0, Controller.DISTORTION_CONTROL_1, 47);
		parameters.updateParameters(cc);
		assertEquals(47, parameters.getMFXParameter(2).getValue());
	}
	
	@Test
	public void testUpdateParameters_Level() throws InvalidMidiDataException {
		Distortion parameters = getTestParameters();
		ControlChangeMessage cc = new ControlChangeMessage(0, Controller.DISTORTION_LEVEL, 47);
		parameters.updateParameters(cc);
		assertEquals(47, parameters.getMFXParameter(1).getValue());
	}
	
	@Test
	public void testGetDistortionType() {
		Distortion pdp = getTestParameters();
		assertEquals(DistortionType.BIT_CRASH, pdp.getDistortionType());
	}

	@Test
	public void testGetMFXParameter() {
		Distortion pdp = getTestParameters();
		assertEquals(127, pdp.getMFXParameter(1).getValue());
		assertEquals(126, pdp.getMFXParameter(2).getValue());
		assertEquals(125, pdp.getMFXParameter(3).getValue());
		assertEquals(124, pdp.getMFXParameter(4).getValue());
		assertEquals(-20000, pdp.getMFXParameter(5).getValue());
		assertEquals(20000, pdp.getMFXParameter(6).getValue());
		assertEquals(19198, pdp.getMFXParameter(31).getValue());
		assertEquals(15038, pdp.getMFXParameter(32).getValue());
	}

	@Test (expected = RuntimeException.class)
	public void testGetMFXParameterInvalidLow() {
		Distortion pdp = getTestParameters();
		pdp.getMFXParameter(0);
	}

	@Test (expected = RuntimeException.class)
	public void testGetMFXParameterInvalidHigh() {
		Distortion pdp = getTestParameters();
		pdp.getMFXParameter(33);
	}

}
