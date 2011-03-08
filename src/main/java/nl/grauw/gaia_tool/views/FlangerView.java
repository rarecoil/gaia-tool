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
import nl.grauw.gaia_tool.parameters.Flanger;
import nl.grauw.gaia_tool.parameters.Flanger.FlangerType;

public class FlangerView extends SingleParametersView {

	private static final long serialVersionUID = 1L;

	private Patch patch;
	
	public FlangerView(Patch patch) {
		this.patch = patch;
		patch.addObserver(this);
		initComponents();
	}

	@Override
	public Parameters getParameters() {
		return patch.getFlanger();
	}

	@Override
	public Gaia getGaia() {
		return patch.getGaia();
	}
	
	@Override
	public void loadParameters() {
		patch.loadFlanger();
	}

	@Override
	public void saveParameters() {
	}

	@Override
	public String getTitle() {
		return "Patch flanger";
	}
	
	@Override
	protected boolean isSyncShown() {
		return patch instanceof TemporaryPatch;
	}

	@Override
	protected String getParametersText() {
		Flanger f = patch.getFlanger();
		
		StringBuilder flangerParameters = new StringBuilder(128);
		for (int i = 1; i <= 20; i++) {
			flangerParameters.append(f.getFlangerParameter(i));
			flangerParameters.append(" ");
		}
		
		StringBuilder text = new StringBuilder();
		text.append(String.format("Flanger type: %s\n", f.getFlangerType()));
		if (f.getFlangerType() == FlangerType.FLANGER) {
			text.append(String.format("Feedback: %s\n", f.getFeedback()));
		}
		if (f.getFlangerType() == FlangerType.PHASER) {
			text.append(String.format("Resonance: %s\n", f.getResonance()));
		}
		if (f.getFlangerType() == FlangerType.FLANGER || f.getFlangerType() == FlangerType.PHASER) {
			text.append(String.format("Rate: %s\n", f.getRate()));
			text.append(String.format("Depth: %s\n", f.getDepth()));
		}
		if (f.getFlangerType() == FlangerType.PITCH_SHIFTER) {
			text.append(String.format("Pitch: %s semitones\n", f.getPitch()));
			text.append(String.format("Detune: %s cent\n", f.getDetune()));
		}
		if (f.getFlangerType() != FlangerType.OFF) {
			text.append(String.format("Level: %s\n", f.getLevel()));
		}
		text.append(String.format("\nFlanger parameters: %s\n", flangerParameters));
		
		return text.toString();
	}

}
