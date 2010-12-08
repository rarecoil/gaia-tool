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
