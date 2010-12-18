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
package nl.grauw.gaia_tool;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;

import nl.grauw.gaia_tool.Note.NoteName;
import nl.grauw.gaia_tool.messages.DataRequest1;
import nl.grauw.gaia_tool.messages.DataSet1;
import nl.grauw.gaia_tool.messages.GMSystemOn;
import nl.grauw.gaia_tool.messages.GM2SystemOn;
import nl.grauw.gaia_tool.messages.GMSystemOff;
import nl.grauw.gaia_tool.messages.IdentityRequest;
import nl.grauw.gaia_tool.messages.NoteOffMessage;
import nl.grauw.gaia_tool.messages.NoteOnMessage;
import nl.grauw.gaia_tool.messages.ProgramChangeMessage;
import nl.grauw.gaia_tool.mvc.Observable;
import nl.grauw.gaia_tool.parameters.System;

/**
 * Represents the Roland GAIA SH-01 synthesizer
 * 
 * Before use, the object should be initialised by calling open().
 * When you are done, you must invoke close() to clean up.
 * 
 * The GAIA actually has three synthesizers:
 * - The polyphonic virtual analog synthesizer (default: channel 0)
 * - A general midi synthesizer (default: channel 1)
 * - A second general midi synthesizer (default: channel 2 and higher (!))
 * 
 * The virtual analog synth channel can actually be configured with the
 * RX/TX channel setting so this value can be changed, I’m assuming in
 * that case the 1st GM synth will move to channel 0 and the 2nd one
 * will also adjust accordingly.
 */
public class Gaia extends Observable {

	private MidiDevice midi_in;
	private MidiDevice midi_out;
	private Receiver receiver;
	private Transmitter transmitter;
	private ResponseReceiver responseReceiver;
	
	final static int synth_channel = 0;
	final static int gm_channel = 1;
	
	final static Note C_4 = new Note(NoteName.C, 4);
	
	private Log log;
	
	private System system;
	private Patch temporaryPatch;
	private Patch[] userPatches = new Patch[64];
	
	public Gaia() {
		temporaryPatch = new Patch(this);
		for (int bank = 0; bank < 8; bank++) {
			for (int patch = 0; patch < 8; patch++) {
				userPatches[bank << 3 | patch] = new Patch(this, bank, patch);
			}
		}
		log = new Log();
		responseReceiver = new ResponseReceiver(this);
	}

	public Log getLog() {
		return log;
	}
	
	/**
	 * Initialises the system.
	 */
	public void open() throws MidiUnavailableException {
		discoverGaiaDevices();
		midi_in.open();
		midi_out.open();
		receiver = midi_in.getReceiver();
		transmitter = midi_out.getTransmitter();
		transmitter.setReceiver(responseReceiver);
	}
	
	/**
	 * Cleans up the system.
	 */
	public void close() {
		midi_in.close();
		midi_out.close();
		receiver.close();
		transmitter.close();
		responseReceiver.close();
	}
	
	private void discoverGaiaDevices() throws MidiUnavailableException {
		MidiDevice.Info[] mdi_arr = MidiSystem.getMidiDeviceInfo();
		
		for (MidiDevice.Info mdi : mdi_arr) {
			if (mdi.getName().contains("SH-01")) {
				MidiDevice md = MidiSystem.getMidiDevice(mdi);
				if (md.getMaxTransmitters() == 0) {
					midi_in = md;
					log.log("Found (IN): " + mdi);
				} else {
					midi_out = md;
					log.log("Found (OUT): " + mdi);
				}
			}
		}

		if (midi_in == null || midi_out == null) {
			throw new MidiUnavailableException("GAIA MIDI devices not found.");
		}
		
		log.log("");
	}
	
	/**
	 * Receives an incoming MidiMessage object from the ResponseReceiver.
	 * @param mm
	 */
	void receive(MidiMessage mm) {
		log.log("Received: " + mm);
		
		if (mm instanceof DataSet1) {
			receive((DataSet1) mm);
		}
	}
	
	private void receive(DataSet1 mm) {
		updateParameters(new ParameterData(mm.getAddress(), mm.getDataSet()));
	}
	
	public Parameters updateParameters(ParameterData parameterData) {
		int byte1 = parameterData.getAddress().getByte1();
		if (byte1 == 0x01) {
			system = new System(parameterData);
			notifyObservers("system");
			return system;
		} else if (byte1 == 0x10) {
			return temporaryPatch.updateParameters(parameterData);
		} else if (byte1 == 0x20) {
			return userPatches[parameterData.getAddress().getByte2()].updateParameters(parameterData);
		} else {
			throw new RuntimeException("Address not recognised.");
		}
	}
	
	public System getSystem() {
		return system;
	}
	
	public void loadSystem() {
		try {
			sendDataRequest(new Address(0x01, 0x00, 0x00, 0x00), 0x6E);
		} catch(InvalidMidiDataException ex) {
			ex.printStackTrace();
		}
	}
	
	public Patch getTemporaryPatch() {
		return temporaryPatch;
	}
	
	public Patch getUserPatch(int bank, int patch) {
		return userPatches[bank << 3 | patch];
	}
	
	/**
	 * Plays a C-4 note for one second.
	 */
	public void playTestNote() throws InvalidMidiDataException {
		receiver.send(new ProgramChangeMessage(synth_channel, 0), -1);
		receiver.send(new NoteOnMessage(synth_channel, C_4, 127), -1);
		
		try {
			Thread.sleep(1000);
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
		
		receiver.send(new NoteOffMessage(synth_channel, C_4, 127), -1);
	}
	
	/**
	 * Plays a C-4 note for one second on the GM channel.
	 */
	public void playGMTestNote() throws InvalidMidiDataException {
		receiver.send(new ProgramChangeMessage(gm_channel, 0), -1);
		receiver.send(new NoteOnMessage(gm_channel, C_4, 127), -1);
		
		try {
			Thread.sleep(1000);
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
		
		receiver.send(new NoteOffMessage(gm_channel, C_4, 127), -1);
	}
	
	/**
	 * Requests the device identity.
	 * @throws InvalidMidiDataException 
	 */
	public void requestIdentity() throws InvalidMidiDataException {
		receiver.send(new IdentityRequest(), -1);
	}
	
	/**
	 * Sends a GM system on directive.
	 * @throws InvalidMidiDataException 
	 */
	public void sendGM1SystemOn() throws InvalidMidiDataException {
		receiver.send(new GMSystemOn(), -1);
	}
	
	/**
	 * Sends a GM2 system on directive.
	 * @throws InvalidMidiDataException 
	 */
	public void sendGM2SystemOn() throws InvalidMidiDataException {
		receiver.send(new GM2SystemOn(), -1);
	}
	
	/**
	 * Sends a GM system off directive.
	 * @throws InvalidMidiDataException 
	 */
	public void sendGMSystemOff() throws InvalidMidiDataException {
		receiver.send(new GMSystemOff(), -1);
	}
	
	/**
	 * Sends a data request directive.
	 * @param address The start address of the desired data.
	 * @param length The length of the desired data.
	 * @throws InvalidMidiDataException
	 */
	public void sendDataRequest(Address address, int length) throws InvalidMidiDataException {
		receiver.send(new DataRequest1(address, length), -1);
	}
	
}
