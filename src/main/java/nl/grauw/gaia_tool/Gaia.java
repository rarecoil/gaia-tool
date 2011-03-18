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

import java.util.Properties;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Transmitter;

import nl.grauw.gaia_tool.Note.NoteName;
import nl.grauw.gaia_tool.Parameters.ParameterChange;
import nl.grauw.gaia_tool.messages.ActiveSensingMessage;
import nl.grauw.gaia_tool.messages.ControlChangeMessage;
import nl.grauw.gaia_tool.messages.DataRequest1;
import nl.grauw.gaia_tool.messages.DataSet1;
import nl.grauw.gaia_tool.messages.GMSystemOn;
import nl.grauw.gaia_tool.messages.GM2SystemOn;
import nl.grauw.gaia_tool.messages.GMSystemOff;
import nl.grauw.gaia_tool.messages.IdentityReply;
import nl.grauw.gaia_tool.messages.IdentityRequest;
import nl.grauw.gaia_tool.messages.NoteOffMessage;
import nl.grauw.gaia_tool.messages.NoteOnMessage;
import nl.grauw.gaia_tool.messages.ProgramChangeMessage;
import nl.grauw.gaia_tool.mvc.Observable;
import nl.grauw.gaia_tool.mvc.Observer;
import nl.grauw.gaia_tool.parameters.System;

/**
 * Represents the Roland GAIA SH-01 synthesizer
 * 
 * Before use, the object should be initialised by calling open().
 * When you are done, you must invoke close() to clean up.
 * 
 * The GAIA actually has two synthesizers:
 * - The polyphonic virtual analog synthesizer (default: channel 0)
 * - A general midi synthesizer (default: channel 1 and higher)
 * 
 * The virtual analog synth channel can actually be configured with the
 * RX/TX channel setting so this value can be changed, and then the
 * GM synth will occupy the rest.
 */
public class Gaia extends Observable implements Observer {
	
	public static class GaiaNotFoundException extends Exception {
		private static final long serialVersionUID = 1L;
		
		public GaiaNotFoundException() {
			super("Gaia MIDI device(s) not found.");
		}
	}

	private boolean opened = false;
	private boolean identityConfirmed = false;
	
	private Receiver receiver;
	private Transmitter transmitter;
	private ResponseReceiver responseReceiver;
	
	private int device_id = 0;
	
	final static int synth_channel = 0;
	final static int gm_channel = 1;
	
	final static Note C_4 = new Note(NoteName.C, 4);
	
	private Log log;
	private Properties settings;
	
	private System system;
	private TemporaryPatch temporaryPatch;
	private UserPatch[] userPatches = new UserPatch[64];
	
	public Gaia(Log log, Properties settings) {
		this.log = log;
		this.settings = settings;
		
		temporaryPatch = new TemporaryPatch(this);
		for (int bank = 0; bank < 8; bank++) {
			for (int patch = 0; patch < 8; patch++) {
				userPatches[bank << 3 | patch] = new UserPatch(this, bank, patch);
			}
		}
		responseReceiver = new ResponseReceiver(this);
	}
	
	public boolean getSynchronize() {
		if (system != null)
			return system.getTxEditData();
		return false;
	}
	
	public void setSynchronize(boolean sync) {
		if (system == null) {
			throw new RuntimeException("Can’t set synchronization mode before system data is loaded.");
		}
		system.setTxEditData(sync);
		if (sync == false) {
			// manually send sync parameter
			sendDataTransmission(system, 0x19, 1);
		}
		notifyObservers("synchronize");
	}
	
	@Override
	public void update(Observable source, Object detail) {
		if (source instanceof Parameters && detail instanceof ParameterChange) {
			update((Parameters) source, (ParameterChange) detail);
		}
	}
	
	private void update(Parameters source, ParameterChange arg) {
		if (getSynchronize() && source.hasChanged(arg)) {
			sendDataTransmission(source, arg.getOffset(), arg.getLength());
		}
		if (source == system && arg.includes(0x19)) {	// Tx edit data
			notifyObservers("synchronize");
		}
	}
	
	/**
	 * Return whether the gaia device is currently opened.
	 * @return True if it is opened.
	 */
	public boolean isOpened() {
		return opened;
	}
	
	/**
	 * Return whether an identity request response has been received,
	 * identifying the GAIA’s presence. The device_id will also be initialised.
	 * @return True if the GAIA’s identity has been confirmed.
	 */
	public boolean isIdentityConfirmed() {
		return identityConfirmed;
	}
	
	/**
	 * Return the device ID of the GAIA.
	 * Will be 0 if the GAIA’s identity has not been confirmed yet.
	 * @return The GAIA’s device ID, or 0.
	 */
	public int getDeviceID() {
		return device_id;
	}
	
	/**
	 * Initialises the system.
	 * @throws GaiaNotFoundException 
	 */
	public void open() throws MidiUnavailableException, GaiaNotFoundException {
		if (opened)
			throw new RuntimeException("GAIA is already opened.");
		
		MidiDevice input = getMidiInput();
		MidiDevice output = getMidiOutput();
		
		if (input == null || output == null)
			throw new GaiaNotFoundException();
		
		log.log("Midi IN: " + input.getDeviceInfo());
		log.log("Midi OUT: " + output.getDeviceInfo());
		log.log("");
		
		input.open();
		output.open();
		transmitter = input.getTransmitter();
		transmitter.setReceiver(responseReceiver);
		receiver = output.getReceiver();
		
		opened = true;
		notifyObservers("opened");
		
		requestIdentity();
		loadSystem();
	}
	
	/**
	 * Cleans up the system.
	 */
	public void close() {
		MidiDevice input = getMidiInput();
		MidiDevice output = getMidiOutput();
		if (input != null)
			input.close();
		if (output != null)
			output.close();
		if (receiver != null)
			receiver.close();
		if (transmitter != null)
			transmitter.close();
		if (responseReceiver != null)
			responseReceiver.close();
		
		opened = false;
		identityConfirmed = false;
		device_id = 0;
		notifyObservers("opened");
		notifyObservers("identityConfirmed");
		notifyObservers("device_id");
	}
	
	public String getDefaultMidiInput() {
		return settings.getProperty("midi.input");
	}
	
	public void setDefaultMidiInput(String inputName) {
		if (inputName != null) {
			settings.setProperty("midi.input", inputName);
		} else {
			settings.remove("midi.input");
		}
	}
	
	public String getDefaultMidiOutput() {
		return settings.getProperty("midi.output");
	}
	
	public void setDefaultMidiOutput(String outputName) {
		if (outputName != null) {
			settings.setProperty("midi.output", outputName);
		} else {
			settings.remove("midi.output");
		}
	}
	
	/**
	 * Get the MIDI input port that receives messages from the GAIA.
	 * @return A MIDI input device, or null.
	 */
	public MidiDevice getMidiInput() {
		String name = settings.getProperty("midi.input");
		if (name == null) {
			return autoDetectMidiInput();
		}
		
		for (MidiDevice.Info mdi : MidiSystem.getMidiDeviceInfo()) {
			if (mdi.getName().contains(name)) {
				try {
					MidiDevice md = MidiSystem.getMidiDevice(mdi);
					if (md.getMaxTransmitters() != 0 && !(md instanceof Sequencer) && !(md instanceof Synthesizer))
						return md;
				} catch (MidiUnavailableException e) {
				}
			}
		}
		return null;
	}
	
	private MidiDevice autoDetectMidiInput() {
		for (MidiDevice.Info mdi : MidiSystem.getMidiDeviceInfo()) {
			if (mdi.getName().contains("SH-01")) {
				try {
					MidiDevice md = MidiSystem.getMidiDevice(mdi);
					if (md.getMaxTransmitters() != 0 && !(md instanceof Sequencer) && !(md instanceof Synthesizer))
						return md;
				} catch (MidiUnavailableException e) {
				}
			}
		}
		return null;
	}
	
	/**
	 * Get the MIDI output port that sends messages to the GAIA.
	 * @return A MIDI output device, or null.
	 */
	public MidiDevice getMidiOutput() {
		String name = settings.getProperty("midi.output");
		if (name == null) {
			return autoDetectMidiOutput();
		}
		
		for (MidiDevice.Info mdi : MidiSystem.getMidiDeviceInfo()) {
			if (mdi.getName().contains(name)) {
				try {
					MidiDevice md = MidiSystem.getMidiDevice(mdi);
					if (md.getMaxReceivers() != 0 && !(md instanceof Sequencer) && !(md instanceof Synthesizer)) {
						return md;
					}
				} catch (MidiUnavailableException e) {
				}
			}
		}
		return null;
	}
	
	public MidiDevice autoDetectMidiOutput() {
		for (MidiDevice.Info mdi : MidiSystem.getMidiDeviceInfo()) {
			if (mdi.getName().contains("SH-01")) {
				try {
					MidiDevice md = MidiSystem.getMidiDevice(mdi);
					if (md.getMaxReceivers() != 0 && !(md instanceof Sequencer) && !(md instanceof Synthesizer)) {
						return md;
					}
				} catch (MidiUnavailableException e) {
				}
			}
		}
		return null;
	}
	
	/**
	 * Sends a MidiMessage to the GAIA.
	 * @param message
	 */
	public void send(MidiMessage message) {
		if (!opened)
			throw new RuntimeException("MIDI connection not open.");
		
		receiver.send(message, -1);
		log.log("Sent: " + message);
	}
	
	/**
	 * Receives an incoming MidiMessage object from the ResponseReceiver.
	 * @param message
	 */
	public void receive(MidiMessage message) {
		if (!(message instanceof ActiveSensingMessage)) {
			log.log("Received: " + message);
		}
		
		if (message instanceof IdentityReply) {
			confirmIdentity((IdentityReply) message);
		} else if (message instanceof DataSet1) {
			updateParameters((DataSet1) message);
		} else if (message instanceof ControlChangeMessage) {
			updateParameters((ControlChangeMessage) message);
		} else if (message instanceof ProgramChangeMessage) {
			if (getSynchronize()) {
				temporaryPatch.clearParameters();
			}
		}
	}
	
	private void confirmIdentity(IdentityReply ir) {
		int[] familyCode = ir.getDeviceFamilyCode();
		int[] familyNumberCode = ir.getDeviceFamilyNumberCode();
		if (ir.getIdNumber() == 0x41 &&	// Roland ID
				familyCode[0] == 0x41 && familyCode[1] == 0x02 &&
				familyNumberCode[0] == 0x00 && familyNumberCode[1] == 0x00) {
			device_id = ir.getDeviceId();
			identityConfirmed = true;
			notifyObservers("identityConfirmed");
			notifyObservers("device_id");
		}
	}
	
	private void updateParameters(DataSet1 message) {
		updateParameters(message.getAddress(), message.getDataSet());
	}
	
	private void updateParameters(ControlChangeMessage message) {
		if (getSynchronize()) {
			temporaryPatch.updateParameters(message);
		}
	}
	
	public void updateParameters(Address address, byte[] data) {
		int byte1 = address.getByte1();
		if (byte1 == 0x01 && address.getByte2() == 0x00 && address.getByte3() == 0x00) {
			if (address.getByte4() == 0x00 && data.length >= 0x6E) {
				system = new System(address, data);
				system.addObserver(this);
				notifyObservers("system");
			} else if (system != null) {
				system.updateParameters(address, data);
			}
		} else if (byte1 == 0x10) {
			temporaryPatch.updateParameters(address, data);
		} else if (byte1 == 0x20) {
			userPatches[address.getByte2()].updateParameters(address, data);
		} else {
			throw new IllegalArgumentException("Address not recognised.");
		}
	}
	
	public System getSystem() {
		return system;
	}
	
	public void loadSystem() {
		sendDataRequest(new Address(0x01, 0x00, 0x00, 0x00), 0x6E);
	}
	
	public TemporaryPatch getTemporaryPatch() {
		return temporaryPatch;
	}
	
	public UserPatch getUserPatch(int bank, int patch) {
		if (bank < 0 || bank > 7)
			throw new IllegalArgumentException("Invalid bank number.");
		if (patch < 0 || patch > 7)
			throw new IllegalArgumentException("Invalid patch number.");
		return userPatches[bank << 3 | patch];
	}
	
	/**
	 * Plays a C-4 note for one second.
	 */
	public void playTestNote() {
		try {
			send(new ProgramChangeMessage(synth_channel, 0));
			send(new NoteOnMessage(synth_channel, C_4, 127));
			
			try {
				Thread.sleep(1000);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
			
			send(new NoteOffMessage(synth_channel, C_4, 127));
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Plays a C-4 note for one second on the GM channel.
	 */
	public void playGMTestNote() {
		try {
			send(new ProgramChangeMessage(gm_channel, 0));
			send(new NoteOnMessage(gm_channel, C_4, 127));
			
			try {
				Thread.sleep(1000);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
			
			send(new NoteOffMessage(gm_channel, C_4, 127));
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Requests the device identity.
	 */
	public void requestIdentity() {
		try {
			send(new IdentityRequest());
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sends a GM system on directive.
	 */
	public void sendGM1SystemOn() {
		try {
			send(new GMSystemOn());
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sends a GM2 system on directive.
	 */
	public void sendGM2SystemOn() {
		try {
			send(new GM2SystemOn());
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sends a GM system off directive.
	 */
	public void sendGMSystemOff() {
		try {
			send(new GMSystemOff());
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sends a data request directive.
	 * @param address The start address of the desired data.
	 * @param length The length of the desired data.
	 */
	public void sendDataRequest(Address address, int length) {
		try {
			send(new DataRequest1(address, length));
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sends a data transmission.
	 * Also updates the parameters to reflect the GAIA’s new state.
	 * @param parameters The Parameters object containing the address and data to send.
	 */
	public void sendDataTransmission(Parameters parameters) {
		sendDataTransmission(parameters, 0, parameters.getLength());
	}
	
	/**
	 * Sends a data transmission.
	 * Also updates the parameters to reflect the GAIA’s new state.
	 * @param parameters The Parameters object containing the address and data to send.
	 * @param offset The start offset of the parameter data to send.
	 * @param length The length of the parameter data to send.
	 */
	public void sendDataTransmission(Parameters parameters, int offset, int length) {
		Address address = parameters.getAddress().add(offset);
		byte[] data = parameters.getData(offset, length);
		try {
			send(new DataSet1(address, data));
			parameters.updateOriginalParameters(address, data);
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
	}
	
}
