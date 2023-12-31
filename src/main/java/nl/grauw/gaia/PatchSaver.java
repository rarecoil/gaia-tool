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

import java.io.File;
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
 * 
 * Parameter chunk name: 'PAT' + address byte 3
 */
public class PatchSaver {
	
	private Patch patch;
	
	final static private Charset UTF8 = Charset.forName("UTF-8");
	
	public PatchSaver(Patch patch) {
		this.patch = patch;
	}
	
	/**
	 * Saves the patch to a file.
	 * @param output
	 */
	public void save(File output) throws IOException {
		save(new FileOutputStream(output));
	}
	
	/**
	 * Saves the patch to an output stream.
	 * @param output
	 */
	public void save(OutputStream output) throws IOException {
		try {
			output.write("GAIATOOL".getBytes(UTF8));
			for (Parameters parameters : patch) {
				output.write('P');
				output.write('A');
				output.write('T');
				output.write(parameters.getAddress().getByte3());
				output.write(parameters.getLength() & 0xFF);
				output.write(parameters.getLength() >> 8 & 0xFF);
				output.write(parameters.getLength() >> 16 & 0xFF);
				output.write(parameters.getLength() >> 24 & 0xFF);
				output.write(parameters.getData());
			}
		} finally {
			output.close();
		}
	}
	
}
