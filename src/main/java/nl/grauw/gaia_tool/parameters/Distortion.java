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
 * Retrieves the distortion parameters.
 * 
 * Note that there are actually four banks of data, one for each distortion type.
 * All are persisted, but only one is visible at a time.
 */
public class Distortion extends Parameters {
	
	public enum DistortionType {
		OFF("Off"), DIST("Distortion"), FUZZ("Fuzz"), BIT_CRASH("Bit crash");
		
		String label;
		
		private DistortionType(String label) {
			this.label = label;
		}
		
		public String toString() {
			return label;
		}
	}
	
	public Distortion(Address address, byte[] data) {
		super(address, data);
		
		if (data.length < 0x81)
			throw new IllegalArgumentException("Parameters data size mismatch.");
	}
	
	public void updateParameters(ControlChangeMessage message) {
		if (message.getController() != null && getDistortionType() != DistortionType.OFF) {
			switch (message.getController()) {
			case DISTORTION_CONTROL_1:
				set16BitValue(0x05, message.getValue() + 32768, true);
				break;
			case DISTORTION_LEVEL:
				set16BitValue(0x01, message.getValue() + 32768, true);
				break;
			default:
				throw new RuntimeException("Control change message not recognised: " + message);
			}
		}
	}
	
	public DistortionType getDistortionType() {
		return DistortionType.values()[getValue(0x00)];
	}
	
	public EnumValue<DistortionType> getDistortionTypeValue() {
		return new EnumValue<DistortionType>(this, 0x00, DistortionType.values());
	}
	
	public IntValue getMFXParameter(int number) {
		if (number < 1 || number > 32)
			throw new IllegalArgumentException("Invalid MFX parameter number.");
		
		int index = (number - 1) * 4;
		return new SignedInt16BitValue(this, 0x01 + index, -20000, 20000);
	}
	
	public IntValue getLevel() {
		if (getDistortionType() == DistortionType.OFF)
			throw new IllegalArgumentException("Only applies to active effect.");
		return new SignedInt16BitValue(this, 0x01, 0, 127);
	}
	
	public IntValue getDrive() {
		if (getDistortionType() != DistortionType.DIST && getDistortionType() != DistortionType.FUZZ)
			throw new IllegalArgumentException("Only applies to distortion or fuzz types.");
		return new SignedInt16BitValue(this, 0x05, 0, 127);
	}
	
	public IntValue getSampleRate() {
		if (getDistortionType() != DistortionType.BIT_CRASH)
			throw new IllegalArgumentException("Only applies to bit crash type.");
		return new SignedInt16BitValue(this, 0x05, 0, 127);
	}
	
	public IntValue getType() {
		if (getDistortionType() != DistortionType.DIST && getDistortionType() != DistortionType.FUZZ)
			throw new IllegalArgumentException("Only applies to distortion or fuzz types.");
		return new SignedInt16BitValue(this, 0x09, 1, 6) {
			@Override
			public int getValue() {
				return super.getValue() + 1;
			}
			@Override
			public void setValueNoCheck(int value) {
				super.setValueNoCheck(value - 1);
			}
		};
	}
	
	public IntValue getBitDown() {
		if (getDistortionType() != DistortionType.BIT_CRASH)
			throw new IllegalArgumentException("Only applies to bit crash type.");
		return new SignedInt16BitValue(this, 0x09, 0, 127);
	}
	
	public IntValue getFilter() {
		if (getDistortionType() != DistortionType.BIT_CRASH)
			throw new IllegalArgumentException("Only applies to bit crash type.");
		return new SignedInt16BitValue(this, 0x0D, 0, 127);
	}
	
	public IntValue getPresence() {
		if (getDistortionType() != DistortionType.DIST && getDistortionType() != DistortionType.FUZZ)
			throw new IllegalArgumentException("Only applies to distortion or fuzz types.");
		return new SignedInt16BitValue(this, 0x0D, 0, 127);
	}
	
}
