package nl.grauw.gaia_tool.parameters;

public class PatchToneParameters {
	
	private byte[] addressMap;	// XXX: make AddressMap type
	
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
	
	public PatchToneParameters(byte[] addressMap) {
		if (addressMap.length < 0x3E)
			throw new RuntimeException("Address map size mismatch.");
		
		this.addressMap = addressMap;
	}
	
	public OSCWave getOSCWave() {
		return OSCWave.values()[addressMap[0x00]];
	}
	
	public OSCWaveVariation getOSCWaveVariation() {
		return OSCWaveVariation.values()[addressMap[0x01]];
	}
	
	public boolean getReserved1() {
		return addressMap[0x02] == 1;
	}
	
	public int getOSCPitch() {
		return addressMap[0x03] - 64;
	}
	
	public int getOSCDetune() {
		return addressMap[0x04] - 64;
	}
	
	public int getOSCPulseWidthModDepth() {
		return addressMap[0x05];
	}
	
	public int getOSCPulseWidth() {
		return addressMap[0x06];
	}
	
	public int getOSCPitchEnvAttackTime() {
		return addressMap[0x07];
	}
	
	// XXX: ...DecayTime? Slightly inconsistent...
	public int getOSCPitchEnvDecay() {
		return addressMap[0x08];
	}
	
	public int getOSCPitchEnvDepth() {
		return addressMap[0x09] - 64;
	}
	
	public FilterMode getFilterMode() {
		return FilterMode.values()[addressMap[0x0A]];
	}
	
	public FilterSlope getFilterSlope() {
		return FilterSlope.values()[addressMap[0x0B]];
	}
	
	public int getFilterCutoff() {
		return addressMap[0x0C];
	}
	
	// -10 ... 10 (-100 ... 100)
	public int getFilterCutoffKeyfollow() {
		return addressMap[0x0D] - 64;
	}
	
	public int getFilterEnvVelocitySens() {
		return addressMap[0x0E] - 64;
	}
	
	public int getFilterResonance() {
		return addressMap[0x09];
	}
	
	public int getFilterEnvAttackTime() {
		return addressMap[0x10];
	}
	
	public int getFilterEnvDecayTime() {
		return addressMap[0x11];
	}
	
	public int getFilterEnvSustainLevel() {
		return addressMap[0x12];
	}
	
	public int getFilterEnvReleaseTime() {
		return addressMap[0x13];
	}
	
	public int getFilterEnvDepth() {
		return addressMap[0x14] - 64;
	}
	
	public int getAmpLevel() {
		return addressMap[0x15];
	}
	
	public int getAmpLevelVelocitySens() {
		return addressMap[0x16] - 64;
	}
	
	public int getAmpEnvAttackTime() {
		return addressMap[0x17];
	}
	
	public int getAmpEnvDecayTime() {
		return addressMap[0x18];
	}
	
	public int getAmpEnvSustainLevel() {
		return addressMap[0x19];
	}
	
	public int getAmpEnvReleaseTime() {
		return addressMap[0x1A];
	}
	
	// -64 ... 63 (L64 ... 63R)
	public int getAmpPan() {
		return addressMap[0x1B] - 64;
	}
	
	public LFOShape getLFOShape() {
		return LFOShape.values()[addressMap[0x1C]];
	}
	
	public int getLFORate() {
		return addressMap[0x1D];
	}
	
	public boolean getLFOTempoSyncSwitch() {
		return addressMap[0x1E] == 1;
	}
	
	public LFOTempoSyncNote getLFOTempoSyncNote() {
		return LFOTempoSyncNote.values()[addressMap[0x1F]];
	}
	
	public int getLFOFadeTime() {
		return addressMap[0x20];
	}
	
	public boolean getLFOKeyTrigger() {
		return addressMap[0x21] == 1;
	}
	
	public int getLFOPitchDepth() {
		return addressMap[0x22] - 64;
	}
	
	public int getLFOFilterDepth() {
		return addressMap[0x23] - 64;
	}
	
	public int getLFOAmpDepth() {
		return addressMap[0x24] - 64;
	}
	
	public int getLFOPanDepth() {
		return addressMap[0x25] - 64;
	}
	
	public LFOShape getModulationLFOShape() {
		return LFOShape.values()[addressMap[0x26]];
	}
	
	public int getModulationLFORate() {
		return addressMap[0x27];
	}
	
	public boolean getModulationLFOTempoSyncSwitch() {
		return addressMap[0x28] == 1;
	}
	
	public LFOTempoSyncNote getModulationLFOTempoSyncNote() {
		return LFOTempoSyncNote.values()[addressMap[0x29]];
	}
	
	public int getReserved2() {
		return addressMap[0x2A];
	}
	
	public boolean getReserved3() {
		return addressMap[0x2B] == 1;
	}
	
	public int getModulationLFOPitchDepth() {
		return addressMap[0x2C] - 64;
	}
	
	public int getModulationLFOFilterDepth() {
		return addressMap[0x2D] - 64;
	}
	
	public int getModulationLFOAmpDepth() {
		return addressMap[0x2E] - 64;
	}
	
	public int getModulationLFOPanDepth() {
		return addressMap[0x2F] - 64;
	}
	
	public int getReserved4() {
		return addressMap[0x30] - 64;
	}
	
	public int getReserved5() {
		return addressMap[0x31] - 64;
	}
	
	public int getReserved6() {
		return addressMap[0x32] - 64;
	}
	
	public int getReserved7() {
		return addressMap[0x33] - 64;
	}
	
	public int getReserved8() {
		return addressMap[0x34];
	}
	
	public int getReserved9() {
		return addressMap[0x35];
	}
	
	public int getReserved10() {
		return addressMap[0x36];
	}
	
	public int getReserved11() {
		return addressMap[0x37];
	}
	
	public int getReserved12() {
		return addressMap[0x38];
	}
	
	public int getReserved13() {
		return addressMap[0x39];
	}
	
	public int getReserved14() {
		return addressMap[0x3A];
	}
	
	public int getReserved15() {
		return addressMap[0x3B];
	}
	
	public int getReserved16() {
		return addressMap[0x3C] - 64;
	}
	
	public int getReserved17() {
		return addressMap[0x3D] - 64;
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
				String.format("Filter cutoff keyfollow: %s\n", getFilterCutoffKeyfollow()) +
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
