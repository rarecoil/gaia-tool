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
package nl.grauw.gaia_tool.views;

import nl.grauw.gaia_tool.Gaia;
import nl.grauw.gaia_tool.Parameters;
import nl.grauw.gaia_tool.Patch;
import nl.grauw.gaia_tool.TemporaryPatch;
import nl.grauw.gaia_tool.parameters.Tone;

public class ToneView extends SingleParametersView {
	
	private static final long serialVersionUID = 1L;
	
	private Patch patch;
	int toneNumber;
	
	public ToneView(Patch patch, int toneNumber) {
		this.patch = patch;
		patch.addObserver(this);
		this.toneNumber = toneNumber;
		initComponents();
	}
	
	@Override
	public Parameters getParameters() {
		return patch.getTone(toneNumber);
	}

	@Override
	public Gaia getGaia() {
		return patch.getGaia();
	}
	
	@Override
	public void loadParameters() {
		patch.loadTone(toneNumber);
	}

	@Override
	public void saveParameters() {
	}

	@Override
	public String getTitle() {
		return "Patch tone " + toneNumber;
	}
	
	@Override
	protected boolean isSyncShown() {
		return patch instanceof TemporaryPatch;
	}

	@Override
	protected String getParametersText() {
		Tone t = patch.getTone(toneNumber);
		
		return String.format("OSC wave: %s\n", t.getOSCWave()) +
				String.format("OSC wave variation: %s\n", t.getOSCWaveVariation()) +
				String.format("OSC pitch: %s\n", t.getOSCPitch()) +
				String.format("OSC detune: %s\n", t.getOSCDetune()) +
				String.format("OSC pulse width mod depth: %s\n", t.getOSCPulseWidthModDepth()) +
				String.format("OSC pulse width: %s\n", t.getOSCPulseWidth()) +
				String.format("OSC pitch env attack time: %s\n", t.getOSCPitchEnvAttackTime()) +
				String.format("OSC pitch env decay: %s\n", t.getOSCPitchEnvDecay()) +
				String.format("OSC pitch env depth: %s\n", t.getOSCPitchEnvDepth()) +
				String.format("Filter mode: %s\n", t.getFilterMode()) +
				String.format("Filter slope: %s\n", t.getFilterSlope()) +
				String.format("Filter cutoff: %s\n", t.getFilterCutoff()) +
				String.format("Filter cutoff keyfollow: %s\n", t.getFilterCutoffKeyfollow().getValue() * 10) +
				String.format("Filter env velocity sens: %s\n", t.getFilterEnvVelocitySens()) +
				String.format("Filter resonance: %s\n", t.getFilterResonance()) +
				String.format("Filter env attack time: %s\n", t.getFilterEnvAttackTime()) +
				String.format("Filter env decay time: %s\n", t.getFilterEnvDecayTime()) +
				String.format("Filter env sustain level: %s\n", t.getFilterEnvSustainLevel()) +
				String.format("Filter env release time: %s\n", t.getFilterEnvReleaseTime()) +
				String.format("Filter env depth: %s\n", t.getFilterEnvDepth()) +
				String.format("Amp level: %s\n", t.getAmpLevel()) +
				String.format("Amp level velocity sens: %s\n", t.getAmpLevelVelocitySens()) +
				String.format("Amp env attack time: %s\n", t.getAmpEnvAttackTime()) +
				String.format("Amp env decay time: %s\n", t.getAmpEnvDecayTime()) +
				String.format("Amp env sustain level: %s\n", t.getAmpEnvSustainLevel()) +
				String.format("Amp env release time: %s\n", t.getAmpEnvReleaseTime()) +
				String.format("Amp pan: %s\n", t.getAmpPan()) +
				String.format("LFO shape: %s\n", t.getLFOShape()) +
				String.format("LFO rate: %s\n", t.getLFORate()) +
				String.format("LFO tempo sync switch: %s\n", t.getLFOTempoSyncSwitch()) +
				String.format("LFO tempo sync note: %s\n", t.getLFOTempoSyncNote()) +
				String.format("LFO fade time: %s\n", t.getLFOFadeTime()) +
				String.format("LFO key trigger: %s\n", t.getLFOKeyTrigger()) +
				String.format("LFO pitch depth: %s\n", t.getLFOPitchDepth()) +
				String.format("LFO filter depth: %s\n", t.getLFOFilterDepth()) +
				String.format("LFO amp depth: %s\n", t.getLFOAmpDepth()) +
				String.format("LFO pan depth: %s\n", t.getLFOPanDepth()) +
				String.format("Modulation LFO shape: %s\n", t.getModulationLFOShape()) +
				String.format("Modulation LFO rate: %s\n", t.getModulationLFORate()) +
				String.format("Modulation LFO tempo sync switch: %s\n", t.getModulationLFOTempoSyncSwitch()) +
				String.format("Modulation LFO tempo sync note: %s\n", t.getModulationLFOTempoSyncNote()) +
				String.format("Modulation LFO pitch depth: %s\n", t.getModulationLFOPitchDepth()) +
				String.format("Modulation LFO filter depth: %s\n", t.getModulationLFOFilterDepth()) +
				String.format("Modulation LFO amp depth: %s\n", t.getModulationLFOAmpDepth()) +
				String.format("Modulation LFO pan depth: %s\n", t.getModulationLFOPanDepth());
	}

}
