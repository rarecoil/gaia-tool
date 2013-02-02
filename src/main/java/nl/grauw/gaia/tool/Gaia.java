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
package nl.grauw.gaia.tool;

import nl.grauw.gaia.tool.Address.AddressException;
import nl.grauw.gaia.tool.Note.NoteName;
import nl.grauw.gaia.tool.Parameters.ParameterChange;
import nl.grauw.gaia.tool.Parameters.ParameterChangeListener;
import nl.grauw.gaia.tool.Patch.PatchChangeListener;
import nl.grauw.gaia.tool.messages.ControlChangeMessage;
import nl.grauw.gaia.tool.messages.DataRequest1;
import nl.grauw.gaia.tool.messages.DataSet1;
import nl.grauw.gaia.tool.messages.IdentityReply;
import nl.grauw.gaia.tool.messages.IdentityRequest;
import nl.grauw.gaia.tool.messages.Message;
import nl.grauw.gaia.tool.messages.NoteOffMessage;
import nl.grauw.gaia.tool.messages.NoteOnMessage;
import nl.grauw.gaia.tool.messages.ProgramChangeMessage;
import nl.grauw.gaia.tool.messages.ControlChangeMessage.Controller;
import nl.grauw.gaia.tool.midi.MidiReceiver;
import nl.grauw.gaia.tool.midi.MidiTransmitter;
import nl.grauw.gaia.tool.mvc.Observable;
import nl.grauw.gaia.tool.parameters.System;
import nl.grauw.gaia.tool.parameters.Tone;


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
public class Gaia extends Observable implements ParameterChangeListener, MidiReceiver {
	
	private boolean opened = false;
	private boolean identityConfirmed = false;
	
	private MidiTransmitter transmitter;
	
	private int device_id = 0;
	
	final static int synth_channel = 0;
	final static int gm_channel = 1;
	
	final static Note C_4 = new Note(NoteName.C, 4);
	
	private Log log;
	
	private System system;
	private TemporaryPatch temporaryPatch;
	private UserPatch[][] userPatches = new UserPatch[8][8];
	
	public Gaia(Log log, MidiTransmitter transmitter) {
		this.log = log;
		this.transmitter = transmitter;
		
		temporaryPatch = new TemporaryPatch(this);
		for (int bank = 0; bank < 8; bank++) {
			for (int patch = 0; patch < 8; patch++) {
				userPatches[bank][patch] = new UserPatch(this, bank, patch);
			}
		}
	}
	
	public void enableTxEditData() {
		if (system == null) {
			send(new DataSet1(new Address(0x01, 0x00, 0x00, 0x19), new byte[] { 1 } ));
		} else {
			system.setTxEditData(true);
		}
	}
	
	@Override
	public void parameterChange(Parameters source, ParameterChange change) {
		if (source.hasChanged(change)) {
			sendDataTransmission(source, change.getOffset(), change.getLength());
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
	 * Returns whether the Gaia has been connected and its identity has been confirmed.
	 * @return True if the GAIA is connected and identity confirmed.
	 */
	public boolean isConnected() {
		return opened && identityConfirmed;
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
	public void open() {
		if (opened)
			throw new RuntimeException("GAIA is already opened.");
		
		opened = true;
		notifyObservers("opened");
		
		requestIdentity();
		enableTxEditData();
		log.log("Automatically enabled TX Edit Data parameter.");
		loadSystem();
	}
	
	/**
	 * Cleans up the system.
	 */
	public void close() {
		opened = false;
		identityConfirmed = false;
		device_id = 0;
		
		notifyObservers("opened");
		notifyObservers("identityConfirmed");
		notifyObservers("device_id");
	}
	
	/**
	 * Sends a MidiMessage to the GAIA.
	 * @param message
	 */
	public void send(Message message) {
		if (!opened)
			throw new RuntimeException("MIDI connection not open.");
		
		transmitter.send(message);
	}
	
	/**
	 * Receives an incoming MidiMessage object from the ResponseReceiver.
	 * @param message
	 */
	public void receive(Message message) {
		if (message instanceof IdentityReply) {
			confirmIdentity((IdentityReply) message);
		} else if (message instanceof DataSet1) {
			updateParameters((DataSet1) message);
		} else if (message instanceof ControlChangeMessage) {
			updateParameters((ControlChangeMessage) message);
		} else if (message instanceof ProgramChangeMessage) {
			temporaryPatch.clearParameters();
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
		try {
			updateParameters(message.getAddress(), message.getDataSet());
		} catch (AddressException e) {
			// s’ok, maybe there’s a new firmware
		}
	}
	
	private void updateParameters(ControlChangeMessage message) {
		temporaryPatch.updateParameters(message);
	}
	
	public void updateParameters(Address address, byte[] data) throws AddressException {
		int byte1 = address.getByte1();
		if (byte1 == 0x01 && address.getByte2() == 0x00 && address.getByte3() == 0x00) {
			if (address.getByte4() == 0x00 && data.length >= 0x6E) {
				system = new System(address, data);
				system.addParameterChangeListener(this);
				notifyObservers("system");
			} else if (system != null) {
				system.updateParameters(address, data);
			}
		} else if (byte1 == 0x0F) {
			if (address.equals(Address.INIT_PATCH) || address.equals(Address.MANUAL) || address.equals(Address.TONE_COPY))
				temporaryPatch.clearParameters();
		} else if (byte1 == 0x10) {
			temporaryPatch.updateParameters(address, data);
		} else if (byte1 == 0x20) {
			userPatches[address.getByte2() / 8][address.getByte2() % 8].updateParameters(address, data);
		} else {
			throw new AddressException("Address not recognised.");
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
		return userPatches[bank][patch];
	}
	
	/**
	 * Plays a C-4 note for one second.
	 */
	public void playTestNote() {
		send(new ProgramChangeMessage(synth_channel, 0));
		send(new NoteOnMessage(synth_channel, C_4, 127));
		
		try {
			Thread.sleep(1000);
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
		
		send(new NoteOffMessage(synth_channel, C_4, 127));
	}
	
	/**
	 * Plays a C-4 note for one second on the GM channel.
	 */
	public void playGMTestNote() {
		send(new ProgramChangeMessage(gm_channel, 0));
		send(new NoteOnMessage(gm_channel, C_4, 127));
		
		try {
			Thread.sleep(1000);
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
		
		send(new NoteOffMessage(gm_channel, C_4, 127));
	}
	
	/**
	 * Requests the device identity.
	 */
	public void requestIdentity() {
		send(new IdentityRequest());
	}
	
	/**
	 * Debugging tool to match up control change values (0..127) with the actual value.
	 * A list is output to the console log with the actual value for each control change value.
	 */
	public void testControlChange() {
		if (temporaryPatch.getTone(1) == null) {
			temporaryPatch.loadTone(1);
			temporaryPatch.addPatchChangeListener(new PatchChangeListener() {
				public void patchChange(Patch patch, String detail) {
					if ("tones".equals(detail))
						testControlChange();
				}
			});
		} else {
			testControlChange(0);
		}
	}
	
	private void testControlChange(final int i) {
		temporaryPatch.getTone(1).addParameterChangeListener(new ParameterChangeListener() {
			public void parameterChange(Parameters source, ParameterChange change) {
				if (change.getOffset() == 0x03) {
					source.removeParameterChangeListener(this);
					
					java.lang.System.out.print(((Tone)source).getOSCPitch().getValue() + ", ");
					
					if (i < 127)
						testControlChange(i + 1);
				}
			}
		});
		
		send(new ControlChangeMessage(synth_channel, Controller.TONE_1_OSC_PITCH, i));
		send(new DataRequest1(new Address(0x10, 0x00, 0x01, 0x03), 1));
	}
	
	/**
	 * Sends a data request directive.
	 * @param address The start address of the desired data.
	 * @param length The length of the desired data.
	 */
	public void sendDataRequest(Address address, int length) {
		send(new DataRequest1(address, length));
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
		
		send(new DataSet1(address, data));
		
		try {
			parameters.updateOriginalParameters(address, data);
		} catch (AddressException e) {
			throw new RuntimeException("AddressException is not supposed to occur.", e);
		}
	}
	
}
