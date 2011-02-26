package nl.grauw.gaia_tool.parameters;

import static org.junit.Assert.*;

import javax.sound.midi.InvalidMidiDataException;

import nl.grauw.gaia_tool.Address;
import nl.grauw.gaia_tool.messages.ControlChangeMessage;
import nl.grauw.gaia_tool.messages.ControlChangeMessage.Controller;
import nl.grauw.gaia_tool.parameters.Flanger.FlangerType;

import org.junit.Test;

public class FlangerTest {

	static Address testAddress = new Address(0x10, 0x00, 0x06, 0x00);
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
		0x0C, 0x0A, 0x0F, 0x0E, 0x0B, 0x0A, 0x0B, 0x0E  // 0x49
	};
	
	public static Flanger getTestParameters() {
		return new Flanger(testAddress, testParameterData.clone());
	}

	@Test (expected = RuntimeException.class)
	public void testPatchFlangerParameters() {
		new Flanger(testAddress, new byte[80]);
	}
	
	@Test
	public void testUpdateParameters_Control_1() throws InvalidMidiDataException {
		Flanger parameters = getTestParameters();
		parameters.setValue(0x00, 1);
		ControlChangeMessage cc = new ControlChangeMessage(0, Controller.FLANGER_CONTROL_1, 47);
		parameters.updateParameters(cc);
		assertEquals(47, parameters.getFlangerParameter(2).getValue());
	}
	
	@Test
	public void testUpdateParameters_Control_1_PitchShift_0() throws InvalidMidiDataException {
		Flanger parameters = getTestParameters();
		ControlChangeMessage cc = new ControlChangeMessage(0, Controller.FLANGER_CONTROL_1, 0);
		parameters.updateParameters(cc);
		assertEquals(0, parameters.getFlangerParameter(2).getValue());
	}
	
	@Test
	public void testUpdateParameters_Control_1_PitchShift_45() throws InvalidMidiDataException {
		Flanger parameters = getTestParameters();
		ControlChangeMessage cc = new ControlChangeMessage(0, Controller.FLANGER_CONTROL_1, 45);
		parameters.updateParameters(cc);
		assertEquals(9, parameters.getFlangerParameter(2).getValue());
	}
	
	@Test
	public void testUpdateParameters_Control_1_PitchShift_56() throws InvalidMidiDataException {
		Flanger parameters = getTestParameters();
		ControlChangeMessage cc = new ControlChangeMessage(0, Controller.FLANGER_CONTROL_1, 56);
		parameters.updateParameters(cc);
		assertEquals(12, parameters.getFlangerParameter(2).getValue());
	}
	
	@Test
	public void testUpdateParameters_Control_1_PitchShift_71() throws InvalidMidiDataException {
		Flanger parameters = getTestParameters();
		ControlChangeMessage cc = new ControlChangeMessage(0, Controller.FLANGER_CONTROL_1, 71);
		parameters.updateParameters(cc);
		assertEquals(12, parameters.getFlangerParameter(2).getValue());
	}
	
	@Test
	public void testUpdateParameters_Control_1_PitchShift_111() throws InvalidMidiDataException {
		Flanger parameters = getTestParameters();
		ControlChangeMessage cc = new ControlChangeMessage(0, Controller.FLANGER_CONTROL_1, 111);
		parameters.updateParameters(cc);
		assertEquals(20, parameters.getFlangerParameter(2).getValue());
	}
	
	@Test
	public void testUpdateParameters_Control_1_PitchShift_127() throws InvalidMidiDataException {
		Flanger parameters = getTestParameters();
		ControlChangeMessage cc = new ControlChangeMessage(0, Controller.FLANGER_CONTROL_1, 127);
		parameters.updateParameters(cc);
		assertEquals(24, parameters.getFlangerParameter(2).getValue());
	}
	
	@Test
	public void testUpdateParameters_Level() throws InvalidMidiDataException {
		Flanger parameters = getTestParameters();
		ControlChangeMessage cc = new ControlChangeMessage(0, Controller.FLANGER_LEVEL, 47);
		parameters.updateParameters(cc);
		assertEquals(47, parameters.getFlangerParameter(1).getValue());
	}

	@Test
	public void testGetFlangerType() {
		Flanger pfp = getTestParameters();
		assertEquals(FlangerType.PITCH_SHIFTER, pfp.getFlangerType());
	}

	@Test
	public void testGetFlangerParameter() {
		Flanger pfp = getTestParameters();
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
		Flanger pfp = getTestParameters();
		pfp.getFlangerParameter(0);
	}

	@Test (expected = RuntimeException.class)
	public void testGetFlangerParameterInvalidHigh() {
		Flanger pfp = getTestParameters();
		pfp.getFlangerParameter(21);
	}

}
