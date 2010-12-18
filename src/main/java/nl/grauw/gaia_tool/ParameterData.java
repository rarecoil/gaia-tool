package nl.grauw.gaia_tool;

import java.util.Arrays;

import nl.grauw.gaia_tool.mvc.Observable;

public class ParameterData extends Observable {
	
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
	
	public void setValue(int offset, int value) {
		if (value < 0 || value >= 128)
			throw new IllegalArgumentException("Value out of range.");
		this.data[offset] = (byte)value;
		this.notifyObservers(offset);
	}
	
	public void set8BitValue(int offset, int value) {
		if (value < 0 || value >= 256)
			throw new IllegalArgumentException("Value out of range.");
		this.data[offset] = (byte) (value >> 4 & 0x0F);
		this.data[offset + 1] = (byte) (value & 0x0F);
		this.notifyObservers(offset);
	}
	
	public void set12BitValue(int offset, int value) {
		if (value < 0 || value >= 4096)
			throw new IllegalArgumentException("Value out of range.");
		this.data[offset] = (byte) (value >> 8 & 0x0F);
		this.data[offset + 1] = (byte) (value >> 4 & 0x0F);
		this.data[offset + 2] = (byte) (value & 0x0F);
		this.notifyObservers(offset);
	}
	
	public void set16BitValue(int offset, int value) {
		if (value < 0 || value >= 65536)
			throw new IllegalArgumentException("Value out of range.");
		this.data[offset] = (byte) (value >> 12 & 0x0F);
		this.data[offset + 1] = (byte) (value >> 8 & 0x0F);
		this.data[offset + 2] = (byte) (value >> 4 & 0x0F);
		this.data[offset + 3] = (byte) (value & 0x0F);
		this.notifyObservers(offset);
	}
	
}
