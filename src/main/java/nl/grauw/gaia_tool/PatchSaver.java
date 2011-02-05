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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * Class that can save a patch to a specified file.
 * 
 * File format:
 * 
 * 0x00: "GAIATOOL" fingerprint
 * 0x08: 0...n Chunks
 * 
 * Chunks:
 * 
 * 0x00: Chunk name
 * 0x04: Length (little endian)
 * 0x08: Data (length bytes)
 */
public class PatchSaver {
	
	Gaia gaia;
	final static Charset UTF8 = Charset.forName("UTF-8");
	
	public PatchSaver(Gaia gaia) {
		this.gaia = gaia;
	}
	
	/**
	 * Saves a patch to the specified file.
	 * @param patchFile
	 * @param patch
	 */
	public void savePatch(File patchFile, Patch patch) {
		if (!patch.isComplete()) {
			throw new RuntimeException("Can not save, because not all patch parameters are loaded.");
		}
		
		FileOutputStream fos;
		try {
			patchFile.createNewFile();
			fos = new FileOutputStream(patchFile);
			fos.write("GAIATOOL".getBytes(UTF8));
			writeParameterData(fos, patch.getCommon());
			writeParameterData(fos, patch.getTone(1));
			writeParameterData(fos, patch.getTone(2));
			writeParameterData(fos, patch.getTone(3));
			writeParameterData(fos, patch.getDistortion());
			writeParameterData(fos, patch.getFlanger());
			writeParameterData(fos, patch.getDelay());
			writeParameterData(fos, patch.getReverb());
			writeParameterData(fos, patch.getArpeggioCommon());
			for (int note = 1; note <= 16; note++) {
				writeParameterData(fos, patch.getArpeggioPattern(note));
			}
			fos.close();
			gaia.getLog().log("Patch data saved to " + patchFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Writes a parameter chunk to the output stream.
	 * Chunk name: 'P' + address bytes 2, 3 and 4
	 * 
	 * @param os
	 * @param p
	 * @throws IOException
	 */
	private void writeParameterData(OutputStream os, Parameters p) throws IOException {
		os.write('P');
		os.write(p.getAddress().getByte2());
		os.write(p.getAddress().getByte3());
		os.write(p.getAddress().getByte4());
		os.write(p.getLength() & 0x7F);
		os.write(p.getLength() >> 8 & 0x7F);
		os.write(p.getLength() >> 16 & 0x7F);
		os.write(p.getLength() >> 24 & 0x7F);
		os.write(p.getData());
	}
	
}
