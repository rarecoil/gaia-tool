package nl.grauw.gaia_tool.parameters;

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
	
	public int getPatchLevel() {
		return addressMap[0x0D];
	}
	
	public int getPatchTempo() {
		return addressMap[0x0D] << 8 |
				addressMap[0x0E] << 4 |
				addressMap[0x0F];
	}
	
	public boolean getArpeggioSwitch() {
		return addressMap[0x10] == 1;
	}
	
	public int getReserved1() {
		return addressMap[0x11];
	}
	
	public boolean getPortamentoSwitch() {
		return addressMap[0x12] == 1;
	}
	
	public int getPortamentoTime() {
		return addressMap[0x13];
	}
	
	public boolean getMonoSwitch() {
		return addressMap[0x14] == 1;
	}
	
	// -3 ... 3
	public int getOctaveShift() {
		return addressMap[0x15] - 64;
	}
	
	public int getPitchBendRangeUp() {
		return addressMap[0x16];
	}
	
	public int getPitchBendRangeDown() {
		return addressMap[0x17];
	}
	
	public int getReserved2() {
		return addressMap[0x18];
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
	
	public int getReserved3() {
		return addressMap[0x21];
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
	
	public int getReserved4() {
		return addressMap[0x25];
	}
	
	public int getReserved5() {
		return addressMap[0x26];
	}
	
	public int getReserved6() {
		return addressMap[0x27];
	}
	
	public int getReserved7() {
		return addressMap[0x28];
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
	
	public int getReserved8() {
		return addressMap[0x2E];
	}
	
	public int getReserved9() {
		return addressMap[0x2F];
	}
	
	public int getReserved10() {
		return addressMap[0x30];
	}
	
	public int getReserved11() {
		return addressMap[0x31];
	}
	
	public int getReserved12() {
		return addressMap[0x32];
	}
	
	public int getReserved13() {
		return addressMap[0x33];
	}
	
	public int getReserved14() {
		return addressMap[0x34];
	}
	
	public int getReserved15() {
		return addressMap[0x35];
	}
	
	public int getReserved16() {
		return addressMap[0x36];
	}
	
	public int getReserved17() {
		return addressMap[0x37];
	}
	
	public int getReserved18() {
		return addressMap[0x38];
	}
	
	public int getReserved19() {
		return addressMap[0x39];
	}
	
	public int getReserved20() {
		return addressMap[0x3A] - 64;
	}
	
	public int getReserved21() {
		return addressMap[0x3B] - 64;
	}
	
	public int getReserved22() {
		return addressMap[0x3C] - 64;
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
