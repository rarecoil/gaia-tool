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
import nl.grauw.gaia_tool.parameters.Distortion;
import nl.grauw.gaia_tool.parameters.Distortion.DistortionType;

public class DistortionView extends SingleParametersView {

	private static final long serialVersionUID = 1L;

	private Patch patch;
	
	public DistortionView(Patch patch) {
		this.patch = patch;
		patch.addObserver(this);
		initComponents();
	}

	@Override
	public Parameters getParameters() {
		return patch.getDistortion();
	}

	@Override
	public Gaia getGaia() {
		return patch.getGaia();
	}
	
	@Override
	public void loadParameters() {
		patch.loadDistortion();
	}

	@Override
	public void saveParameters() {
	}

	@Override
	public String getTitle() {
		return "Patch distortion";
	}
	
	@Override
	protected boolean isSyncShown() {
		return patch instanceof TemporaryPatch;
	}

	@Override
	protected String getParametersText() {
		Distortion d = patch.getDistortion();
		
		StringBuilder mfxParameters = new StringBuilder(128);
		for (int i = 1; i <= 32; i++) {
			mfxParameters.append(d.getMFXParameter(i));
			mfxParameters.append(" ");
		}
		
		StringBuilder text = new StringBuilder();
		text.append(String.format("Distortion type: %s\n", d.getDistortionType()));
		if (d.getDistortionType() == DistortionType.DIST || d.getDistortionType() == DistortionType.FUZZ) {
			text.append(String.format("Drive: %s\n", d.getDrive()));
			text.append(String.format("Type: %s\n", d.getType()));
			text.append(String.format("Presence: %s\n", d.getPresence()));
		}
		if (d.getDistortionType() == DistortionType.BIT_CRASH) {
			text.append(String.format("Sample rate: %s\n", d.getSampleRate()));
			text.append(String.format("Bit down: %s\n", d.getBitDown()));
			text.append(String.format("Filter: %s\n", d.getFilter()));
		}
		if (d.getDistortionType() != DistortionType.OFF) {
			text.append(String.format("Level: %s\n", d.getLevel()));
		}
		text.append(String.format("\nMFX parameters: %s\n", mfxParameters));
		
		return text.toString();
	}

}
