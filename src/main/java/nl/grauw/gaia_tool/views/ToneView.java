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

}
