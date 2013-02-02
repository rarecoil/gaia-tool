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
package nl.grauw.gaia.parameters;

import nl.grauw.gaia.Address;
import nl.grauw.gaia.EnumValue;
import nl.grauw.gaia.IntValue;
import nl.grauw.gaia.Parameters;
import nl.grauw.gaia.SignedInt16BitValue;
import nl.grauw.gaia.midi.messages.ControlChangeMessage;

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
		
		if (address.getByte3() != 0x08)
			throw new Error("Invalid parameters address.");
		if (data.length < 0x51)
			throw new IllegalArgumentException("Parameters data size mismatch.");
	}
	
	public void updateParameters(ControlChangeMessage message) {
		if (message.getController() != null && getDelayType() != DelayType.OFF) {
			switch (message.getController()) {
			case DELAY_LEVEL:
				update16BitValue(0x01, message.getValue() + 32768);
				break;
			// Commented out because functionality depends on non-local tempo sync parameter
//			case DELAY_CONTROL_1:
// 				if (!tempo_sync)
//					set16BitValue(0x05, message.getValue() + 32768, true);
//				else
//					set16BitValue(0x09, (message.getValue() + 8) / 9 + 32768, true);
//				break;
			default:
				throw new RuntimeException("Control change message not recognised: " + message);
			}
		}
	}
	
	public DelayType getDelayType() {
		return DelayType.values()[getValue(0x00)];
	}
	
	public EnumValue<DelayType> getDelayTypeValue() {
		return new EnumValue<DelayType>(this, 0x00, DelayType.values());
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
	
}
