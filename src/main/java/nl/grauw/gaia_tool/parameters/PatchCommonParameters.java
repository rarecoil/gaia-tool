package nl.grauw.gaia_tool.parameters;

import nl.grauw.gaia_tool.Value;

public class PatchCommonParameters {
	
	private byte[] addressMap;	// XXX: make AddressMap type
	
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
	
	public PatchCommonParameters(byte[] addressMap) {
		if (addressMap.length < 0x3D)
			throw new RuntimeException("Address map size mismatch.");
		
		this.addressMap = addressMap;
	}
	
	public String getPatchName() {
		return new String(addressMap, 0, 12);
	}
	
	public Value getPatchLevel() {
		return new Value(addressMap[0x0D], 0, 127);
	}
	
	public Value getPatchTempo() {
		return new Value(addressMap[0x0D] << 8 |
				addressMap[0x0E] << 4 |
				addressMap[0x0F], 5, 300);
	}
	
	public boolean getArpeggioSwitch() {
		return addressMap[0x10] == 1;
	}
	
	public Value getReserved1() {
		return new Value(addressMap[0x11], 0, 1);
	}
	
	public boolean getPortamentoSwitch() {
		return addressMap[0x12] == 1;
	}
	
	public Value getPortamentoTime() {
		return new Value(addressMap[0x13], 0, 127);
	}
	
	public boolean getMonoSwitch() {
		return addressMap[0x14] == 1;
	}
	
	public Value getOctaveShift() {
		return new Value(addressMap[0x15] - 64, -3, 3);
	}
	
	public Value getPitchBendRangeUp() {
		return new Value(addressMap[0x16], 0, 24);
	}
	
	public Value getPitchBendRangeDown() {
		return new Value(addressMap[0x17], 0, 24);
	}
	
	public Value getReserved2() {
		return new Value(addressMap[0x18], 0, 1);
	}
	
	public boolean getTone1Switch() {
		return addressMap[0x19] == 1;
	}
	
	public boolean getTone1Select() {
		return addressMap[0x1A] == 1;
	}
	
	public boolean getTone2Switch() {
		return addressMap[0x1B] == 1;
	}
	
	public boolean getTone2Select() {
		return addressMap[0x1C] == 1;
	}
	
	public boolean getTone3Switch() {
		return addressMap[0x1D] == 1;
	}
	
	public boolean getTone3Select() {
		return addressMap[0x1E] == 1;
	}
	
	public SyncRingSelect getSyncRingSelect() {
		return SyncRingSelect.values()[addressMap[0x1F]];
	}
	
	public boolean getEffectsMasterSwitch() {
		return addressMap[0x20] == 1;
	}
	
	public Value getReserved3() {
		return new Value(addressMap[0x21], 0, 3);
	}
	
	public boolean getDelayTempoSyncSwitch() {
		return addressMap[0x22] == 1;
	}
	
	public boolean getLowBoostSwitch() {
		return addressMap[0x23] == 1;
	}
	
	public DBeamAssign getDBeamAssign() {
		return DBeamAssign.values()[addressMap[0x24]];
	}
	
	public Value getReserved4() {
		return new Value(addressMap[0x25], 0, 1);
	}
	
	public Value getReserved5() {
		return new Value(addressMap[0x26], 0, 1);
	}
	
	public Value getReserved6() {
		return new Value(addressMap[0x27], 0, 1);
	}
	
	public Value getReserved7() {
		return new Value(addressMap[0x28], 0, 1);
	}
	
	public DBeamPolarity getDBeamPolarity() {
		return DBeamPolarity.values()[addressMap[0x29]];
	}
	
	public boolean getEffectsDistortionSelect() {
		return addressMap[0x2A] == 1;
	}
	
	public boolean getEffectsFlangerSelect() {
		return addressMap[0x2B] == 1;
	}
	
	public boolean getEffectsDelaySelect() {
		return addressMap[0x2C] == 1;
	}
	
	public boolean getEffectsReverbSelect() {
		return addressMap[0x2D] == 1;
	}
	
	public Value getReserved8() {
		return new Value(addressMap[0x2E], 0, 1);
	}
	
	public Value getReserved9() {
		return new Value(addressMap[0x2F], 0, 1);
	}
	
	public Value getReserved10() {
		return new Value(addressMap[0x30], 0, 1);
	}
	
	public Value getReserved11() {
		return new Value(addressMap[0x31], 0, 1);
	}
	
	public Value getReserved12() {
		return new Value(addressMap[0x32], 0, 1);
	}
	
	public Value getReserved13() {
		return new Value(addressMap[0x33], 0, 1);
	}
	
	public Value getReserved14() {
		return new Value(addressMap[0x34], 0, 127);
	}
	
	public Value getReserved15() {
		return new Value(addressMap[0x35], 0, 127);
	}
	
	public Value getReserved16() {
		return new Value(addressMap[0x36], 0, 127);
	}
	
	public Value getReserved17() {
		return new Value(addressMap[0x37], 0, 127);
	}
	
	public Value getReserved18() {
		return new Value(addressMap[0x38], 0, 127);
	}
	
	public Value getReserved19() {
		return new Value(addressMap[0x39], 0, 127);
	}
	
	public Value getReserved20() {
		return new Value(addressMap[0x3A] - 64, -63, 63);
	}
	
	public Value getReserved21() {
		return new Value(addressMap[0x3B] - 64, -63, 63);
	}
	
	public Value getReserved22() {
		return new Value(addressMap[0x3C] - 64, -63, 63);
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
