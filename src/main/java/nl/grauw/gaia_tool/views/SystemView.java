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
package nl.grauw.gaia_tool.views;

import nl.grauw.gaia_tool.Gaia;
import nl.grauw.gaia_tool.Parameters;
import nl.grauw.gaia_tool.parameters.System;

public class SystemView extends SingleParametersView {

	private static final long serialVersionUID = 1L;
	
	private Gaia gaia;
	
	public SystemView(Gaia g) {
		gaia = g;
		g.addObserver(this);
		initComponents();
	}

	@Override
	public Parameters getParameters() {
		return gaia.getSystem();
	}

	@Override
	public Gaia getGaia() {
		return gaia;
	}
	
	@Override
	public void loadParameters() {
		gaia.loadSystem();
	}

	@Override
	public void saveParameters() {
	}

	@Override
	public String getTitle() {
		return "System parameters";
	}
	
	@Override
	protected boolean isSyncShown() {
		return true;
	}

	@Override
	protected String getParametersText() {
		System s = gaia.getSystem();
		
		StringBuffer text = new StringBuffer();
		text.append(String.format("Bank select: %s (MSB: %s, LSB: %s)\n", s.getBankSelect(), s.getBankSelectMSB(), s.getBankSelectLSB()));
		text.append(String.format("Program number: %s\n", s.getProgramNumber()));
		text.append(String.format("Master level: %s\n", s.getMasterLevel()));
		text.append(String.format("Master tune: %.1f cent\n", s.getMasterTune().getValue() / 10.0));
		text.append(String.format("Patch remain: %s\n", s.getPatchRemain() ? "On" : "Off"));
		text.append(String.format("Clock source: %s\n", s.getClockSource()));
		text.append(String.format("System tempo: %s bpm\n", s.getSystemTempo()));
		text.append(String.format("Keyboard velocity: %s\n", s.getKeyboardVelocity()));
		text.append(String.format("Pedal polarity: %s\n", s.getPedalPolarity()));
		text.append(String.format("Pedal assign: %s\n", s.getPedalAssign()));
		text.append(String.format("D-Beam sens: %s\n", s.getDBeamSens()));
		text.append(String.format("Rx/Tx channel: %s\n", s.getRxTxChannel()));
		text.append(String.format("MIDI-USB thru: %s\n", s.getMidiUSBThru() ? "On" : "Off"));
		text.append(String.format("Soft thru: %s\n", s.getSoftThru() ? "On" : "Off"));
		text.append(String.format("Rx program change: %s\n", s.getRxProgramChange() ? "On" : "Off"));
		text.append(String.format("Rx bank select: %s\n", s.getRxBankSelect() ? "On" : "Off"));
		text.append(String.format("Remote keyboard: %s\n", s.getRemoteKeyboard() ? "On" : "Off"));
		text.append(String.format("Tx program change: %s\n", s.getTxProgramChange() ? "On" : "Off"));
		text.append(String.format("Tx bank select: %s\n", s.getTxBankSelect() ? "On" : "Off"));
		text.append(String.format("Tx edit data: %s\n", s.getTxEditData() ? "On" : "Off"));
		text.append(String.format("Recorder sync output: %s\n", s.getRecorderSyncOutput() ? "On" : "Off"));
		text.append(String.format("Recorder metronome mode: %s\n", s.getRecorderMetronomeMode()));
		text.append(String.format("Recorder metronome level: %s\n", s.getRecorderMetronomeLevel()));
		text.append(String.format("Power save mode: %s\n", s.getPowerSaveMode()));
		text.append(String.format("GM parts switch: %s\n", s.getGMPartsSwitch()));
		
		for (int bank = 0; bank <= 7; bank++) {
			text.append(String.format("Write protect bank %s: ", "ABCDEFGH".charAt(bank)));
			for (int patch = 0; patch <= 7; patch++) {
				text.append(s.getWriteProtect(bank, patch) ? "O" : "X");
			}
			text.append("\n");
		}
		
//		text.append(String.format("\nReserved 1: %s\n", s.getReserved1()));
//		text.append(String.format("Reserved 2: %s\n", s.getReserved2()));
//		text.append(String.format("Reserved 3: %s\n", s.getReserved3()));
//		text.append(String.format("Reserved 4: %s\n", s.getReserved4()));
//		text.append(String.format("Reserved 5: %s\n", s.getReserved5()));
//		text.append(String.format("Reserved 6: %s\n", s.getReserved6()));
//		text.append(String.format("Reserved 7: %s\n", s.getReserved7()));
//		text.append(String.format("Reserved 8: %s\n", s.getReserved8()));
//		text.append(String.format("Reserved 9: %s\n", s.getReserved9()));
//		text.append(String.format("Reserved 11: %s\n", s.getReserved11()));
//		text.append(String.format("Reserved 12: %s\n", s.getReserved12()));
//		text.append(String.format("Reserved 13: %s\n", s.getReserved13()));
//		text.append(String.format("Reserved 14: %s\n", s.getReserved14()));
//		text.append(String.format("Reserved 15: %s\n", s.getReserved15()));
//		text.append(String.format("Reserved 16: %s\n", s.getReserved16()));
		
		return text.toString();
	}

}
