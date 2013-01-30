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
package nl.grauw.gaia.tool.parameters;

import nl.grauw.gaia.tool.Address;
import nl.grauw.gaia.tool.Int12BitValue;
import nl.grauw.gaia.tool.Int16BitValue;
import nl.grauw.gaia.tool.IntValue;
import nl.grauw.gaia.tool.Parameters;
import nl.grauw.gaia.tool.SignedIntValue;

public class System extends Parameters {
	
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
	
	public System(Address address, byte[] data) {
		super(address, data);
		
		if (!address.equals(new Address(0x01, 0x00, 0x00, 0x00)))
			throw new Error("Invalid parameters address.");
		if (data.length < 0x6E)
			throw new IllegalArgumentException("Parameters data size mismatch.");
	}
	
	public IntValue getBankSelect() {
		return new IntValue(this, 0x00, 0, 16384) {
			@Override
			public int getValue() {
				return parameters.getValue(offset) << 7 | parameters.getValue(offset + 1);
			}
			@Override
			public void setValue(int value) {
				parameters.setValues(offset, value >> 7 & 0x7F, value & 0x7F);
			}
		};
	}
	
	public IntValue getBankSelectMSB() {
		return new IntValue(this, 0x00, 0, 127);
	}
	
	public IntValue getBankSelectLSB() {
		return new IntValue(this, 0x01, 0, 127);
	}
	
	public IntValue getProgramNumber() {
		return new IntValue(this, 0x02, 0, 127);
	}
	
	public IntValue getMasterLevel() {
		return new IntValue(this, 0x03, 0, 127);
	}
	
	// -1000 ... 1000 (-100.0 ... 100.0 cent)
	public IntValue getMasterTune() {
		return new Int16BitValue(this, 0x04, -1000, 1000) {
			@Override
			public int getValue() {
				return super.getValue() - 1024;
			}
			@Override
			public void setValueNoCheck(int value) {
				super.setValueNoCheck(value + 1024);
			}
		};
	}
	
	public boolean getPatchRemain() {
		return getValue(0x08) == 1;
	}
	
	public ClockSource getClockSource() {
		return ClockSource.values()[getValue(0x09)];
	}
	
	public IntValue getSystemTempo() {
		return new Int12BitValue(this, 0x0A, 5, 300);
	}
	
	public KeyboardVelocity getKeyboardVelocity() {
		return KeyboardVelocity.values()[getValue(0x0D)];
	}
	
	public PedalPolarity getPedalPolarity() {
		return PedalPolarity.values()[getValue(0x0E)];
	}
	
	public PedalAssign getPedalAssign() {
		return PedalAssign.values()[getValue(0x0F)];
	}
	
	public IntValue getDBeamSens() {
		return new IntValue(this, 0x10, 1, 8);
	}
	
	public IntValue getRxTxChannel() {
		return new IntValue(this, 0x11, 0, 15) {
			@Override
			public int getValue() {
				return super.getValue() + 1;
			}
			@Override
			public void setValueNoCheck(int value) {
				super.setValueNoCheck(value - 1);
			}
		};
	}
	
	public boolean getMidiUSBThru() {
		return getValue(0x12) == 1;
	}
	
	public boolean getSoftThru() {
		return getValue(0x13) == 1;
	}
	
	public boolean getRxProgramChange() {
		return getValue(0x14) == 1;
	}
	
	public boolean getRxBankSelect() {
		return getValue(0x15) == 1;
	}
	
	public boolean getRemoteKeyboard() {
		return getValue(0x16) == 1;
	}
	
	public boolean getTxProgramChange() {
		return getValue(0x17) == 1;
	}
	
	public boolean getTxBankSelect() {
		return getValue(0x18) == 1;
	}
	
	public boolean getTxEditData() {
		return getValue(0x19) == 1;
	}
	
	public void setTxEditData(boolean value) {
		setValue(0x19, value ? 1 : 0);
	}
	
	public boolean getRecorderSyncOutput() {
		return getValue(0x1A) == 1;
	}
	
	public RecorderMetronomeMode getRecorderMetronomeMode() {
		return RecorderMetronomeMode.values()[getValue(0x1B)];
	}
	
	public IntValue getRecorderMetronomeLevel() {
		return new IntValue(this, 0x1C, 0, 7);
	}
	
	public IntValue getReserved1() {
		return new IntValue(this, 0x1D, 0, 1);
	}
	
	public IntValue getReserved2() {
		return new IntValue(this, 0x1E, 0, 127);
	}
	
	public IntValue getReserved3() {
		return new IntValue(this, 0x1F, 0, 127);
	}
	
	public IntValue getReserved4() {
		return new IntValue(this, 0x20, 0, 1);
	}
	
	public IntValue getReserved5() {
		return new SignedIntValue(this, 0x21, -5, 6);
	}
	
	public IntValue getReserved6() {
		return new SignedIntValue(this, 0x22, -3, 3);
	}
	
	public IntValue getReserved7() {
		return new IntValue(this, 0x23, 0, 127);
	}
	
	public IntValue getReserved8() {
		return new IntValue(this, 0x24, 0, 1);
	}
	
	public IntValue getReserved9() {
		return new IntValue(this, 0x25, 0, 1);
	}
	
	public boolean getGMPartsSwitch() {
		return getValue(0x26) == 1;
	}
	
	public IntValue getReserved11() {
		return new IntValue(this, 0x27, 0, 1);
	}
	
	public IntValue getReserved12() {
		return new IntValue(this, 0x28, 0, 127);
	}
	
	public IntValue getReserved13() {
		return new IntValue(this, 0x29, 0, 127);
	}
	
	public IntValue getReserved14() {
		return new SignedIntValue(this, 0x2A, -63, 63);
	}
	
	public boolean getWriteProtect(int bank, int patch) {
		if (bank < 0 || bank > 7 || patch < 0 || patch > 7)
			throw new IllegalArgumentException("Invalid bank or patch number.");
		return getValue(0x2B + (bank << 3 | patch)) == 1;
	}
	
	public PowerSaveMode getPowerSaveMode() {
		return PowerSaveMode.values()[getValue(0x6B)];
	}
	
	public IntValue getReserved15() {
		return new IntValue(this, 0x6C, 0, 15);
	}
	
	public IntValue getReserved16() {
		return new IntValue(this, 0x6D, 0, 16);
	}
	
}
