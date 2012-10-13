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

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;

import nl.grauw.gaia_tool.Address.AddressException;

/**
 * Class that can load a patch from a specified file.
 */
public class PatchLoader {
	
	private Patch patch;
	
	final static private Charset UTF8 = Charset.forName("UTF-8");
	
	public PatchLoader(Patch patch) {
		this.patch = patch;
	}
	
	/**
	 * Populates the patch from a file.
	 */
	public void load(File input) throws IOException {
		load(new FileInputStream(input));
	}
	
	/**
	 * Populates the patch from an input stream.
	 */
	public void load(InputStream input) throws IOException {
		load(new DataInputStream(input));
	}
	
	/**
	 * Populates the patch from a data input stream.
	 */
	public void load(DataInputStream input) throws IOException {
		try {
			byte[] header = new byte[8];
			input.readFully(header);
			if (!Arrays.equals(header, "GAIATOOL".getBytes(UTF8))) {
				throw new IOException("Fingerprint mismatch.");
			}
			
			byte[] chunk = new byte[8];
			while (true) {
				try {
					input.readFully(chunk);
				} catch (EOFException e) {
					if (!patch.isComplete())
						throw new IOException("Incomplete patch.");
					return;
				}
				int length = (chunk[4] & 0xFF) | (chunk[5] & 0xFF) << 8 |
						(chunk[6] & 0xFF) << 16 | (chunk[7] & 0xFF) << 24;
				byte[] data = new byte[length];
				input.readFully(data);
				
				if (chunk[0] == 'P' && chunk[1] == 'A' && chunk[2] == 'T') {
					Address address = new Address(0x10, 0x00, chunk[3], 0x00);
					try {
						patch.updateParameters(address, data);
					} catch (AddressException e) {
						// s’ok, maybe it was saved on a new firmware
					}
				} else {
					// ignore
				}
			}
		} finally {
			input.close();
		}
	}
	
}
