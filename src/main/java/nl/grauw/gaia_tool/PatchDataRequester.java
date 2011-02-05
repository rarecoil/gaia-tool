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
package nl.grauw.gaia_tool;

import nl.grauw.gaia_tool.mvc.Observable;
import nl.grauw.gaia_tool.mvc.Observer;

/**
 * Utility class that requests all missing data in a patch.
 */
public class PatchDataRequester implements Observer {
	
	interface PatchCompleteListener {
		
		/**
		 * Invoked when the patch has loaded completely.
		 * @param source The observed object.
		 * @param detail Object providing details on the state change.
		 */
		public void patchComplete(Patch patch);
		
	}
	
	Patch patch;
	PatchCompleteListener listener;
	
	public PatchDataRequester(Patch patch, PatchCompleteListener listener) {
		this.patch = patch;
		this.listener = listener;
	}
	
	/**
	 * Checks whether patch parameters data is missing, and requests the missing
	 * data from the GAIA if that’s the case.
	 * 
	 * @return True if the patch has loaded completely.
	 */
	public void requestMissingParameters() {
		if (!patch.hasObserver(this)) {
			patch.addObserver(this);
		}
		if (patch.getCommon() == null) {
			patch.loadCommon();
		} else if (patch.getTone(1) == null) {
			patch.loadTone(1);
		} else if (patch.getTone(2) == null) {
			patch.loadTone(2);
		} else if (patch.getTone(3) == null) {
			patch.loadTone(3);
		} else if (patch.getDistortion() == null) {
			patch.loadDistortion();
		} else if (patch.getFlanger() == null) {
			patch.loadFlanger();
		} else if (patch.getDelay() == null) {
			patch.loadDelay();
		} else if (patch.getReverb() == null) {
			patch.loadReverb();
		} else if (patch.getArpeggioCommon() == null) {
			patch.loadArpeggioCommon();
		} else {
			for (int note = 1; note <= 16; note++) {
				if (patch.getArpeggioPattern(note) == null) {
					patch.loadArpeggioPattern(note);
					return;
				}
			}
			// done
			patch.removeObserver(this);
			listener.patchComplete(patch);
		}
	}
	
	@Override
	public void update(Observable source, Object detail) {
		if (source == patch && ("common".equals(detail) || "tones".equals(detail) || "distortion".equals(detail) ||
				"flanger".equals(detail) || "delay".equals(detail) || "reverb".equals(detail) ||
				"arpeggioCommon".equals(detail) || "arpeggioPatterns".equals(detail))) {
			
			requestMissingParameters();
		}
	}
	
}
