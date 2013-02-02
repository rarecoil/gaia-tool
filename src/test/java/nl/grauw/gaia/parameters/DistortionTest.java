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
package nl.grauw.gaia.parameters;

import static org.junit.Assert.*;

import nl.grauw.gaia.Address;
import nl.grauw.gaia.midi.messages.ControlChangeMessage;
import nl.grauw.gaia.midi.messages.ControlChangeMessage.Controller;
import nl.grauw.gaia.parameters.Distortion;
import nl.grauw.gaia.parameters.Distortion.DistortionType;

import org.junit.Test;

public class DistortionTest {

	static Address testAddress = new Address(0x10, 0x00, 0x04, 0x00);
	static byte[] testParameterData = {
		0x03, // 0x00
		0x08, 0x00, 0x07, 0x0F, 0x08, 0x00, 0x07, 0x0E, // 0x01
		0x08, 0x00, 0x00, 0x05, 0x08, 0x00, 0x07, 0x0C, // 0x09
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
	
	public static Distortion createTestParameters() {
		return new Distortion(testAddress, testParameterData.clone());
	}

	@Test (expected = RuntimeException.class)
	public void testPatchDistortionParameters() {
		new Distortion(testAddress, new byte[128]);
	}
	
	@Test
	public void testGetDistortionType() {
		Distortion pdp = createTestParameters();
		assertEquals(DistortionType.BIT_CRASH, pdp.getDistortionType());
	}

	@Test
	public void testGetMFXParameter() {
		Distortion pdp = createTestParameters();
		assertEquals(127, pdp.getMFXParameter(1).getValue());
		assertEquals(126, pdp.getMFXParameter(2).getValue());
		assertEquals(5, pdp.getMFXParameter(3).getValue());
		assertEquals(124, pdp.getMFXParameter(4).getValue());
		assertEquals(-20000, pdp.getMFXParameter(5).getValue());
		assertEquals(20000, pdp.getMFXParameter(6).getValue());
		assertEquals(19198, pdp.getMFXParameter(31).getValue());
		assertEquals(15038, pdp.getMFXParameter(32).getValue());
	}

	@Test
	public void testGetMFXParameter_SetValue() {
		Distortion pdp = createTestParameters();
		pdp.getMFXParameter(3).setValue(45);
		assertEquals(45, pdp.getBitDown().getValue());
	}

	@Test (expected = RuntimeException.class)
	public void testGetMFXParameterInvalidLow() {
		Distortion pdp = createTestParameters();
		pdp.getMFXParameter(0);
	}

	@Test (expected = RuntimeException.class)
	public void testGetMFXParameterInvalidHigh() {
		Distortion pdp = createTestParameters();
		pdp.getMFXParameter(33);
	}

	@Test
	public void testGetLevel() {
		Distortion pdp = createTestParameters();
		pdp.getDistortionTypeValue().setValue(DistortionType.DIST);
		assertEquals(true, pdp.getLevel().isValid());
		assertEquals(127, pdp.getLevel().getValue());
	}

	@Test
	public void testGetDrive() {
		Distortion pdp = createTestParameters();
		pdp.getDistortionTypeValue().setValue(DistortionType.DIST);
		assertEquals(true, pdp.getDrive().isValid());
		assertEquals(126, pdp.getDrive().getValue());
	}

	@Test
	public void testGetType() {
		Distortion pdp = createTestParameters();
		pdp.getDistortionTypeValue().setValue(DistortionType.FUZZ);
		assertEquals(true, pdp.getType().isValid());
		assertEquals(6, pdp.getType().getValue());
	}

	@Test
	public void testGetPresence() {
		Distortion pdp = createTestParameters();
		pdp.getDistortionTypeValue().setValue(DistortionType.FUZZ);
		assertEquals(true, pdp.getPresence().isValid());
		assertEquals(124, pdp.getPresence().getValue());
	}

	@Test
	public void testGetSampleRate() {
		Distortion pdp = createTestParameters();
		pdp.getDistortionTypeValue().setValue(DistortionType.BIT_CRASH);
		assertEquals(true, pdp.getSampleRate().isValid());
		assertEquals(126, pdp.getSampleRate().getValue());
	}

	@Test
	public void testGetBitDown() {
		Distortion pdp = createTestParameters();
		pdp.getDistortionTypeValue().setValue(DistortionType.BIT_CRASH);
		assertEquals(true, pdp.getBitDown().isValid());
		assertEquals(5, pdp.getBitDown().getValue());
	}

	@Test
	public void testGetFilter() {
		Distortion pdp = createTestParameters();
		pdp.getDistortionTypeValue().setValue(DistortionType.BIT_CRASH);
		assertEquals(true, pdp.getFilter().isValid());
		assertEquals(124, pdp.getFilter().getValue());
	}
	
	@Test
	public void testUpdateParameters_Level() {
		Distortion parameters = createTestParameters();
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
			ControlChangeMessage cc = new ControlChangeMessage(0, Controller.DISTORTION_LEVEL, i);
			parameters.updateParameters(cc);
			assertEquals(expected[i], parameters.getLevel().getValue());
		}
	}
	
	@Test
	public void testUpdateParameters_Control_1_Distortion() {
		Distortion parameters = createTestParameters();
		parameters.getDistortionTypeValue().setValue(DistortionType.DIST);
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
			ControlChangeMessage cc = new ControlChangeMessage(0, Controller.DISTORTION_CONTROL_1, i);
			parameters.updateParameters(cc);
			assertEquals(expected[i], parameters.getDrive().getValue());
		}
	}
	
	@Test
	public void testUpdateParameters_Control_1_Fuzz() {
		Distortion parameters = createTestParameters();
		parameters.getDistortionTypeValue().setValue(DistortionType.FUZZ);
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
			ControlChangeMessage cc = new ControlChangeMessage(0, Controller.DISTORTION_CONTROL_1, i);
			parameters.updateParameters(cc);
			assertEquals(expected[i], parameters.getDrive().getValue());
		}
	}
	
	@Test
	public void testUpdateParameters_Control_1_Bit_Crash() {
		Distortion parameters = createTestParameters();
		parameters.getDistortionTypeValue().setValue(DistortionType.BIT_CRASH);
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
			ControlChangeMessage cc = new ControlChangeMessage(0, Controller.DISTORTION_CONTROL_1, i);
			parameters.updateParameters(cc);
			assertEquals(expected[i], parameters.getSampleRate().getValue());
		}
	}

}
