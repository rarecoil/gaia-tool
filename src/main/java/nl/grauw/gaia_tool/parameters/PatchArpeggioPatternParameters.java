package nl.grauw.gaia_tool.parameters;

import nl.grauw.gaia_tool.Value;

public class PatchArpeggioPatternParameters {
	
	private byte[] addressMap;	// XXX: make AddressMap type
	
	public PatchArpeggioPatternParameters(byte[] addressMap) {
		if (addressMap.length < 0x42)
			throw new RuntimeException("Address map size mismatch.");
		
		this.addressMap = addressMap;
	}
	
	public Value getOriginalNote() {
		return new Value(addressMap[0x00] << 4 | addressMap[0x01], 0, 128);
	}
	
	public Value getStepData(int step) {
		if (step < 1 || step > 32)
			throw new RuntimeException("Invalid step number.");
		
		return new Value(addressMap[step * 2] << 4 | addressMap[step * 2 + 1], 0, 128);
	}
	
	public String toString() {
		StringBuilder stepData = new StringBuilder(128);
		for (int i = 1; i <= 32; i++) {
			stepData.append(getStepData(i));
			stepData.append(" ");
		}
		
		return "Patch arpeggio pattern parameters:\n" +
				String.format("Original note: %s\n", getOriginalNote()) +
				String.format("Step data: %s\n", stepData);
	}
	
}
