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

import nl.grauw.gaia_tool.PatchDataRequester.PatchCompleteListener;

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
public class PatchSaver implements PatchCompleteListener {
	
	File patchFile;
	Patch patch;
	Log log;
	
	final static Charset UTF8 = Charset.forName("UTF-8");
	
	public PatchSaver(File patchFile, Patch patch, Log log) {
		this.patchFile = patchFile;
		this.patch = patch;
		this.log = log;
	}
	
	/**
	 * Saves the patch.
	 * @param patchFile
	 * @param patch
	 */
	public void save() {
		new PatchDataRequester(patch, this).requestMissingParameters();
		// continues in patchComplete once all patch data is loaded
	}

	@Override
	public void patchComplete(Patch patch) {
		doSave();
	}
	
	private void doSave() {
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
			log.log("Patch data saved to " + patchFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Writes a parameter chunk to the output stream.
	 * Chunk name: 'PAT' + address byte 3
	 * 
	 * @param os
	 * @param p
	 * @throws IOException
	 */
	private void writeParameterData(OutputStream os, Parameters p) throws IOException {
		os.write('P');
		os.write('A');
		os.write('T');
		os.write(p.getAddress().getByte3());
		os.write(p.getLength() & 0xFF);
		os.write(p.getLength() >> 8 & 0xFF);
		os.write(p.getLength() >> 16 & 0xFF);
		os.write(p.getLength() >> 24 & 0xFF);
		os.write(p.getData());
	}
	
}
