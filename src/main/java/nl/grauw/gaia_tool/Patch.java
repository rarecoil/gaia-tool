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

import nl.grauw.gaia_tool.messages.ControlChangeMessage;
import nl.grauw.gaia_tool.mvc.Observable;
import nl.grauw.gaia_tool.parameters.ArpeggioCommon;
import nl.grauw.gaia_tool.parameters.ArpeggioPattern;
import nl.grauw.gaia_tool.parameters.PatchCommon;
import nl.grauw.gaia_tool.parameters.Delay;
import nl.grauw.gaia_tool.parameters.Distortion;
import nl.grauw.gaia_tool.parameters.Flanger;
import nl.grauw.gaia_tool.parameters.Reverb;
import nl.grauw.gaia_tool.parameters.Tone;

public abstract class Patch extends Observable {
	
	private Gaia gaia;
	
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
		this.gaia = gaia;
	}
	
	public Gaia getGaia() {
		return gaia;
	}
	
	public Address getAddress() {
		return getAddress(0);
	}
	
	public abstract Address getAddress(int byte3);
	
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
	
	protected void loadData(Address address, int length) {
		if (!address.equals(lastRequestAddress)) {
			gaia.sendDataRequest(address, length);
		}
	}
	
	protected void saveData(Parameters parameters) {
		gaia.sendDataTransmission(parameters);
	}
	
	public void updateParameters(Address address, byte[] data) {
		byte byte3 = address.getByte3();
		byte byte4 = address.getByte4();
		if (byte3 == 0x00) {
			if (byte4 == 0x00 && data.length >= 0x3D) {
				setCommon(new PatchCommon(address, data));
			} else if (common != null) {
				common.updateParameters(address, data);
			}
		} else if (byte3 == 0x01 || byte3 == 0x02 || byte3 == 0x03) {
			if (byte4 == 0x00 && data.length >= 0x3E) {
				setTone(byte3, new Tone(address, data));
			} else if (tones[byte3 - 0x01] != null) {
				tones[byte3 - 0x01].updateParameters(address, data);
			}
		} else if (byte3 == 0x04 || byte3 == 0x05) {
			if (byte3 == 0x04 && byte4 == 0x00 && data.length >= 0x81) {
				setDistortion(new Distortion(address, data));
			} else if (distortion != null) {
				distortion.updateParameters(address, data);
			}
		} else if (byte3 == 0x06) {
			if (byte4 == 0x00 && data.length >= 0x51) {
				setFlanger(new Flanger(address, data));
			} else if (flanger != null) {
				flanger.updateParameters(address, data);
			}
		} else if (byte3 == 0x08) {
			if (byte4 == 0x00 && data.length >= 0x51) {
				setDelay(new Delay(address, data));
			} else if (delay != null) {
				delay.updateParameters(address, data);
			}
		} else if (byte3 == 0x0A) {
			if (byte4 == 0x00 && data.length >= 0x51) {
				setReverb(new Reverb(address, data));
			} else if (reverb != null) {
				reverb.updateParameters(address, data);
			}
		} else if (byte3 == 0x0C) {
			if (byte4 == 0x00 && data.length >= 0x08) {
				setArpeggioCommon(new ArpeggioCommon(address, data));
			} else if (arpeggioCommon != null) {
				arpeggioCommon.updateParameters(address, data);
			}
		} else if (byte3 >= 0x0D && byte3 <= 0x1C) {
			if (byte4 == 0x00 && data.length >= 0x42) {
				setArpeggioPattern(byte3 - 0x0C, new ArpeggioPattern(address, data));
			} else if (arpeggioPatterns[byte3 - 0x0D] != null) {
				arpeggioPatterns[byte3 - 0x0D].updateParameters(address, data);
			}
		} else {
			throw new IllegalArgumentException("Address not recognised.");
		}
	}
	
	public void updateParameters(ControlChangeMessage message) {
		if (message.getController() != null) {
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
				if (delay != null) {
					loadData(delay.getAddress().add(0x01), 0x0C);
				}
				break;
			case REVERB_CONTROL_1:
			case REVERB_LEVEL:
				if (reverb != null) {
					reverb.updateParameters(message);
				}
				break;
			}
		}
	}
	
	public void clearParameters() {
		common = null;
		for (int i = 0; i < 3; i++)
			tones[i] = null;
		distortion = null;
		flanger = null;
		delay = null;
		reverb = null;
		arpeggioCommon = null;
		for (int i = 0; i < 16; i++)
			arpeggioPatterns[i] = null;
		notifyObservers("common");
		notifyObservers("tones");
		notifyObservers("distortion");
		notifyObservers("flanger");
		notifyObservers("delay");
		notifyObservers("reverb");
		notifyObservers("arpeggioCommon");
		notifyObservers("arpeggioPatterns");
	}
	
	public PatchCommon getCommon() {
		return common;
	}

	protected void setCommon(PatchCommon common) {
		this.common = common;
		notifyObservers("common");
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

	protected void setTone(int number, Tone tone) {
		this.tones[number - 1] = tone;
		notifyObservers("tones");
	}
	
	public void loadTone(int number) {
		loadData(getAddress(0x01 + number - 1), 0x3E);
	}
	
	public Distortion getDistortion() {
		return distortion;
	}

	protected void setDistortion(Distortion distortion) {
		this.distortion = distortion;
		notifyObservers("distortion");
	}
	
	public void loadDistortion() {
		loadData(getAddress(0x04), 0x81);
	}
	
	public Flanger getFlanger() {
		return flanger;
	}

	protected void setFlanger(Flanger flanger) {
		this.flanger = flanger;
		notifyObservers("flanger");
	}
	
	public void loadFlanger() {
		loadData(getAddress(0x06), 0x51);
	}
	
	public Delay getDelay() {
		return delay;
	}

	protected void setDelay(Delay delay) {
		this.delay = delay;
		notifyObservers("delay");
	}
	
	public void loadDelay() {
		loadData(getAddress(0x08), 0x51);
	}
	
	public Reverb getReverb() {
		return reverb;
	}

	protected void setReverb(Reverb reverb) {
		this.reverb = reverb;
		notifyObservers("reverb");
	}
	
	public void loadReverb() {
		loadData(getAddress(0x0A), 0x51);
	}
	
	public ArpeggioCommon getArpeggioCommon() {
		return arpeggioCommon;
	}

	protected void setArpeggioCommon(ArpeggioCommon arpeggioCommon) {
		this.arpeggioCommon = arpeggioCommon;
		notifyObservers("arpeggioCommon");
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

	protected void setArpeggioPattern(int note, ArpeggioPattern arpeggioPattern) {
		this.arpeggioPatterns[note - 1] = arpeggioPattern;
		notifyObservers("arpeggioPatterns");
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
