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
import nl.grauw.gaia_tool.mvc.Observer;

public class Value extends Observable implements Observer {

	protected ParameterData parameterData;
	protected int offset;
	private int min;
	private int max;
	
	public Value(ParameterData parameterData, int offset, int min, int max) {
		this.parameterData = parameterData;
		this.offset = offset;
		this.min = min;
		this.max = max;
		
		parameterData.addObserver(this);
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
			throw new IllegalArgumentException("Value out of range.");
	}
	
	public int getMinimum() {
		return min;
	}
	
	public int getMaximum() {
		return max;
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o == parameterData && arg instanceof Integer && (Integer)arg == offset) {
			notifyObservers();
		}
	}
	
	public String toString() {
		return String.valueOf(getValue());
	}
	
}
