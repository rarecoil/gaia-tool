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

import nl.grauw.gaia_tool.mvc.Observable;

public class Parameters extends Observable {
	
	public class ParameterChange {
		private int offset;
		private int length;
		
		public ParameterChange(int offset, int length) {
			this.offset = offset;
			this.length = length;
		}
		
		public int getOffset() {
			return offset;
		}
		
		public int getLength() {
			return length;
		}
		
		/**
		 * Test whether the parameter changes include a certain offset.
		 */
		public boolean includes(int testOffset) {
			return testOffset >= offset && testOffset < (offset + length);
		}
		
		public String toString() {
			return "data";
		}
	}
	
	private Address address;
	private byte[] data;
	
	public Parameters(Address address, byte[] data) {
		this.address = address;
		this.data = data;
	}
	
	public Address getAddress() {
		return address;
	}
	
	public byte[] getData() {
		return getData(0, data.length);
	}
	
	public byte[] getData(int offset, int length) {
		byte[] copy = new byte[length];
		System.arraycopy(data, offset, copy, 0, length);
		return copy;
	}
	
	public int getLength() {
		return data.length;
	}
	
	public void updateParameters(Address address, byte[] data) {
		int offset = this.address.offsetOf(address);
		if (offset < 0 || offset >= getLength() || offset + data.length > getLength())
			throw new Error("Address or data out of range.");
		
		for (int i = 0; i < data.length; i++) {
			this.data[offset + i] = data[i];
		}
		this.notifyObservers(new ParameterChange(offset, data.length));
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
		this.notifyObservers(new ParameterChange(offset, 1));
	}
	
	public void set8BitValue(int offset, int value) {
		if (value < 0 || value >= 256)
			throw new IllegalArgumentException("Value out of range.");
		this.data[offset] = (byte) (value >> 4 & 0x0F);
		this.data[offset + 1] = (byte) (value & 0x0F);
		this.notifyObservers(new ParameterChange(offset, 2));
	}
	
	public void set12BitValue(int offset, int value) {
		if (value < 0 || value >= 4096)
			throw new IllegalArgumentException("Value out of range.");
		this.data[offset] = (byte) (value >> 8 & 0x0F);
		this.data[offset + 1] = (byte) (value >> 4 & 0x0F);
		this.data[offset + 2] = (byte) (value & 0x0F);
		this.notifyObservers(new ParameterChange(offset, 3));
	}
	
	public void set16BitValue(int offset, int value) {
		if (value < 0 || value >= 65536)
			throw new IllegalArgumentException("Value out of range.");
		this.data[offset] = (byte) (value >> 12 & 0x0F);
		this.data[offset + 1] = (byte) (value >> 8 & 0x0F);
		this.data[offset + 2] = (byte) (value >> 4 & 0x0F);
		this.data[offset + 3] = (byte) (value & 0x0F);
		this.notifyObservers(new ParameterChange(offset, 4));
	}
	
	@Override
	public String toString() {
		StringBuilder parameterData = new StringBuilder(128);
		for (byte b : data) {
			parameterData.append(String.format("%02X ", b));
		}
		
		return String.format("Parameters. Address: %s. Data: %s.", getAddress(), parameterData.toString().trim());
	}
	
}
