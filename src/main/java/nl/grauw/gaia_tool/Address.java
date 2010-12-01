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
	
	public String toHexString() {
		return String.format("%02X %02X %02X %02X", getByte1(), getByte2(), getByte3(), getByte4());
	}
	
	public String toString() {
		return "Address " + toHexString();
	}

}
