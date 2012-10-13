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
import nl.grauw.gaia_tool.EnumValue;
import nl.grauw.gaia_tool.Parameters;
import nl.grauw.gaia_tool.SignedInt16BitValue;
import nl.grauw.gaia_tool.IntValue;
import nl.grauw.gaia_tool.messages.ControlChangeMessage;

/**
 * Retrieves the reverb parameters.
 * 
 * Note that there are actually two banks of data, one for each reverb type.
 * All are persisted, but only one is visible at a time.
 */
public class Reverb extends Parameters {
	
	public enum ReverbType {
		OFF, REVERB
	}
	
	public Reverb(Address address, byte[] data) {
		super(address, data);
		
		if (address.getByte3() != 0x0A)
			throw new Error("Invalid parameters address.");
		if (data.length < 0x51)
			throw new IllegalArgumentException("Parameters data size mismatch.");
	}
	
	public void updateParameters(ControlChangeMessage message) {
		if (message.getController() != null && getReverbType() != ReverbType.OFF) {
			switch (message.getController()) {
			case REVERB_CONTROL_1:
				update16BitValue(0x05, message.getValue() + 32768);
				break;
			case REVERB_LEVEL:
				update16BitValue(0x01, message.getValue() + 32768);
				break;
			default:
				throw new RuntimeException("Control change message not recognised: " + message);
			}
		}
	}
	
	public ReverbType getReverbType() {
		return ReverbType.values()[getValue(0x00)];
	}
	
	public EnumValue<ReverbType> getReverbTypeValue() {
		return new EnumValue<ReverbType>(this, 0x00, ReverbType.values());
	}
	
	public IntValue getReverbParameter(int number) {
		if (number < 1 || number > 20)
			throw new IllegalArgumentException("Invalid parameter number.");
		
		int index = (number - 1) * 4;
		return new SignedInt16BitValue(this, 0x01 + index, -20000, 20000);
	}
	
	public IntValue getLevel() {
		if (getReverbType() == ReverbType.OFF)
			throw new IllegalArgumentException("Only applies to active effect.");
		return new SignedInt16BitValue(this, 0x01, 0, 127);
	}
	
	public IntValue getTime() {
		if (getReverbType() == ReverbType.OFF)
			throw new IllegalArgumentException("Only applies to active effect.");
		return new SignedInt16BitValue(this, 0x05, 0, 127);
	}
	
	public IntValue getType() {
		if (getReverbType() == ReverbType.OFF)
			throw new IllegalArgumentException("Only applies to active effect.");
		return new SignedInt16BitValue(this, 0x09, 0, 2);
	}
	
	public IntValue getHighDamp() {
		if (getReverbType() == ReverbType.OFF)
			throw new IllegalArgumentException("Only applies to active effect.");
		return new SignedInt16BitValue(this, 0x0D, 0, 127);
	}
	
}
