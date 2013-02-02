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
package nl.grauw.gaia;

import java.io.IOException;

public class SVDPatch extends Patch {
	
	private SVDPatchGroup patchGroup;
	private int bank;
	private int patch;
	
	public SVDPatch(SVDPatchGroup patchGroup, int bank, int patch) {
		if (bank < 0 || bank > 7)
			throw new IllegalArgumentException("Invalid bank number.");
		if (patch < 0 || patch > 7)
			throw new IllegalArgumentException("Invalid patch number.");
		
		this.patchGroup = patchGroup;
		this.bank = bank;
		this.patch = patch;
	}
	
	public int getBank() {
		return bank;
	}
	
	public int getPatch() {
		return patch;
	}
	
	public void load() throws IOException {
		new SVDPatchLoader(patchGroup).load(patchGroup.getSource(), this);
	}
	
}
