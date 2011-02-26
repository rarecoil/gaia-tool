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
package nl.grauw.gaia_tool.parameters;

import nl.grauw.gaia_tool.Address;
import nl.grauw.gaia_tool.Parameters;
import nl.grauw.gaia_tool.SignedInt16BitValue;
import nl.grauw.gaia_tool.IntValue;

public class Delay extends Parameters {
	
	/**
	 * Retrieves the delay parameters.
	 * 
	 * Note that there are actually three banks of data, one for each delay type.
	 * All are persisted, but only one is visible at a time.
	 */
	public enum DelayType {
		OFF, DELAY, PANNING_DELAY
	}
	
	public Delay(Address address, byte[] data) {
		super(address, data);
		
		if (data.length < 0x51)
			throw new IllegalArgumentException("Parameters data size mismatch.");
	}
	
	public DelayType getDelayType() {
		return DelayType.values()[getValue(0x00)];
	}
	
	public IntValue getDelayParameter(int number) {
		if (number < 1 || number > 20)
			throw new IllegalArgumentException("Invalid parameter number.");
		
		int index = (number - 1) * 4;
		return new SignedInt16BitValue(this, 0x01 + index, -20000, 20000);
	}
	
	public IntValue getLevel() {
		if (getDelayType() == DelayType.OFF)
			throw new IllegalArgumentException("Only applies to active effect.");
		return new SignedInt16BitValue(this, 0x01, 0, 127);
	}
	
	public IntValue getTime() {
		if (getDelayType() == DelayType.OFF)
			throw new IllegalArgumentException("Only applies to active effect.");
		return new SignedInt16BitValue(this, 0x05, 0, 127);
	}
	
	public IntValue getSyncedTime() {
		if (getDelayType() == DelayType.OFF)
			throw new IllegalArgumentException("Only applies to active effect.");
		return new SignedInt16BitValue(this, 0x09, 0, 15);
	}
	
	public IntValue getFeedback() {
		if (getDelayType() == DelayType.OFF)
			throw new IllegalArgumentException("Only applies to active effect.");
		return new SignedInt16BitValue(this, 0x0D, 0, 127);
	}
	
	public IntValue getHighDamp() {
		if (getDelayType() == DelayType.OFF)
			throw new IllegalArgumentException("Only applies to active effect.");
		return new SignedInt16BitValue(this, 0x11, -36, 0) {
			@Override
			public int getValue() {
				return super.getValue() - 36;
			}
			@Override
			public void setValueNoCheck(int value) {
				super.setValueNoCheck(value + 36);
			}
		};
	}
	
	public String toString() {
		StringBuilder delayParameters = new StringBuilder(128);
		for (int i = 1; i <= 20; i++) {
			delayParameters.append(getDelayParameter(i));
			delayParameters.append(" ");
		}
		
		return "Patch delay parameters:\n" +
				String.format("Delay type: %s\n", getDelayType()) +
				(
					getDelayType() != DelayType.OFF ?
					String.format("Time: %s\n", getTime()) +
					String.format("Synced time: %s\n", getSyncedTime()) +
					String.format("Feedback: %s\n", getFeedback()) +
					String.format("High damp: %s\n", getHighDamp()) +
					String.format("Level: %s\n", getLevel()) : ""
				) +
				String.format("\nDelay parameters: %s\n", delayParameters);
	}
	
}
