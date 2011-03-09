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

/**
 * Parameters encodes a set of parameters at a certain address.
 * It provides methods to get and set individual parameter values, and to notify listeners about these changes.
 */
public class Parameters extends Observable {
	
	/**
	 * A parameter data change notification object.
	 * Indicates what range of values has changed.
	 */
	public static class ParameterChange {
		
		private int offset;
		private int length;
		private boolean fromUpdate;
		
		public ParameterChange(int offset, int length, boolean fromUpdate) {
			this.offset = offset;
			this.length = length;
			this.fromUpdate = fromUpdate;
		}
		
		/**
		 * Get the start offset of the changed parameter data.
		 * @return The start offset of the changed data.
		 */
		public int getOffset() {
			return offset;
		}
		
		/**
		 * Get the length of the changed parameter data.
		 * @return The length of the changed data.
		 */
		public int getLength() {
			return length;
		}
		
		/**
		 * Returns whether the parameter change is an update originating from an edit data
		 * transmission by the GAIA, in which case they should not be synced.
		 * @return True if the change was done by an update.
		 */
		public boolean fromUpdate() {
			return fromUpdate;
		}
		
		/**
		 * Test whether the parameter changes include a certain offset.
		 * @param testOffset The offset to test.
		 * @return True if the specified offset is included in the changes.
		 */
		public boolean includes(int testOffset) {
			return testOffset >= offset && testOffset < (offset + length);
		}
		
		@Override
		public String toString() {
			return "data";
		}
	}
	
	private Address address;
	private byte[] data;
	
	/**
	 * Constructs a new Parameters object.
	 * @param address The start address of the data to change.
	 * @param data The data to change. All bytes must be in the range 0-127.
	 * @param address
	 * @param data All bytes must be in the range 0-127.
	 */
	public Parameters(Address address, byte[] data) {
		this.address = address;
		this.data = data.clone();
	}
	
	public Address getAddress() {
		return address;
	}
	
	/**
	 * Gets the raw parameter data.
	 * @return A copy of the parameter data.
	 */
	public byte[] getData() {
		return getData(0, data.length);
	}
	
	/**
	 * Gets the raw parameter data in the specified range.
	 * @param offset The start position of the data to return.
	 * @param length The length of the data to return.
	 * @return A copy of the parameter data requested.
	 */
	public byte[] getData(int offset, int length) {
		byte[] copy = new byte[length];
		System.arraycopy(data, offset, copy, 0, length);
		return copy;
	}
	
	/**
	 * Returns the length of the parameter data.
	 * @return The parameter data length.
	 */
	public int getLength() {
		return data.length;
	}
	
	/**
	 * Update parameter data.
	 * @param address The start address of the data to change.
	 * @param newData The data to change. All bytes must be in the range 0-127.
	 */
	public void updateParameters(Address address, byte[] newData) {
		int offset = this.address.offsetOf(address);
		if (offset < 0 || offset >= getLength() || offset + newData.length > getLength())
			throw new Error("Address or data out of range.");
		
		for (int i = 0; i < newData.length; i++) {
			data[offset + i] = newData[i];
		}
		this.notifyObservers(new ParameterChange(offset, newData.length, true));
	}
	
	/**
	 * Gets the 7-bit parameter value at the specified offset.
	 * @param offset Offset relative to the base address.
	 * @return The value in the range 0-127.
	 */
	public int getValue(int offset) {
		return data[offset];
	}
	
	/**
	 * Gets the 8-bit parameter value at the specified offset.
	 * @param offset Offset relative to the base address.
	 * @return The value in the range 0-255.
	 */
	public int get8BitValue(int offset) {
		return data[offset] << 4 | data[offset + 1];
	}
	
	/**
	 * Gets the 12-bit parameter value at the specified offset.
	 * @param offset Offset relative to the base address.
	 * @return The value in the range 0-4095.
	 */
	public int get12BitValue(int offset) {
		return data[offset] << 8 | data[offset + 1] << 4 | data[offset + 2];
	}
	
	/**
	 * Gets the 16-bit parameter value at the specified offset.
	 * @param offset Offset relative to the base address.
	 * @return The value in the range 0-65535.
	 */
	public int get16BitValue(int offset) {
		return data[offset] << 12 | data[offset + 1] << 8 |
				data[offset + 2] << 4 | data[offset + 3];
	}
	
	/**
	 * Gets a string from data at the specified offset.
	 * @param offset Offset relative to the base address.
	 * @param length
	 * @return The 
	 */
	public String getString(int offset, int length) {
		return new String(data, offset, length);
	}
	
	/**
	 * Sets a 7-bit parameter value at the specified offset.
	 * @param offset Offset relative to the base address.
	 * @param value A value in the range 0-127.
	 */
	public void setValue(int offset, int value) {
		setValue(offset, value, false);
	}
	
	protected void setValue(int offset, int value, boolean fromUpdate) {
		if (value < 0 || value >= 128)
			throw new IllegalArgumentException("Value out of range.");
		data[offset] = (byte)value;
		this.notifyObservers(new ParameterChange(offset, 1, fromUpdate));
	}
	
	/**
	 * Sets an 8-bit parameter value at the specified offset.
	 * @param offset Offset relative to the base address.
	 * @param value A value in the range 0-255.
	 */
	public void set8BitValue(int offset, int value) {
		set8BitValue(offset, value, false);
	}
	
	protected void set8BitValue(int offset, int value, boolean fromUpdate) {
		if (value < 0 || value >= 256)
			throw new IllegalArgumentException("Value out of range.");
		data[offset] = (byte) (value >> 4 & 0x0F);
		data[offset + 1] = (byte) (value & 0x0F);
		this.notifyObservers(new ParameterChange(offset, 2, fromUpdate));
	}
	
	/**
	 * Sets a 12-bit parameter value at the specified offset.
	 * @param offset Offset relative to the base address.
	 * @param value A value in the range 0-4095.
	 */
	public void set12BitValue(int offset, int value) {
		set12BitValue(offset, value, false);
	}
	
	protected void set12BitValue(int offset, int value, boolean fromUpdate) {
		if (value < 0 || value >= 4096)
			throw new IllegalArgumentException("Value out of range.");
		data[offset] = (byte) (value >> 8 & 0x0F);
		data[offset + 1] = (byte) (value >> 4 & 0x0F);
		data[offset + 2] = (byte) (value & 0x0F);
		this.notifyObservers(new ParameterChange(offset, 3, fromUpdate));
	}
	
	/**
	 * Sets a 16-bit parameter value at the specified offset.
	 * @param offset Offset relative to the base address.
	 * @param value A value in the range 0-65535.
	 */
	public void set16BitValue(int offset, int value) {
		set16BitValue(offset, value, false);
	}
	
	protected void set16BitValue(int offset, int value, boolean fromUpdate) {
		if (value < 0 || value >= 65536)
			throw new IllegalArgumentException("Value out of range.");
		data[offset] = (byte) (value >> 12 & 0x0F);
		data[offset + 1] = (byte) (value >> 8 & 0x0F);
		data[offset + 2] = (byte) (value >> 4 & 0x0F);
		data[offset + 3] = (byte) (value & 0x0F);
		this.notifyObservers(new ParameterChange(offset, 4, fromUpdate));
	}
	
	/**
	 * Set a range of values, passed either as an array or varargs.
	 * @param offset The offset to start setting.
	 * @param values The values to set. They must be between 0 and 128.
	 */
	public void setValues(int offset, int... values) {
		for (int i = 0; i < values.length; i++) {
			if (values[i] < 0 || values[i] >= 128)
				throw new IllegalArgumentException("Value out of range.");
		}
		for (int i = 0; i < values.length; i++) {
			data[offset + i] = (byte) values[i];
		}
		this.notifyObservers(new ParameterChange(offset, values.length, false));
	}
	
	/**
	 * Set a range of values, passed either as an array or varargs.
	 * @param offset The offset to start setting.
	 * @param values The values to set. They must be between 0 and 128.
	 */
	public void setValues(int offset, byte... values) {
		for (int i = 0; i < values.length; i++) {
			if (values[i] < 0 || values[i] >= 128)
				throw new IllegalArgumentException("Value out of range.");
		}
		for (int i = 0; i < values.length; i++) {
			data[offset + i] = values[i];
		}
		this.notifyObservers(new ParameterChange(offset, values.length, false));
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
