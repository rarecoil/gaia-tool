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

public class SystemParameters extends Parameters {
	
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
		super(addressMap);
		
		if (addressMap.length < 0x6E)
			throw new RuntimeException("Address map size mismatch.");
	}
	
	public Value getBankSelectMSB() {
		return new Value(addressMap[0x00], 0, 127);
	}
	
	public Value getBankSelectLSB() {
		return new Value(addressMap[0x01], 0, 127);
	}
	
	public Value getProgramNumber() {
		return new Value(addressMap[0x02], 0, 127);
	}
	
	public Value getMasterLevel() {
		return new Value(addressMap[0x03], 0, 127);
	}
	
	// -1000 ... 1000 (-100.0 ... 100.0 cent)
	public Value getMasterTune() {
		return new Value((addressMap[0x04] << 12 |
				addressMap[0x05] << 8 |
				addressMap[0x06] << 4 |
				addressMap[0x07]) - 1024, -1000, 1000);
	}
	
	public boolean getPatchRemain() {
		return addressMap[0x08] == 1;
	}
	
	public ClockSource getClockSource() {
		return ClockSource.values()[addressMap[0x09]];
	}
	
	public Value getSystemTempo() {
		return new Value(addressMap[0x0A] << 8 |
				addressMap[0x0B] << 4 |
				addressMap[0x0C], 5, 300);
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
	
	public Value getDBeamSens() {
		return new Value(addressMap[0x10], 1, 8);
	}
	
	public Value getRxTxChannel() {
		return new Value(addressMap[0x11] + 1, 0, 15);
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
	
	public Value getRecorderMetronomeLevel() {
		return new Value(addressMap[0x1C], 0, 7);
	}
	
	public Value getReserved1() {
		return new Value(addressMap[0x1D], 0, 1);
	}
	
	public Value getReserved2() {
		return new Value(addressMap[0x1E], 0, 127);
	}
	
	public Value getReserved3() {
		return new Value(addressMap[0x1F], 0, 127);
	}
	
	public Value getReserved4() {
		return new Value(addressMap[0x20], 0, 1);
	}
	
	public Value getReserved5() {
		return new Value(addressMap[0x21] - 64, -5, 6);
	}
	
	public Value getReserved6() {
		return new Value(addressMap[0x22] - 64, -3, 3);
	}
	
	public Value getReserved7() {
		return new Value(addressMap[0x23], 0, 127);
	}
	
	public Value getReserved8() {
		return new Value(addressMap[0x24], 0, 1);
	}
	
	public Value getReserved9() {
		return new Value(addressMap[0x25], 0, 1);
	}
	
	public Value getReserved10() {
		return new Value(addressMap[0x26], 0, 1);
	}
	
	public Value getReserved11() {
		return new Value(addressMap[0x27], 0, 1);
	}
	
	public Value getReserved12() {
		return new Value(addressMap[0x28], 0, 127);
	}
	
	public Value getReserved13() {
		return new Value(addressMap[0x29], 0, 127);
	}
	
	public Value getReserved14() {
		return new Value(addressMap[0x2A] - 64, -63, 63);
	}
	
	public boolean getWriteProtect(int bank, int patch) {
		if (bank < 0 || bank > 8 || patch < 0 || patch > 8)
			throw new RuntimeException("Invalid bank or patch number.");
		return addressMap[0x2B + bank * 8 + patch] == 1;
	}
	
	public PowerSaveMode getPowerSaveMode() {
		return PowerSaveMode.values()[addressMap[0x6B]];
	}
	
	public Value getReserved15() {
		return new Value(addressMap[0x6C], 0, 15);
	}
	
	public Value getReserved16() {
		return new Value(addressMap[0x6D], 0, 16);
	}
	
	public String toString() {
		return "System parameters:\n" +
				String.format("Bank select MSB: %s\n", getBankSelectMSB()) +
				String.format("Bank select LSB: %s\n", getBankSelectLSB()) +
				String.format("Program number: %s\n", getProgramNumber()) +
				String.format("Master level: %s\n", getMasterLevel()) +
				String.format("Master tune: %.1f cent\n", getMasterTune().getValue() / 10.0) +
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
