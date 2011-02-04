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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
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

	private boolean opened = false;
	
	private MidiDevice midi_in;	// port where messages are received from the GAIA
	private MidiDevice midi_out;	// port where messages are sent to the GAIA
	private Receiver receiver;
	private Transmitter transmitter;
	private ResponseReceiver responseReceiver;
	
	final static int synth_channel = 0;
	final static int gm_channel = 1;
	
	final static Note C_4 = new Note(NoteName.C, 4);
	final static Charset UTF8 = Charset.forName("UTF-8");
	
	private Log log;
	
	private System system;
	private TemporaryPatch temporaryPatch;
	private UserPatch[] userPatches = new UserPatch[64];
	
	public Gaia() {
		temporaryPatch = new TemporaryPatch(this);
		for (int bank = 0; bank < 8; bank++) {
			for (int patch = 0; patch < 8; patch++) {
				userPatches[bank << 3 | patch] = new UserPatch(this, bank, patch);
			}
		}
		log = new Log();
		responseReceiver = new ResponseReceiver(this);
	}

	public Log getLog() {
		return log;
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
			sendDataTransmission(new Parameters(system.getAddress().add(0x19),
							new byte[] {(byte) system.getValue(0x19)}));
		}
		notifyObservers("synchronize");
	}
	
	@Override
	public void update(Observable source, Object arg) {
		if (source instanceof Parameters && arg instanceof ParameterChange) {
			update((Parameters) source, (ParameterChange) arg);
		}
	}
	
	private void update(Parameters source, ParameterChange arg) {
		if (getSynchronize() && !arg.fromUpdate()) {
			Address address = source.getAddress().add(arg.getOffset());
			byte[] data = source.getData(arg.getOffset(), arg.getLength());
			sendDataTransmission(new Parameters(address, data));
		}
	}
	
	/**
	 * Return whether the gaia device can be opened.
	 * This tests whether the MIDI devices have been properly configured.
	 * @return True if it can be opened.
	 */
	public boolean canOpen() {
		return midi_in != null && midi_out != null;
	}
	
	/**
	 * Return whether the gaia device is currently opened.
	 * @return True if it is opened.
	 */
	public boolean isOpened() {
		return opened;
	}
	
	/**
	 * Initialises the system.
	 */
	public void open() throws MidiUnavailableException {
		if (!canOpen())
			throw new RuntimeException("MIDI devices not configured.");
		
		log.log("Midi IN: " + midi_in.getDeviceInfo());
		log.log("Midi OUT: " + midi_in.getDeviceInfo());
		log.log("");
		
		midi_in.open();
		midi_out.open();
		transmitter = midi_in.getTransmitter();
		transmitter.setReceiver(responseReceiver);
		receiver = midi_out.getReceiver();
		
		opened = true;
		notifyObservers("opened");
		
		requestIdentity();
		loadSystem();
	}
	
	/**
	 * Cleans up the system.
	 */
	public void close() {
		if (midi_in != null)
			midi_in.close();
		if (midi_out != null)
			midi_out.close();
		if (receiver != null)
			receiver.close();
		if (transmitter != null)
			transmitter.close();
		if (responseReceiver != null)
			responseReceiver.close();
		
		opened = false;
		notifyObservers("opened");
	}
	
	public MidiDevice getMidiInput() {
		return midi_in;
	}
	
	public MidiDevice getMidiOutput() {
		return midi_out;
	}
	
	public void autoDetectMIDIDevices() {
		setMIDIDevices(null, null);
	}
	
	public void setMIDIDevices(MidiDevice input, MidiDevice output) {
		if (opened)
			throw new RuntimeException("GAIA device already opened.");
		
		if (input == null)
			input = discoverInputMIDIDevice();
		if (output == null)
			output = discoverOutputMIDIDevice();
		
		midi_in = input;
		midi_out = output;
	}
	
	private MidiDevice discoverInputMIDIDevice() {
		MidiDevice.Info[] devicesInfo = MidiSystem.getMidiDeviceInfo();
		
		for (MidiDevice.Info mdi : devicesInfo) {
			if (mdi.getName().contains("SH-01")) {
				try {
					MidiDevice md = MidiSystem.getMidiDevice(mdi);
					if (md.getMaxTransmitters() != 0)
						return md;
				} catch (MidiUnavailableException e) {
				}
			}
		}
		return null;
	}
	
	private MidiDevice discoverOutputMIDIDevice() {
		MidiDevice.Info[] devicesInfo = MidiSystem.getMidiDeviceInfo();
		
		for (MidiDevice.Info mdi : devicesInfo) {
			if (mdi.getName().contains("SH-01")) {
				try {
					MidiDevice md = MidiSystem.getMidiDevice(mdi);
					if (md.getMaxReceivers() != 0) {
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
		
		if (message instanceof DataSet1) {
			updateParameters((DataSet1) message);
		} else if (message instanceof ControlChangeMessage) {
			updateParameters((ControlChangeMessage) message);
		} else if (message instanceof ProgramChangeMessage) {
			if (getSynchronize()) {
				temporaryPatch.clearParameters();
			}
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
	
	public Patch getTemporaryPatch() {
		return temporaryPatch;
	}
	
	public Patch getUserPatch(int bank, int patch) {
		if (bank < 0 || bank > 7)
			throw new IllegalArgumentException("Invalid bank number.");
		if (patch < 0 || patch > 7)
			throw new IllegalArgumentException("Invalid patch number.");
		return userPatches[bank << 3 | patch];
	}
	
	public void savePatch(Patch patch) {
		if (patch.isComplete()) {
			File patchFile = getPatchPath(patch);
			FileOutputStream fos;
			try {
				patchFile.createNewFile();
				fos = new FileOutputStream(patchFile);
				fos.write("GAIATOOL".getBytes(UTF8));
				fos.write("v1".getBytes(UTF8));
				writeParameterData(fos, patch.getCommon());
				writeParameterData(fos, patch.getTone(1));
				writeParameterData(fos, patch.getTone(2));
				writeParameterData(fos, patch.getTone(3));
				writeParameterData(fos, patch.getDistortion());
				writeParameterData(fos, patch.getFlanger());
				writeParameterData(fos, patch.getDelay());
				writeParameterData(fos, patch.getReverb());
				writeParameterData(fos, patch.getArpeggioCommon());
				for (int note = 1; note <= 16; note++) {
					writeParameterData(fos, patch.getArpeggioPattern(note));
				}
				fos.close();
				log.log("Patch data saved to " + patchFile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			throw new RuntimeException("Can not save, because not all patch parameters are loaded.");
		}
	}
	
	/**
	 * Writes a parameter chunk to the output stream.
	 * Format:
	 * 
	 *   0x00: Address, 7-bit notation
	 *         (3 bytes little endian, last byte is ignored)
	 *   0x04: Length, 7-bit notation
	 *         (2 bytes little endian)
	 *   0x08: Data, 7-bit notation
	 *         (length bytes)
	 * 
	 * @param os
	 * @param p
	 * @throws IOException
	 */
	private void writeParameterData(OutputStream os, Parameters p) throws IOException {
		os.write(p.getAddress().getByte4());
		os.write(p.getAddress().getByte3());
		os.write(p.getAddress().getByte2());
		os.write(0);
		os.write(p.getLength() & 0x7F);
		os.write(p.getLength() >> 7 & 0x7F);
		os.write(p.getLength() >> 14 & 0x7F);
		os.write(p.getLength() >> 21 & 0x7F);
		os.write(p.getData());
	}
	
	private File getPatchPath(Patch patch) {
		if (patch instanceof TemporaryPatch) {
			return getPatchPath((TemporaryPatch) patch);
		} else if (patch instanceof UserPatch) {
			return getPatchPath((UserPatch) patch);
		} else {
			throw new Error("Unrecognised patch type.");
		}
	}
	
	private File getPatchPath(TemporaryPatch patch) {
		return new File(getAndCreateSettingsPath(), "patch-temporary.gaia");
	}
	
	private File getPatchPath(UserPatch patch) {
		return new File(getAndCreateSettingsPath(),
				String.format("patch-%s-%d.gaia", "ABCDEFGH".charAt(patch.getBank()), patch.getPatch() - 1));
	}
	
	/**
	 * Returns a File object for the settings path.
	 * Note: also creates the settings directory if it does not exist.
	 * @return The settings path.
	 */
	private File getAndCreateSettingsPath() {
		File path = getSettingsPath();
		if (!path.exists()) {
			path.mkdir();
		}
		return path;
	}
	
	private File getSettingsPath() {
		String appData = java.lang.System.getenv("APPDATA");
		String home = java.lang.System.getenv("HOME");
		if (appData != null) {
			return new File(appData, "gaia-tool");
		} else if (home != null) {
			return new File(home, ".gaia-tool");
		}
		throw new RuntimeException("Home directory not found.");
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
	 * @param parameters The Parameters object containing the address and data to send.
	 */
	public void sendDataTransmission(Parameters parameters) {
		try {
			send(new DataSet1(parameters.getAddress(), parameters.getData()));
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
	}
	
}
