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
package nl.grauw.gaia_tool.parameters;

import nl.grauw.gaia_tool.Address;
import nl.grauw.gaia_tool.Parameters;
import nl.grauw.gaia_tool.SignedValue;
import nl.grauw.gaia_tool.Value;
import nl.grauw.gaia_tool.Value8Bit;

public class ArpeggioCommon extends Parameters {
	
	public enum ArpeggioGrid {
		_04_, _08_, _08L, _08H, _08t, _16_, _16L, _16H, _16t;
		
		public String toString() {
			return super.toString().substring(1);
		}
	}
	
	public enum ArpeggioDuration {
		_30, _40, _50, _60, _70, _80, _90, _100, _120, FUL;
		
		public String toString() {
			if (this == FUL)
				return "FUL";
			return super.toString().substring(1);
		}
	}
	
	public enum ArpeggioMotif {
		UP_L, UP_L_AND_H, UP__, DOWN_L, DOWN_L_AND_H, DOWN__,
		UP_AND_DOWN_L, UP_AND_DOWN_L_AND_H, UP_AND_DOWN__, RANDOM_L, RANDOM__, PHRASE
	}
	
	public ArpeggioCommon(Address address, byte[] data) {
		super(address, data);
		
		if (data.length < 0x08)
			throw new IllegalArgumentException("Parameters data size mismatch.");
	}
	
	public ArpeggioGrid getArpeggioGrid() {
		return ArpeggioGrid.values()[getValue(0x00)];
	}
	
	public ArpeggioDuration getArpeggioDuration() {
		return ArpeggioDuration.values()[getValue(0x01)];
	}
	
	public ArpeggioMotif getArpeggioMotif() {
		return ArpeggioMotif.values()[getValue(0x02)];
	}
	
	public Value getArpeggioOctaveRange() {
		return new SignedValue(this, 0x03, -3, +3);
	}
	
	public Value getArpeggioAccentRate() {
		return new Value(this, 0x04, 0, 100);
	}
	
	// 0 ... 127 (REAL, 1 ... 127)
	public Value getArpeggioVelocity() {
		return new Value(this, 0x05, 0, 127);
	}
	
	public Value getEndStep() {
		return new Value8Bit(this, 0x06, 1, 32);
	}
	
	public String toString() {
		return "Patch arpeggio common parameters:\n" +
				String.format("Arpeggio grid: %s\n", getArpeggioGrid()) +
				String.format("Arpeggio duration: %s\n", getArpeggioDuration()) +
				String.format("Arpeggio motif: %s\n", getArpeggioMotif()) +
				String.format("Arpeggio octave range: %s\n", getArpeggioOctaveRange()) +
				String.format("Arpeggio accent rate: %s\n", getArpeggioAccentRate()) +
				String.format("Arpeggio velocity: %s\n", getArpeggioVelocity()) +
				String.format("End step: %s\n", getEndStep());
	}
	
}
