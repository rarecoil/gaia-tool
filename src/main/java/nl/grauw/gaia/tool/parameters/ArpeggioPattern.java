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
package nl.grauw.gaia.tool.parameters;

import nl.grauw.gaia.tool.Address;
import nl.grauw.gaia.tool.Int8BitValue;
import nl.grauw.gaia.tool.IntValue;
import nl.grauw.gaia.tool.NoteValue;
import nl.grauw.gaia.tool.Parameters;

public class ArpeggioPattern extends Parameters {
	
	public ArpeggioPattern(Address address, byte[] data) {
		super(address, data);
		
		if (address.getByte3() < 0x0D || address.getByte3() > 0x1C)
			throw new Error("Invalid parameters address.");
		if (data.length < 0x42)
			throw new IllegalArgumentException("Parameters data size mismatch.");
	}
	
	public int getNoteNumber() {
		return getAddress().getByte3() - 0x0D + 1;
	}
	
	/**
	 * Get the pattern’s original note (C-4 is the base).
	 * Note number 128 (G#9) means OFF.
	 * @return The pattern original note.
	 */
	public NoteValue getOriginalNote() {
		return new NoteValue(this, 0x00);
	}
	
	public IntValue getStepData(int step) {
		if (step < 1 || step > 32)
			throw new IllegalArgumentException("Invalid step number.");
		
		return new Int8BitValue(this, step * 2, 0, 128);
	}
	
}
