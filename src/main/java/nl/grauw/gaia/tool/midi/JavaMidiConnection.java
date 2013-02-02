package nl.grauw.gaia.tool.midi;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.SysexMessage;
import javax.sound.midi.Transmitter;

import nl.grauw.gaia.tool.Log;
import nl.grauw.gaia.tool.messages.ActiveSensingMessage;
import nl.grauw.gaia.tool.messages.ControlChangeMessage;
import nl.grauw.gaia.tool.messages.DataSet1;
import nl.grauw.gaia.tool.messages.IdentityReply;
import nl.grauw.gaia.tool.messages.Message;
import nl.grauw.gaia.tool.messages.NoteOffMessage;
import nl.grauw.gaia.tool.messages.NoteOnMessage;
import nl.grauw.gaia.tool.messages.PitchBendChangeMessage;
import nl.grauw.gaia.tool.messages.ProgramChangeMessage;
import nl.grauw.gaia.tool.messages.Sysex;

public class JavaMidiConnection implements MidiConnection, MidiTransmitter {
	
	final static int UNIVERSAL_NONREALTIME_SYSEX = 0x7E;
	final static int GENERAL_INFORMATION = 0x06;
	final static int IDENTITY_REPLY = 0x02;
	
	final static int ROLAND_ID = 0x41;
	final static int MODEL_SH01 = 0x41;
	final static int COMMAND_DT1 = 0x12;
	
	private boolean opened = false;
	
	private Receiver receiver;
	private Transmitter transmitter;
	private ResponseReceiver responseReceiver = new ResponseReceiver();
	
	private List<MidiReceiver> midiReceivers = new ArrayList<MidiReceiver>();
	
	private Log log;
	private Properties settings;
	
	public JavaMidiConnection(Log log, Properties settings) {
		this.log = log;
		this.settings = settings;
	}
	
	public void addMidiReceiver(MidiReceiver receiver) {
		midiReceivers.add(receiver);
	}
	
	public void removeMidiReceiver(MidiReceiver receiver) {
		midiReceivers.remove(receiver);
	}
	
	public MidiTransmitter getMidiTransmitter() {
		return this;
	}
	
	/**
	 * Initialises the system.
	 * @throws GaiaNotFoundException 
	 */
	public void open() throws MidiUnavailableException, GaiaNotFoundException {
		if (opened)
			throw new RuntimeException("MIDI device is already opened.");
		
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
	}
	
	/**
	 * Cleans up the system.
	 */
	public void close() {
		if (!opened)
			throw new RuntimeException("MIDI device is already closed.");
		
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
	}
	
	public boolean isOpened() {
		return opened;
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
	public void send(Message message) {
		if (!opened)
			throw new RuntimeException("MIDI connection not open.");
		
		if (message instanceof Sysex) {
			receiver.send(new SysexMessageWrapper(message.getMessage()), -1);
		} else {
			receiver.send(new ShortMessageWrapper(message.getMessage()), -1);
		}
		
		log.log("Sent: " + message);
	}
	
	private static class SysexMessageWrapper extends SysexMessage {
		public SysexMessageWrapper(byte[] message) {
			super(message);
		}
	}
	
	private static class ShortMessageWrapper extends ShortMessage {
		public ShortMessageWrapper(byte[] message) {
			super(message);
		}
	}
	
	private void receive(MidiMessage midiMessage, long timeStamp) {
		Message message = processMidiMessage(midiMessage);
		
		if (!(message instanceof ActiveSensingMessage)) {
			log.log("Received: " + message);
		}
		
		for (MidiReceiver receiver : new ArrayList<MidiReceiver>(midiReceivers))
			receiver.receive(message);
	}
	
	public Message processMidiMessage(MidiMessage message) {
		if (message instanceof SysexMessage) {
			return processMidiMessage((SysexMessage) message);
		} else if (message instanceof ShortMessage) {
			return processMidiMessage((ShortMessage) message);
		}
		return new Message(message.getMessage());
	}
	
	public Message processMidiMessage(SysexMessage message) {
		if (message.getStatus() == SysexMessage.SYSTEM_EXCLUSIVE) {
			byte[] data = message.getData();
			if (data.length > 0 && data[0] == UNIVERSAL_NONREALTIME_SYSEX) {
				if (data.length > 3 && data[2] == GENERAL_INFORMATION && data[3] == IDENTITY_REPLY) {
					return new IdentityReply(message.getMessage());
				}
			} else if (data.length > 4 && data[0] == ROLAND_ID && data[2] == 0 && data[3] == 0 && data[4] == MODEL_SH01) {
				if (data.length > 5 && data[5] == COMMAND_DT1) {
					return new DataSet1(message.getMessage());
				}
			}
		}
		return new Message(message.getMessage());
	}
	
	public Message processMidiMessage(ShortMessage message) {
		if (message.getCommand() == ShortMessage.NOTE_ON) {
			return new NoteOnMessage(message.getMessage());
		} else if (message.getCommand() == ShortMessage.NOTE_OFF) {
			return new NoteOffMessage(message.getMessage());
		} else if (message.getCommand() == ShortMessage.PROGRAM_CHANGE) {
			return new ProgramChangeMessage(message.getMessage());
		} else if (message.getCommand() == ShortMessage.CONTROL_CHANGE) {
			return new ControlChangeMessage(message.getMessage());
		} else if (message.getCommand() == ShortMessage.PITCH_BEND) {
			return new PitchBendChangeMessage(message.getMessage());
		} else if (message.getStatus() == ShortMessage.ACTIVE_SENSING) {
			return new ActiveSensingMessage(message.getMessage());
		}
		return new Message(message.getMessage());
	}
	
	public static class GaiaNotFoundException extends Exception {
		private static final long serialVersionUID = 1L;
		
		public GaiaNotFoundException() {
			super("Gaia MIDI device(s) not found.");
		}
	}
	
	private class ResponseReceiver implements Receiver {
		
		@Override
		public void send(MidiMessage message, long timeStamp) {
			receive(message, timeStamp);
		}
		
		@Override
		public void close() {
		}
		
	}
	
}
