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
import nl.grauw.gaia_tool.EnumValue;
import nl.grauw.gaia_tool.Parameters;
import nl.grauw.gaia_tool.SignedIntValue;
import nl.grauw.gaia_tool.IntValue;
import nl.grauw.gaia_tool.Int8BitValue;

public class ArpeggioCommon extends Parameters {
	
	public enum ArpeggioGrid {
		_04_("Quarter"), _08_("Eigth"), _08L("Eigth light shuffle"), _08H("Eigth heavy shuffle"), _08t("Eigth triplet"),
		_16_("Sixteenth"), _16L("Sixteenth light shuffle"), _16H("Sixteenth heavy shuffle"), _16t("Sixteenth triplet");
		
		String label;
		
		private ArpeggioGrid(String label) {
			this.label = label;
		}
		
		public String toString() {
			return label;
		}
	}
	
	public enum ArpeggioDuration {
		_30("30%"), _40("40%"), _50("50%"), _60("60%"), _70("70%"), _80("80%"), _90("90%"),
		_100("100%"), _120("120%"), FUL("Full");
		
		String label;
		
		private ArpeggioDuration(String label) {
			this.label = label;
		}
		
		public String toString() {
			return label;
		}
	}
	
	public enum ArpeggioMotif {
		UP_L("Up (L)"), UP_L_AND_H("Up (L&H)"), UP__("Up"), DOWN_L("Down (L)"), DOWN_L_AND_H("Down (L&H)"),
		DOWN__("Down"), UP_AND_DOWN_L("Up & down (L)"), UP_AND_DOWN_L_AND_H("Up & down (L&H)"),
		UP_AND_DOWN__("Up & down"), RANDOM_L("Random (L)"), RANDOM__("Random"), PHRASE("Phrase");
		
		String label;
		
		private ArpeggioMotif(String label) {
			this.label = label;
		}
		
		public String toString() {
			return label;
		}
	}
	
	public ArpeggioCommon(Address address, byte[] data) {
		super(address, data);
		
		if (data.length < 0x08)
			throw new IllegalArgumentException("Parameters data size mismatch.");
	}
	
	public EnumValue<ArpeggioGrid> getArpeggioGrid() {
		return new EnumValue<ArpeggioGrid>(this, 0x00, ArpeggioGrid.values());
	}
	
	public EnumValue<ArpeggioDuration> getArpeggioDuration() {
		return new EnumValue<ArpeggioDuration>(this, 0x01, ArpeggioDuration.values());
	}
	
	public EnumValue<ArpeggioMotif> getArpeggioMotif() {
		return new EnumValue<ArpeggioMotif>(this, 0x02, ArpeggioMotif.values());
	}
	
	public IntValue getArpeggioOctaveRange() {
		return new SignedIntValue(this, 0x03, -3, +3);
	}
	
	public IntValue getArpeggioAccentRate() {
		return new IntValue(this, 0x04, 0, 100);
	}
	
	// 0 ... 127 (REAL, 1 ... 127)
	public IntValue getArpeggioVelocity() {
		return new IntValue(this, 0x05, 0, 127);
	}
	
	public IntValue getEndStep() {
		return new Int8BitValue(this, 0x06, 1, 32);
	}
	
}
