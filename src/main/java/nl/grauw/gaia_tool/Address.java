package nl.grauw.gaia_tool;

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
	
	public byte getByte1() {
		return (byte) (address >> 21 & 0x7F);
	}
	
	public byte getByte2() {
		return (byte) (address >> 14 & 0x7F);
	}
	
	public byte getByte3() {
		return (byte) (address >> 7 & 0x7F);
	}
	
	public byte getByte4() {
		return (byte) (address & 0x7F);
	}
	
	public String getDescription() {
		if (address >= 0x200000 && address < 0x20006E) {
			return "System";
		} else if (getByte1() == 0x10) {
			return "Temporary patch - " + getSubDescription();
		} else if (getByte1() == 0x20) {
			return "User patch (" + "ABCDEFGH".charAt(getByte2() >> 3) + ((getByte2() & 7) + 1) +
					") - " + getSubDescription();
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
	
	public String toString() {
		return toHexString() + " (" + getDescription() + ")";
	}

}
