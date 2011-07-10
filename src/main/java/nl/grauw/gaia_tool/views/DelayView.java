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
import nl.grauw.gaia_tool.Patch;
import nl.grauw.gaia_tool.TemporaryPatch;
import nl.grauw.gaia_tool.parameters.Delay;
import nl.grauw.gaia_tool.parameters.Delay.DelayType;

public class DelayView extends SingleParametersView {

	private static final long serialVersionUID = 1L;
	
	private Patch patch;
	
	public DelayView(Patch patch) {
		this.patch = patch;
		patch.addObserver(this);
		initComponents();
	}

	@Override
	public Parameters getParameters() {
		return patch.getDelay();
	}

	@Override
	public Gaia getGaia() {
		if (patch instanceof GaiaPatch)
			return ((GaiaPatch)patch).getGaia();
		return null;
	}
	
	@Override
	public void loadParameters() {
		if (patch instanceof GaiaPatch) {
			((GaiaPatch)patch).loadDelay();
		}
	}

	@Override
	public void saveParameters() {
		if (patch instanceof GaiaPatch) {
			((GaiaPatch)patch).saveModifiedParameters();
		}
	}

	@Override
	public String getTitle() {
		return "Patch delay";
	}
	
	@Override
	protected boolean isSyncShown() {
		return patch instanceof TemporaryPatch;
	}

	@Override
	protected String getParametersText() {
		Delay d = patch.getDelay();
		
		StringBuilder delayParameters = new StringBuilder(128);
		for (int i = 1; i <= 20; i++) {
			delayParameters.append(d.getDelayParameter(i));
			delayParameters.append(" ");
		}
		
		StringBuilder text = new StringBuilder();
		text.append(String.format("Delay type: %s\n", d.getDelayType()));
		if (d.getDelayType() != DelayType.OFF) {
			text.append(String.format("Time: %s\n", d.getTime()));
			text.append(String.format("Synced time: %s\n", d.getSyncedTime()));
			text.append(String.format("Feedback: %s\n", d.getFeedback()));
			text.append(String.format("High damp: %s dB\n", d.getHighDamp()));
			text.append(String.format("Level: %s\n", d.getLevel()));
		}
		text.append(String.format("\nDelay parameters: %s\n", delayParameters));
		
		return text.toString();
	}

}
