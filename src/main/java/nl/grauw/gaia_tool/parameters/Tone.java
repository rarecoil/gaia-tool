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

import nl.grauw.gaia_tool.Address;
import nl.grauw.gaia_tool.Parameters;
import nl.grauw.gaia_tool.SignedIntValue;
import nl.grauw.gaia_tool.IntValue;
import nl.grauw.gaia_tool.messages.ControlChangeMessage;

public class Tone extends Parameters {
	
	public enum OSCWave {
		SAW, SQR, PW_SQR, TRI, SINE, NOISE, SUPER_SAW
	}
	
	public enum OSCWaveVariation {
		A, B, C
	}
	
	public enum FilterMode {
		BYPASS, LPF, HPF, BPF, PKG
	}
	
	public enum FilterSlope {
		MINUS_12_DB, MINUS_24_DB
	}
	
	public enum LFOShape {
		TRI, SIN, SAW, SQR, S_AND_H, RND
	}
	
	public enum LFOTempoSyncNote {
		_16, _12, _8, _4, _2, _1, _3_4TH, _2_3RD, _1_2ND,
		_3_8TH, _1_3RD, _1_4TH, _3_16TH, _1_6TH, _1_8TH, _3_32ND,
		_1_12TH, _1_16TH, _1_24TH, _1_32ND
	}
	
	// mapping for pitch control change message values to pitch parameter values
	private final static int[] oscPitchControlMapping = {
		 40,  40,  41,  41,  41,  42,  42,  42,  43,  43,  44,  44,  44,  45,  45,  45,
		 46,  46,  47,  47,  47,  48,  48,  49,  49,  50,  50,  50,  51,  51,  51,  52,
		 52,  52,  53,  53,  53,  54,  54,  54,  55,  55,  56,  56,  56,  57,  57,  57,
		 58,  58,  59,  59,  59,  60,  60,  61,  61,  62,  62,  62,  63,  63,  63,  64,
		 64,  64,  65,  65,  65,  66,  66,  66,  67,  67,  68,  68,  69,  69,  69,  70,
		 70,  71,  71,  71,  72,  72,  72,  73,  73,  74,  74,  74,  75,  75,  75,  76,
		 76,  76,  77,  77,  77,  78,  78,  78,  79,  79,  80,  80,  81,  81,  81,  82,
		 82,  83,  83,  83,  84,  84,  84,  85,  85,  86,  86,  86,  87,  87,  87,  88
	};
	
	// mapping for detune control change message values to detune parameter values
	private final static int[] oscDetuneControlMapping = {
		 14,  14,  14,  15,  16,  17,  18,  19,  19,  20,  21,  22,  23,  24,  24,  25,
		 26,  27,  28,  29,  29,  30,  31,  32,  33,  34,  34,  35,  36,  37,  38,  39,
		 39,  40,  41,  42,  43,  44,  44,  45,  46,  47,  48,  49,  49,  50,  51,  52,
		 53,  54,  54,  55,  56,  57,  58,  58,  59,  60,  60,  61,  62,  62,  63,  64,
		 64,  64,  65,  66,  66,  67,  68,  68,  69,  70,  70,  71,  72,  73,  74,  74,
		 75,  76,  77,  78,  79,  79,  80,  81,  82,  83,  84,  84,  85,  86,  87,  88,
		 89,  89,  90,  91,  92,  93,  94,  94,  95,  96,  97,  98,  99,  99, 100, 101,
		102, 103, 104, 104, 105, 106, 107, 108, 109, 109, 110, 111, 112, 113, 114, 114
	};
	
	// mapping for rate control change message values to tempo synced rates
	private final static int[] lfoTempoSyncNoteMapping = {
		  0,   0,   0,   0,   0,   0,   1,   1,   1,   1,   1,   1,   2,   2,   2,   2,
		  2,   2,   3,   3,   3,   3,   3,   3,   4,   4,   4,   4,   4,   4,   5,   5,
		  5,   5,   5,   5,   6,   6,   6,   6,   6,   6,   6,   7,   7,   7,   7,   7,
		  7,   7,   8,   8,   8,   8,   8,   8,   8,   9,   9,   9,   9,   9,   9,   9,
		 10,  10,  10,  10,  10,  10,  10,  11,  11,  11,  11,  11,  11,  11,  12,  12,
		 12,  12,  12,  12,  12,  13,  13,  13,  13,  13,  13,  13,  14,  14,  14,  14,
		 14,  14,  15,  15,  15,  15,  15,  15,  16,  16,  16,  16,  16,  16,  17,  17,
		 17,  17,  17,  17,  18,  18,  18,  18,  18,  18,  19,  19,  19,  19,  19,  19
	};
	
	public Tone(Address address, byte[] data) {
		super(address, data);
		
		if (data.length < 0x3E)
			throw new IllegalArgumentException("Parameters data size mismatch.");
	}
	
	public void updateParameters(ControlChangeMessage message) {
		if (message.getController() != null) {
			switch (message.getController()) {
			case TONE_1_LFO_RATE:
			case TONE_2_LFO_RATE:
			case TONE_3_LFO_RATE:
				if (getLFOTempoSyncSwitch()) {
					// use a mapping because the values don’t change on a linear scale
					updateValue(0x1F, lfoTempoSyncNoteMapping[message.getValue()]);
				} else {
					updateValue(0x1D, message.getValue());
				}
				break;
			case TONE_1_LFO_FADE_TIME:
			case TONE_2_LFO_FADE_TIME:
			case TONE_3_LFO_FADE_TIME:
				updateValue(0x20, message.getValue());
				break;
			case TONE_1_LFO_PITCH_DEPTH:
			case TONE_2_LFO_PITCH_DEPTH:
			case TONE_3_LFO_PITCH_DEPTH:
				updateValue(0x22, Math.max(message.getValue(), 1));
				break;
			case TONE_1_LFO_FILTER_DEPTH:
			case TONE_2_LFO_FILTER_DEPTH:
			case TONE_3_LFO_FILTER_DEPTH:
				updateValue(0x23, Math.max(message.getValue(), 1));
				break;
			case TONE_1_LFO_AMP_DEPTH:
			case TONE_2_LFO_AMP_DEPTH:
			case TONE_3_LFO_AMP_DEPTH:
				updateValue(0x24, Math.max(message.getValue(), 1));
				break;
			case TONE_1_OSC_PITCH:
			case TONE_2_OSC_PITCH:
			case TONE_3_OSC_PITCH:
				// use a mapping because the values don’t change on a linear scale
				updateValue(0x03, oscPitchControlMapping[message.getValue()]);
				break;
			case TONE_1_OSC_DETUNE:
			case TONE_2_OSC_DETUNE:
			case TONE_3_OSC_DETUNE:
				// use a mapping because the values don’t change on a linear scale
				updateValue(0x04, oscDetuneControlMapping[message.getValue()]);
				break;
			case TONE_1_OSC_PULSE_WIDTH_MODULATION:
			case TONE_2_OSC_PULSE_WIDTH_MODULATION:
			case TONE_3_OSC_PULSE_WIDTH_MODULATION:
				updateValue(0x05, message.getValue());
				break;
			case TONE_1_OSC_PULSE_WIDTH:
			case TONE_2_OSC_PULSE_WIDTH:
			case TONE_3_OSC_PULSE_WIDTH:
				updateValue(0x06, message.getValue());
				break;
			case TONE_1_OSC_ENV_DEPTH:
			case TONE_2_OSC_ENV_DEPTH:
			case TONE_3_OSC_ENV_DEPTH:
				updateValue(0x09, Math.max(message.getValue(), 1));
				break;
			case TONE_1_FILTER_CUTOFF:
			case TONE_2_FILTER_CUTOFF:
			case TONE_3_FILTER_CUTOFF:
				updateValue(0x0C, message.getValue());
				break;
			case TONE_1_FILTER_RESONANCE:
			case TONE_2_FILTER_RESONANCE:
			case TONE_3_FILTER_RESONANCE:
				updateValue(0x0F, message.getValue());
				break;
			case TONE_1_FILTER_ENV_DEPTH:
			case TONE_2_FILTER_ENV_DEPTH:
			case TONE_3_FILTER_ENV_DEPTH:
				updateValue(0x14, Math.max(message.getValue(), 1));
				break;
			case TONE_1_FILTER_KEY_FOLLOW:
			case TONE_2_FILTER_KEY_FOLLOW:
			case TONE_3_FILTER_KEY_FOLLOW:
				updateValue(0x0D, (int) (message.getValue() / 6.1) + 54);
				break;
			case TONE_1_AMP_LEVEL:
			case TONE_2_AMP_LEVEL:
			case TONE_3_AMP_LEVEL:
				updateValue(0x15, message.getValue());
				break;
			default:
				throw new RuntimeException("Control change message not recognised: " + message);
			}
		}
	}
	
	public OSCWave getOSCWave() {
		return OSCWave.values()[getValue(0x00)];
	}
	
	public OSCWaveVariation getOSCWaveVariation() {
		return OSCWaveVariation.values()[getValue(0x01)];
	}
	
	public boolean getReserved1() {
		return getValue(0x02) == 1;
	}
	
	public IntValue getOSCPitch() {
		return new SignedIntValue(this, 0x03, -24, 24);
	}
	
	public IntValue getOSCDetune() {
		return new SignedIntValue(this, 0x04, -50, 50);
	}
	
	public IntValue getOSCPulseWidthModDepth() {
		return new IntValue(this, 0x05, 0, 127);
	}
	
	public IntValue getOSCPulseWidth() {
		return new IntValue(this, 0x06, 0, 127);
	}
	
	public IntValue getOSCPitchEnvAttackTime() {
		return new IntValue(this, 0x07, 0, 127);
	}
	
	// XXX: ...DecayTime? Slightly inconsistent...
	public IntValue getOSCPitchEnvDecay() {
		return new IntValue(this, 0x08, 0, 127);
	}
	
	public IntValue getOSCPitchEnvDepth() {
		return new SignedIntValue(this, 0x09, -63, 63);
	}
	
	public FilterMode getFilterMode() {
		return FilterMode.values()[getValue(0x0A)];
	}
	
	public FilterSlope getFilterSlope() {
		return FilterSlope.values()[getValue(0x0B)];
	}
	
	public IntValue getFilterCutoff() {
		return new IntValue(this, 0x0C, 0, 127);
	}
	
	// -10 ... 10 (-100 ... 100)
	public IntValue getFilterCutoffKeyfollow() {
		return new SignedIntValue(this, 0x0D, -10, 10);
	}
	
	public IntValue getFilterEnvVelocitySens() {
		return new SignedIntValue(this, 0x0E, -63, 63);
	}
	
	public IntValue getFilterResonance() {
		return new IntValue(this, 0x0F, 0, 127);
	}
	
	public IntValue getFilterEnvAttackTime() {
		return new IntValue(this, 0x10, 0, 127);
	}
	
	public IntValue getFilterEnvDecayTime() {
		return new IntValue(this, 0x11, 0, 127);
	}
	
	public IntValue getFilterEnvSustainLevel() {
		return new IntValue(this, 0x12, 0, 127);
	}
	
	public IntValue getFilterEnvReleaseTime() {
		return new IntValue(this, 0x13, 0, 127);
	}
	
	public IntValue getFilterEnvDepth() {
		return new SignedIntValue(this, 0x14, -63, 63);
	}
	
	public IntValue getAmpLevel() {
		return new IntValue(this, 0x15, 0, 127);
	}
	
	public IntValue getAmpLevelVelocitySens() {
		return new SignedIntValue(this, 0x16, -63, 63);
	}
	
	public IntValue getAmpEnvAttackTime() {
		return new IntValue(this, 0x17, 0, 127);
	}
	
	public IntValue getAmpEnvDecayTime() {
		return new IntValue(this, 0x18, 0, 127);
	}
	
	public IntValue getAmpEnvSustainLevel() {
		return new IntValue(this, 0x19, 0, 127);
	}
	
	public IntValue getAmpEnvReleaseTime() {
		return new IntValue(this, 0x1A, 0, 127);
	}
	
	// -64 ... 63 (L64 ... 63R)
	public IntValue getAmpPan() {
		return new SignedIntValue(this, 0x1B, -64, 63);
	}
	
	public LFOShape getLFOShape() {
		return LFOShape.values()[getValue(0x1C)];
	}
	
	public IntValue getLFORate() {
		return new IntValue(this, 0x1D, 0, 127);
	}
	
	public boolean getLFOTempoSyncSwitch() {
		return getValue(0x1E) == 1;
	}
	
	public LFOTempoSyncNote getLFOTempoSyncNote() {
		return LFOTempoSyncNote.values()[getValue(0x1F)];
	}
	
	public IntValue getLFOFadeTime() {
		return new IntValue(this, 0x20, 0, 127);
	}
	
	public boolean getLFOKeyTrigger() {
		return getValue(0x21) == 1;
	}
	
	public IntValue getLFOPitchDepth() {
		return new SignedIntValue(this, 0x22, -63, 63);
	}
	
	public IntValue getLFOFilterDepth() {
		return new SignedIntValue(this, 0x23, -63, 63);
	}
	
	public IntValue getLFOAmpDepth() {
		return new SignedIntValue(this, 0x24, -63, 63);
	}
	
	public IntValue getLFOPanDepth() {
		return new SignedIntValue(this, 0x25, -63, 63);
	}
	
	public LFOShape getModulationLFOShape() {
		return LFOShape.values()[getValue(0x26)];
	}
	
	public IntValue getModulationLFORate() {
		return new IntValue(this, 0x27, 0, 127);
	}
	
	public boolean getModulationLFOTempoSyncSwitch() {
		return getValue(0x28) == 1;
	}
	
	public LFOTempoSyncNote getModulationLFOTempoSyncNote() {
		return LFOTempoSyncNote.values()[getValue(0x29)];
	}
	
	public IntValue getReserved2() {
		return new IntValue(this, 0x2A, 0, 127);
	}
	
	public IntValue getReserved3() {
		return new IntValue(this, 0x2B, 0, 1);
	}
	
	public IntValue getModulationLFOPitchDepth() {
		return new SignedIntValue(this, 0x2C, -63, 63);
	}
	
	public IntValue getModulationLFOFilterDepth() {
		return new SignedIntValue(this, 0x2D, -63, 63);
	}
	
	public IntValue getModulationLFOAmpDepth() {
		return new SignedIntValue(this, 0x2E, -63, 63);
	}
	
	public IntValue getModulationLFOPanDepth() {
		return new SignedIntValue(this, 0x2F, -63, 63);
	}
	
	public IntValue getReserved4() {
		return new SignedIntValue(this, 0x30, -63, 63);
	}
	
	public IntValue getReserved5() {
		return new SignedIntValue(this, 0x31, -63, 63);
	}
	
	public IntValue getReserved6() {
		return new SignedIntValue(this, 0x32, -63, 63);
	}
	
	public IntValue getReserved7() {
		return new SignedIntValue(this, 0x33, -63, 63);
	}
	
	public IntValue getReserved8() {
		return new IntValue(this, 0x34, 0, 1);
	}
	
	public IntValue getReserved9() {
		return new IntValue(this, 0x35, 0, 1);
	}
	
	public IntValue getReserved10() {
		return new IntValue(this, 0x36, 0, 1);
	}
	
	public IntValue getReserved11() {
		return new IntValue(this, 0x37, 0, 1);
	}
	
	public IntValue getReserved12() {
		return new IntValue(this, 0x38, 0, 127);
	}
	
	public IntValue getReserved13() {
		return new IntValue(this, 0x39, 0, 127);
	}
	
	public IntValue getReserved14() {
		return new IntValue(this, 0x3A, 0, 127);
	}
	
	public IntValue getReserved15() {
		return new IntValue(this, 0x3B, 0, 127);
	}
	
	public IntValue getReserved16() {
		return new SignedIntValue(this, 0x3C, -63, 63);
	}
	
	public IntValue getReserved17() {
		return new SignedIntValue(this, 0x3D, -63, 63);
	}
	
}
