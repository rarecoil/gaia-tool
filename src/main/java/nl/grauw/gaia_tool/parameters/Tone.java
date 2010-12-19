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
import nl.grauw.gaia_tool.SignedValue;
import nl.grauw.gaia_tool.Value;

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
	
	public Tone(Address address, byte[] data) {
		super(address, data);
		
		if (data.length < 0x3E)
			throw new IllegalArgumentException("Parameters data size mismatch.");
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
	
	public Value getOSCPitch() {
		return new SignedValue(this, 0x03, -24, 24);
	}
	
	public Value getOSCDetune() {
		return new SignedValue(this, 0x04, -50, 50);
	}
	
	public Value getOSCPulseWidthModDepth() {
		return new Value(this, 0x05, 0, 127);
	}
	
	public Value getOSCPulseWidth() {
		return new Value(this, 0x06, 0, 127);
	}
	
	public Value getOSCPitchEnvAttackTime() {
		return new Value(this, 0x07, 0, 127);
	}
	
	// XXX: ...DecayTime? Slightly inconsistent...
	public Value getOSCPitchEnvDecay() {
		return new Value(this, 0x08, 0, 127);
	}
	
	public Value getOSCPitchEnvDepth() {
		return new SignedValue(this, 0x09, -63, 63);
	}
	
	public FilterMode getFilterMode() {
		return FilterMode.values()[getValue(0x0A)];
	}
	
	public FilterSlope getFilterSlope() {
		return FilterSlope.values()[getValue(0x0B)];
	}
	
	public Value getFilterCutoff() {
		return new Value(this, 0x0C, 0, 127);
	}
	
	// -10 ... 10 (-100 ... 100)
	public Value getFilterCutoffKeyfollow() {
		return new SignedValue(this, 0x0D, -10, 10);
	}
	
	public Value getFilterEnvVelocitySens() {
		return new SignedValue(this, 0x0E, -63, 63);
	}
	
	public Value getFilterResonance() {
		return new Value(this, 0x0F, 0, 127);
	}
	
	public Value getFilterEnvAttackTime() {
		return new Value(this, 0x10, 0, 127);
	}
	
	public Value getFilterEnvDecayTime() {
		return new Value(this, 0x11, 0, 127);
	}
	
	public Value getFilterEnvSustainLevel() {
		return new Value(this, 0x12, 0, 127);
	}
	
	public Value getFilterEnvReleaseTime() {
		return new Value(this, 0x13, 0, 127);
	}
	
	public Value getFilterEnvDepth() {
		return new SignedValue(this, 0x14, -63, 63);
	}
	
	public Value getAmpLevel() {
		return new Value(this, 0x15, 0, 127);
	}
	
	public Value getAmpLevelVelocitySens() {
		return new SignedValue(this, 0x16, -63, 63);
	}
	
	public Value getAmpEnvAttackTime() {
		return new Value(this, 0x17, 0, 127);
	}
	
	public Value getAmpEnvDecayTime() {
		return new Value(this, 0x18, 0, 127);
	}
	
	public Value getAmpEnvSustainLevel() {
		return new Value(this, 0x19, 0, 127);
	}
	
	public Value getAmpEnvReleaseTime() {
		return new Value(this, 0x1A, 0, 127);
	}
	
	// -64 ... 63 (L64 ... 63R)
	public Value getAmpPan() {
		return new SignedValue(this, 0x1B, -64, 63);
	}
	
	public LFOShape getLFOShape() {
		return LFOShape.values()[getValue(0x1C)];
	}
	
	public Value getLFORate() {
		return new Value(this, 0x1D, 0, 127);
	}
	
	public boolean getLFOTempoSyncSwitch() {
		return getValue(0x1E) == 1;
	}
	
	public LFOTempoSyncNote getLFOTempoSyncNote() {
		return LFOTempoSyncNote.values()[getValue(0x1F)];
	}
	
	public Value getLFOFadeTime() {
		return new Value(this, 0x20, 0, 127);
	}
	
	public boolean getLFOKeyTrigger() {
		return getValue(0x21) == 1;
	}
	
	public Value getLFOPitchDepth() {
		return new SignedValue(this, 0x22, -63, 63);
	}
	
	public Value getLFOFilterDepth() {
		return new SignedValue(this, 0x23, -63, 63);
	}
	
	public Value getLFOAmpDepth() {
		return new SignedValue(this, 0x24, -63, 63);
	}
	
	public Value getLFOPanDepth() {
		return new SignedValue(this, 0x25, -63, 63);
	}
	
	public LFOShape getModulationLFOShape() {
		return LFOShape.values()[getValue(0x26)];
	}
	
	public Value getModulationLFORate() {
		return new Value(this, 0x27, 0, 127);
	}
	
	public boolean getModulationLFOTempoSyncSwitch() {
		return getValue(0x28) == 1;
	}
	
	public LFOTempoSyncNote getModulationLFOTempoSyncNote() {
		return LFOTempoSyncNote.values()[getValue(0x29)];
	}
	
	public Value getReserved2() {
		return new Value(this, 0x2A, 0, 127);
	}
	
	public Value getReserved3() {
		return new Value(this, 0x2B, 0, 1);
	}
	
	public Value getModulationLFOPitchDepth() {
		return new SignedValue(this, 0x2C, -63, 63);
	}
	
	public Value getModulationLFOFilterDepth() {
		return new SignedValue(this, 0x2D, -63, 63);
	}
	
	public Value getModulationLFOAmpDepth() {
		return new SignedValue(this, 0x2E, -63, 63);
	}
	
	public Value getModulationLFOPanDepth() {
		return new SignedValue(this, 0x2F, -63, 63);
	}
	
	public Value getReserved4() {
		return new SignedValue(this, 0x30, -63, 63);
	}
	
	public Value getReserved5() {
		return new SignedValue(this, 0x31, -63, 63);
	}
	
	public Value getReserved6() {
		return new SignedValue(this, 0x32, -63, 63);
	}
	
	public Value getReserved7() {
		return new SignedValue(this, 0x33, -63, 63);
	}
	
	public Value getReserved8() {
		return new Value(this, 0x34, 0, 1);
	}
	
	public Value getReserved9() {
		return new Value(this, 0x35, 0, 1);
	}
	
	public Value getReserved10() {
		return new Value(this, 0x36, 0, 1);
	}
	
	public Value getReserved11() {
		return new Value(this, 0x37, 0, 1);
	}
	
	public Value getReserved12() {
		return new Value(this, 0x38, 0, 127);
	}
	
	public Value getReserved13() {
		return new Value(this, 0x39, 0, 127);
	}
	
	public Value getReserved14() {
		return new Value(this, 0x3A, 0, 127);
	}
	
	public Value getReserved15() {
		return new Value(this, 0x3B, 0, 127);
	}
	
	public Value getReserved16() {
		return new SignedValue(this, 0x3C, -63, 63);
	}
	
	public Value getReserved17() {
		return new SignedValue(this, 0x3D, -63, 63);
	}
	
	public String toString() {
		return "Patch tone parameters:\n" +
				String.format("OSC wave: %s\n", getOSCWave()) +
				String.format("OSC wave variation: %s\n", getOSCWaveVariation()) +
				String.format("OSC pitch: %s\n", getOSCPitch()) +
				String.format("OSC detune: %s\n", getOSCDetune()) +
				String.format("OSC pulse width mod depth: %s\n", getOSCPulseWidthModDepth()) +
				String.format("OSC pulse width: %s\n", getOSCPulseWidth()) +
				String.format("OSC pitch env attack time: %s\n", getOSCPitchEnvAttackTime()) +
				String.format("OSC pitch env decay: %s\n", getOSCPitchEnvDecay()) +
				String.format("OSC pitch env depth: %s\n", getOSCPitchEnvDepth()) +
				String.format("Filter mode: %s\n", getFilterMode()) +
				String.format("Filter slope: %s\n", getFilterSlope()) +
				String.format("Filter cutoff: %s\n", getFilterCutoff()) +
				String.format("Filter cutoff keyfollow: %s\n", getFilterCutoffKeyfollow().getValue() * 10) +
				String.format("Filter env velocity sens: %s\n", getFilterEnvVelocitySens()) +
				String.format("Filter resonance: %s\n", getFilterResonance()) +
				String.format("Filter env attack time: %s\n", getFilterEnvAttackTime()) +
				String.format("Filter env decay time: %s\n", getFilterEnvDecayTime()) +
				String.format("Filter env sustain level: %s\n", getFilterEnvSustainLevel()) +
				String.format("Filter env release time: %s\n", getFilterEnvReleaseTime()) +
				String.format("Filter env depth: %s\n", getFilterEnvDepth()) +
				String.format("Amp level: %s\n", getAmpLevel()) +
				String.format("Amp level velocity sens: %s\n", getAmpLevelVelocitySens()) +
				String.format("Amp env attack time: %s\n", getAmpEnvAttackTime()) +
				String.format("Amp env decay time: %s\n", getAmpEnvDecayTime()) +
				String.format("Amp env sustain level: %s\n", getAmpEnvSustainLevel()) +
				String.format("Amp env release time: %s\n", getAmpEnvReleaseTime()) +
				String.format("Amp pan: %s\n", getAmpPan()) +
				String.format("LFO shape: %s\n", getLFOShape()) +
				String.format("LFO rate: %s\n", getLFORate()) +
				String.format("LFO tempo sync switch: %s\n", getLFOTempoSyncSwitch()) +
				String.format("LFO tempo sync note: %s\n", getLFOTempoSyncNote()) +
				String.format("LFO fade time: %s\n", getLFOFadeTime()) +
				String.format("LFO key trigger: %s\n", getLFOKeyTrigger()) +
				String.format("LFO pitch depth: %s\n", getLFOPitchDepth()) +
				String.format("LFO filter depth: %s\n", getLFOFilterDepth()) +
				String.format("LFO amp depth: %s\n", getLFOAmpDepth()) +
				String.format("LFO pan depth: %s\n", getLFOPanDepth()) +
				String.format("Modulation LFO shape: %s\n", getModulationLFOShape()) +
				String.format("Modulation LFO rate: %s\n", getModulationLFORate()) +
				String.format("Modulation LFO tempo sync switch: %s\n", getModulationLFOTempoSyncSwitch()) +
				String.format("Modulation LFO tempo sync note: %s\n", getModulationLFOTempoSyncNote()) +
				String.format("Modulation LFO pitch depth: %s\n", getModulationLFOPitchDepth()) +
				String.format("Modulation LFO filter depth: %s\n", getModulationLFOFilterDepth()) +
				String.format("Modulation LFO amp depth: %s\n", getModulationLFOAmpDepth()) +
				String.format("Modulation LFO pan depth: %s\n", getModulationLFOPanDepth());
	}
	
}