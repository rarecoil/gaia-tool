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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * Class that can load a patch from a specified file.
 */
public class PatchLoader {
	
	File patchFile;
	Patch patch;
	Log log;
	
	final static Charset UTF8 = Charset.forName("UTF-8");
	
	public PatchLoader(File patchFile, Patch patch, Log log) {
		this.patchFile = patchFile;
		this.patch = patch;
		this.log = log;
	}
	
	/**
	 * Loads a patch file into the given patch.
	 * @param patchFile
	 * @param patch
	 */
	public void loadPatch() {
		log.log("Loading " + patchFile);
		
		FileInputStream fis;
		try {
			fis = new FileInputStream(patchFile);
			byte[] header = new byte[8];
			if (fis.read(header) == -1) {
				throw new RuntimeException("Read error: Unexpected end of file.");
			}
			if (!Arrays.equals(header, "GAIATOOL".getBytes(UTF8))) {
				throw new RuntimeException("Read error: Fingerprint mismatch.");
			}
			
			byte[] chunk = new byte[8];
			while (fis.read(chunk) != -1) {
				int length = (chunk[4] & 0xFF) | (chunk[5] & 0xFF) << 8 |
						(chunk[6] & 0xFF) << 16 | (chunk[7] & 0xFF) << 24;
				if (chunk[0] == 'P' && chunk[1] == 'A' && chunk[2] == 'T') {
					byte[] data = new byte[length];
					if (fis.read(data) == -1) {
						throw new RuntimeException("Read error: Unexpected end of file.");
					}
					Address address = new Address(0x10, 0x00, chunk[3], 0x00);
					patch.updateParameters(address, data);
				} else {
					if (fis.skip(length) != length) {
						throw new RuntimeException("Read error: Unexpected end of file.");
					}
				}
			}
			fis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
