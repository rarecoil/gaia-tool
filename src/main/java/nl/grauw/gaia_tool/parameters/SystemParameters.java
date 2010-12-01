package nl.grauw.gaia_tool.parameters;

public class SystemParameters {
	
	private byte[] addressMap;	// XXX: make AddressMap type
	
	public SystemParameters(byte[] addressMap) {
		if (addressMap.length != 0x6E)
			throw new RuntimeException("Address map size mismatch.");
		
		this.addressMap = addressMap;
	}
	
	// 0-16383
	public int getBankSelect() {
		return (int) addressMap[0x00] * 128 +
				(int) addressMap[0x01];
	}
	
	// 0-127
	public int getProgramNumber() {
		return addressMap[0x02];
	}
	
	// 0-127
	public int getMasterLevel() {
		return addressMap[0x02];
	}
	
	// 24-2024 (-100.0 - 100.0 cent)
	public int getMasterTune() {
		return (int) addressMap[0x04] * 4096 +
				(int) addressMap[0x04] * 256 +
				(int) addressMap[0x04] * 16 +
				(int) addressMap[0x04];
	}
	
	public boolean getPatchRemain() {
		return addressMap[0x08] == 1;
	}
	
	public boolean getClockSource() {
		return addressMap[0x09] == 1;
	}
	
	// 5-300 bpm
	public int getSystemTempo() {
		return (int) addressMap[0x0A] * 256 +
				(int) addressMap[0x0B] * 16 +
				(int) addressMap[0x0C];
	}
	
	public String toString() {
		return "System parameters:\n" +
				"Bank select: " + getBankSelect() + "\n" +
				"Program number: " + getProgramNumber() + "\n" +
				"Master level: " + getMasterLevel() + "\n" +
				"Master tune: " + getMasterTune() + "\n" +
				"Patch remain: " + getPatchRemain() + "\n" +
				"Clock source: " + getClockSource() + "\n" +
				"System tempo: " + getSystemTempo() + "\n";
	}
	
}
