package nl.grauw.gaia_tool.parameters;

public class PatchDistortionParameters {
	
	private byte[] addressMap;	// XXX: make AddressMap type
	
	public enum DistortionType {
		OFF, DIST, FUZZ, BIT_CRASH
	}
	
	public PatchDistortionParameters(byte[] addressMap) {
		if (addressMap.length < 0x81)
			throw new RuntimeException("Address map size mismatch.");
		
		this.addressMap = addressMap;
	}
	
	public DistortionType getDistortionType() {
		return DistortionType.values()[addressMap[0x00]];
	}
	
	// -20000 ... 20000
	public int getMFXParameter(int number) {
		if (number < 1 || number > 32)
			throw new RuntimeException("Invalid parameter number.");
		
		int index = (number - 1) * 4;
		return (addressMap[0x01 + index] << 12 |
				addressMap[0x02 + index] << 8 |
				addressMap[0x03 + index] << 4 |
				addressMap[0x04 + index]) - 32768;
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
