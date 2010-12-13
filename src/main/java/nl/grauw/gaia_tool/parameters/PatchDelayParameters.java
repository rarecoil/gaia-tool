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

import nl.grauw.gaia_tool.Value;

public class PatchDelayParameters extends Parameters {
	
	public enum DelayType {
		OFF, DELAY, PANNING_DELAY
	}
	
	public PatchDelayParameters(byte[] addressMap) {
		super(addressMap);
		
		if (addressMap.length < 0x51)
			throw new RuntimeException("Address map size mismatch.");
	}
	
	public DelayType getDelayType() {
		return DelayType.values()[addressMap[0x00]];
	}
	
	public Value getDelayParameter(int number) {
		if (number < 1 || number > 20)
			throw new RuntimeException("Invalid parameter number.");
		
		int index = (number - 1) * 4;
		return new Value((addressMap[0x01 + index] << 12 |
				addressMap[0x02 + index] << 8 |
				addressMap[0x03 + index] << 4 |
				addressMap[0x04 + index]) - 32768, -20000, 20000);
	}
	
	public String toString() {
		StringBuilder delayParameters = new StringBuilder(128);
		for (int i = 1; i <= 20; i++) {
			delayParameters.append(getDelayParameter(i));
			delayParameters.append(" ");
		}
		
		return "Patch delay parameters:\n" +
				String.format("Delay type: %s\n", getDelayType()) +
				String.format("Delay parameters: %s\n", delayParameters);
	}
	
}
