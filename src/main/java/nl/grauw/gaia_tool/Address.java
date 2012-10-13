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

/**
 * Object representing an address in the GAIA memory.
 * This object is invariant.
 */
public class Address {
	
	private int address;
	
	public Address(int byte1, int byte2, int byte3, int byte4) {
		this(byte1 << 21 | byte2 << 14 | byte3 << 7 | byte4);
	}
	
	public Address(int address) {
		this.address = address;
	}
	
	public int getValue() {
		return address;
	}
	
	/**
	 * Subsystem part of the address.
	 * 
	 * System:          0x01
	 * Temporary Patch: 0x10
	 * User Patches:    0x20
	 */
	public byte getByte1() {
		return (byte) (address >> 21 & 0x7F);
	}
	
	/**
	 * Patch number part of the address.
	 * 
	 * Patch A-1: 0x00
	 * Patch H-8: 0x3F
	 */
	public byte getByte2() {
		return (byte) (address >> 14 & 0x7F);
	}
	
	/**
	 * Parameter MSB part of the address.
	 * 
	 * Patch Common:             0x00
	 * Patch Tone 1-3:           0x01 - 0x03
	 * Patch Distortion:         0x04
	 * Patch Flanger:            0x06
	 * Patch Delay:              0x08
	 * Patch Reverb:             0x0A
	 * Patch Arpeggio Common:    0x0C
	 * Patch Arpeggio Pattern 1: 0x0D - 0x1C
	 */
	public byte getByte3() {
		return (byte) (address >> 7 & 0x7F);
	}
	
	/**
	 * Parameter LSB part of the address.
	 */
	public byte getByte4() {
		return (byte) (address & 0x7F);
	}
	
	public Address add(int offset) {
		return new Address(address + offset);
	}
	
	public int offsetOf(Address other) {
		return other.getValue() - address;
	}
	
	public String getDescription() {
		if (address >= 0x200000 && address < 0x20006E) {
			return "System";
		} else if (getByte1() == 0x10) {
			return "Temporary patch - " + getSubDescription();
		} else if (getByte1() == 0x20) {
			return "User patch (" + "ABCDEFGH".charAt(getByte2() >> 3) + "-" + 
					((getByte2() & 7) + 1) + ") - " + getSubDescription();
		}
		return "Unknown";
	}
	
	public String getSubDescription() {
		if (getByte1() == 0x10 || getByte1() == 0x20) {
			int byte3 = getByte3();
			if (byte3 == 0x00) {
				return "Common";
			} else if (byte3 == 0x01 || byte3 == 0x02 || byte3 == 0x03) {
				return "Tone " + getByte3();
			} else if (byte3 == 0x04 || byte3 == 0x05) {
				return "Distortion";
			} else if (byte3 == 0x06 || byte3 == 0x07) {
				return "Flanger";
			} else if (byte3 == 0x08 || byte3 == 0x09) {
				return "Delay";
			} else if (byte3 == 0x0A || byte3 == 0x0B) {
				return "Reverb";
			} else if (byte3 == 0x0C) {
				return "Arpeggio common";
			} else if (byte3 >= 0x0D && byte3 <= 0x1C) {
				return "Arpeggion pattern (note " + (byte3 - 0x0C) + ")";
			}
		}
		return "Unknown";
	}
	
	public String toHexString() {
		return String.format("%02X %02X %02X %02X", getByte1(), getByte2(), getByte3(), getByte4());
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof Address)
			return ((Address)other).getValue() == address;
		return false;
	}
	
	@Override
	public int hashCode() {
		return address;
	}
	
	/**
	 * Returns whether the address is fuzzy-identical to another.
	 * “Fuzzy” means that it ignores ignores temporary/user patch difference.
	 */
	public boolean fuzzyEquals(Address other) {
		if ((getByte1() == 0x10 || getByte1() == 0x20) && (other.getByte1() == 0x10 || other.getByte1() == 0x20))
			return getByte3() == other.getByte3() && getByte4() == other.getByte4();
		return address == other.address;
	}
	
	public String toString() {
		return toHexString() + " (" + getDescription() + ")";
	}
	
	public static class AddressException extends Exception {
		private static final long serialVersionUID = 1L;
		
		public AddressException(String message) {
			super(message);
		}
	}
	
}
