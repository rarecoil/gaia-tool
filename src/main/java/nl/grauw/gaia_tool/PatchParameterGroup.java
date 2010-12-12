package nl.grauw.gaia_tool;

import nl.grauw.gaia_tool.mvc.Observable;
import nl.grauw.gaia_tool.parameters.Parameters;
import nl.grauw.gaia_tool.parameters.PatchArpeggioCommonParameters;
import nl.grauw.gaia_tool.parameters.PatchArpeggioPatternParameters;
import nl.grauw.gaia_tool.parameters.PatchCommonParameters;
import nl.grauw.gaia_tool.parameters.PatchDelayParameters;
import nl.grauw.gaia_tool.parameters.PatchDistortionParameters;
import nl.grauw.gaia_tool.parameters.PatchFlangerParameters;
import nl.grauw.gaia_tool.parameters.PatchReverbParameters;
import nl.grauw.gaia_tool.parameters.PatchToneParameters;

public class PatchParameterGroup extends Observable {
	
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
			common = new PatchCommonParameters(data);
			notifyObservers("common");
			return common;
		} else if (byte3 == 0x01 || byte3 == 0x02 || byte3 == 0x03) {
			tones[byte3 - 0x01] = new PatchToneParameters(data);
			notifyObservers("tones");
			return tones[byte3 - 0x01];
		} else if (byte3 == 0x04) {
			distortion = new PatchDistortionParameters(data);
			notifyObservers("distortion");
			return distortion;
		} else if (byte3 == 0x06) {
			flanger = new PatchFlangerParameters(data);
			notifyObservers("flanger");
			return flanger;
		} else if (byte3 == 0x08) {
			delay = new PatchDelayParameters(data);
			notifyObservers("delay");
			return delay;
		} else if (byte3 == 0x0A) {
			reverb = new PatchReverbParameters(data);
			notifyObservers("reverb");
			return reverb;
		} else if (byte3 == 0x0C) {
			arpeggioCommon = new PatchArpeggioCommonParameters(data);
			notifyObservers("arpeggioCommon");
			return arpeggioCommon;
		} else if (byte3 >= 0x0D && byte3 <= 0x1C) {
			arpeggioPatterns[byte3 - 0x0D] = new PatchArpeggioPatternParameters(data);
			notifyObservers("arpeggioPatterns");
			return arpeggioPatterns[byte3 - 0x0D];
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
