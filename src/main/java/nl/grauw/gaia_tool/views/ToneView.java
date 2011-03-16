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
import nl.grauw.gaia_tool.GaiaPatch;
import nl.grauw.gaia_tool.TemporaryPatch;
import nl.grauw.gaia_tool.parameters.Tone;

public class ToneView extends SingleParametersView {
	
	private static final long serialVersionUID = 1L;
	
	private GaiaPatch patch;
	int toneNumber;
	
	public ToneView(GaiaPatch patch, int toneNumber) {
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
		patch.saveParameters();
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
		
		StringBuffer text = new StringBuffer();
		text.append(String.format("OSC wave: %s\n", t.getOSCWave()));
		text.append(String.format("OSC wave variation: %s\n", t.getOSCWaveVariation()));
		text.append(String.format("OSC pitch: %s\n", t.getOSCPitch()));
		text.append(String.format("OSC detune: %s\n", t.getOSCDetune()));
		text.append(String.format("OSC pulse width mod depth: %s\n", t.getOSCPulseWidthModDepth()));
		text.append(String.format("OSC pulse width: %s\n", t.getOSCPulseWidth()));
		text.append(String.format("OSC pitch env attack time: %s\n", t.getOSCPitchEnvAttackTime()));
		text.append(String.format("OSC pitch env decay: %s\n", t.getOSCPitchEnvDecay()));
		text.append(String.format("OSC pitch env depth: %s\n", t.getOSCPitchEnvDepth()));
		text.append(String.format("Filter mode: %s\n", t.getFilterMode()));
		text.append(String.format("Filter slope: %s\n", t.getFilterSlope()));
		text.append(String.format("Filter cutoff: %s\n", t.getFilterCutoff()));
		text.append(String.format("Filter cutoff keyfollow: %s\n", t.getFilterCutoffKeyfollow().getValue() * 10));
		text.append(String.format("Filter env velocity sens: %s\n", t.getFilterEnvVelocitySens()));
		text.append(String.format("Filter resonance: %s\n", t.getFilterResonance()));
		text.append(String.format("Filter env attack time: %s\n", t.getFilterEnvAttackTime()));
		text.append(String.format("Filter env decay time: %s\n", t.getFilterEnvDecayTime()));
		text.append(String.format("Filter env sustain level: %s\n", t.getFilterEnvSustainLevel()));
		text.append(String.format("Filter env release time: %s\n", t.getFilterEnvReleaseTime()));
		text.append(String.format("Filter env depth: %s\n", t.getFilterEnvDepth()));
		text.append(String.format("Amp level: %s\n", t.getAmpLevel()));
		text.append(String.format("Amp level velocity sens: %s\n", t.getAmpLevelVelocitySens()));
		text.append(String.format("Amp env attack time: %s\n", t.getAmpEnvAttackTime()));
		text.append(String.format("Amp env decay time: %s\n", t.getAmpEnvDecayTime()));
		text.append(String.format("Amp env sustain level: %s\n", t.getAmpEnvSustainLevel()));
		text.append(String.format("Amp env release time: %s\n", t.getAmpEnvReleaseTime()));
		text.append(String.format("Amp pan: %s\n", t.getAmpPan()));
		text.append(String.format("LFO shape: %s\n", t.getLFOShape()));
		text.append(String.format("LFO rate: %s\n", t.getLFORate()));
		text.append(String.format("LFO tempo sync switch: %s\n", t.getLFOTempoSyncSwitch()));
		text.append(String.format("LFO tempo sync note: %s\n", t.getLFOTempoSyncNote()));
		text.append(String.format("LFO fade time: %s\n", t.getLFOFadeTime()));
		text.append(String.format("LFO key trigger: %s\n", t.getLFOKeyTrigger()));
		text.append(String.format("LFO pitch depth: %s\n", t.getLFOPitchDepth()));
		text.append(String.format("LFO filter depth: %s\n", t.getLFOFilterDepth()));
		text.append(String.format("LFO amp depth: %s\n", t.getLFOAmpDepth()));
		text.append(String.format("LFO pan depth: %s\n", t.getLFOPanDepth()));
		text.append(String.format("Modulation LFO shape: %s\n", t.getModulationLFOShape()));
		text.append(String.format("Modulation LFO rate: %s\n", t.getModulationLFORate()));
		text.append(String.format("Modulation LFO tempo sync switch: %s\n", t.getModulationLFOTempoSyncSwitch()));
		text.append(String.format("Modulation LFO tempo sync note: %s\n", t.getModulationLFOTempoSyncNote()));
		text.append(String.format("Modulation LFO pitch depth: %s\n", t.getModulationLFOPitchDepth()));
		text.append(String.format("Modulation LFO filter depth: %s\n", t.getModulationLFOFilterDepth()));
		text.append(String.format("Modulation LFO amp depth: %s\n", t.getModulationLFOAmpDepth()));
		text.append(String.format("Modulation LFO pan depth: %s\n", t.getModulationLFOPanDepth()));
		
//		text.append(String.format("\nReserved 1: %s\n", t.getReserved1()));
//		text.append(String.format("Reserved 2: %s\n", t.getReserved2()));
//		text.append(String.format("Reserved 3: %s\n", t.getReserved3()));
//		text.append(String.format("Reserved 4: %s\n", t.getReserved4()));
//		text.append(String.format("Reserved 5: %s\n", t.getReserved5()));
//		text.append(String.format("Reserved 6: %s\n", t.getReserved6()));
//		text.append(String.format("Reserved 7: %s\n", t.getReserved7()));
//		text.append(String.format("Reserved 8: %s\n", t.getReserved8()));
//		text.append(String.format("Reserved 9: %s\n", t.getReserved9()));
//		text.append(String.format("Reserved 10: %s\n", t.getReserved10()));
//		text.append(String.format("Reserved 11: %s\n", t.getReserved11()));
//		text.append(String.format("Reserved 12: %s\n", t.getReserved12()));
//		text.append(String.format("Reserved 13: %s\n", t.getReserved13()));
//		text.append(String.format("Reserved 14: %s\n", t.getReserved14()));
//		text.append(String.format("Reserved 15: %s\n", t.getReserved15()));
//		text.append(String.format("Reserved 16: %s\n", t.getReserved16()));
//		text.append(String.format("Reserved 17: %s\n", t.getReserved17()));
		
		return text.toString();
	}

}
