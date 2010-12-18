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

import nl.grauw.gaia_tool.ParameterData;
import nl.grauw.gaia_tool.Parameters;
import nl.grauw.gaia_tool.SignedValue16Bit;
import nl.grauw.gaia_tool.Value;

public class Flanger extends Parameters {
	
	public enum FlangerType {
		OFF, FLANGER, PHASER, PITCH_SHIFTER
	}
	
	public Flanger(ParameterData parameterData) {
		super(parameterData);
		
		if (parameterData.getLength() < 0x51)
			throw new RuntimeException("Address map size mismatch.");
	}
	
	public FlangerType getFlangerType() {
		return FlangerType.values()[parameterData.getValue(0x00)];
	}
	
	public Value getFlangerParameter(int number) {
		if (number < 1 || number > 20)
			throw new RuntimeException("Invalid parameter number.");
		
		int index = (number - 1) * 4;
		return new SignedValue16Bit(parameterData, 0x01 + index, -20000, 20000);
	}
	
	public String toString() {
		StringBuilder flangerParameters = new StringBuilder(128);
		for (int i = 1; i <= 20; i++) {
			flangerParameters.append(getFlangerParameter(i));
			flangerParameters.append(" ");
		}
		
		return "Patch flanger parameters:\n" +
				String.format("Flanger type: %s\n", getFlangerType()) +
				String.format("Flanger parameters: %s\n", flangerParameters);
	}
	
}
