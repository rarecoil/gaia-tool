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
package nl.grauw.gaia.tool;

import nl.grauw.gaia.tool.Parameters.ParameterChange;
import nl.grauw.gaia.tool.Parameters.ParameterChangeListener;
import nl.grauw.gaia.tool.messages.ControlChangeMessage;
import nl.grauw.gaia.tool.parameters.ArpeggioCommon;
import nl.grauw.gaia.tool.parameters.ArpeggioPattern;
import nl.grauw.gaia.tool.parameters.Delay;
import nl.grauw.gaia.tool.parameters.Distortion;
import nl.grauw.gaia.tool.parameters.Flanger;
import nl.grauw.gaia.tool.parameters.PatchCommon;
import nl.grauw.gaia.tool.parameters.Reverb;
import nl.grauw.gaia.tool.parameters.Tone;

public class TemporaryPatch extends GaiaPatch implements ParameterChangeListener {
	
	public TemporaryPatch(Gaia gaia) {
		super(gaia);
	}
	
	@Override
	public Address getAddress(int byte3) {
		return new Address(0x10, 0x00, byte3, 0x00);
	}
	
	@Override
	public void parameterChange(Parameters source, ParameterChange change) {
		if (source.hasChanged(change)) {
			getGaia().sendDataTransmission(source, change.getOffset(), change.getLength());
			
			// reload effect parameters when effect type changes
			if (source == getDistortion() && change.getOffset() == 0x00 && change.getLength() < 0x11) {
				loadData(getDistortion().getAddress().add(0x01), 0x10);
			}
			if (source == getFlanger() && change.getOffset() == 0x00 && change.getLength() < 0x11) {
				loadData(getFlanger().getAddress().add(0x01), 0x10);
			}
			if (source == getDelay() && change.getOffset() == 0x00 && change.getLength() < 0x15) {
				loadData(getDelay().getAddress().add(0x01), 0x14);
			}
			if (source == getReverb() && change.getOffset() == 0x00 && change.getLength() < 0x10) {
				loadData(getReverb().getAddress().add(0x01), 0x10);
			}
		}
	}
	
	public void updateParameters(ControlChangeMessage message) {
		if (message.getController() != null) {
			switch (message.getController()) {
			case DISTORTION_CONTROL_1:
			case DISTORTION_LEVEL:
				if (getDistortion() != null) {
					getDistortion().updateParameters(message);
				}
				break;
			case FLANGER_CONTROL_1:
			case FLANGER_LEVEL:
				if (getFlanger() != null) {
					getFlanger().updateParameters(message);
				}
				break;
			case DELAY_CONTROL_1:
				if (getDelay() != null) {
					loadData(getDelay().getAddress().add(0x05), 0x08);
				}
				break;
			case DELAY_LEVEL:
				if (getDelay() != null) {
					getDelay().updateParameters(message);
				}
				break;
			case REVERB_CONTROL_1:
			case REVERB_LEVEL:
				if (getReverb() != null) {
					getReverb().updateParameters(message);
				}
				break;
			case PORTAMENTO_TIME:
				if (getCommon() != null) {
					getCommon().updateParameters(message);
				}
				break;
			case TONE_1_LFO_RATE:
			case TONE_1_LFO_FADE_TIME:
			case TONE_1_LFO_PITCH_DEPTH:
			case TONE_1_LFO_FILTER_DEPTH:
			case TONE_1_LFO_AMP_DEPTH:
			case TONE_1_OSC_PITCH:
			case TONE_1_OSC_DETUNE:
			case TONE_1_OSC_PULSE_WIDTH_MODULATION:
			case TONE_1_OSC_PULSE_WIDTH:
			case TONE_1_OSC_ENV_DEPTH:
			case TONE_1_FILTER_CUTOFF:
			case TONE_1_FILTER_RESONANCE:
			case TONE_1_FILTER_ENV_DEPTH:
			case TONE_1_FILTER_KEY_FOLLOW:
			case TONE_1_AMP_LEVEL:
				if (getTone(1) != null) {
					getTone(1).updateParameters(message);
				}
				break;
			case TONE_2_LFO_RATE:
			case TONE_2_LFO_FADE_TIME:
			case TONE_2_LFO_PITCH_DEPTH:
			case TONE_2_LFO_FILTER_DEPTH:
			case TONE_2_LFO_AMP_DEPTH:
			case TONE_2_OSC_PITCH:
			case TONE_2_OSC_DETUNE:
			case TONE_2_OSC_PULSE_WIDTH_MODULATION:
			case TONE_2_OSC_PULSE_WIDTH:
			case TONE_2_OSC_ENV_DEPTH:
			case TONE_2_FILTER_CUTOFF:
			case TONE_2_FILTER_RESONANCE:
			case TONE_2_FILTER_ENV_DEPTH:
			case TONE_2_FILTER_KEY_FOLLOW:
			case TONE_2_AMP_LEVEL:
				if (getTone(2) != null) {
					getTone(2).updateParameters(message);
				}
				break;
			case TONE_3_LFO_RATE:
			case TONE_3_LFO_FADE_TIME:
			case TONE_3_LFO_PITCH_DEPTH:
			case TONE_3_LFO_FILTER_DEPTH:
			case TONE_3_LFO_AMP_DEPTH:
			case TONE_3_OSC_PITCH:
			case TONE_3_OSC_DETUNE:
			case TONE_3_OSC_PULSE_WIDTH_MODULATION:
			case TONE_3_OSC_PULSE_WIDTH:
			case TONE_3_OSC_ENV_DEPTH:
			case TONE_3_FILTER_CUTOFF:
			case TONE_3_FILTER_RESONANCE:
			case TONE_3_FILTER_ENV_DEPTH:
			case TONE_3_FILTER_KEY_FOLLOW:
			case TONE_3_AMP_LEVEL:
				if (getTone(3) != null) {
					getTone(3).updateParameters(message);
				}
				break;
			}
		}
	}
	
	@Override
	protected void setCommon(PatchCommon common) {
		common.addParameterChangeListener(this);
		super.setCommon(common);
	}
	
	@Override
	protected void setTone(int number, Tone tone) {
		tone.addParameterChangeListener(this);
		super.setTone(number, tone);
	}
	
	@Override
	protected void setDistortion(Distortion distortion) {
		distortion.addParameterChangeListener(this);
		super.setDistortion(distortion);
	}
	
	@Override
	protected void setFlanger(Flanger flanger) {
		flanger.addParameterChangeListener(this);
		super.setFlanger(flanger);
	}
	
	@Override
	protected void setDelay(Delay delay) {
		delay.addParameterChangeListener(this);
		super.setDelay(delay);
	}
	
	@Override
	protected void setReverb(Reverb reverb) {
		reverb.addParameterChangeListener(this);
		super.setReverb(reverb);
	}
	
	@Override
	protected void setArpeggioCommon(ArpeggioCommon arpeggioCommon) {
		arpeggioCommon.addParameterChangeListener(this);
		super.setArpeggioCommon(arpeggioCommon);
	}
	
	@Override
	protected void setArpeggioPattern(int note, ArpeggioPattern arpeggioPattern) {
		arpeggioPattern.addParameterChangeListener(this);
		super.setArpeggioPattern(note, arpeggioPattern);
	}
	
}
