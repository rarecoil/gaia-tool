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

import nl.grauw.gaia_tool.Parameters;
import nl.grauw.gaia_tool.Patch;
import nl.grauw.gaia_tool.parameters.Delay;
import nl.grauw.gaia_tool.parameters.Delay.DelayType;

public class DelayView extends SingleParametersPanel {

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
