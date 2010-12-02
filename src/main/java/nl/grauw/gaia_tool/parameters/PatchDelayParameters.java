package nl.grauw.gaia_tool.parameters;

public class PatchDelayParameters {
	
	private byte[] addressMap;	// XXX: make AddressMap type
	
	public enum DelayType {
		OFF, DELAY, PANNING_DELAY
	}
	
	public PatchDelayParameters(byte[] addressMap) {
		if (addressMap.length < 0x51)
			throw new RuntimeException("Address map size mismatch.");
		
		this.addressMap = addressMap;
	}
	
	public DelayType getDelayType() {
		return DelayType.values()[addressMap[0x00]];
	}
	
	// -20000 ... 20000
	public int getDelayParameter(int number) {
		if (number < 1 || number > 20)
			throw new RuntimeException("Invalid parameter number.");
		
		int index = (number - 1) * 4;
		return (addressMap[0x01 + index] << 12 |
				addressMap[0x02 + index] << 8 |
				addressMap[0x03 + index] << 4 |
				addressMap[0x04 + index]) - 32768;
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
