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

import nl.grauw.gaia_tool.mvc.Observable;
import nl.grauw.gaia_tool.parameters.ArpeggioCommon;
import nl.grauw.gaia_tool.parameters.ArpeggioPattern;
import nl.grauw.gaia_tool.parameters.Delay;
import nl.grauw.gaia_tool.parameters.Distortion;
import nl.grauw.gaia_tool.parameters.Flanger;
import nl.grauw.gaia_tool.parameters.PatchCommon;
import nl.grauw.gaia_tool.parameters.Reverb;
import nl.grauw.gaia_tool.parameters.Tone;

public class Patch extends Observable implements Iterable<Parameters> {

	protected PatchCommon common;
	private Tone[] tones = new Tone[3];
	private Distortion distortion;
	private Flanger flanger;
	private Delay delay;
	private Reverb reverb;
	protected ArpeggioCommon arpeggioCommon;
	protected ArpeggioPattern[] arpeggioPatterns = new ArpeggioPattern[16];

	public Patch() {
		super();
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

	public Distortion getDistortion() {
		return distortion;
	}

	protected void setDistortion(Distortion distortion) {
		this.distortion = distortion;
		notifyObservers("distortion");
	}

	public Flanger getFlanger() {
		return flanger;
	}

	protected void setFlanger(Flanger flanger) {
		this.flanger = flanger;
		notifyObservers("flanger");
	}

	public Delay getDelay() {
		return delay;
	}

	protected void setDelay(Delay delay) {
		this.delay = delay;
		notifyObservers("delay");
	}

	public Reverb getReverb() {
		return reverb;
	}

	protected void setReverb(Reverb reverb) {
		this.reverb = reverb;
		notifyObservers("reverb");
	}

	public ArpeggioCommon getArpeggioCommon() {
		return arpeggioCommon;
	}

	protected void setArpeggioCommon(ArpeggioCommon arpeggioCommon) {
		this.arpeggioCommon = arpeggioCommon;
		notifyObservers("arpeggioCommon");
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

	/**
	 * Returns an iterator to loop over all parameters on this patch.
	 */
	@Override
	public Iterator<Parameters> iterator() {
		return new PatchIterator();
	}

	private class PatchIterator implements Iterator<Parameters> {

		int position = 0;

		@Override
		public boolean hasNext() {
			return position < 25;
		}

		@Override
		public Parameters next() {
			if (position >= 25)
				throw new NoSuchElementException();
			
			int pos = position++;
			switch (pos) {
			case 0:
				return common;
			case 1:
			case 2:
			case 3:
				return tones[pos - 1];
			case 4:
				return distortion;
			case 5:
				return flanger;
			case 6:
				return delay;
			case 7:
				return reverb;
			case 8:
				return arpeggioCommon;
			default:
				return arpeggioPatterns[pos - 9];
			}
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	}
	
}
