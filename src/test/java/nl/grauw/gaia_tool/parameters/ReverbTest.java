/*
 * Copyright 2010 Laurens Holst
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.grauw.gaia_tool.parameters;

import static org.junit.Assert.*;

import javax.sound.midi.InvalidMidiDataException;

import nl.grauw.gaia_tool.Address;
import nl.grauw.gaia_tool.messages.ControlChangeMessage;
import nl.grauw.gaia_tool.messages.ControlChangeMessage.Controller;
import nl.grauw.gaia_tool.parameters.Reverb.ReverbType;

import org.junit.Test;

public class ReverbTest {

	static Address testAddress = new Address(0x10, 0x00, 0x0A, 0x00);
	static byte[] testParameterData = {
		0x01, // 0x00
		0x08, 0x00, 0x07, 0x0F, 0x08, 0x00, 0x07, 0x0E, // 0x01
		0x08, 0x00, 0x00, 0x02, 0x08, 0x00, 0x07, 0x0C, // 0x09
		0x03, 0x01, 0x0E, 0x00, 0x0C, 0x0E, 0x02, 0x00, // 0x11
		0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // 0x19
		0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // 0x21
		0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // 0x29
		0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // 0x31
		0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // 0x39
		0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // 0x41
		0x0C, 0x0A, 0x0F, 0x0E, 0x0B, 0x0A, 0x0B, 0x0E  // 0x49
	};
	
	public static Reverb createTestParameters() {
		return new Reverb(testAddress, testParameterData.clone());
	}

	@Test (expected = RuntimeException.class)
	public void testPatchReverbParameters() {
		new Reverb(testAddress, new byte[80]);
	}

	@Test
	public void testGetReverbType() {
		Reverb prp = createTestParameters();
		assertEquals(ReverbType.REVERB, prp.getReverbType());
	}

	@Test
	public void testGetReverbParameter() {
		Reverb prp = createTestParameters();
		assertEquals(127, prp.getReverbParameter(1).getValue());
		assertEquals(126, prp.getReverbParameter(2).getValue());
		assertEquals(2, prp.getReverbParameter(3).getValue());
		assertEquals(124, prp.getReverbParameter(4).getValue());
		assertEquals(-20000, prp.getReverbParameter(5).getValue());
		assertEquals(20000, prp.getReverbParameter(6).getValue());
		assertEquals(19198, prp.getReverbParameter(19).getValue());
		assertEquals(15038, prp.getReverbParameter(20).getValue());
	}

	@Test
	public void testGetReverbParameter_SetValue() {
		Reverb prp = createTestParameters();
		prp.getReverbParameter(2).setValue(48);
		assertEquals(48, prp.getTime().getValue());
	}

	@Test (expected = RuntimeException.class)
	public void testGetReverbParameterInvalidLow() {
		Reverb prp = createTestParameters();
		prp.getReverbParameter(0);
	}

	@Test (expected = RuntimeException.class)
	public void testGetReverbParameterInvalidHigh() {
		Reverb prp = createTestParameters();
		prp.getReverbParameter(21);
	}
	
	@Test
	public void testGetLevel() {
		Reverb prp = createTestParameters();
		assertEquals(true, prp.getLevel().isValid());
		assertEquals(127, prp.getLevel().getValue());
	}

	@Test
	public void testGetTime() {
		Reverb prp = createTestParameters();
		assertEquals(true, prp.getTime().isValid());
		assertEquals(126, prp.getTime().getValue());
	}

	@Test
	public void testGetType() {
		Reverb prp = createTestParameters();
		assertEquals(true, prp.getType().isValid());
		assertEquals(2, prp.getType().getValue());
	}

	@Test
	public void testGetHighDamp() {
		Reverb prp = createTestParameters();
		assertEquals(true, prp.getHighDamp().isValid());
		assertEquals(124, prp.getHighDamp().getValue());
	}

	@Test
	public void testUpdateParameters_Level() throws InvalidMidiDataException {
		Reverb parameters = createTestParameters();
		int[] expected = {
				  0,   1,   2,   3,   4,   5,   6,   7,   8,   9,  10,  11,  12,  13,  14,  15,
				 16,  17,  18,  19,  20,  21,  22,  23,  24,  25,  26,  27,  28,  29,  30,  31,
				 32,  33,  34,  35,  36,  37,  38,  39,  40,  41,  42,  43,  44,  45,  46,  47,
				 48,  49,  50,  51,  52,  53,  54,  55,  56,  57,  58,  59,  60,  61,  62,  63,
				 64,  65,  66,  67,  68,  69,  70,  71,  72,  73,  74,  75,  76,  77,  78,  79,
				 80,  81,  82,  83,  84,  85,  86,  87,  88,  89,  90,  91,  92,  93,  94,  95,
				 96,  97,  98,  99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111,
				112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 125, 126, 127
			};
		for (int i = 0; i < 128; i++) {
			ControlChangeMessage cc = new ControlChangeMessage(0, Controller.REVERB_LEVEL, i);
			parameters.updateParameters(cc);
			assertEquals(expected[i], parameters.getLevel().getValue());
		}
	}
	
	@Test
	public void testUpdateParameters_Control_1() throws InvalidMidiDataException {
		Reverb parameters = createTestParameters();
		int[] expected = {
				  0,   1,   2,   3,   4,   5,   6,   7,   8,   9,  10,  11,  12,  13,  14,  15,
				 16,  17,  18,  19,  20,  21,  22,  23,  24,  25,  26,  27,  28,  29,  30,  31,
				 32,  33,  34,  35,  36,  37,  38,  39,  40,  41,  42,  43,  44,  45,  46,  47,
				 48,  49,  50,  51,  52,  53,  54,  55,  56,  57,  58,  59,  60,  61,  62,  63,
				 64,  65,  66,  67,  68,  69,  70,  71,  72,  73,  74,  75,  76,  77,  78,  79,
				 80,  81,  82,  83,  84,  85,  86,  87,  88,  89,  90,  91,  92,  93,  94,  95,
				 96,  97,  98,  99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111,
				112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 125, 126, 127
			};
		for (int i = 0; i < 128; i++) {
			ControlChangeMessage cc = new ControlChangeMessage(0, Controller.REVERB_CONTROL_1, i);
			parameters.updateParameters(cc);
			assertEquals(expected[i], parameters.getTime().getValue());
		}
	}

}
