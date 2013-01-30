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
package nl.grauw.gaia.tool;

/**
 * Representation of a positive signed 16-bit integer value.
 * Positive signed means that -32768 .. 0 .. 32767 is stored as 0 .. 32768 .. 65536.
 */
public class SignedInt16BitValue extends Int16BitValue {
	
	public SignedInt16BitValue(Parameters parameters, int offset, int min, int max) {
		super(parameters, offset, min, max);
	}
	
	@Override
	public int getValue() {
		return super.getValue() - 32768;
	}
	
	@Override
	public void setValueNoCheck(int value) {
		super.setValueNoCheck(value + 32768);
	}
	
}
