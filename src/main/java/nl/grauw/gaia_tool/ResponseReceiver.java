package nl.grauw.gaia_tool;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.SysexMessage;

import nl.grauw.gaia_tool.messages.DataSet1;
import nl.grauw.gaia_tool.messages.GenericMessage;
import nl.grauw.gaia_tool.messages.IdentityReply;

public class ResponseReceiver implements Receiver {

	final static int UNIVERSAL_NONREALTIME_SYSEX = 0x7E;
	final static int GENERAL_INFORMATION = 0x06;
	final static int IDENTITY_REPLY = 0x02;
	
	final static int ROLAND_ID = 0x41;
	final static int MODEL_SH01 = 0x41;
	final static int COMMAND_DT1 = 0x12;
	
	@Override
	public void send(MidiMessage message, long timeStamp) {
		System.out.println("MIDI message received at " + timeStamp + ":");
		try {
			System.out.println("* " + processMidiMessage(message));
		} catch(Exception e) {
			System.out.println("*** Error: " + e);
		}
	}

	@Override
	public void close() {
	}
	
	public MidiMessage processMidiMessage(MidiMessage message) throws InvalidMidiDataException {
		if (message instanceof SysexMessage) {
			SysexMessage sem = (SysexMessage) message;
			byte[] data = sem.getData();
			if (data[0] == UNIVERSAL_NONREALTIME_SYSEX) {
				if (data[2] == GENERAL_INFORMATION && sem.getData()[3] == IDENTITY_REPLY) {
					return new IdentityReply(sem);
				}
			} else if (data[0] == ROLAND_ID && data[2] == 0 && data[3] == 0 && data[4] == MODEL_SH01) {
				if (data[5] == COMMAND_DT1) {
					return new DataSet1(sem);
				}
			}
		}
		return new GenericMessage(message);
	}

}
