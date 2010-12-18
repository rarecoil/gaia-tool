package nl.grauw.gaia_tool;

import java.util.Arrays;

public class ParameterData {
	
	private Address address;
	private byte[] data;
	
	public ParameterData(Address address, byte[] data) {
		this.address = address;
		this.data = data;
	}
	
	public Address getAddress() {
		return address;
	}
	
	public byte[] getData() {
		return Arrays.copyOf(data, data.length);
	}
	
	public int getLength() {
		return data.length;
	}
	
	public int getValue(int offset) {
		return this.data[offset];
	}
	
	public int get8BitValue(int offset) {
		return this.data[offset] << 4 | this.data[offset + 1];
	}
	
	public int get12BitValue(int offset) {
		return this.data[offset] << 8 | this.data[offset + 1] << 4 | this.data[offset + 2];
	}
	
	public int get16BitValue(int offset) {
		return this.data[offset] << 12 | this.data[offset + 1] << 8 |
				this.data[offset + 2] << 4 | this.data[offset + 3];
	}
	
	public String getString(int offset, int length) {
		return new String(data, offset, length);
	}
	
}
