package nl.grauw.gaia_tool.parameters;

import nl.grauw.gaia_tool.Value;

public class PatchFlangerParameters {
	
	private byte[] addressMap;	// XXX: make AddressMap type
	
	public enum FlangerType {
		OFF, FLANGER, PHASER, PITCH_SHIFTER
	}
	
	public PatchFlangerParameters(byte[] addressMap) {
		if (addressMap.length < 0x51)
			throw new RuntimeException("Address map size mismatch.");
		
		this.addressMap = addressMap;
	}
	
	public FlangerType getFlangerType() {
		return FlangerType.values()[addressMap[0x00]];
	}
	
	public Value getFlangerParameter(int number) {
		if (number < 1 || number > 20)
			throw new RuntimeException("Invalid parameter number.");
		
		int index = (number - 1) * 4;
		return new Value((addressMap[0x01 + index] << 12 |
				addressMap[0x02 + index] << 8 |
				addressMap[0x03 + index] << 4 |
				addressMap[0x04 + index]) - 32768, -20000, 20000);
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
