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
package nl.grauw.gaia_tool;

public abstract class GaiaPatch extends Patch {
	
	private Gaia gaia;
	
	private Address lastRequestAddress;
	
	public GaiaPatch(Gaia gaia) {
		this.gaia = gaia;
	}
	
	public Gaia getGaia() {
		return gaia;
	}
	
	public Address getAddress() {
		return getAddress(0);
	}
	
	public abstract Address getAddress(int byte3);
	
	protected void loadData(Address address, int length) {
		if (!address.equals(lastRequestAddress)) {
			gaia.sendDataRequest(address, length);
		}
	}
	
	public void loadCommon() {
		loadData(getAddress(0x00), 0x3D);
	}
	
	public void loadTone(int number) {
		loadData(getAddress(0x01 + number - 1), 0x3E);
	}
	
	public void loadDistortion() {
		loadData(getAddress(0x04), 0x81);
	}
	
	public void loadFlanger() {
		loadData(getAddress(0x06), 0x51);
	}
	
	public void loadDelay() {
		loadData(getAddress(0x08), 0x51);
	}
	
	public void loadReverb() {
		loadData(getAddress(0x0A), 0x51);
	}
	
	public void loadArpeggioCommon() {
		loadData(getAddress(0x0C), 0x08);
	}
	
	public void loadArpeggioPattern(int note) {
		loadData(getAddress(0x0D + note - 1), 0x42);
	}
	
	public void loadArpeggioAll() {
		loadData(getAddress(0x0C), 0xB80);
	}
	
	/**
	 * Saves all modified parameters.
	 */
	public void saveModifiedParameters() {
		for (Parameters parameters : this) {
			if (parameters != null && parameters.hasChanged()) {
				gaia.sendDataTransmission(parameters);
			}
		}
	}
	
}
