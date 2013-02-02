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
package nl.grauw.gaia;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import nl.grauw.gaia.Address.AddressException;
import nl.grauw.gaia.tool.mvc.Observable;

/**
 * Parameters encodes a set of parameters at a certain address.
 * It provides methods to get and set individual parameter values, and to notify listeners about these changes.
 */
public class Parameters extends Observable {
	
	private Address address;
	private byte[] originalData;
	private byte[] data;
	
	private List<ParameterChangeListener> changeListeners = Collections.synchronizedList(new ArrayList<ParameterChangeListener>());
	
	/**
	 * Constructs a new Parameters object.
	 * @param address The start address of the data.
	 * @param data The parameter data. All bytes must be in the range 0-127.
	 */
	public Parameters(Address address, byte[] data) {
		if (address.getByte4() != 0x00)
			throw new Error("Invalid parameters address.");
		
		this.address = address;
		this.originalData = data.clone();
		this.data = data.clone();
	}
	
	/**
	 * Copy constructor.
	 * @param parameters The parameters to copy.
	 */
	public Parameters(Parameters parameters) {
		this(parameters.address, parameters.data);
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
	 * Returns whether the data has changed since the last update.
	 * @return True if the data has changed.
	 */
	public boolean hasChanged() {
		return hasChanged(0, data.length);
	}
	
	/**
	 * Returns whether the data in the specified range has changed since the last update.
	 * @param change The ParameterChange object to take offset and length from.
	 * @return True if the data has changed.
	 */
	public boolean hasChanged(ParameterChange change) {
		return hasChanged(change.getOffset(), change.getLength());
	}
	
	/**
	 * Returns whether the data in the specified range has changed since the last update.
	 * @param offset The start position to check.
	 * @param length The number of bytes to check.
	 * @return True if the data has changed.
	 */
	public boolean hasChanged(int offset, int length) {
		for (int i = offset, end = offset + length; i < end; i++) {
			if (data[i] != originalData[i])
				return true;
		}
		return false;
	}
	
	/**
	 * Returns the length of the parameter data.
	 * @return The parameter data length.
	 */
	public int getLength() {
		return data.length;
	}
	
	/**
	 * Returns whether these parameters’ address and data are equal to another.
	 * The address comparison is fuzzy (ignores temporary/user patch difference).
	 * Also it does not compare the original data.
	 */
	public boolean isEqualTo(Parameters other) {
		return address.fuzzyEquals(other.address) && Arrays.equals(data, other.data);
	}
	
	/**
	 * Update parameter data.
	 * @param address The start address of the data to change.
	 * @param newData The data to change. All bytes must be in the range 0-127.
	 */
	public void updateParameters(Address address, byte[] newData) throws AddressException {
		int offset = this.address.offsetOf(address);
		if (offset < 0 || offset >= getLength() || offset + newData.length > getLength())
			throw new AddressException("Address or data out of range.");
		
		for (int i = 0; i < newData.length; i++) {
			originalData[offset + i] = data[offset + i] = newData[i];
		}
		fireParameterChange(new ParameterChange(offset, newData.length));
	}
	
	/**
	 * Update original parameter data only.
	 * (Used by parameter synchronisation.)
	 * @param address The start address of the data to change.
	 * @param newData The data to change. All bytes must be in the range 0-127.
	 */
	public void updateOriginalParameters(Address address, byte[] newData) throws AddressException {
		int offset = this.address.offsetOf(address);
		if (offset < 0 || offset >= getLength() || offset + newData.length > getLength())
			throw new AddressException("Address or data out of range.");
		
		for (int i = 0; i < newData.length; i++) {
			originalData[offset + i] = newData[i];
		}
	}
	
	/**
	 * Gets the 7-bit parameter value at the specified offset.
	 * @param offset Offset relative to the base address.
	 * @return The value in the range 0-127.
	 */
	public int getValue(int offset) {
		return data[offset] & 0x7F;
	}
	
	/**
	 * Gets the 8-bit parameter value at the specified offset.
	 * @param offset Offset relative to the base address.
	 * @return The value in the range 0-255.
	 */
	public int get8BitValue(int offset) {
		return (data[offset] & 0x0F) << 4 | data[offset + 1] & 0x0F;
	}
	
	/**
	 * Gets the 12-bit parameter value at the specified offset.
	 * @param offset Offset relative to the base address.
	 * @return The value in the range 0-4095.
	 */
	public int get12BitValue(int offset) {
		return (data[offset] & 0x0F) << 8 | (data[offset + 1] & 0x0F) << 4 | data[offset + 2] & 0x0F;
	}
	
	/**
	 * Gets the 16-bit parameter value at the specified offset.
	 * @param offset Offset relative to the base address.
	 * @return The value in the range 0-65535.
	 */
	public int get16BitValue(int offset) {
		return (data[offset] & 0x0F) << 12 | (data[offset + 1] & 0x0F) << 8 |
				(data[offset + 2] & 0x0F) << 4 | data[offset + 3] & 0x0F;
	}
	
	/**
	 * Gets a string from data at the specified offset.
	 * @param offset Offset relative to the base address.
	 * @param length The desired length of the string.
	 * @return The string.
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
		if (value < 0 || value >= 128)
			throw new IllegalArgumentException("Value out of range.");
		data[offset] = (byte)value;
		fireParameterChange(new ParameterChange(offset, 1));
	}
	
	/**
	 * Update a 7-bit parameter value at the specified offset.
	 * Also updates the original data, so the data appears unchanged.
	 * @param offset Offset relative to the base address.
	 * @param value A value in the range 0-127.
	 */
	protected void updateValue(int offset, int value) {
		if (value < 0 || value >= 128)
			throw new IllegalArgumentException("Value out of range.");
		originalData[offset] = data[offset] = (byte)value;
		fireParameterChange(new ParameterChange(offset, 1));
	}
	
	/**
	 * Sets an 8-bit parameter value at the specified offset.
	 * @param offset Offset relative to the base address.
	 * @param value A value in the range 0-255.
	 */
	public void set8BitValue(int offset, int value) {
		if (value < 0 || value >= 256)
			throw new IllegalArgumentException("Value out of range.");
		data[offset] = (byte) (value >> 4 & 0x0F);
		data[offset + 1] = (byte) (value & 0x0F);
		fireParameterChange(new ParameterChange(offset, 2));
	}
	
	/**
	 * Sets a 12-bit parameter value at the specified offset.
	 * @param offset Offset relative to the base address.
	 * @param value A value in the range 0-4095.
	 */
	public void set12BitValue(int offset, int value) {
		if (value < 0 || value >= 4096)
			throw new IllegalArgumentException("Value out of range.");
		data[offset] = (byte) (value >> 8 & 0x0F);
		data[offset + 1] = (byte) (value >> 4 & 0x0F);
		data[offset + 2] = (byte) (value & 0x0F);
		fireParameterChange(new ParameterChange(offset, 3));
	}
	
	/**
	 * Sets a 16-bit parameter value at the specified offset.
	 * @param offset Offset relative to the base address.
	 * @param value A value in the range 0-65535.
	 */
	public void set16BitValue(int offset, int value) {
		if (value < 0 || value >= 65536)
			throw new IllegalArgumentException("Value out of range.");
		data[offset] = (byte) (value >> 12 & 0x0F);
		data[offset + 1] = (byte) (value >> 8 & 0x0F);
		data[offset + 2] = (byte) (value >> 4 & 0x0F);
		data[offset + 3] = (byte) (value & 0x0F);
		fireParameterChange(new ParameterChange(offset, 4));
	}
	
	/**
	 * Update a 16-bit parameter value at the specified offset.
	 * Also updates the original data, so the data appears unchanged.
	 * @param offset Offset relative to the base address.
	 * @param value A value in the range 0-65535.
	 */
	protected void update16BitValue(int offset, int value) {
		if (value < 0 || value >= 65536)
			throw new IllegalArgumentException("Value out of range.");
		originalData[offset] = data[offset] = (byte) (value >> 12 & 0x0F);
		originalData[offset + 1] = data[offset + 1] = (byte) (value >> 8 & 0x0F);
		originalData[offset + 2] = data[offset + 2] = (byte) (value >> 4 & 0x0F);
		originalData[offset + 3] = data[offset + 3] = (byte) (value & 0x0F);
		fireParameterChange(new ParameterChange(offset, 4));
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
		fireParameterChange(new ParameterChange(offset, values.length));
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
		fireParameterChange(new ParameterChange(offset, values.length));
	}
	
	@Override
	public String toString() {
		StringBuilder parameterData = new StringBuilder(128);
		for (byte b : data) {
			parameterData.append(String.format("%02X ", b));
		}
		
		return String.format("Parameters. Address: %s. Data: %s.", getAddress(), parameterData.toString().trim());
	}
	
	public void addParameterChangeListener(ParameterChangeListener listener) {
		changeListeners.add(listener);
	}
	
	public void removeParameterChangeListener(ParameterChangeListener listener) {
		changeListeners.remove(listener);
	}
	
	private void fireParameterChange(ParameterChange change) {
		for (ParameterChangeListener listener : new ArrayList<ParameterChangeListener>(changeListeners))
			listener.parameterChange(this, change);
		notifyObservers(change);
	}
	
	public interface ParameterChangeListener {
		public void parameterChange(Parameters source, ParameterChange change);
	}
	
	/**
	 * A parameter data change notification object.
	 * Indicates what range of values has changed.
	 */
	public static class ParameterChange {
		
		private int offset;
		private int length;
		
		private ParameterChange(int offset, int length) {
			this.offset = offset;
			this.length = length;
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
	
}
