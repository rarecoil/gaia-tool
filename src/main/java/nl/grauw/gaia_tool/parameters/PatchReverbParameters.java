package nl.grauw.gaia_tool.parameters;

public class PatchReverbParameters {
	
	private byte[] addressMap;	// XXX: make AddressMap type
	
	public enum ReverbType {
		OFF, REVERB
	}
	
	public PatchReverbParameters(byte[] addressMap) {
		if (addressMap.length < 0x51)
			throw new RuntimeException("Address map size mismatch.");
		
		this.addressMap = addressMap;
	}
	
	public ReverbType getReverbType() {
		return ReverbType.values()[addressMap[0x00]];
	}
	
	// -20000 ... 20000
	public int getReverbParameter(int number) {
		if (number < 1 || number > 20)
			throw new RuntimeException("Invalid parameter number.");
		
		int index = (number - 1) * 4;
		return (addressMap[0x01 + index] << 12 |
				addressMap[0x02 + index] << 8 |
				addressMap[0x03 + index] << 4 |
				addressMap[0x04 + index]) - 32768;
	}
	
	public String toString() {
		StringBuilder reverbParameters = new StringBuilder(128);
		for (int i = 1; i <= 20; i++) {
			reverbParameters.append(getReverbParameter(i));
			reverbParameters.append(" ");
		}
		
		return "Patch reverb parameters:\n" +
				String.format("Reverb type: %s\n", getReverbType()) +
				String.format("Reverb parameters: %s\n", reverbParameters);
	}
	
}
