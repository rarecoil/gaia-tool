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
import nl.grauw.gaia_tool.mvc.Observable;
import nl.grauw.gaia_tool.mvc.Observer;
import nl.grauw.gaia_tool.parameters.ArpeggioCommon;
import nl.grauw.gaia_tool.parameters.ArpeggioPattern;
import nl.grauw.gaia_tool.parameters.Delay;
import nl.grauw.gaia_tool.parameters.Distortion;
import nl.grauw.gaia_tool.parameters.Flanger;
import nl.grauw.gaia_tool.parameters.PatchCommon;
import nl.grauw.gaia_tool.parameters.Reverb;
import nl.grauw.gaia_tool.parameters.Tone;

public class TemporaryPatch extends Patch implements Observer {
	
	public TemporaryPatch(Gaia gaia) {
		super(gaia);
	}
	
	@Override
	public Address getAddress(int byte3) {
		return new Address(0x10, 0x00, byte3, 0x00);
	}
	
	@Override
	public void update(Observable source, Object detail) {
		if (source instanceof Parameters && detail instanceof ParameterChange) {
			update((Parameters) source, (ParameterChange) detail);
		}
	}
	
	private void update(Parameters source, ParameterChange arg) {
		if (!arg.fromUpdate()) {
			if (getGaia().getSynchronize()) {
				Address address = source.getAddress().add(arg.getOffset());
				byte[] data = source.getData(arg.getOffset(), arg.getLength());
				saveData(new Parameters(address, data));
			}
			// reload effect parameters when effect type changes
			if (source == getDistortion() && arg.getOffset() == 0x00 && arg.getLength() < 0x11) {
				loadData(getDistortion().getAddress().add(0x01), 0x10);
			}
			if (source == getFlanger() && arg.getOffset() == 0x00 && arg.getLength() < 0x11) {
				loadData(getFlanger().getAddress().add(0x01), 0x10);
			}
			if (source == getDelay() && arg.getOffset() == 0x00 && arg.getLength() < 0x15) {
				loadData(getDelay().getAddress().add(0x01), 0x14);
			}
			if (source == getReverb() && arg.getOffset() == 0x00 && arg.getLength() < 0x10) {
				loadData(getReverb().getAddress().add(0x01), 0x10);
			}
		}
	}
	
	@Override
	protected void setCommon(PatchCommon common) {
		common.addObserver(this);
		super.setCommon(common);
	}
	
	@Override
	protected void setTone(int number, Tone tone) {
		tone.addObserver(this);
		super.setTone(number, tone);
	}
	
	@Override
	protected void setDistortion(Distortion distortion) {
		distortion.addObserver(this);
		super.setDistortion(distortion);
	}
	
	@Override
	protected void setFlanger(Flanger flanger) {
		flanger.addObserver(this);
		super.setFlanger(flanger);
	}
	
	@Override
	protected void setDelay(Delay delay) {
		delay.addObserver(this);
		super.setDelay(delay);
	}
	
	@Override
	protected void setReverb(Reverb reverb) {
		reverb.addObserver(this);
		super.setReverb(reverb);
	}
	
	@Override
	protected void setArpeggioCommon(ArpeggioCommon arpeggioCommon) {
		arpeggioCommon.addObserver(this);
		super.setArpeggioCommon(arpeggioCommon);
	}
	
	@Override
	protected void setArpeggioPattern(int note, ArpeggioPattern arpeggioPattern) {
		arpeggioPattern.addObserver(this);
		super.setArpeggioPattern(note, arpeggioPattern);
	}
	
}
