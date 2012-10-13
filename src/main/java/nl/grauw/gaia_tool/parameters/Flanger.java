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
 * Retrieves the flanger parameters.
 * 
 * Note that there are actually four banks of data, one for each flanger type.
 * All are persisted, but only one is visible at a time.
 */
public class Flanger extends Parameters {
	
	public enum FlangerType {
		OFF, FLANGER, PHASER, PITCH_SHIFTER
	}
	
	public Flanger(Address address, byte[] data) {
		super(address, data);
		
		if (address.getByte3() != 0x06)
			throw new Error("Invalid parameters address.");
		if (data.length < 0x51)
			throw new IllegalArgumentException("Parameters data size mismatch.");
	}
	
	// mapping for pitch control change message values to pitch parameter values
	private final static int[] pitchControlMapping = {
		  0,   1,   1,   1,   1,   1,   2,   2,   2,   2,   2,   3,   3,   3,   3,   3,
		  4,   4,   4,   4,   4,   5,   5,   5,   5,   5,   6,   6,   6,   6,   6,   7,
		  7,   7,   7,   7,   8,   8,   8,   8,   8,   9,   9,   9,   9,   9,  10,  10,
		 10,  10,  10,  11,  11,  11,  11,  11,  12,  12,  12,  12,  12,  12,  12,  12,
		 12,  12,  12,  12,  12,  12,  12,  12,  13,  13,  13,  13,  13,  14,  14,  14,
		 14,  14,  15,  15,  15,  15,  15,  16,  16,  16,  16,  16,  17,  17,  17,  17,
		 17,  18,  18,  18,  18,  18,  19,  19,  19,  19,  19,  20,  20,  20,  20,  20,
		 21,  21,  21,  21,  21,  22,  22,  22,  22,  22,  23,  23,  23,  23,  23,  24
	};
	
	public void updateParameters(ControlChangeMessage message) {
		FlangerType type = getFlangerType();
		if (message.getController() != null && type != FlangerType.OFF) {
			switch (message.getController()) {
			case FLANGER_CONTROL_1:
				if (type == FlangerType.PITCH_SHIFTER) {
					// use a mapping because the values don’t change on a linear scale
					update16BitValue(0x05, pitchControlMapping[message.getValue()] + 32768);
				} else {
					update16BitValue(0x05, message.getValue() + 32768);
				}
				break;
			case FLANGER_LEVEL:
				update16BitValue(0x01, message.getValue() + 32768);
				break;
			default:
				throw new RuntimeException("Control change message not recognised: " + message);
			}
		}
	}
	
	public FlangerType getFlangerType() {
		return FlangerType.values()[getValue(0x00)];
	}
	
	public EnumValue<FlangerType> getFlangerTypeValue() {
		return new EnumValue<FlangerType>(this, 0x00, FlangerType.values());
	}
	
	public IntValue getFlangerParameter(int number) {
		if (number < 1 || number > 20)
			throw new IllegalArgumentException("Invalid parameter number.");
		
		int index = (number - 1) * 4;
		return new SignedInt16BitValue(this, 0x01 + index, -20000, 20000);
	}
	
	public IntValue getLevel() {
		if (getFlangerType() == FlangerType.OFF)
			throw new IllegalArgumentException("Only applies to active effect.");
		return new SignedInt16BitValue(this, 0x01, 0, 127);
	}
	
	public IntValue getFeedback() {
		if (getFlangerType() != FlangerType.FLANGER)
			throw new IllegalArgumentException("Only applies to flanger type.");
		return new SignedInt16BitValue(this, 0x05, 0, 127);
	}
	
	public IntValue getResonance() {
		if (getFlangerType() != FlangerType.PHASER)
			throw new IllegalArgumentException("Only applies to phaser type.");
		return new SignedInt16BitValue(this, 0x05, 0, 127);
	}
	
	public IntValue getPitch() {
		if (getFlangerType() != FlangerType.PITCH_SHIFTER)
			throw new IllegalArgumentException("Only applies to pitch shifter type.");
		return new SignedInt16BitValue(this, 0x05, -12, 12) {
			@Override
			public int getValue() {
				return super.getValue() - 12;
			}
			@Override
			public void setValueNoCheck(int value) {
				super.setValueNoCheck(value + 12);
			}
		};
	}
	
	public IntValue getDepth() {
		if (getFlangerType() != FlangerType.FLANGER && getFlangerType() != FlangerType.PHASER)
			throw new IllegalArgumentException("Only applies to flanger or phaser types.");
		return new SignedInt16BitValue(this, 0x09, 0, 127);
	}
	
	public IntValue getDetune() {
		if (getFlangerType() != FlangerType.PITCH_SHIFTER)
			throw new IllegalArgumentException("Only applies to pitch shifter type.");
		return new SignedInt16BitValue(this, 0x09, 0, 50);
	}
	
	public IntValue getRate() {
		if (getFlangerType() != FlangerType.FLANGER && getFlangerType() != FlangerType.PHASER)
			throw new IllegalArgumentException("Only applies to flanger or phaser types.");
		return new SignedInt16BitValue(this, 0x0D, 0, 127);
	}
	
}
