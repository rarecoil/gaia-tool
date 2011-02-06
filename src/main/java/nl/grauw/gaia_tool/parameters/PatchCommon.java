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

import java.nio.charset.Charset;

import nl.grauw.gaia_tool.Address;
import nl.grauw.gaia_tool.Parameters;
import nl.grauw.gaia_tool.SignedIntValue;
import nl.grauw.gaia_tool.IntValue;
import nl.grauw.gaia_tool.Int12BitValue;

public class PatchCommon extends Parameters {
	
	public enum SyncRingSelect {
		OFF, SYNC, RING
	}
	
	public enum DBeamAssign {
		LFO_RATE, LFO_FADE_TIME, LFO_PITCH_MOD, LFO_FILTER_MOD, LFO_AMP_MOD,
		OSC_PITCH, OSC_DETUNE, OSC_PWM, OSC_PW, OSC_ENV_A, OSC_ENV_D, OSC_ENV_MOD,
		FILTER_CUTOFF, FILTER_RESONANCE, FILTER_ENV_A, FILTER_ENV_D, FILTER_ENV_S,
		FILTER_ENV_R, FILTER_ENV_MOD, AMP_LEVEL, AMP_ENV_A, AMP_ENV_D,
		AMP_ENV_S, AMP_ENV_R, EFX_CTRL, PORT_TIME, BENDER, MODULATION,
		FILTER_CUTOFF_KF, EFX_LEVEL
	}
	
	public enum DBeamPolarity {
		NORMAL, REVERSE
	}
	
	private Charset US_ASCII = Charset.forName("US-ASCII");
	
	public PatchCommon(Address address, byte[] data) {
		super(address, data);
		
		if (data.length < 0x3D)
			throw new IllegalArgumentException("Parameters data size mismatch.");
	}
	
	public String getPatchName() {
		return getString(0x00, 12);
	}
	
	public void setPatchName(String name) {
		setValues(0x00, (name + "            ").substring(0, 12).getBytes(US_ASCII));
	}
	
	public IntValue getPatchLevel() {
		return new IntValue(this, 0x0C, 0, 127);
	}
	
	public IntValue getPatchTempo() {
		return new Int12BitValue(this, 0x0D, 5, 300);
	}
	
	public boolean getArpeggioSwitch() {
		return getValue(0x10) == 1;
	}
	
	public IntValue getReserved1() {
		return new IntValue(this, 0x11, 0, 1);
	}
	
	public boolean getPortamentoSwitch() {
		return getValue(0x12) == 1;
	}
	
	public IntValue getPortamentoTime() {
		return new IntValue(this, 0x13, 0, 127);
	}
	
	public boolean getMonoSwitch() {
		return getValue(0x14) == 1;
	}
	
	public IntValue getOctaveShift() {
		return new SignedIntValue(this, 0x15, -3, 3);
	}
	
	public IntValue getPitchBendRangeUp() {
		return new IntValue(this, 0x16, 0, 24);
	}
	
	public IntValue getPitchBendRangeDown() {
		return new IntValue(this, 0x17, 0, 24);
	}
	
	public IntValue getReserved2() {
		return new IntValue(this, 0x18, 0, 1);
	}
	
	public boolean getTone1Switch() {
		return getValue(0x19) == 1;
	}
	
	public boolean getTone1Select() {
		return getValue(0x1A) == 1;
	}
	
	public boolean getTone2Switch() {
		return getValue(0x1B) == 1;
	}
	
	public boolean getTone2Select() {
		return getValue(0x1C) == 1;
	}
	
	public boolean getTone3Switch() {
		return getValue(0x1D) == 1;
	}
	
	public boolean getTone3Select() {
		return getValue(0x1E) == 1;
	}
	
	public SyncRingSelect getSyncRingSelect() {
		return SyncRingSelect.values()[getValue(0x1F)];
	}
	
	public boolean getEffectsMasterSwitch() {
		return getValue(0x20) == 1;
	}
	
	public IntValue getReserved3() {
		return new IntValue(this, 0x21, 0, 3);
	}
	
	public boolean getDelayTempoSyncSwitch() {
		return getValue(0x22) == 1;
	}
	
	public boolean getLowBoostSwitch() {
		return getValue(0x23) == 1;
	}
	
	public DBeamAssign getDBeamAssign() {
		return DBeamAssign.values()[getValue(0x24)];
	}
	
	public IntValue getReserved4() {
		return new IntValue(this, 0x25, 0, 1);
	}
	
	public IntValue getReserved5() {
		return new IntValue(this, 0x26, 0, 1);
	}
	
	public IntValue getReserved6() {
		return new IntValue(this, 0x27, 0, 1);
	}
	
	public IntValue getReserved7() {
		return new IntValue(this, 0x28, 0, 1);
	}
	
	public DBeamPolarity getDBeamPolarity() {
		return DBeamPolarity.values()[getValue(0x29)];
	}
	
	public boolean getEffectsDistortionSelect() {
		return getValue(0x2A) == 1;
	}
	
	public boolean getEffectsFlangerSelect() {
		return getValue(0x2B) == 1;
	}
	
	public boolean getEffectsDelaySelect() {
		return getValue(0x2C) == 1;
	}
	
	public boolean getEffectsReverbSelect() {
		return getValue(0x2D) == 1;
	}
	
	public IntValue getReserved8() {
		return new IntValue(this, 0x2E, 0, 1);
	}
	
	public IntValue getReserved9() {
		return new IntValue(this, 0x2F, 0, 1);
	}
	
	public IntValue getReserved10() {
		return new IntValue(this, 0x30, 0, 1);
	}
	
	public IntValue getReserved11() {
		return new IntValue(this, 0x31, 0, 1);
	}
	
	public IntValue getReserved12() {
		return new IntValue(this, 0x32, 0, 1);
	}
	
	public IntValue getReserved13() {
		return new IntValue(this, 0x33, 0, 1);
	}
	
	public IntValue getReserved14() {
		return new IntValue(this, 0x34, 0, 127);
	}
	
	public IntValue getReserved15() {
		return new IntValue(this, 0x35, 0, 127);
	}
	
	public IntValue getReserved16() {
		return new IntValue(this, 0x36, 0, 127);
	}
	
	public IntValue getReserved17() {
		return new IntValue(this, 0x37, 0, 127);
	}
	
	public IntValue getReserved18() {
		return new IntValue(this, 0x38, 0, 127);
	}
	
	public IntValue getReserved19() {
		return new IntValue(this, 0x39, 0, 127);
	}
	
	public IntValue getReserved20() {
		return new SignedIntValue(this, 0x3A, -63, 63);
	}
	
	public IntValue getReserved21() {
		return new SignedIntValue(this, 0x3B, -63, 63);
	}
	
	public IntValue getReserved22() {
		return new SignedIntValue(this, 0x3C, -63, 63);
	}
	
	public String toString() {
		return "Patch common parameters:\n" +
				String.format("Patch name: %s\n", getPatchName()) +
				String.format("Patch level: %s\n", getPatchLevel()) +
				String.format("Patch tempo: %s\n", getPatchTempo()) +
				String.format("Arpeggio switch: %s\n", getArpeggioSwitch()) +
				String.format("Portamento switch: %s\n", getPortamentoSwitch()) +
				String.format("Portamento time: %s\n", getPortamentoTime()) +
				String.format("Mono switch: %s\n", getMonoSwitch()) +
				String.format("Octave shift: %s\n", getOctaveShift()) +
				String.format("Pitch bend range up: %s\n", getPitchBendRangeUp()) +
				String.format("Pitch bend range down: %s\n", getPitchBendRangeDown()) +
				String.format("Tone 1 switch: %s\n", getTone1Switch()) +
				String.format("Tone 1 select: %s\n", getTone1Select()) +
				String.format("Tone 2 switch: %s\n", getTone2Switch()) +
				String.format("Tone 2 select: %s\n", getTone2Select()) +
				String.format("Tone 3 switch: %s\n", getTone3Switch()) +
				String.format("Tone 3 select: %s\n", getTone3Select()) +
				String.format("SYNC/RING select: %s\n", getSyncRingSelect()) +
				String.format("Effects master switch: %s\n", getEffectsMasterSwitch()) +
				String.format("Delay tempo sync switch: %s\n", getDelayTempoSyncSwitch()) +
				String.format("Low boost switch: %s\n", getLowBoostSwitch()) +
				String.format("D-Beam assign: %s\n", getDBeamAssign()) +
				String.format("D-Beam polarity: %s\n", getDBeamPolarity()) +
				String.format("Effects distortion select: %s\n", getEffectsDistortionSelect()) +
				String.format("Effects flanger select: %s\n", getEffectsFlangerSelect()) +
				String.format("Effects delay select: %s\n", getEffectsDelaySelect()) +
				String.format("Effects reverb select: %s\n", getEffectsReverbSelect());
	}
	
}
