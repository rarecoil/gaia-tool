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
		return patch.getGaia();
	}
	
	@Override
	public void loadParameters() {
		patch.loadDelay();
	}

	@Override
	public void saveParameters() {
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
		
		return String.format("Delay type: %s\n", d.getDelayType()) +
				(
					d.getDelayType() != DelayType.OFF ?
					String.format("Time: %s\n", d.getTime()) +
					String.format("Synced time: %s\n", d.getSyncedTime()) +
					String.format("Feedback: %s\n", d.getFeedback()) +
					String.format("High damp: %s dB\n", d.getHighDamp()) +
					String.format("Level: %s\n", d.getLevel()) : ""
				) +
				String.format("\nDelay parameters: %s\n", delayParameters);
	}

}
