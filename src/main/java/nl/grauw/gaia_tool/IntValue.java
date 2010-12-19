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

public class IntValue extends Value {

	private int min;
	private int max;
	
	public IntValue(Parameters parameters, int offset, int min, int max) {
		super(parameters, offset);
		this.min = min;
		this.max = max;
	}
	
	public int getValue() {
		return parameters.getValue(offset);
	}
	
	public void setValue(int value) {
		if (!isValidValue(value))
			throw new IllegalArgumentException("Value out of range.");
		setValueNoCheck(value);
	}
	
	protected void setValueNoCheck(int value) {
		parameters.setValue(offset, value);
	}
	
	public boolean isValidValue(int value) {
		return value >= min && value <= max;
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
