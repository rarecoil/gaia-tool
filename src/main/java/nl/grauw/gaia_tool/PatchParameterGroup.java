package nl.grauw.gaia_tool;

import nl.grauw.gaia_tool.parameters.Parameters;
import nl.grauw.gaia_tool.parameters.PatchArpeggioCommonParameters;
import nl.grauw.gaia_tool.parameters.PatchArpeggioPatternParameters;
import nl.grauw.gaia_tool.parameters.PatchCommonParameters;
import nl.grauw.gaia_tool.parameters.PatchDelayParameters;
import nl.grauw.gaia_tool.parameters.PatchDistortionParameters;
import nl.grauw.gaia_tool.parameters.PatchFlangerParameters;
import nl.grauw.gaia_tool.parameters.PatchReverbParameters;
import nl.grauw.gaia_tool.parameters.PatchToneParameters;

public class PatchParameterGroup {
	
	private PatchCommonParameters common;
	private PatchToneParameters[] tones = new PatchToneParameters[3];
	private PatchDistortionParameters distortion;
	private PatchFlangerParameters flanger;
	private PatchDelayParameters delay;
	private PatchReverbParameters reverb;
	private PatchArpeggioCommonParameters arpeggioCommon;
	private PatchArpeggioPatternParameters[] arpeggioPatterns = new PatchArpeggioPatternParameters[16];
	
	public Parameters updateParameters(Address address, byte[] data) {
		byte byte3 = address.getByte3();
		if (byte3 == 0x00) {
			return common = new PatchCommonParameters(data);
		} else if (byte3 == 0x01 || byte3 == 0x02 || byte3 == 0x03) {
			return tones[byte3 - 0x01] = new PatchToneParameters(data);
		} else if (byte3 == 0x04) {
			return distortion = new PatchDistortionParameters(data);
		} else if (byte3 == 0x06) {
			return flanger = new PatchFlangerParameters(data);
		} else if (byte3 == 0x08) {
			return delay = new PatchDelayParameters(data);
		} else if (byte3 == 0x0A) {
			return reverb = new PatchReverbParameters(data);
		} else if (byte3 == 0x0C) {
			return arpeggioCommon = new PatchArpeggioCommonParameters(data);
		} else if (byte3 >= 0x0D && byte3 <= 0x1C) {
			return arpeggioPatterns[byte3 - 0x0D] = new PatchArpeggioPatternParameters(data);
		} else {
			throw new RuntimeException("Address not recognised.");
		}
	}
	
	public PatchCommonParameters getCommon() {
		return common;
	}
	
	public PatchToneParameters getTone(int number) {
		return tones[number];
	}
	
	public PatchDistortionParameters getDistortion() {
		return distortion;
	}
	
	public PatchFlangerParameters getFlanger() {
		return flanger;
	}
	
	public PatchDelayParameters getDelay() {
		return delay;
	}
	
	public PatchReverbParameters getReverb() {
		return reverb;
	}
	
	public PatchArpeggioCommonParameters getArpeggioCommon() {
		return arpeggioCommon;
	}
	
	public PatchArpeggioPatternParameters getArpeggioPattern(int number) {
		return arpeggioPatterns[number];
	}
	
}
