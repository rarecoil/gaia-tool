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

public class Value {

	protected ParameterData parameterData;
	protected int offset;
	private int min;
	private int max;
	
	public Value(ParameterData parameterData, int offset, int min, int max) {
		this.parameterData = parameterData;
		this.offset = offset;
		this.min = min;
		this.max = max;
	}
	
	public int getValue() {
		return parameterData.getValue(offset);
	}
	
	public void setValue(int value) {
		checkRange(value);
		setValueNoCheck(value);
	}
	
	protected void setValueNoCheck(int value) {
		parameterData.setValue(offset, value);
	}
	
	protected void checkRange(int value) {
		if (value < min || value > max)
			throw new RuntimeException("Value out of range.");
	}
	
	public int getMinimum() {
		return min;
	}
	
	public int getMaximum() {
		return max;
	}
	
	public String toString() {
		return String.valueOf(getValue());
	}
	
}
