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
		
		return String.format("Bank select: %s (MSB: %s, LSB: %s)\n", s.getBankSelect(), s.getBankSelectMSB(), s.getBankSelectLSB()) +
				String.format("Program number: %s\n", s.getProgramNumber()) +
				String.format("Master level: %s\n", s.getMasterLevel()) +
				String.format("Master tune: %.1f cent\n", s.getMasterTune().getValue() / 10.0) +
				String.format("Patch remain: %s\n", s.getPatchRemain() ? "On" : "Off") +
				String.format("Clock source: %s\n", s.getClockSource()) +
				String.format("System tempo: %s bpm\n", s.getSystemTempo()) +
				String.format("Keyboard velocity: %s\n", s.getKeyboardVelocity()) +
				String.format("Pedal polarity: %s\n", s.getPedalPolarity()) +
				String.format("Pedal assign: %s\n", s.getPedalAssign()) +
				String.format("D-Beam sens: %s\n", s.getDBeamSens()) +
				String.format("Rx/Tx channel: %s\n", s.getRxTxChannel()) +
				String.format("MIDI-USB thru: %s\n", s.getMidiUSBThru() ? "On" : "Off") +
				String.format("Soft thru: %s\n", s.getSoftThru() ? "On" : "Off") +
				String.format("Rx program change: %s\n", s.getRxProgramChange() ? "On" : "Off") +
				String.format("Rx bank select: %s\n", s.getRxBankSelect() ? "On" : "Off") +
				String.format("Remote keyboard: %s\n", s.getRemoteKeyboard() ? "On" : "Off") +
				String.format("Tx program change: %s\n", s.getTxProgramChange() ? "On" : "Off") +
				String.format("Tx bank select: %s\n", s.getTxBankSelect() ? "On" : "Off") +
				String.format("Tx edit data: %s\n", s.getTxEditData() ? "On" : "Off") +
				String.format("Recorder sync output: %s\n", s.getRecorderSyncOutput() ? "On" : "Off") +
				String.format("Recorder metronome mode: %s\n", s.getRecorderMetronomeMode()) +
				String.format("Recorder metronome level: %s\n", s.getRecorderMetronomeLevel()) +
				String.format("Power save mode: %s\n", s.getPowerSaveMode());
	}

}
