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

import nl.grauw.gaia_tool.Parameters.ParameterChange;
import nl.grauw.gaia_tool.messages.ControlChangeMessage;
import nl.grauw.gaia_tool.mvc.Observable;
import nl.grauw.gaia_tool.mvc.Observer;
import nl.grauw.gaia_tool.parameters.ArpeggioCommon;
import nl.grauw.gaia_tool.parameters.ArpeggioPattern;
import nl.grauw.gaia_tool.parameters.PatchCommon;
import nl.grauw.gaia_tool.parameters.Delay;
import nl.grauw.gaia_tool.parameters.Distortion;
import nl.grauw.gaia_tool.parameters.Flanger;
import nl.grauw.gaia_tool.parameters.Reverb;
import nl.grauw.gaia_tool.parameters.Tone;

public class Patch extends Observable implements Observer {
	
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
	
	public Patch(Gaia gaia) {
		this(gaia, -1, -1);
	}
	
	public Patch(Gaia gaia, int bank, int patch) {
		if (bank < -1 || bank > 7)
			throw new IllegalArgumentException("Invalid bank number.");
		if (patch < -1 || patch > 7)
			throw new IllegalArgumentException("Invalid patch number.");
		if (bank == -1 && patch != -1)
			throw new IllegalArgumentException("Bank and patch number must both be -1.");
		this.gaia = gaia;
		this.bank = bank;
		this.patch = patch;
	}
	
	public Gaia getGaia() {
		return gaia;
	}
	
	public int getBank() {
		return bank;
	}
	
	public int getPatch() {
		return patch;
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
	
	public boolean isComplete() {
		if (common == null || tones[0] == null || tones[1] == null || tones[2] == null ||
				distortion == null || flanger == null || delay == null || reverb == null ||
				arpeggioCommon == null)
			return false;
		for (int i = 0; i < 16; i++) {
			if (arpeggioPatterns[i] == null)
				return false;
		}
		return true;
	}
	
	private void loadData(Address address, int length) {
		if (!address.equals(lastRequestAddress)) {
			gaia.sendDataRequest(address, length);
		}
	}
	
	private void saveData(Parameters parameters) {
		gaia.sendDataTransmission(parameters);
	}
	
	public void updateParameters(Address address, byte[] data) {
		Parameters p = null;
		byte byte3 = address.getByte3();
		byte byte4 = address.getByte4();
		if (byte3 == 0x00) {
			if (byte4 == 0x00 && data.length >= 0x3D) {
				p = common = new PatchCommon(address, data);
				notifyObservers("common");
			} else if (common != null) {
				common.updateParameters(address, data);
			}
		} else if (byte3 == 0x01 || byte3 == 0x02 || byte3 == 0x03) {
			if (byte4 == 0x00 && data.length >= 0x3E) {
				p = tones[byte3 - 0x01] = new Tone(address, data);
				notifyObservers("tones");
			} else if (tones[byte3 - 0x01] != null) {
				tones[byte3 - 0x01].updateParameters(address, data);
			}
		} else if (byte3 == 0x04 || byte3 == 0x05) {
			if (byte3 == 0x04 && byte4 == 0x00 && data.length >= 0x81) {
				p = distortion = new Distortion(address, data);
				notifyObservers("distortion");
			} else if (distortion != null) {
				distortion.updateParameters(address, data);
			}
		} else if (byte3 == 0x06) {
			if (byte4 == 0x00 && data.length >= 0x51) {
				p = flanger = new Flanger(address, data);
				notifyObservers("flanger");
			} else if (flanger != null) {
				flanger.updateParameters(address, data);
			}
		} else if (byte3 == 0x08) {
			if (byte4 == 0x00 && data.length >= 0x51) {
				p = delay = new Delay(address, data);
				notifyObservers("delay");
			} else if (delay != null) {
				delay.updateParameters(address, data);
			}
		} else if (byte3 == 0x0A) {
			if (byte4 == 0x00 && data.length >= 0x51) {
				p = reverb = new Reverb(address, data);
				notifyObservers("reverb");
			} else if (reverb != null) {
				reverb.updateParameters(address, data);
			}
		} else if (byte3 == 0x0C) {
			if (byte4 == 0x00 && data.length >= 0x08) {
				p = arpeggioCommon = new ArpeggioCommon(address, data);
				notifyObservers("arpeggioCommon");
			} else if (arpeggioCommon != null) {
				arpeggioCommon.updateParameters(address, data);
			}
		} else if (byte3 >= 0x0D && byte3 <= 0x1C) {
			if (byte4 == 0x00 && data.length >= 0x42) {
				p = arpeggioPatterns[byte3 - 0x0D] = new ArpeggioPattern(address, data);
				notifyObservers("arpeggioPatterns");
			} else if (arpeggioPatterns[byte3 - 0x0D] != null) {
				arpeggioPatterns[byte3 - 0x0D].updateParameters(address, data);
			}
		} else {
			throw new IllegalArgumentException("Address not recognised.");
		}
		if (p != null && !p.hasObserver(this))
			p.addObserver(this);
	}
	
	public void updateParameters(ControlChangeMessage message) {
		if (message.getController() != null) {
			Parameters currentEffect = null;
			switch (message.getController()) {
			case DISTORTION_CONTROL_1:
			case DISTORTION_LEVEL:
				if (distortion != null) {
					distortion.updateParameters(message);
				}
				break;
			case FLANGER_CONTROL_1:
			case FLANGER_LEVEL:
				if (flanger != null) {
					flanger.updateParameters(message);
				}
				break;
			case DELAY_CONTROL_1:
			case DELAY_LEVEL:
				currentEffect = delay;
				break;
			case REVERB_CONTROL_1:
			case REVERB_LEVEL:
				if (reverb != null) {
					reverb.updateParameters(message);
				}
				break;
			}
			if (currentEffect != null) {
				loadData(currentEffect.getAddress().add(0x01), 0x08);
			}
		}
	}
	
	@Override
	public void update(Observable source, Object arg) {
		if (source instanceof Parameters && arg instanceof ParameterChange) {
			update((Parameters) source, (ParameterChange) arg);
		}
	}
	
	private void update(Parameters source, ParameterChange arg) {
		if (gaia.getSynchronize() && !arg.fromUpdate()) {
			Address address = source.getAddress().add(arg.getOffset());
			byte[] data = source.getData(arg.getOffset(), arg.getLength());
			saveData(new Parameters(address, data));
		}
	}
	
	public PatchCommon getCommon() {
		return common;
	}
	
	public void loadCommon() {
		loadData(getAddress(0x00), 0x3D);
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
	
	public void saveArpeggioAll() {
		if (arpeggioCommon != null)
			saveData(arpeggioCommon);
		for (ArpeggioPattern pattern : arpeggioPatterns) {
			if (pattern != null)
				saveData(pattern);
		}
	}
	
}
