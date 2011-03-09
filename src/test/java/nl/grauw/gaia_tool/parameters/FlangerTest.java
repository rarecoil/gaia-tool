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
	
	@Test
	public void testUpdateParameters_Level() throws InvalidMidiDataException {
		Flanger parameters = getTestParameters();
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
			ControlChangeMessage cc = new ControlChangeMessage(0, Controller.FLANGER_LEVEL, i);
			parameters.updateParameters(cc);
			assertEquals(expected[i], parameters.getLevel().getValue());
		}
	}
	
	@Test
	public void testUpdateParameters_Control_1_Flanger() throws InvalidMidiDataException {
		Flanger parameters = getTestParameters();
		parameters.getFlangerTypeValue().setValue(FlangerType.FLANGER);
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
			ControlChangeMessage cc = new ControlChangeMessage(0, Controller.FLANGER_CONTROL_1, i);
			parameters.updateParameters(cc);
			assertEquals(expected[i], parameters.getFeedback().getValue());
		}
	}
	
	@Test
	public void testUpdateParameters_Control_1_Phaser() throws InvalidMidiDataException {
		Flanger parameters = getTestParameters();
		parameters.getFlangerTypeValue().setValue(FlangerType.PHASER);
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
			ControlChangeMessage cc = new ControlChangeMessage(0, Controller.FLANGER_CONTROL_1, i);
			parameters.updateParameters(cc);
			assertEquals(expected[i], parameters.getResonance().getValue());
		}
	}
	
	@Test
	public void testUpdateParameters_Control_1_Pitch_Shifter() throws InvalidMidiDataException {
		Flanger parameters = getTestParameters();
		parameters.getFlangerTypeValue().setValue(FlangerType.PITCH_SHIFTER);
		int[] expected = {
				-12, -11, -11, -11, -11, -11, -10, -10, -10, -10, -10,  -9,  -9,  -9,  -9,  -9,
				 -8,  -8,  -8,  -8,  -8,  -7,  -7,  -7,  -7,  -7,  -6,  -6,  -6,  -6,  -6,  -5,
				 -5,  -5,  -5,  -5,  -4,  -4,  -4,  -4,  -4,  -3,  -3,  -3,  -3,  -3,  -2,  -2,
				 -2,  -2,  -2,  -1,  -1,  -1,  -1,  -1,   0,   0,   0,   0,   0,   0,   0,   0,
				  0,   0,   0,   0,   0,   0,   0,   0,   1,   1,   1,   1,   1,   2,   2,   2,
				  2,   2,   3,   3,   3,   3,   3,   4,   4,   4,   4,   4,   5,   5,   5,   5,
				  5,   6,   6,   6,   6,   6,   7,   7,   7,   7,   7,   8,   8,   8,   8,   8,
				  9,   9,   9,   9,   9,  10,  10,  10,  10,  10,  11,  11,  11,  11,  11,  12
			};
		for (int i = 0; i < 128; i++) {
			ControlChangeMessage cc = new ControlChangeMessage(0, Controller.FLANGER_CONTROL_1, i);
			parameters.updateParameters(cc);
			assertEquals(expected[i], parameters.getPitch().getValue());
		}
	}

}
