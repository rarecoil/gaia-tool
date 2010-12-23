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

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;

import nl.grauw.gaia_tool.Note.NoteName;
import nl.grauw.gaia_tool.Parameters.ParameterChange;
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
public class Gaia extends Observable implements Observer {

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
	
	private boolean synchronize = false;
	
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
	
	public boolean getSynchronize() {
		return synchronize;
	}
	
	public void setSynchronize(boolean sync) {
		synchronize = sync;
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
	 * Initialises the system.
	 */
	public void open() throws MidiUnavailableException {
		discoverGaiaDevices();
		midi_in.open();
		midi_out.open();
		receiver = midi_in.getReceiver();
		transmitter = midi_out.getTransmitter();
		transmitter.setReceiver(responseReceiver);
		
		requestIdentity();
		loadSystem();
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
	 * Sends a MidiMessage to the GAIA.
	 * @param message
	 */
	public void send(MidiMessage message) {
		receiver.send(message, -1);
		log.log("Sent: " + message);
	}
	
	/**
	 * Receives an incoming MidiMessage object from the ResponseReceiver.
	 * @param mm
	 */
	public void receive(MidiMessage mm) {
		log.log("Received: " + mm);
		
		if (mm instanceof DataSet1) {
			receive((DataSet1) mm);
		}
	}
	
	private void receive(DataSet1 mm) {
		updateParameters(mm.getAddress(), mm.getDataSet());
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
			File settingsPath = getAndCreateSettingsPath();
			String fileName;
			if (patch.getBank() == -1) {
				fileName = "patch-temporary.gaia";
			} else {
				fileName = String.format("patch-%s-%d.gaia", "ABCDEFGH".charAt(patch.getBank()), patch.getPatch() + 1);
			}
			File patchFile = new File(settingsPath, fileName);
			FileOutputStream fos;
			try {
				patchFile.createNewFile();
				fos = new FileOutputStream(patchFile);
				fos.write(patch.getCommon().getData());
				fos.write(patch.getTone(1).getData());
				fos.write(patch.getTone(2).getData());
				fos.write(patch.getTone(3).getData());
				fos.write(patch.getDistortion().getData());
				fos.write(patch.getFlanger().getData());
				fos.write(patch.getDelay().getData());
				fos.write(patch.getReverb().getData());
				fos.write(patch.getArpeggioCommon().getData());
				for (int note = 1; note <= 16; note++) {
					fos.write(patch.getArpeggioPattern(note).getData());
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
