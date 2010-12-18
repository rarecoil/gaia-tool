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
import nl.grauw.gaia_tool.parameters.ArpeggioCommon;
import nl.grauw.gaia_tool.parameters.ArpeggioPattern;
import nl.grauw.gaia_tool.parameters.PatchCommon;
import nl.grauw.gaia_tool.parameters.Delay;
import nl.grauw.gaia_tool.parameters.Distortion;
import nl.grauw.gaia_tool.parameters.Flanger;
import nl.grauw.gaia_tool.parameters.Reverb;
import nl.grauw.gaia_tool.parameters.Tone;

public class PatchParameterGroup extends Observable {
	
	private Gaia gaia;
	private int bank;
	private int patch;
	
	private Address lastRequestAddress;
	
	private PatchCommon common;
	private Tone[] tones = new Tone[3];
	private Distortion distortion;
	private Flanger flanger;
	private Delay delay;
	private Reverb reverb;
	private ArpeggioCommon arpeggioCommon;
	private ArpeggioPattern[] arpeggioPatterns = new ArpeggioPattern[16];
	
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
	
	private void loadData(Address address, int length) {
		if (!address.equals(lastRequestAddress)) {
			try {
				gaia.sendDataRequest(address, length);
			} catch(InvalidMidiDataException e) {
				e.printStackTrace();
			}
		}
	}
	
	public Parameters updateParameters(ParameterData parameterData) {
		byte byte3 = parameterData.getAddress().getByte3();
		if (byte3 == 0x00) {
			common = new PatchCommon(parameterData);
			notifyObservers("common");
			return common;
		} else if (byte3 == 0x01 || byte3 == 0x02 || byte3 == 0x03) {
			tones[byte3 - 0x01] = new Tone(parameterData);
			notifyObservers("tones");
			return tones[byte3 - 0x01];
		} else if (byte3 == 0x04) {
			distortion = new Distortion(parameterData);
			notifyObservers("distortion");
			return distortion;
		} else if (byte3 == 0x06) {
			flanger = new Flanger(parameterData);
			notifyObservers("flanger");
			return flanger;
		} else if (byte3 == 0x08) {
			delay = new Delay(parameterData);
			notifyObservers("delay");
			return delay;
		} else if (byte3 == 0x0A) {
			reverb = new Reverb(parameterData);
			notifyObservers("reverb");
			return reverb;
		} else if (byte3 == 0x0C) {
			arpeggioCommon = new ArpeggioCommon(parameterData);
			notifyObservers("arpeggioCommon");
			return arpeggioCommon;
		} else if (byte3 >= 0x0D && byte3 <= 0x1C) {
			arpeggioPatterns[byte3 - 0x0D] = new ArpeggioPattern(parameterData);
			notifyObservers("arpeggioPatterns");
			return arpeggioPatterns[byte3 - 0x0D];
		} else {
			throw new RuntimeException("Address not recognised.");
		}
	}
	
	public PatchCommon getCommon() {
		return common;
	}
	
	public void loadCommon() {
		loadData(getAddress(0x00), 0x6E);
	}
	
	/**
	 * Return one of the patch’s tone parameters.
	 * @param number The tone number (1 - 3).
	 * @return The patch tone parameters.
	 */
	public Tone getTone(int number) {
		return tones[number - 1];
	}
	
	public void loadTone(int number) {
		loadData(getAddress(0x01 + number - 1), 0x3E);
	}
	
	public Distortion getDistortion() {
		return distortion;
	}
	
	public void loadDistortion() {
		loadData(getAddress(0x04), 0x81);
	}
	
	public Flanger getFlanger() {
		return flanger;
	}
	
	public void loadFlanger() {
		loadData(getAddress(0x06), 0x51);
	}
	
	public Delay getDelay() {
		return delay;
	}
	
	public void loadDelay() {
		loadData(getAddress(0x08), 0x51);
	}
	
	public Reverb getReverb() {
		return reverb;
	}
	
	public void loadReverb() {
		loadData(getAddress(0x0A), 0x51);
	}
	
	public ArpeggioCommon getArpeggioCommon() {
		return arpeggioCommon;
	}
	
	public void loadArpeggioCommon() {
		loadData(getAddress(0x0C), 0x08);
	}
	
	/**
	 * Return one of the patch’s arpeggio pattern parameters.
	 * @param note The arpeggio pattern note (1 - 16).
	 * @return The patch arpeggio pattern parameters.
	 */
	public ArpeggioPattern getArpeggioPattern(int note) {
		return arpeggioPatterns[note - 1];
	}
	
	public void loadArpeggioPattern(int note) {
		loadData(getAddress(0x0D + note - 1), 0x42);
	}
	
	public void loadArpeggioAll() {
		loadData(getAddress(0x0C), 0xB80);
	}
	
}
