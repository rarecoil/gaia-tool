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
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import nl.grauw.gaia_tool.Address.AddressException;

/**
 * Class that can load a patch from a specified file.
 */
public class SVDPatchLoader {
	
	private SVDPatchGroup patchGroup;
	
	public SVDPatchLoader(SVDPatchGroup patchGroup) {
		this.patchGroup = patchGroup;
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
			loadHeader(input);
			
			for (int bank = 0; bank < 8; bank++) {
				for (int patch = 0; patch < 8; patch++) {
					loadPatch(input, patchGroup.getPatch(bank, patch));
				}
			}
		} finally {
			input.close();
		}
	}
	
	private void loadHeader(DataInputStream input) throws IOException {
		byte[] header = new byte[0x30];
		input.readFully(header);
		if (!Arrays.equals(header, headerTemplate))
			throw new IOException("Unrecognised header.");
	}
	
	private void loadPatch(DataInputStream input, Patch patch) throws IOException {
		try {
			byte[] patchData = new byte[0x388];
			input.readFully(patchData);
			
			patch.updateParameters(new Address(0x10, 0x00, 0x00, 0x00), unpackCommon(patchData));
			for (int tone = 1; tone <= 3; tone++)
				patch.updateParameters(new Address(0x10, 0x00, 0x01 + tone - 1, 0x00), unpackTone(patchData, tone));
			patch.updateParameters(new Address(0x10, 0x00, 0x04, 0x00), unpackDistortion(patchData));
			patch.updateParameters(new Address(0x10, 0x00, 0x06, 0x00), unpackFlanger(patchData));
			patch.updateParameters(new Address(0x10, 0x00, 0x08, 0x00), unpackDelay(patchData));
			patch.updateParameters(new Address(0x10, 0x00, 0x0A, 0x00), unpackReverb(patchData));
			patch.updateParameters(new Address(0x10, 0x00, 0x0C, 0x00), unpackArpeggioCommon(patchData));
			for (int note = 1; note <= 16; note++)
				patch.updateParameters(new Address(0x10, 0x00, 0x0D + note - 1, 0x00), unpackArpeggioPattern(patchData, note));
		} catch (AddressException e) {
			throw new RuntimeException(e);
		}
	}
	
	public byte[] unpackCommon(byte[] packed) {
		byte[] unpacked = unpack(packed, 0x00 * 8, commonBits);
		unpacked[0x15] = (byte)(60 + unpacked[0x15]);  // stored 3-bit, communicated 7-bit (sign extended)
		return unpacked;
	}
	
	public byte[] unpackTone(byte[] packed, int tone) {
		if (tone < 1 || tone > 3)
			throw new RuntimeException("Tone number out of bounds.");
		byte[] unpacked = unpack(packed, (0x1E + 0x2C * (tone - 1)) * 8, toneBits);
		unpacked[0x3] = (byte)(32 + unpacked[0x3]);  // stored 6-bit, communicated 7-bit (sign extended)
		unpacked[0xD] = (byte)(32 + unpacked[0xD]);  // stored 6-bit, communicated 7-bit (sign extended)
		return unpacked;
	}
	
	public byte[] unpackDistortion(byte[] packed) {
		return unpack(packed, 0xA2 * 8, distortionBits);
	}
	
	public byte[] unpackFlanger(byte[] packed) {
		return unpack(packed, 0xE4 * 8, flangerDelayReverbBits);
	}
	
	public byte[] unpackDelay(byte[] packed) {
		return unpack(packed, 0x10E * 8, flangerDelayReverbBits);
	}
	
	public byte[] unpackReverb(byte[] packed) {
		return unpack(packed, 0x138 * 8, flangerDelayReverbBits);
	}
	
	public byte[] unpackArpeggioCommon(byte[] packed) {
		byte[] unpacked = unpack(packed, 0x162 * 8, arpeggioCommonBits);
		unpacked[0x3] = (byte)(60 + unpacked[0x3]);  // stored 3-bit, communicated 7-bit (sign extended)
		return unpacked;
	}
	
	public byte[] unpackArpeggioPattern(byte[] packed, int note) {
		if (note < 1 || note > 16)
			throw new RuntimeException("Note number out of bounds.");
		return unpack(packed, (0x168 + 0x22 * (note - 1)) * 8, arpeggioPatternBits);
	}
	
	public byte[] unpack(byte[] packed, int offset, int[] bits) {
		byte[] expanded = new byte[bits.length];
		
		for (int i = 0; i < bits.length; i++) {
			int length = bits[i];
			int nextOffset = offset + length;
			int unprocessedValue = packed[offset / 8] << 8 | (packed[nextOffset / 8] & 0xFF);
			expanded[i] = (byte)(unprocessedValue >> (8 - (nextOffset % 8)) & bitMasks[length]);
			offset = nextOffset;
		}
		
		return expanded;
	}
	
	/*
	 * 0x30-byte header dump
	 * =====================
	 * 
	 * 00: 001e 5356 4431 0000 0000 0000 0000 0000  ..SVD1..........
	 *          ^^^^^^^^^                             ^^^^
	 * 10: 5041 5461 5348 3031 0000 0020 0000 e210  PATaSH01... ....
	 *     ^^^^^^^^^ ^^^^^^^^^                      ^^^^^^^^
	 * 20: 0000 0040 0000 0388 0000 0010 0000 0000  ...@............
	 *            ^^(1)   ^^^^(2)
	 *
	 * (1) the file contains 64 patches
	 * (2) 388 bytes per patch
	 */
	private static final byte[] headerTemplate = {
		0x00, 0x1E, 0x53, 0x56, 0x44, 0x31, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
		0x50, 0x41, 0x54, 0x61, 0x53, 0x48, 0x30, 0x31, 0x00, 0x00, 0x00, 0x20, 0x00, 0x00, (byte)0xE2, 0x10,
		0x00, 0x00, 0x00, 0x40, 0x00, 0x00, 0x03, (byte)0x88, 0x00, 0x00, 0x00, 0x10, 0x00, 0x00, 0x00, 0x00
	};
	
	private static final int[] bitMasks = { 0x00, 0x01, 0x03, 0x07, 0x0F, 0x1F, 0x3F, 0x7F, 0xFF };
	
	private static final int[] commonBits = {
		7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 4, 4, 4,  // 10
		1, 1, 1, 7, 2, 3, 5, 5, 3, 1, 1, 1, 1, 1, 1, 2,  // 20
		1, 2, 1, 1, 6, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,  // 30
		1, 1, 1, 1, 7, 7, 7, 7, 7, 7, 7, 7, 7            // 3D
	};
	
	private static final int[] toneBits = {
		3, 6, 2, 6, 7, 7, 7, 7, 7, 7, 3, 1, 7, 6, 7, 7,  // 10
		7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 3, 7, 1, 5,  // 20
		7, 1, 7, 7, 7, 7, 3, 7, 1, 5, 7, 1, 7, 7, 7, 7,  // 30
		7, 7, 7, 7, 1, 1, 1, 1, 7, 7, 7, 7, 7, 7         // 3E
	};
	
	private static final int[] distortionBits = {
		7, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4,  //  10
		4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4,  //  20
		4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4,  //  30
		4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4,  //  40
		4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4,  //  50
		4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4,  //  60
		4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4,  //  70
		4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4,  // 100
		4                                                // 101
	};
	
	private static final int[] flangerDelayReverbBits = {
		7, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4,  // 10
		4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4,  // 20
		4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4,  // 30
		4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4,  // 40
		4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4,  // 50
		4                                                // 51
	};
	
	private static final int[] arpeggioCommonBits = {
		7, 7, 7, 3, 7, 7, 4, 4                           // 08
	};
	
	private static final int[] arpeggioPatternBits = {
		4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4,  // 10
		4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4,  // 20
		4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4,  // 30
		4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4,  // 40
		4, 4                                             // 42
	};
	
}
