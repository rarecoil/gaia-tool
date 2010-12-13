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
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.SysexMessage;

import nl.grauw.gaia_tool.messages.ControlChangeMessage;
import nl.grauw.gaia_tool.messages.DataSet1;
import nl.grauw.gaia_tool.messages.GenericMessage;
import nl.grauw.gaia_tool.messages.IdentityReply;
import nl.grauw.gaia_tool.messages.NoteOffMessage;
import nl.grauw.gaia_tool.messages.NoteOnMessage;
import nl.grauw.gaia_tool.messages.PitchBendChangeMessage;
import nl.grauw.gaia_tool.messages.ProgramChangeMessage;

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
		} catch(InvalidMidiDataException e) {
			e.printStackTrace();
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
		} else if (message.getCommand() == ShortMessage.PROGRAM_CHANGE) {
			return new ProgramChangeMessage(message);
		} else if (message.getCommand() == ShortMessage.CONTROL_CHANGE) {
			return new ControlChangeMessage(message);
		} else if (message.getCommand() == ShortMessage.PITCH_BEND) {
			return new PitchBendChangeMessage(message);
		}
		return new GenericMessage(message);
	}

}
