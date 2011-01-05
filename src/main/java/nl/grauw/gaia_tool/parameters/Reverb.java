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
		
		if (data.length < 0x51)
			throw new IllegalArgumentException("Parameters data size mismatch.");
	}
	
	public void updateParameters(ControlChangeMessage message) {
		if (message.getController() != null && getReverbType() != ReverbType.OFF) {
			switch (message.getController()) {
			case REVERB_CONTROL_1:
				set16BitValue(0x05, message.getValue() + 32768, true);
				break;
			case REVERB_LEVEL:
				set16BitValue(0x01, message.getValue() + 32768, true);
				break;
			}
		}
	}
	
	public ReverbType getReverbType() {
		return ReverbType.values()[getValue(0x00)];
	}
	
	public IntValue getReverbParameter(int number) {
		if (number < 1 || number > 20)
			throw new IllegalArgumentException("Invalid parameter number.");
		
		int index = (number - 1) * 4;
		return new SignedInt16BitValue(this, 0x01 + index, -20000, 20000);
	}
	
	public String toString() {
		StringBuilder reverbParameters = new StringBuilder(128);
		for (int i = 1; i <= 20; i++) {
			reverbParameters.append(getReverbParameter(i));
			reverbParameters.append(" ");
		}
		
		return "Patch reverb parameters:\n" +
				String.format("Reverb type: %s\n", getReverbType()) +
				String.format("Reverb parameters: %s\n", reverbParameters);
	}
	
}
