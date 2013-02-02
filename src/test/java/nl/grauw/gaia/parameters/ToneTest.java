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
import nl.grauw.gaia.parameters.Tone;
import nl.grauw.gaia.parameters.Tone.FilterMode;
import nl.grauw.gaia.parameters.Tone.FilterSlope;
import nl.grauw.gaia.parameters.Tone.LFOShape;
import nl.grauw.gaia.parameters.Tone.LFOTempoSyncNote;
import nl.grauw.gaia.parameters.Tone.OSCWave;
import nl.grauw.gaia.parameters.Tone.OSCWaveVariation;

import org.junit.Test;

public class ToneTest {
	
	static Address testAddress = new Address(0x10, 0x00, 0x01, 0x00);
	static byte[] testParameterData = {
		0x06, 0x02, 0x01, 0x58, 0x0E, 0x7F, 0x7E, 0x7D, // 0x00
		0x7C, 0x3F, 0x04, 0x01, 0x7B, 0x36, 0x3E, 0x7A, // 0x08
		0x79, 0x78, 0x77, 0x76, 0x3D, 0x75, 0x3C, 0x74, // 0x10
		0x73, 0x72, 0x71, 0x00, 0x05, 0x70, 0x01, 0x11, // 0x18
		0x6F, 0x01, 0x3B, 0x3A, 0x39, 0x38, 0x03, 0x6E, // 0x20
		0x00, 0x13, 0x6D, 0x01, 0x37, 0x36, 0x35, 0x34, // 0x28
		0x33, 0x32, 0x31, 0x30, 0x00, 0x01, 0x00, 0x01, // 0x30
		0x6C, 0x6B, 0x6A, 0x69, 0x2F, 0x2E
	};
	
	public static Tone createTestParameters() {
		return createTestParameters(1);
	}
	
	public static Tone createTestParameters(int tone) {
		return new Tone(testAddress.add((tone - 1) * 0x80), testParameterData.clone());
	}

	@Test (expected = RuntimeException.class)
	public void testPatchToneParameters() {
		new Tone(testAddress, new byte[61]);
	}

	@Test
	public void testGetToneNumber() {
		Tone tone = createTestParameters(2);
		assertEquals(2, tone.getToneNumber());
	}

	@Test
	public void testGetOSCWave() {
		Tone ptp = createTestParameters();
		assertEquals(OSCWave.SUPER_SAW, ptp.getOSCWave());
	}

	@Test
	public void testGetOSCWaveVariation() {
		Tone ptp = createTestParameters();
		assertEquals(OSCWaveVariation.C, ptp.getOSCWaveVariation());
	}

	@Test
	public void testGetReserved1() {
		Tone ptp = createTestParameters();
		assertEquals(true, ptp.getReserved1());
	}

	@Test
	public void testGetOSCPitch() {
		Tone ptp = createTestParameters();
		assertEquals(24, ptp.getOSCPitch().getValue());
	}

	@Test
	public void testGetOSCDetune() {
		Tone ptp = createTestParameters();
		assertEquals(-50, ptp.getOSCDetune().getValue());
	}

	@Test
	public void testGetOSCPulseWidthModDepth() {
		Tone ptp = createTestParameters();
		assertEquals(127, ptp.getOSCPulseWidthModDepth().getValue());
	}

	@Test
	public void testGetOSCPulseWidth() {
		Tone ptp = createTestParameters();
		assertEquals(126, ptp.getOSCPulseWidth().getValue());
	}

	@Test
	public void testGetOSCPitchEnvAttackTime() {
		Tone ptp = createTestParameters();
		assertEquals(125, ptp.getOSCPitchEnvAttackTime().getValue());
	}

	@Test
	public void testGetOSCPitchEnvDecay() {
		Tone ptp = createTestParameters();
		assertEquals(124, ptp.getOSCPitchEnvDecay().getValue());
	}

	@Test
	public void testGetOSCPitchEnvDepth() {
		Tone ptp = createTestParameters();
		assertEquals(-1, ptp.getOSCPitchEnvDepth().getValue());
	}

	@Test
	public void testGetFilterMode() {
		Tone ptp = createTestParameters();
		assertEquals(FilterMode.PKG, ptp.getFilterMode());
	}

	@Test
	public void testGetFilterSlope() {
		Tone ptp = createTestParameters();
		assertEquals(FilterSlope.MINUS_24_DB, ptp.getFilterSlope());
	}

	@Test
	public void testGetFilterCutoff() {
		Tone ptp = createTestParameters();
		assertEquals(123, ptp.getFilterCutoff().getValue());
	}

	@Test
	public void testGetFilterCutoffKeyfollow() {
		Tone ptp = createTestParameters();
		assertEquals(-10, ptp.getFilterCutoffKeyfollow().getValue());
	}

	@Test
	public void testGetFilterEnvVelocitySens() {
		Tone ptp = createTestParameters();
		assertEquals(-2, ptp.getFilterEnvVelocitySens().getValue());
	}

	@Test
	public void testGetFilterResonance() {
		Tone ptp = createTestParameters();
		assertEquals(122, ptp.getFilterResonance().getValue());
	}

	@Test
	public void testGetFilterEnvAttackTime() {
		Tone ptp = createTestParameters();
		assertEquals(121, ptp.getFilterEnvAttackTime().getValue());
	}

	@Test
	public void testGetFilterEnvDecayTime() {
		Tone ptp = createTestParameters();
		assertEquals(120, ptp.getFilterEnvDecayTime().getValue());
	}

	@Test
	public void testGetFilterEnvSustainLevel() {
		Tone ptp = createTestParameters();
		assertEquals(119, ptp.getFilterEnvSustainLevel().getValue());
	}

	@Test
	public void testGetFilterEnvReleaseTime() {
		Tone ptp = createTestParameters();
		assertEquals(118, ptp.getFilterEnvReleaseTime().getValue());
	}

	@Test
	public void testGetFilterEnvDepth() {
		Tone ptp = createTestParameters();
		assertEquals(-3, ptp.getFilterEnvDepth().getValue());
	}

	@Test
	public void testGetAmpLevel() {
		Tone ptp = createTestParameters();
		assertEquals(117, ptp.getAmpLevel().getValue());
	}

	@Test
	public void testGetAmpLevelVelocitySens() {
		Tone ptp = createTestParameters();
		assertEquals(-4, ptp.getAmpLevelVelocitySens().getValue());
	}

	@Test
	public void testGetAmpEnvAttackTime() {
		Tone ptp = createTestParameters();
		assertEquals(116, ptp.getAmpEnvAttackTime().getValue());
	}

	@Test
	public void testGetAmpEnvDecayTime() {
		Tone ptp = createTestParameters();
		assertEquals(115, ptp.getAmpEnvDecayTime().getValue());
	}

	@Test
	public void testGetAmpEnvSustainLevel() {
		Tone ptp = createTestParameters();
		assertEquals(114, ptp.getAmpEnvSustainLevel().getValue());
	}

	@Test
	public void testGetAmpEnvReleaseTime() {
		Tone ptp = createTestParameters();
		assertEquals(113, ptp.getAmpEnvReleaseTime().getValue());
	}

	@Test
	public void testGetAmpPan() {
		Tone ptp = createTestParameters();
		assertEquals(-64, ptp.getAmpPan().getValue());
	}

	@Test
	public void testGetLFOShape() {
		Tone ptp = createTestParameters();
		assertEquals(LFOShape.RND, ptp.getLFOShape());
	}

	@Test
	public void testGetLFORate() {
		Tone ptp = createTestParameters();
		assertEquals(112, ptp.getLFORate().getValue());
	}

	@Test
	public void testGetLFOTempoSyncSwitch() {
		Tone ptp = createTestParameters();
		assertEquals(true, ptp.getLFOTempoSyncSwitch());
	}

	@Test
	public void testGetLFOTempoSyncNote() {
		Tone ptp = createTestParameters();
		assertEquals(LFOTempoSyncNote._1_16TH, ptp.getLFOTempoSyncNote());
	}

	@Test
	public void testGetLFOFadeTime() {
		Tone ptp = createTestParameters();
		assertEquals(111, ptp.getLFOFadeTime().getValue());
	}

	@Test
	public void testGetLFOKeyTrigger() {
		Tone ptp = createTestParameters();
		assertEquals(true, ptp.getLFOKeyTrigger());
	}

	@Test
	public void testGetLFOPitchDepth() {
		Tone ptp = createTestParameters();
		assertEquals(-5, ptp.getLFOPitchDepth().getValue());
	}

	@Test
	public void testGetLFOFilterDepth() {
		Tone ptp = createTestParameters();
		assertEquals(-6, ptp.getLFOFilterDepth().getValue());
	}

	@Test
	public void testGetLFOAmpDepth() {
		Tone ptp = createTestParameters();
		assertEquals(-7, ptp.getLFOAmpDepth().getValue());
	}

	@Test
	public void testGetLFOPanDepth() {
		Tone ptp = createTestParameters();
		assertEquals(-8, ptp.getLFOPanDepth().getValue());
	}

	@Test
	public void testGetModulationLFOShape() {
		Tone ptp = createTestParameters();
		assertEquals(LFOShape.SQR, ptp.getModulationLFOShape());
	}

	@Test
	public void testGetModulationLFORate() {
		Tone ptp = createTestParameters();
		assertEquals(110, ptp.getModulationLFORate().getValue());
	}

	@Test
	public void testGetModulationLFOTempoSyncSwitch() {
		Tone ptp = createTestParameters();
		assertEquals(false, ptp.getModulationLFOTempoSyncSwitch());
	}

	@Test
	public void testGetModulationLFOTempoSyncNote() {
		Tone ptp = createTestParameters();
		assertEquals(LFOTempoSyncNote._1_32ND, ptp.getModulationLFOTempoSyncNote());
	}

	@Test
	public void testGetReserved2() {
		Tone ptp = createTestParameters();
		assertEquals(109, ptp.getReserved2().getValue());
	}

	@Test
	public void testGetReserved3() {
		Tone ptp = createTestParameters();
		assertEquals(1, ptp.getReserved3().getValue());
	}

	@Test
	public void testGetModulationLFOPitchDepth() {
		Tone ptp = createTestParameters();
		assertEquals(-9, ptp.getModulationLFOPitchDepth().getValue());
	}

	@Test
	public void testGetModulationLFOFilterDepth() {
		Tone ptp = createTestParameters();
		assertEquals(-10, ptp.getModulationLFOFilterDepth().getValue());
	}

	@Test
	public void testGetModulationLFOAmpDepth() {
		Tone ptp = createTestParameters();
		assertEquals(-11, ptp.getModulationLFOAmpDepth().getValue());
	}

	@Test
	public void testGetModulationLFOPanDepth() {
		Tone ptp = createTestParameters();
		assertEquals(-12, ptp.getModulationLFOPanDepth().getValue());
	}

	@Test
	public void testGetReserved4() {
		Tone ptp = createTestParameters();
		assertEquals(-13, ptp.getReserved4().getValue());
	}

	@Test
	public void testGetReserved5() {
		Tone ptp = createTestParameters();
		assertEquals(-14, ptp.getReserved5().getValue());
	}

	@Test
	public void testGetReserved6() {
		Tone ptp = createTestParameters();
		assertEquals(-15, ptp.getReserved6().getValue());
	}

	@Test
	public void testGetReserved7() {
		Tone ptp = createTestParameters();
		assertEquals(-16, ptp.getReserved7().getValue());
	}

	@Test
	public void testGetReserved8() {
		Tone ptp = createTestParameters();
		assertEquals(0, ptp.getReserved8().getValue());
	}

	@Test
	public void testGetReserved9() {
		Tone ptp = createTestParameters();
		assertEquals(1, ptp.getReserved9().getValue());
	}

	@Test
	public void testGetReserved10() {
		Tone ptp = createTestParameters();
		assertEquals(0, ptp.getReserved10().getValue());
	}

	@Test
	public void testGetReserved11() {
		Tone ptp = createTestParameters();
		assertEquals(1, ptp.getReserved11().getValue());
	}

	@Test
	public void testGetReserved12() {
		Tone ptp = createTestParameters();
		assertEquals(108, ptp.getReserved12().getValue());
	}

	@Test
	public void testGetReserved13() {
		Tone ptp = createTestParameters();
		assertEquals(107, ptp.getReserved13().getValue());
	}

	@Test
	public void testGetReserved14() {
		Tone ptp = createTestParameters();
		assertEquals(106, ptp.getReserved14().getValue());
	}

	@Test
	public void testGetReserved15() {
		Tone ptp = createTestParameters();
		assertEquals(105, ptp.getReserved15().getValue());
	}

	@Test
	public void testGetReserved16() {
		Tone ptp = createTestParameters();
		assertEquals(-17, ptp.getReserved16().getValue());
	}

	@Test
	public void testGetReserved17() {
		Tone ptp = createTestParameters();
		assertEquals(-18, ptp.getReserved17().getValue());
	}
	
	@Test
	public void testUpdateParameters_LFO_Rate() {
		Tone parameters = createTestParameters();
		parameters.setValue(0x1E, 0);	// disable LFO tempo sync
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
			ControlChangeMessage cc = new ControlChangeMessage(0, Controller.TONE_1_LFO_RATE, i);
			parameters.updateParameters(cc);
			assertEquals(expected[i], parameters.getLFORate().getValue());
		}
	}
	
	@Test
	public void testUpdateParameters_LFO_TempoSyncNote() {
		Tone parameters = createTestParameters();
		parameters.setValue(0x1E, 1);	// enable LFO tempo sync
		int[] expected = {
				  0,   0,   0,   0,   0,   0,   1,   1,   1,   1,   1,   1,   2,   2,   2,   2,
				  2,   2,   3,   3,   3,   3,   3,   3,   4,   4,   4,   4,   4,   4,   5,   5,
				  5,   5,   5,   5,   6,   6,   6,   6,   6,   6,   6,   7,   7,   7,   7,   7,
				  7,   7,   8,   8,   8,   8,   8,   8,   8,   9,   9,   9,   9,   9,   9,   9,
				 10,  10,  10,  10,  10,  10,  10,  11,  11,  11,  11,  11,  11,  11,  12,  12,
				 12,  12,  12,  12,  12,  13,  13,  13,  13,  13,  13,  13,  14,  14,  14,  14,
				 14,  14,  15,  15,  15,  15,  15,  15,  16,  16,  16,  16,  16,  16,  17,  17,
				 17,  17,  17,  17,  18,  18,  18,  18,  18,  18,  19,  19,  19,  19,  19,  19
			};
		for (int i = 0; i < 128; i++) {
			ControlChangeMessage cc = new ControlChangeMessage(0, Controller.TONE_1_LFO_RATE, i);
			parameters.updateParameters(cc);
			assertEquals(expected[i], parameters.getLFOTempoSyncNote().ordinal());
		}
	}
	
	@Test
	public void testUpdateParameters_LFO_Fade_Time() {
		Tone parameters = createTestParameters();
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
			ControlChangeMessage cc = new ControlChangeMessage(0, Controller.TONE_1_LFO_FADE_TIME, i);
			parameters.updateParameters(cc);
			assertEquals(expected[i], parameters.getLFOFadeTime().getValue());
		}
	}
	
	@Test
	public void testUpdateParameters_LFO_Pitch_Depth() {
		Tone parameters = createTestParameters();
		int[] expected = {
				-63, -63, -62, -61, -60, -59, -58, -57, -56, -55, -54, -53, -52, -51, -50, -49,
				-48, -47, -46, -45, -44, -43, -42, -41, -40, -39, -38, -37, -36, -35, -34, -33,
				-32, -31, -30, -29, -28, -27, -26, -25, -24, -23, -22, -21, -20, -19, -18, -17,
				-16, -15, -14, -13, -12, -11, -10,  -9,  -8,  -7,  -6,  -5,  -4,  -3,  -2,  -1,
				  0,   1,   2,   3,   4,   5,   6,   7,   8,   9,  10,  11,  12,  13,  14,  15,
				 16,  17,  18,  19,  20,  21,  22,  23,  24,  25,  26,  27,  28,  29,  30,  31,
				 32,  33,  34,  35,  36,  37,  38,  39,  40,  41,  42,  43,  44,  45,  46,  47,
				 48,  49,  50,  51,  52,  53,  54,  55,  56,  57,  58,  59,  60,  61,  62,  63
			};
		for (int i = 0; i < 128; i++) {
			ControlChangeMessage cc = new ControlChangeMessage(0, Controller.TONE_1_LFO_PITCH_DEPTH, i);
			parameters.updateParameters(cc);
			assertEquals(expected[i], parameters.getLFOPitchDepth().getValue());
		}
	}
	
	@Test
	public void testUpdateParameters_LFO_Filter_Depth() {
		Tone parameters = createTestParameters();
		int[] expected = {
				-63, -63, -62, -61, -60, -59, -58, -57, -56, -55, -54, -53, -52, -51, -50, -49,
				-48, -47, -46, -45, -44, -43, -42, -41, -40, -39, -38, -37, -36, -35, -34, -33,
				-32, -31, -30, -29, -28, -27, -26, -25, -24, -23, -22, -21, -20, -19, -18, -17,
				-16, -15, -14, -13, -12, -11, -10,  -9,  -8,  -7,  -6,  -5,  -4,  -3,  -2,  -1,
				  0,   1,   2,   3,   4,   5,   6,   7,   8,   9,  10,  11,  12,  13,  14,  15,
				 16,  17,  18,  19,  20,  21,  22,  23,  24,  25,  26,  27,  28,  29,  30,  31,
				 32,  33,  34,  35,  36,  37,  38,  39,  40,  41,  42,  43,  44,  45,  46,  47,
				 48,  49,  50,  51,  52,  53,  54,  55,  56,  57,  58,  59,  60,  61,  62,  63
			};
		for (int i = 0; i < 128; i++) {
			ControlChangeMessage cc = new ControlChangeMessage(0, Controller.TONE_1_LFO_FILTER_DEPTH, i);
			parameters.updateParameters(cc);
			assertEquals(expected[i], parameters.getLFOFilterDepth().getValue());
		}
	}
	
	@Test
	public void testUpdateParameters_LFO_Amp_Depth() {
		Tone parameters = createTestParameters();
		int[] expected = {
				-63, -63, -62, -61, -60, -59, -58, -57, -56, -55, -54, -53, -52, -51, -50, -49,
				-48, -47, -46, -45, -44, -43, -42, -41, -40, -39, -38, -37, -36, -35, -34, -33,
				-32, -31, -30, -29, -28, -27, -26, -25, -24, -23, -22, -21, -20, -19, -18, -17,
				-16, -15, -14, -13, -12, -11, -10,  -9,  -8,  -7,  -6,  -5,  -4,  -3,  -2,  -1,
				  0,   1,   2,   3,   4,   5,   6,   7,   8,   9,  10,  11,  12,  13,  14,  15,
				 16,  17,  18,  19,  20,  21,  22,  23,  24,  25,  26,  27,  28,  29,  30,  31,
				 32,  33,  34,  35,  36,  37,  38,  39,  40,  41,  42,  43,  44,  45,  46,  47,
				 48,  49,  50,  51,  52,  53,  54,  55,  56,  57,  58,  59,  60,  61,  62,  63
			};
		for (int i = 0; i < 128; i++) {
			ControlChangeMessage cc = new ControlChangeMessage(0, Controller.TONE_1_LFO_AMP_DEPTH, i);
			parameters.updateParameters(cc);
			assertEquals(expected[i], parameters.getLFOAmpDepth().getValue());
		}
	}
	
	@Test
	public void testUpdateParameters_OSC_Pitch() {
		Tone parameters = createTestParameters();
		int[] expected = {
				-24, -24, -23, -23, -23, -22, -22, -22, -21, -21, -20, -20, -20, -19, -19, -19,
				-18, -18, -17, -17, -17, -16, -16, -15, -15, -14, -14, -14, -13, -13, -13, -12,
				-12, -12, -11, -11, -11, -10, -10, -10,  -9,  -9,  -8,  -8,  -8,  -7,  -7,  -7,
				 -6,  -6,  -5,  -5,  -5,  -4,  -4,  -3,  -3,  -2,  -2,  -2,  -1,  -1,  -1,   0,
				  0,   0,   1,   1,   1,   2,   2,   2,   3,   3,   4,   4,   5,   5,   5,   6,
				  6,   7,   7,   7,   8,   8,   8,   9,   9,  10,  10,  10,  11,  11,  11,  12,
				 12,  12,  13,  13,  13,  14,  14,  14,  15,  15,  16,  16,  17,  17,  17,  18,
				 18,  19,  19,  19,  20,  20,  20,  21,  21,  22,  22,  22,  23,  23,  23,  24
			};
		for (int i = 0; i < 128; i++) {
			ControlChangeMessage cc = new ControlChangeMessage(0, Controller.TONE_1_OSC_PITCH, i);
			parameters.updateParameters(cc);
			assertEquals(expected[i], parameters.getOSCPitch().getValue());
		}
	}
	
	@Test
	public void testUpdateParameters_OSC_Detune() {
		Tone parameters = createTestParameters();
		int[] expected = {
				-50, -50, -50, -49, -48, -47, -46, -45, -45, -44, -43, -42, -41, -40, -40, -39,
				-38, -37, -36, -35, -35, -34, -33, -32, -31, -30, -30, -29, -28, -27, -26, -25,
				-25, -24, -23, -22, -21, -20, -20, -19, -18, -17, -16, -15, -15, -14, -13, -12,
				-11, -10, -10,  -9,  -8,  -7,  -6,  -6,  -5,  -4,  -4,  -3,  -2,  -2,  -1,   0,
				  0,   0,   1,   2,   2,   3,   4,   4,   5,   6,   6,   7,   8,   9,  10,  10,
				 11,  12,  13,  14,  15,  15,  16,  17,  18,  19,  20,  20,  21,  22,  23,  24,
				 25,  25,  26,  27,  28,  29,  30,  30,  31,  32,  33,  34,  35,  35,  36,  37,
				 38,  39,  40,  40,  41,  42,  43,  44,  45,  45,  46,  47,  48,  49,  50,  50
			};
		for (int i = 0; i < 128; i++) {
			ControlChangeMessage cc = new ControlChangeMessage(0, Controller.TONE_1_OSC_DETUNE, i);
			parameters.updateParameters(cc);
			assertEquals(expected[i], parameters.getOSCDetune().getValue());
		}
	}
	
	@Test
	public void testUpdateParameters_OSC_Pulse_Width_Modulation() {
		Tone parameters = createTestParameters();
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
			ControlChangeMessage cc = new ControlChangeMessage(0, Controller.TONE_1_OSC_PULSE_WIDTH_MODULATION, i);
			parameters.updateParameters(cc);
			assertEquals(expected[i], parameters.getOSCPulseWidthModDepth().getValue());
		}
	}
	
	@Test
	public void testUpdateParameters_OSC_Pulse_Width() {
		Tone parameters = createTestParameters();
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
			ControlChangeMessage cc = new ControlChangeMessage(0, Controller.TONE_1_OSC_PULSE_WIDTH, i);
			parameters.updateParameters(cc);
			assertEquals(expected[i], parameters.getOSCPulseWidth().getValue());
		}
	}
	
	@Test
	public void testUpdateParameters_OSC_Env_Depth() {
		Tone parameters = createTestParameters();
		int[] expected = {
				-63, -63, -62, -61, -60, -59, -58, -57, -56, -55, -54, -53, -52, -51, -50, -49,
				-48, -47, -46, -45, -44, -43, -42, -41, -40, -39, -38, -37, -36, -35, -34, -33,
				-32, -31, -30, -29, -28, -27, -26, -25, -24, -23, -22, -21, -20, -19, -18, -17,
				-16, -15, -14, -13, -12, -11, -10,  -9,  -8,  -7,  -6,  -5,  -4,  -3,  -2,  -1,
				  0,   1,   2,   3,   4,   5,   6,   7,   8,   9,  10,  11,  12,  13,  14,  15,
				 16,  17,  18,  19,  20,  21,  22,  23,  24,  25,  26,  27,  28,  29,  30,  31,
				 32,  33,  34,  35,  36,  37,  38,  39,  40,  41,  42,  43,  44,  45,  46,  47,
				 48,  49,  50,  51,  52,  53,  54,  55,  56,  57,  58,  59,  60,  61,  62,  63
			};
		for (int i = 0; i < 128; i++) {
			ControlChangeMessage cc = new ControlChangeMessage(0, Controller.TONE_1_OSC_ENV_DEPTH, i);
			parameters.updateParameters(cc);
			assertEquals(expected[i], parameters.getOSCPitchEnvDepth().getValue());
		}
	}
	
	@Test
	public void testUpdateParameters_Filter_Cutoff() {
		Tone parameters = createTestParameters();
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
			ControlChangeMessage cc = new ControlChangeMessage(0, Controller.TONE_1_FILTER_CUTOFF, i);
			parameters.updateParameters(cc);
			assertEquals(expected[i], parameters.getFilterCutoff().getValue());
		}
	}
	
	@Test
	public void testUpdateParameters_Filter_Resonance() {
		Tone parameters = createTestParameters();
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
			ControlChangeMessage cc = new ControlChangeMessage(0, Controller.TONE_1_FILTER_RESONANCE, i);
			parameters.updateParameters(cc);
			assertEquals(expected[i], parameters.getFilterResonance().getValue());
		}
	}
	
	@Test
	public void testUpdateParameters_Filter_Env_Depth() {
		Tone parameters = createTestParameters();
		int[] expected = {
				-63, -63, -62, -61, -60, -59, -58, -57, -56, -55, -54, -53, -52, -51, -50, -49,
				-48, -47, -46, -45, -44, -43, -42, -41, -40, -39, -38, -37, -36, -35, -34, -33,
				-32, -31, -30, -29, -28, -27, -26, -25, -24, -23, -22, -21, -20, -19, -18, -17,
				-16, -15, -14, -13, -12, -11, -10,  -9,  -8,  -7,  -6,  -5,  -4,  -3,  -2,  -1,
				  0,   1,   2,   3,   4,   5,   6,   7,   8,   9,  10,  11,  12,  13,  14,  15,
				 16,  17,  18,  19,  20,  21,  22,  23,  24,  25,  26,  27,  28,  29,  30,  31,
				 32,  33,  34,  35,  36,  37,  38,  39,  40,  41,  42,  43,  44,  45,  46,  47,
				 48,  49,  50,  51,  52,  53,  54,  55,  56,  57,  58,  59,  60,  61,  62,  63
			};
		for (int i = 0; i < 128; i++) {
			ControlChangeMessage cc = new ControlChangeMessage(0, Controller.TONE_1_FILTER_ENV_DEPTH, i);
			parameters.updateParameters(cc);
			assertEquals(expected[i], parameters.getFilterEnvDepth().getValue());
		}
	}
	
	@Test
	public void testUpdateParameters_Filter_Key_Follow() {
		Tone parameters = createTestParameters();
		int[] expected = {
				-10, -10, -10, -10, -10, -10, -10,  -9,  -9,  -9,  -9,  -9,  -9,  -8,  -8,  -8,
				 -8,  -8,  -8,  -7,  -7,  -7,  -7,  -7,  -7,  -6,  -6,  -6,  -6,  -6,  -6,  -5,
				 -5,  -5,  -5,  -5,  -5,  -4,  -4,  -4,  -4,  -4,  -4,  -3,  -3,  -3,  -3,  -3,
				 -3,  -2,  -2,  -2,  -2,  -2,  -2,  -1,  -1,  -1,  -1,  -1,  -1,   0,   0,   0,
				  0,   0,   0,   0,   1,   1,   1,   1,   1,   1,   2,   2,   2,   2,   2,   2,
				  3,   3,   3,   3,   3,   3,   4,   4,   4,   4,   4,   4,   5,   5,   5,   5,
				  5,   5,   6,   6,   6,   6,   6,   6,   7,   7,   7,   7,   7,   7,   8,   8,
				  8,   8,   8,   8,   9,   9,   9,   9,   9,   9,  10,  10,  10,  10,  10,  10
			};
		for (int i = 0; i < 128; i++) {
			ControlChangeMessage cc = new ControlChangeMessage(0, Controller.TONE_1_FILTER_KEY_FOLLOW, i);
			parameters.updateParameters(cc);
			assertEquals(expected[i], parameters.getFilterCutoffKeyfollow().getValue());
		}
	}
	
	@Test
	public void testUpdateParameters_Amp_Level() {
		Tone parameters = createTestParameters();
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
			ControlChangeMessage cc = new ControlChangeMessage(0, Controller.TONE_1_AMP_LEVEL, i);
			parameters.updateParameters(cc);
			assertEquals(expected[i], parameters.getAmpLevel().getValue());
		}
	}

}
