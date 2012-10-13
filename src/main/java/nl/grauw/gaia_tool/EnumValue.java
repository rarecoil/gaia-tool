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

public class EnumValue<T extends Enum<T>> extends Value {
	
	protected T[] choices;
	
	public EnumValue(Parameters parameters, int offset, T[] choices) {
		super(parameters, offset);
		this.choices = choices;
	}
	
	public T getValue() {
		return choices[parameters.getValue(offset)];
	}
	
	public void setValue(T value) {
		parameters.setValue(offset, value.ordinal());
	}
	
	/**
	 * Sets the value to an object.
	 * This setter takes any object, to work around impossibility to reliably cast to generics.
	 * @param value The value to set. Must be one of getChoices().
	 */
	public void setValue(Object value) {
		for (T choice : choices) {
			if (choice == value) {
				parameters.setValue(offset, choice.ordinal());
				return;
			}
		}
		throw new IllegalArgumentException("Provided type ");
	}
	
	public T[] getChoices() {
		return choices;
	}
	
	public String toString() {
		return getValue().toString();
	}
	
}
