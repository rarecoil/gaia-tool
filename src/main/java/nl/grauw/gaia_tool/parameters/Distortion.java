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
import nl.grauw.gaia_tool.Value;

public class Distortion extends Parameters {
	
	public enum DistortionType {
		OFF, DIST, FUZZ, BIT_CRASH
	}
	
	public Distortion(ParameterData parameterData) {
		super(parameterData);
		
		if (parameterData.getLength() < 0x81)
			throw new RuntimeException("Address map size mismatch.");
	}
	
	public DistortionType getDistortionType() {
		return DistortionType.values()[parameterData.getValue(0x00)];
	}
	
	public Value getMFXParameter(int number) {
		if (number < 1 || number > 32)
			throw new RuntimeException("Invalid parameter number.");
		
		int index = (number - 1) * 4;
		return new Value(parameterData.get16BitValue(0x01 + index) - 32768, -20000, 20000);
	}
	
	public String toString() {
		StringBuilder mfxParameters = new StringBuilder(128);
		for (int i = 1; i <= 32; i++) {
			mfxParameters.append(getMFXParameter(i));
			mfxParameters.append(" ");
		}
		
		return "Patch distortion parameters:\n" +
				String.format("Distortion type: %s\n", getDistortionType()) +
				String.format("MFX parameters: %s\n", mfxParameters);
	}
	
}
