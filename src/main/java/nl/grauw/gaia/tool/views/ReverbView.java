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
package nl.grauw.gaia.tool.views;

import nl.grauw.gaia.Parameters;
import nl.grauw.gaia.Patch;
import nl.grauw.gaia.parameters.Reverb;
import nl.grauw.gaia.parameters.Reverb.ReverbType;

public class ReverbView extends SingleParametersPanel {

	private static final long serialVersionUID = 1L;

	private Patch patch;
	
	public ReverbView(Patch patch) {
		this.patch = patch;
		patch.addObserver(this);
		initComponents();
	}

	@Override
	public Parameters getParameters() {
		return patch.getReverb();
	}
	
	@Override
	protected String getParametersText() {
		Reverb r = patch.getReverb();
		
		StringBuilder reverbParameters = new StringBuilder(128);
		for (int i = 1; i <= 20; i++) {
			reverbParameters.append(r.getReverbParameter(i));
			reverbParameters.append(" ");
		}
		
		StringBuilder text = new StringBuilder();
		text.append(String.format("Reverb type: %s\n", r.getReverbType()));
		if (r.getReverbType() != ReverbType.OFF) {
			text.append(String.format("Time: %s\n", r.getTime()));
			text.append(String.format("Type: %s\n", (new String[] {"Room", "Plate", "Hall"})[r.getType().getValue()]));
			text.append(String.format("High damp: %.1f%%\n", r.getHighDamp().getValue() / 127.0 * 100));
			text.append(String.format("Level: %s\n", r.getLevel()));
		}
		text.append(String.format("\nReverb parameters: %s\n", reverbParameters));
		
		return text.toString();
	}

}
