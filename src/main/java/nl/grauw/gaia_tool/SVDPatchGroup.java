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

import java.io.File;
import java.io.IOException;

/**
 * SVD is the file format used by the GAIA when saving to USB memory.
 * An SVD file contains 64 patches, divided into 8 banks of 8 patches.
 */
public class SVDPatchGroup {
	
	private File source;
	private SVDPatch[][] patches = new SVDPatch[8][8];
	
	public SVDPatchGroup(File source) {
		if (!source.getName().endsWith(".SVD"))
			throw new RuntimeException("Not an SVD file");
		
		this.source = source;
		for (int bank = 0; bank < 8; bank++) {
			for (int patch = 0; patch < 8; patch++) {
				patches[bank][patch] = new SVDPatch(bank, patch);
			}
		}
	}
	
	public File getSource() {
		return source;
	}
	
	public String getName() {
		return source.getName().replaceFirst("\\.[^.]+$", "");
	}
	
	public SVDPatch getPatch(int bank, int patch) {
		if (bank < 0 || bank > 7)
			throw new IllegalArgumentException("Invalid bank number.");
		if (patch < 0 || patch > 7)
			throw new IllegalArgumentException("Invalid patch number.");
		return patches[bank][patch];
	}
	
	public void load() throws IOException {
		new SVDPatchLoader(this).load(source);
	}
	
}
