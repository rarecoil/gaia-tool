package nl.grauw.gaia_tool.parameters;

public class SystemParameters {
	
	private byte[] addressMap;	// XXX: make AddressMap type
	
	public enum ClockSource {
		PATCH, SYSTEM, MIDI, USB
	}
	
	public enum KeyboardVelocity {
		REAL, FIX
	}
	
	public enum PedalPolarity {
		STANDARD, REVERSE
	}
	
	public enum PedalAssign {
		HOLD, MODULATION, VOLUME, EXPRESSION, BEND_MODE, D_BEAM_SYNC, TAP_TEMPO
	}
	
	public enum RecorderMetronomeMode {
		OFF, REC_ONLY, REC_AND_PLAY, ALWAYS
	}
	
	public enum PowerSaveMode {
		OFF(0), MIN_1(1), MIN_3(3), MIN_5(5), MIN_10(10), MIN_20(20), MIN_30(30), MIN_60(60);
		
		private final int minutes;
		
		PowerSaveMode(int minutes) {
			this.minutes = minutes;
		}
		
		public int getMinutes() {
			return this.minutes;
		}
		
		public String toString() {
			if (this.minutes == 0)
				return "Off";
			return "" + this.minutes + " min";
		}
	}
	
	public SystemParameters(byte[] addressMap) {
		if (addressMap.length != 0x6E)
			throw new RuntimeException("Address map size mismatch.");
		
		this.addressMap = addressMap;
	}
	
	// 0-16383
	public int getBankSelectMSB() {
		return addressMap[0x00];
	}
	
	public int getBankSelectLSB() {
		return addressMap[0x01];
	}
	
	// 0-127
	public int getProgramNumber() {
		return addressMap[0x02];
	}
	
	// 0-127
	public int getMasterLevel() {
		return addressMap[0x02];
	}
	
	// -1000 - 1000 (-100.0 - 100.0 cent)
	public int getMasterTune() {
		return (addressMap[0x04] << 12) +
				(addressMap[0x05] << 8) +
				(addressMap[0x06] << 4) +
				addressMap[0x07] - 1024;
	}
	
	public boolean getPatchRemain() {
		return addressMap[0x08] == 1;
	}
	
	public ClockSource getClockSource() {
		return ClockSource.values()[addressMap[0x09]];
	}
	
	// 5-300 bpm
	public int getSystemTempo() {
		return (addressMap[0x0A] << 8) +
				(addressMap[0x0B] << 4) +
				addressMap[0x0C];
	}
	
	public KeyboardVelocity getKeyboardVelocity() {
		return KeyboardVelocity.values()[addressMap[0x0D]];
	}
	
	public PedalPolarity getPedalPolarity() {
		return PedalPolarity.values()[addressMap[0x0E]];
	}
	
	public PedalAssign getPedalAssign() {
		return PedalAssign.values()[addressMap[0x0F]];
	}
	
	public int getDBeamSens() {
		return addressMap[0x10];
	}
	
	public int getRxTxChannel() {
		return addressMap[0x11] + 1;
	}
	
	public boolean getMidiUSBThru() {
		return addressMap[0x12] == 1;
	}
	
	public boolean getSoftThru() {
		return addressMap[0x13] == 1;
	}
	
	public boolean getRxProgramChange() {
		return addressMap[0x14] == 1;
	}
	
	public boolean getRxBankSelect() {
		return addressMap[0x15] == 1;
	}
	
	public boolean getRemoteKeyboard() {
		return addressMap[0x16] == 1;
	}
	
	public boolean getTxProgramChange() {
		return addressMap[0x17] == 1;
	}
	
	public boolean getTxBankSelect() {
		return addressMap[0x18] == 1;
	}
	
	public boolean getTxEditData() {
		return addressMap[0x19] == 1;
	}
	
	public boolean getRecorderSyncOutput() {
		return addressMap[0x1A] == 1;
	}
	
	public RecorderMetronomeMode getRecorderMetronomeMode() {
		return RecorderMetronomeMode.values()[addressMap[0x1B]];
	}
	
	public int getRecorderMetronomeLevel() {
		return addressMap[0x1C];
	}
	
	public int getReserved1() {
		return addressMap[0x1D];
	}
	
	public int getReserved2() {
		return addressMap[0x1E];
	}
	
	public int getReserved3() {
		return addressMap[0x1F];
	}
	
	public int getReserved4() {
		return addressMap[0x20];
	}
	
	public int getReserved5() {
		return addressMap[0x21] - 64;
	}
	
	public int getReserved6() {
		return addressMap[0x22] - 64;
	}
	
	public int getReserved7() {
		return addressMap[0x23];
	}
	
	public int getReserved8() {
		return addressMap[0x24];
	}
	
	public int getReserved9() {
		return addressMap[0x25];
	}
	
	public int getReserved10() {
		return addressMap[0x26];
	}
	
	public int getReserved11() {
		return addressMap[0x27];
	}
	
	public int getReserved12() {
		return addressMap[0x28];
	}
	
	public int getReserved13() {
		return addressMap[0x29];
	}
	
	public int getReserved14() {
		return addressMap[0x2A] - 64;
	}
	
	public int getWriteProtect(int bank, int patch) {
		if (bank < 0 || bank > 8 || patch < 0 || patch > 8)
			throw new RuntimeException("Invalid bank or patch number.");
		return addressMap[0x2B + (bank << 3) + patch];
	}
	
	public PowerSaveMode getPowerSaveMode() {
		return PowerSaveMode.values()[addressMap[0x6B]];
	}
	
	public int getReserved15() {
		return addressMap[0x6C];
	}
	
	public int getReserved16() {
		return addressMap[0x6D];
	}
	
	public String toString() {
		return "System parameters:\n" +
				String.format("Bank select MSB: %s\n", getBankSelectMSB()) +
				String.format("Bank select LSB: %s\n", getBankSelectLSB()) +
				String.format("Program number: %s\n", getProgramNumber()) +
				String.format("Master level: %s\n", getMasterLevel()) +
				String.format("Master tune: %.1f cent\n", getMasterTune() / 10.0) +
				String.format("Patch remain: %s\n", getPatchRemain() ? "On" : "Off") +
				String.format("Clock source: %s\n", getClockSource()) +
				String.format("System tempo: %s bpm\n", getSystemTempo()) +
				String.format("Keyboard velocity: %s\n", getKeyboardVelocity()) +
				String.format("Pedal polarity: %s\n", getPedalPolarity()) +
				String.format("Pedal assign: %s\n", getPedalAssign()) +
				String.format("D-Beam sens: %s\n", getDBeamSens()) +
				String.format("Rx/Tx channel: %s\n", getRxTxChannel()) +
				String.format("MIDI-USB thru: %s\n", getMidiUSBThru() ? "On" : "Off") +
				String.format("Soft thru: %s\n", getSoftThru() ? "On" : "Off") +
				String.format("Rx program change: %s\n", getRxProgramChange() ? "On" : "Off") +
				String.format("Rx bank select: %s\n", getRxBankSelect() ? "On" : "Off") +
				String.format("Remote keyboard: %s\n", getRemoteKeyboard() ? "On" : "Off") +
				String.format("Tx program change: %s\n", getTxProgramChange() ? "On" : "Off") +
				String.format("Tx bank select: %s\n", getTxBankSelect() ? "On" : "Off") +
				String.format("Tx edit data: %s\n", getTxEditData() ? "On" : "Off") +
				String.format("Recorder sync output: %s\n", getRecorderSyncOutput() ? "On" : "Off") +
				String.format("Recorder metronome mode: %s\n", getRecorderMetronomeMode()) +
				String.format("Recorder metronome level: %s\n", getRecorderMetronomeLevel()) +
				String.format("Power save mode: %s\n", getPowerSaveMode());
	}
	
}
