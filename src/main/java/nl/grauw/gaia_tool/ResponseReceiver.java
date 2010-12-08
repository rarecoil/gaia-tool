package nl.grauw.gaia_tool;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.SysexMessage;

import nl.grauw.gaia_tool.messages.DataSet1;
import nl.grauw.gaia_tool.messages.GenericMessage;
import nl.grauw.gaia_tool.messages.IdentityReply;
import nl.grauw.gaia_tool.messages.NoteOffMessage;
import nl.grauw.gaia_tool.messages.NoteOnMessage;

public class ResponseReceiver implements Receiver {

	final static int UNIVERSAL_NONREALTIME_SYSEX = 0x7E;
	final static int GENERAL_INFORMATION = 0x06;
	final static int IDENTITY_REPLY = 0x02;
	
	final static int ROLAND_ID = 0x41;
	final static int MODEL_SH01 = 0x41;
	final static int COMMAND_DT1 = 0x12;
	
	private Gaia gaia;
	
	public ResponseReceiver(Gaia gaia) {
		this.gaia = gaia;
	}
	
	@Override
	public void send(MidiMessage message, long timeStamp) {
		try {
			gaia.receive(processMidiMessage(message));
		} catch(Exception e) {
			System.out.println("*** Error: " + e);
		}
	}

	@Override
	public void close() {
	}
	
	public MidiMessage processMidiMessage(MidiMessage message) throws InvalidMidiDataException {
		if (message instanceof SysexMessage) {
			return processMidiMessage((SysexMessage) message);
		} else if (message instanceof ShortMessage) {
			return processMidiMessage((ShortMessage) message);
		}
		return new GenericMessage(message);
	}
	
	public MidiMessage processMidiMessage(SysexMessage message) throws InvalidMidiDataException {
		byte[] data = message.getData();
		if (data[0] == UNIVERSAL_NONREALTIME_SYSEX) {
			if (data[2] == GENERAL_INFORMATION && message.getData()[3] == IDENTITY_REPLY) {
				return new IdentityReply(message);
			}
		} else if (data[0] == ROLAND_ID && data[2] == 0 && data[3] == 0 && data[4] == MODEL_SH01) {
			if (data[5] == COMMAND_DT1) {
				return new DataSet1(message);
			}
		}
		return new GenericMessage(message);
	}
	
	public MidiMessage processMidiMessage(ShortMessage message) throws InvalidMidiDataException {
		if (message.getCommand() == ShortMessage.NOTE_ON) {
			return new NoteOnMessage(message);
		} else if (message.getCommand() == ShortMessage.NOTE_OFF) {
			return new NoteOffMessage(message);
		}
		return new GenericMessage(message);
	}

}
