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
package nl.grauw.gaia_tool;

import javax.sound.midi.InvalidMidiDataException;

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
	
	private Gaia gaia;
	private int bank;
	private int patch;
	
	private Address lastRequestAddress;
	
	private PatchCommonParameters common;
	private PatchToneParameters[] tones = new PatchToneParameters[3];
	private PatchDistortionParameters distortion;
	private PatchFlangerParameters flanger;
	private PatchDelayParameters delay;
	private PatchReverbParameters reverb;
	private PatchArpeggioCommonParameters arpeggioCommon;
	private PatchArpeggioPatternParameters[] arpeggioPatterns = new PatchArpeggioPatternParameters[16];
	
	public PatchParameterGroup(Gaia gaia) {
		this(gaia, -1, -1);
	}
	
	public PatchParameterGroup(Gaia gaia, int bank, int patch) {
		if (bank < -1 || bank > 7)
			throw new RuntimeException("Invalid bank number.");
		if (patch < -1 || patch > 7)
			throw new RuntimeException("Invalid patch number.");
		if (bank == -1 && patch != -1)
			throw new RuntimeException("Bank and patch number must both be -1.");
		this.gaia = gaia;
		this.bank = bank;
		this.patch = patch;
	}
	
	public Address getAddress() {
		return getAddress(0);
	}
	
	public Address getAddress(int byte3) {
		if (this.bank == -1) {
			return new Address(0x10, 0x00, byte3, 0x00);
		} else {
			return new Address(0x20, bank << 3 | patch, byte3, 0x00);
		}
	}
	
	public void requestMissingData(Address address, int length) {
		if (!address.equals(lastRequestAddress)) {
			try {
				gaia.sendDataRequest(address, length);
			} catch(InvalidMidiDataException e) {
				e.printStackTrace();
			}
		}
	}
	
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
		if (common == null)
			requestMissingData(getAddress(0x00), 0x6E);
		return common;
	}
	
	/**
	 * Return one of the patch’s tone parameters.
	 * @param number The tone number (1 - 3).
	 * @return The patch tone parameters.
	 */
	public PatchToneParameters getTone(int number) {
		if (tones[number - 1] == null)
			requestMissingData(getAddress(0x01 + number - 1), 0x3E);
		return tones[number - 1];
	}
	
	public PatchDistortionParameters getDistortion() {
		if (distortion == null)
			requestMissingData(getAddress(0x04), 0x81);
		return distortion;
	}
	
	public PatchFlangerParameters getFlanger() {
		if (flanger == null)
			requestMissingData(getAddress(0x06), 0x51);
		return flanger;
	}
	
	public PatchDelayParameters getDelay() {
		if (delay == null)
			requestMissingData(getAddress(0x08), 0x51);
		return delay;
	}
	
	public PatchReverbParameters getReverb() {
		if (reverb == null)
			requestMissingData(getAddress(0x0A), 0x51);
		return reverb;
	}
	
	public PatchArpeggioCommonParameters getArpeggioCommon() {
		if (arpeggioCommon == null)
			requestMissingData(getAddress(0x0C), 0x08);
		return arpeggioCommon;
	}
	
	/**
	 * Return one of the patch’s arpeggio pattern parameters.
	 * @param note The arpeggio pattern note (1 - 16).
	 * @return The patch arpeggio pattern parameters.
	 */
	public PatchArpeggioPatternParameters getArpeggioPattern(int note) {
		if (arpeggioPatterns[note - 1] == null)
			requestMissingData(getAddress(0x0D + note - 1), 0x42);
		return arpeggioPatterns[note - 1];
	}
	
}
